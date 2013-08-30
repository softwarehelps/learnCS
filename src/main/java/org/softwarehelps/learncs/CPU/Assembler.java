package org.softwarehelps.learncs.CPU;

/* This file was automatically generated from a .mac file. */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

class Assembler extends Frame implements ActionListener, ItemListener
{
     TextArea ta1, ta2;
     Cpu parent;
     Button assembleB, disassembleB, clear1B, clear2B, saveB, loadB,
            loadasmB, saveasmB, exampleB;
     String[] memory;
     Choice exampleCH;
     
     public Assembler (Cpu parent) {
          setLayout(null);
          setTitle("Super Simple CPU Assembler Window");

          this.parent = parent;

          setBackground (new Color(152,239,244));

          int x = 7;
          int y = 40;

          ta1 = new TextArea(3,40);
          ta1.setBackground(Color.white);
          ta1.setFont(new Font("Courier", Font.PLAIN, 11));
          ta1.setBounds (x, y, 360, 270);
          add(ta1);

          ta2 = new TextArea(3,40);
          ta2.setBackground(Color.white);
          ta2.setFont(new Font("Courier", Font.PLAIN, 11));
          ta2.setBounds (x+ta1.getSize().width+5, y, 250, 270);
          add(ta2);

          x = 7;
          y += ta1.getSize().height + 5;

          assembleB = new Button("Assemble");
          assembleB.addActionListener(this);
          assembleB.setBounds(x, y, 70, 25);
          add(assembleB);

          exampleCH = new Choice();
          exampleCH.setBackground(Color.white);
          exampleCH.setBounds(x, y+assembleB.getSize().height+5, 200, 25);
          exampleCH.addItem(" -- examples --");
          exampleCH.addItem("sequence");
          exampleCH.addItem("Input/Output");
          exampleCH.addItem("Negative numbers");
          exampleCH.addItem("Copy a number");
          exampleCH.addItem("Counting loop");
          exampleCH.addItem("GCD");
          exampleCH.addItemListener(this);
          add(exampleCH);

/*
          exampleB = new Button("load example");
          exampleB.addActionListener(this);
          exampleB.setBounds(x+exampleCH.getSize().width+5,
                             y+assembleB.getSize().height+5, 100, 25);
          add(exampleB);
*/

          x += assembleB.getSize().width + 5;

          clear1B = new Button("Clear");
          clear1B.addActionListener(this);
          clear1B.setBounds(x, y, 40, 25);
          add(clear1B);
          x += clear1B.getSize().width + 5;

          loadasmB = new Button("Load");
          loadasmB.addActionListener(this);
          loadasmB.setBounds(x, y, 40, 25);
          add(loadasmB);
          x += loadasmB.getSize().width + 5;

          saveasmB = new Button("Save");
          saveasmB.addActionListener(this);
          saveasmB.setBounds(x, y, 40, 25);
          add(saveasmB);
          x += saveasmB.getSize().width + 5;

          x += 120;

          disassembleB = new Button("Disassemble");
          disassembleB.addActionListener(this);
          disassembleB.setBounds(x, y, 85, 25);
          add(disassembleB);
          x += disassembleB.getSize().width + 5;

          loadB = new Button("Load from Memory");
          loadB.addActionListener(this);
          loadB.setBounds(x, y, 130, 25);
          add(loadB);

          saveB = new Button("Save to Memory");
          saveB.addActionListener(this);
          saveB.setBounds(x, y+loadB.getSize().height+5, 130, 25);
          add(saveB);
          x += saveB.getSize().width + 5;

          clear2B = new Button("Clear");
          clear2B.addActionListener(this);
          clear2B.setBounds(x, y, 40, 25);
          add(clear2B);
          x += clear2B.getSize().width + 5;

          addWindowListener(
               new WindowAdapter() {
                    public void windowClosing (WindowEvent we) {
                         setVisible(false);
                    }
               }    
          );

          setSize(660, 400);
          setLocation(30, 30);
          setVisible(false);
     }

     public void actionPerformed (ActionEvent e) {
          if (e.getSource() == assembleB) {
               ta2.setText("");
               assemble();
          }
/*
          if (e.getSource() == exampleB) {
               loadExample();
          }
*/
          if (e.getSource() == disassembleB) {
               ta1.setText("");
               disassemble();
          }
          if (e.getSource() == clear1B) {
               ta1.setText("");
          }
          if (e.getSource() == clear2B) {
               ta2.setText("");
          }
          if (e.getSource() == saveB) {
               if (ta2.getText().length() == 0) {
                    new Popup("You must assemble your program first.");
                    return;
               }
               saveToCPU();
          }
          if (e.getSource() == loadB) {
               loadFromCPU();
          }
          if (e.getSource() == loadasmB) {
               ta1.setText(loadAsmProgram());
          }
          if (e.getSource() == saveasmB) {
               saveAsmProgram(ta1.getText());
          }
     }

     public void itemStateChanged(ItemEvent e) {
          if (e.getSource() == exampleCH)
               loadExample();
     }

     private void loadExample() {
          String example = exampleCH.getSelectedItem();
          if (example.equals("sequence")) {
               ta1.setText("    LDI  5\n"+
                           "    ADD  X\n"+
                           "    STO  Y\n"+
                           "    STP\n"+
                           "X   DAT  40\n"+
                           "Y   DAT  0\n");
          }
          if (example.equals("Input/Output")) {
               ta1.setText("    INP\n"+
                           "    OUT\n"+
                           "    STP\n");
          }
          if (example.equals("Negative numbers")) {
               ta1.setText("    LDI  4\n"+
                           "    SUB  5\n"+
                           "    STP\n"+
                           "    DAT  0      ; Filler\n"+
                           "    DAT  0      ; More filler\n"+
                           "    DAT  6      ; The data number\n");
          }
          if (example.equals("Copy a number")) {
               ta1.setText("    LDI  13     ; Notice how LDI differs from\n"+
                           "    LOD  13     ; LOD!\n"+
                           "    STO  12     ; Store ACC into memory cell 12\n"+
                           "    STP\n"+
                           "    DAT  0      ; Lots of Filler\n"+
                           "    DAT  0      ;\n"+
                           "    DAT  0      ;\n"+
                           "    DAT  0      ;\n"+
                           "    DAT  0      ;\n"+
                           "    DAT  0      ;\n"+
                           "    DAT  0      ;\n"+
                           "    DAT  0      ;\n"+
                           "    DAT  6      ; The data number\n");
          }
          if (example.equals("Counting loop")) {
               ta1.setText("     LDI  6     ; How many times thru the loop\n"+
                           "TOP  JNG  DONE  ; Top of the loop\n"+
                           "     SUB  ONE   ; Subtract 1\n"+
                           "     JMP  TOP   ; Bottom of the loop\n"+
                           "DONE STP        ; Stop the program\n"+
                           "ONE  DAT  1     ; A data value, the constant 1\n");
          }
          if (example.equals("GCD")) {
               ta1.setText("TOP  LOD  A     ;loop; if (A = B) then\n"+
                           "     SUB  B     ;    \n"+
                           "     JZR  DONE  ;    exit;\n"+
                           "     JNG  ELSE  ;if (A > B) then\n"+
                           "     STO  A     ;    A = A - B\n"+
                           "     JMP  TOP   ;\n"+
                           "ELSE LOD  B     ;else\n"+
                           "     SUB  A     ;\n"+
                           "     STO  B     ;    B = B - A\n"+
                           "     JMP  TOP   ;endloop\n"+
                           "DONE STP        ;stop\n"+
                           "     DAT  0     ;(filler, 3 memory cells)\n"+
                           "     DAT  0     ;\n"+
                           "     DAT  0     ;\n"+
                           "A    DAT  18    ;A = 18\n"+
                           "B    DAT  24    ;B = 24\n");
          }
          ta2.setText("");
     }

     private void assemble() {
          memory = new String[16];
          StringList sl = new StringList(ta1.getText(), "\n");
          SymTab st = new SymTab();
          int nextaddress = 0;
          int numerrors = 0;

          // first pass, find only the symbols

          for (int i=0; i<sl.length(); i++) {
               String line = stripComments(sl.get(i)).toUpperCase();
               if (line.length() == 0) continue;
               if (line.charAt(0) == ' ')
                    line = ".  "+line;
               line = line.trim();
               if (countFields(line) < 2) {
                    output (ta2,"# ERROR in line "+i+"  not enough fields");
                    numerrors++;
                    continue;
               }
               String address = getField(line,0);
               if (address.equals("."))
                    address = nextaddress+"";
               if (SymTab.alpha(address)) {
                    String trans;
                    if ((trans = st.install(address, nextaddress)) == null) {
                         numerrors++;
                         continue;
                    }
                    if (trans == null) {
                         new Popup ("Cannot find the symbol: "+address);
                         numerrors++;
                         continue;
                    }
                    address = trans;
               }
               nextaddress = U.atoi(address) + 1;
          }

//        st.display();

          if (numerrors > 0) return;

          // second pass, generate code

          nextaddress = 0;
          for (int i=0; i<sl.length(); i++) {
               String line = stripComments(sl.get(i)).toUpperCase();
               if (line.length() == 0) continue;
               if (line.charAt(0) == ' ')
                    line = ".  "+line;
               line = line.trim();
               String address = getField(line,0);
               if (address.equals("."))
                    address = nextaddress+"";
               if (SymTab.alpha(address)) 
                    address = ""+U.bin2dec(st.lookup(address));

               String opcode = getField(line,1);
               String operand = getField(line,2);
               if (SymTab.alpha(address)) 
                    address = st.lookup(address);
               nextaddress = U.atoi(address) + 1;
               if (operand == null) 
                    operand = "0";
               if (SymTab.alpha(operand)) {
                    operand = st.lookup(operand);
               }
               else {
                    if (operand.endsWith("b") || operand.endsWith("B"))
                         operand = operand.substring(0,operand.length()-1);
                    else
                         operand = U.dec2bin(U.atoi(operand));
               }
               if (operand.length() > 12)
                    operand = operand.substring(0,12);

               int addr = U.atoi(address);
               if (addr < 0 || addr > 15)
                    output (ta2,"# ERROR in line "+i+"  illegal address");
               else {
                    if (opcode.equals("DAT")) {
                         memory[addr] = U.padout(operand,'0',16);
                    }
                    else {
                         String binopcode = Cpu.detranslate(opcode);
                         if (binopcode.equals("ERROR"))
                              output (ta2,"# ERROR in line "+i+"  Illegal opcode");
                         else
                              memory[addr] = binopcode+U.padout(operand, '0', 12);
                    }
               }
          }

          for (int i=0;  i<16; i++)
               if (memory[i] == null)
                    output(ta2,"0000000000000000");
               else
                    output(ta2,memory[i]);
     }

     public void output (TextArea t, String s) {
          if (t.getText().length() == 0)
               t.setText(s);
          else
               t.setText(t.getText()+"\n"+s);
     }

     public static String getField (String s, int fieldnum) {
          return getField (s, fieldnum, ' ');
     }

     public static String getField (String s, int fieldnum, char separator) {
          StringTokenizer st = new StringTokenizer(s, separator+"");
          for (int i=0; i<=fieldnum; i++) {
               if (!st.hasMoreTokens())
                    return null;
               String tok = st.nextToken();
               if (i == fieldnum)
                    return tok;
          }
          return null;
     }

     public static int countFields (String s) {
          StringTokenizer st = new StringTokenizer(s);
          int count = 0;
          while (st.hasMoreTokens()) {
               st.nextToken();
               count++;
          }
          return count;
     }

     public static String stripComments (String s) {
          int n = s.indexOf(";");
          if (n == -1)
               return s;
          else
               return s.substring(0,n);
     }

     private void disassemble() {
          if (memory == null) {
               new Popup("There must be a program in the binary field.");
               return;
          }
          ta1.setText("");
          for (int i=0; i<16; i++) {
               String line = memory[i];
               if (line == null) continue;
               if (line.length() < 16)
                    line = U.padout(line,'0',16);
               String binopcode = line.substring(0,4);
               String operand = line.substring(4);
               String mnemonic = Cpu.translate(binopcode);
               if (mnemonic.equals("???"))
                    mnemonic = "DAT";        // we'll assume this is right!
               int n = U.bin2dec(operand);
               output(ta1,i+"  "+mnemonic+"  "+n);
          }
     }

     public void saveToCPU() {
          for (int i=0; i<16; i++)
               if (memory[i] == null)
                    parent.memory[i].setText("0000000000000000");
               else
                    parent.memory[i].setText(memory[i]);
     }

     public void loadFromCPU() {
          ta2.setText("");
          memory = new String[16];
          for (int i=0; i<16; i++) {
               memory[i] = parent.memory[i].getText();
               output(ta2,memory[i]);
          }
     }

     public String loadAsmProgram() {
          String text = "";

          FileDialog loadfile = new
               FileDialog(this, "Load Assembler Program", FileDialog.LOAD);
          loadfile.setFile("*.txt");
          loadfile.show();

          String filename = loadfile.getFile();
          String directory = loadfile.getDirectory();
          if (filename == null)
               return "";
          if (!filename.endsWith(".txt")) {
               System.err.println ("An assembler source file must have an extension of .txt.");
               return "";
          }

          BufferedReader is;        // input stream

          if(filename == null || filename.length() == 0)
               return "";

          this.setTitle(filename);
          try {
               is = new BufferedReader(new FileReader(directory+"\\"+filename));
               String line;
               while ((line = is.readLine()) != null) {
                    if (text.length() == 0)
                         text += line;
                    else
                         text += "\n"+line;
               }
               is.close();
          }
          catch(IOException ioe) {
               System.err.println("LoadProgram:  i/o exception.");
               ioe.printStackTrace();
          }
          return text;
     }

     public void saveAsmProgram (String text) {
          FileDialog savefile =
               new FileDialog(this,"Save Asembler Program",FileDialog.SAVE);
          savefile.setFile("*.txt");
          savefile.show();

          String filename = savefile.getFile();
          String directory = savefile.getDirectory();
          String fullname = directory + "\\"+filename;
          
          if(filename == null)
               return;

          BufferedWriter os;        // output stream

          if(filename == null || filename.length() == 0)
               return;
          else {
               this.setTitle(filename);
               try {
                    os = new BufferedWriter(new FileWriter(fullname, false));

                    os.write(text);
                    os.close();
               }
               catch(IOException ioe) {
                    System.err.println("SaveProgram:  i/o exception.");
               }
          }
     }

}

class SymTab {
     String[] symbols, values;
     int numsymbols;
     final static int MAX = 25;

     public SymTab() {
          numsymbols = 0;
          symbols = new String[MAX];
          values = new String[MAX];
     }

     /*
          The symbol may look like       
                  A=12
          in which case we set "A" to "12".
          If it does not have an equals sign like this, we 
          simply set it to the possibleValue, which is the next
          address.
     */

     public String install (String symbol, int possibleValue) {
          if (lookup(symbol) != null) {
               new Popup("This symbol: "+symbol+" is already defined.");
               return null;
          }
          if (numsymbols == MAX) {
               new Popup("Too many symbols!  Only "+MAX+" symbols are allowed.");
               return null;
          }
          int n = symbol.indexOf("=");
          if (n == -1) {
               symbols[numsymbols] = symbol;
               values[numsymbols] = U.padout(U.dec2bin(possibleValue),'0',12);
               numsymbols++;
          }
          else {
               symbols[numsymbols] = symbol.substring(0,n);
               int temp = U.atoi(symbol.substring(n+1));
               values[numsymbols] = U.padout(U.dec2bin(temp),'0',12);
               numsymbols++;
          }
          return possibleValue+"";
     }

     public String lookup (String symbol) {
          for (int i=0; i<numsymbols; i++)
               if (symbols[i].equals(symbol))
                    return values[i];
          return null;
     }

     public static boolean alpha(String s) {
          if (s == null || s.length() == 0) return false;
          return Character.isLetter(s.charAt(0));
     }

     public void display() {
          for (int i=0; i<numsymbols; i++)
               System.out.println (i+". "+symbols[i]+"  "+values[i]);
     }
}
