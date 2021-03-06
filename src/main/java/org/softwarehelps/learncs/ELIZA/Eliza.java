package org.softwarehelps.learncs.ELIZA;

/* This file was automatically generated from a .mac file. */

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;

import java.io.*;

public class Eliza extends Frame
                   implements ActionListener, TextListener
{
     TextField input, output;
     TextArea rulesTA, transcriptTA;
     Button respondB, rulesB, loadrulesB, saverulesB;
     Color bg = new Color(245,230,209);
     String allRules;

     String[] genericResponses =
               {"Let's talk about you some more.",
                "Could we please get back on track?",
                "Please be more precise.",
                "I'd like to investigate your early childhood.",
                "Hmmmmmm",
                "How interesting!"};
     int whichGeneric = 0;

     public Eliza() {
          setLayout (null);

          setTitle("Eliza");

          int y = 30;
          int x = 5;

          Label lab = new Label("Eliza Therapist");
          lab.setFont(new Font("SansSerif", Font.BOLD, 36));
          lab.setBounds(45,y,350,45);
          lab.setBackground(bg);
          add(lab);
          y += lab.getSize().height;

          lab = new Label("Your input here:");
          lab.setBounds(5,y,185,25);
          lab.setBackground(bg);
          add(lab);
          y += lab.getSize().height;

          input = new TextField(45);
          input.setBounds(30,y,300,25);
          input.addActionListener(this);
          input.setBackground(Color.white);
          add(input);
          y += input.getSize().height + 2;

          respondB = new Button("Respond!");
          respondB.addActionListener(this);
          respondB.setBounds(30,y,60,25);
          add(respondB);
          y += respondB.getSize().height+5;

          lab = new Label("The computer's response:");
          lab.setBounds(5,y,200,25);
          lab.setBackground(bg);
          add(lab);
          y += lab.getSize().height;

          output = new TextField(35);
          output.setBounds(30,y,300,25);
          output.setBackground(Color.white);
          output.setEditable(false);
          add(output);
          y += output.getSize().height;

          lab = new Label("Transcript of interaction:");
          lab.setBounds(5,y,200,25);
          lab.setBackground(bg);
          add(lab);
          y += lab.getSize().height;

          transcriptTA = new TextArea(10, 35);
          transcriptTA.setBounds(30,y,300,280);
          transcriptTA.setBackground(Color.white);
          transcriptTA.setEditable(false);
          add(transcriptTA);
          y += transcriptTA.getSize().height;

          int xboundary = 350;
          x = xboundary;
          y = 70;

          lab = new Label("The transformation rules:");
          lab.setBounds(x,y,160,25);
          lab.setBackground(bg);
          add(lab);

          rulesB = new Button("Show rules");
          rulesB.addActionListener(this);
          rulesB.setBounds(x+160,y,80,25);
          add(rulesB);
          y += lab.getSize().height + 5;

          rulesTA = new TextArea(5,55);
          rulesTA.setBounds(x+20,y,350,400);
          rulesTA.setBackground(Color.lightGray);
          rulesTA.addTextListener(this);
          add(rulesTA);
          y += rulesTA.getSize().height + 5;


          loadrulesB = new Button("Load Rules");
          loadrulesB.addActionListener(this);
          loadrulesB.setBounds(xboundary+20,y,90,25);
          add(loadrulesB);

          x = xboundary+20 + loadrulesB.getSize().width+5;

          saverulesB = new Button("Save Rules");
          saverulesB.addActionListener(this);
          saverulesB.setBounds(x,y,90,25);
          add(saverulesB);

          y += loadrulesB.getSize().height+5;


          setRules();

          setBackground(bg);

          addWindowListener(
               new WindowAdapter() {
                    public void windowClosing (WindowEvent we) {
                         dispose();
                         // System.exit(1);
                    }
               }    
          );

          setSize(750, 600);
          setVisible(true);
          repaint();
     }
     
     public void actionPerformed(ActionEvent e) {
          if (e.getSource() == respondB || e.getSource() == input)
               respond();
          if (e.getSource() == rulesB)  {
               if (rulesB.getLabel().equals("Show rules")) {
                    rulesTA.setText(allRules);
                    rulesTA.setEnabled(true);
                    rulesTA.setBackground(Color.white);
                    rulesB.setLabel("Hide rules");
               }
               else {
                    rulesTA.setText("");
                    rulesTA.setEnabled(false);
                    rulesTA.setBackground(Color.lightGray);
                    rulesB.setLabel("Show rules");
               }
          }
          if (e.getSource() == loadrulesB) {
               allRules = loadRules();
               rulesTA.setText(allRules);
          }
          if (e.getSource() == saverulesB)
               saveRules(rulesTA.getText());
     }

     public void textValueChanged (TextEvent te) {
          if (te.getSource() == rulesTA)
               allRules = rulesTA.getText();
          System.out.println (allRules);
     }

     public static int atoi (String s) {
          try {
               if (s.startsWith("+"))
                    s = s.substring(1);
               return Integer.valueOf(s).intValue();
          }
          catch (NumberFormatException nfe) {
               return 0;
          }
     }

     private void respond () {
          StringTokenizer st = new StringTokenizer(allRules,"\n");
          String[] rulesArray = new String[st.countTokens()];
          int i = 0;
          while (st.hasMoreTokens()) {
               String rule = st.nextToken();
               rulesArray[i++] = rule;
          }

          String in = trimPunctuation(input.getText());
//        String in = input.getText();          // doesn't work

          Random r = new Random();
          long n = new Date().getTime();
          r.setSeed(n);
          int pos = (int)(r.nextFloat() * rulesArray.length);

          boolean foundMatch = false;
          int count = 0;

          while (count < rulesArray.length) {
               String rule = rulesArray[pos];
               int m = rule.indexOf("=>");
               if (m == -1) continue;      // shouldn't happen, bad rule!
               String part1 = rule.substring(0,m);
System.out.println("in="+in);
System.out.println("part1="+part1);
               String part2 = rule.substring(m+2);
System.out.println("part2="+part2);
               StringList subs;
               if ((subs = Matcher.match(in,part1)) != null) {
                    String response = Matcher.substitute(part2, subs);
                    StringList sl = new StringList(response);
                    //flipPronouns(sl);
                    output.setText(sl.toString());
                    foundMatch = true;
                    break;
               }
               count++;
               pos++;
               if (pos >= rulesArray.length)
                    pos=0;
          }
          if (!foundMatch) {
               output.setText(genericResponses[whichGeneric]);
               whichGeneric = (whichGeneric + 1) % genericResponses.length;
          }
          transcriptTA.setText(transcriptTA.getText()+
                             input.getText()+"\n      "+
                             output.getText()+"\n");
          transcriptTA.setCaretPosition(transcriptTA.getText().length());
          input.setText("");
     }

     private void setRules() {
          allRules = 
             "Hello *=>Greetings!\n" +
             "I have a problem=>What kind of problem?\n" +
             "$0 is $1=>Do you have evidence that $0 is $1 ?\n" +
             "My $0 wanted me to come *=>Your $0 wanted you to come?\n" +
             "I hate my $0 *=>Tell me more about your $0.\n" +
             "I hate $0 $1 *=>Why are you so hostile about $0 $1 ?\n" +
             "$0/You are $1=>Why are $0 $1 ?\n" +
             "$0 is a monster=>That's pretty strong language!\n" +
             "You are dumb=>Please don't insult me!\n" +
             "You are stupid=>Please don't insult me!\n" +
             "You are $0=>I'll interpret that as a compliment.\n" +
             "Are you $0=>What makes you think I am $0 ?\n" +
             "No=>Come, let's not be so negative!\n"+
             "* is fun=>Yes, I think so, too!\n" +
             "*($0) usually believe *($1)=>Do $0 usually believe $1 ?\n" +
             "$0/You are $1=>Do you really believe that $0 are $1 ?\n" +
             "";
     }

     private void flipPronouns (StringList sl) {
     }

     private String trimPunctuation(String s) {
          String ret = "";
          for (int i=0; i<s.length(); i++) {
               char ch = s.charAt(i);
               if (ch == '.' || ch == ',') continue;
               ret += ch;
          }
          return ret;
     }

     public String loadRules() {
          String text = "";

          FileDialog loadfile = new
               FileDialog(this, "Load Eliza Rules", FileDialog.LOAD);
          loadfile.setFile("*.txt");
          loadfile.show();

          String filename = loadfile.getFile();
          String directory = loadfile.getDirectory();
          if (filename == null)
               return "";
          if (!filename.endsWith(".txt")) {
               System.err.println ("An Eliza rule file must have an extension of .txt.");
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
          }
          return text;
     }

     public void saveRules (String text) {
          FileDialog savefile =
               new FileDialog(this,"Save Eliza Rules",FileDialog.SAVE);
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
