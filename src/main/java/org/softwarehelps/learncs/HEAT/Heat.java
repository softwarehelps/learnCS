package org.softwarehelps.learncs.HEAT;

/* This file was automatically generated from a .mac file. */

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Heat extends Frame
                implements MouseListener, ActionListener
{
     final static int CELLSIZE = 20;
     final static int NUMCELLS = 20;
     final static int PIXELSIZE = CELLSIZE*NUMCELLS;
     double[][] cells, oldcells;
     boolean[][] fixed;
     boolean muststop = false;
     boolean running = false;
     Checkbox fixedCB;
     Label runningStatus, timeCount;
     int time = 0;          // shown on screen so you can see the number of
                            // iterations that have occurred

     double currentTemp = 0;

     TextArea explainTA;
  
     Image buffer;
     Graphics gg;

     Button runB, onestepB, stopB, clearB, exampleB, example2B,
            loadB, saveB;
     int startingX, startingY;

     public Heat() {
          setLayout(null);

          setTitle("Heat Simulator");
          cells = new double[NUMCELLS][NUMCELLS];
          oldcells = new double[NUMCELLS][NUMCELLS];
          fixed = new boolean[NUMCELLS][NUMCELLS];
          for (int i=0; i<NUMCELLS; i++)
               for (int j=0; j<NUMCELLS; j++)
                    fixed[i][j] = false;
          clearCells();
          initializeLevels();

          int xpos = 5;
          startingX = 10;

          int y = 25;

          fixedCB = new Checkbox("Fixed");
          fixedCB.setBounds(startingX+CELLSIZE*NUMCELLS+25,startingY+300,
                                70, 25);
          fixedCB.setBackground(Color.white);
          add(fixedCB);

          Label lab = new Label("Heat Transfer");
          lab.setFont(new Font("SansSerif", Font.BOLD, 36));
          lab.setBackground(Color.white);
          lab.setBounds(90,y,350,45);
          add(lab);
          startingY = lab.getSize().height+y;

          runB = new Button("Run");
          runB.addActionListener(this);
          add(runB);
          runB.setBounds(xpos, startingY+PIXELSIZE+5, 40, 25);
          xpos += runB.getSize().width+5;

          onestepB = new Button("1 step");
          onestepB.addActionListener(this);
          add(onestepB);
          onestepB.setBounds(xpos, startingY+PIXELSIZE+5, 40, 25);
          xpos += onestepB.getSize().width+5;

          stopB = new Button("Stop");
          stopB.addActionListener(this);
          add(stopB);
          stopB.setEnabled(false);
          stopB.setBounds(xpos, startingY+PIXELSIZE+5, 40, 25);
          xpos += stopB.getSize().width+5;

          runningStatus = new Label("");
          runningStatus.setBackground(Color.white);
          add(runningStatus);
          runningStatus.setBounds(stopB.getLocation().x,
                 startingY+PIXELSIZE+5+stopB.getSize().height,90,25);

          timeCount = new Label("");
          timeCount.setBackground(Color.white);
          add(timeCount);
          timeCount.setBounds(runningStatus.getLocation().x +
                              runningStatus.getWidth() + 2,
                startingY+PIXELSIZE+5+stopB.getSize().height,90,25);


          clearB = new Button("Clear Cells");
          clearB.addActionListener(this);
          add(clearB);
          clearB.setBounds(xpos, startingY+PIXELSIZE+5, 70, 25);
          xpos += clearB.getSize().width+5;

          exampleB = new Button("Example 1");
          exampleB.addActionListener(this);
          add(exampleB);
          exampleB.setBounds(xpos, startingY+PIXELSIZE+5, 70, 25);
          xpos += exampleB.getSize().width+5;

          example2B = new Button("Example 2");
          example2B.addActionListener(this);
          add(example2B);
          example2B.setBounds(xpos, startingY+PIXELSIZE+5, 70, 25);
          xpos += example2B.getSize().width+5;

          loadB = new Button("Load");
          loadB.addActionListener(this);
          add(loadB);
          loadB.setBounds(xpos, startingY+PIXELSIZE+5, 50, 25);
          xpos += loadB.getSize().width+5;

          saveB = new Button("Save");
          saveB.addActionListener(this);
          add(saveB);
          saveB.setBounds(xpos, startingY+PIXELSIZE+5, 50, 25);
          xpos += saveB.getSize().width+5;


          addMouseListener(this);

          setVisible(true);
          setSize(650, 530);

          addWindowListener(
               new WindowAdapter() {
                    public void windowClosing (WindowEvent we) {
                         dispose();
                         System.exit(1);
                    }
               }    
          );

          buffer = createImage (getSize().width, getSize().height);

          repaint();
     }

     public void update (Graphics g) {
          paint (g);
     }

     public void paint (Graphics g) {
          if (buffer == null) 
               return;
          if (buffer != null) {
               if (gg == null)
                    gg = buffer.getGraphics();
          }
          gg.setColor(Color.white);
          gg.fillRect(0,0,getSize().width, getSize().height);
          gg.setColor(Color.white);
          gg.fillRect(0,0, PIXELSIZE, PIXELSIZE);
          paintCells(gg);
          gg.setColor(Color.white);
          drawGrid(gg);
          drawLegend(gg);
          if (running)
               timeCount.setText(time+"");
          g.drawImage(buffer, 0, 0, this);
     }

     private void clearCells() {
          for (int i=0; i<NUMCELLS; i++)
               for (int j=0; j<NUMCELLS; j++) {
                    cells[i][j] = 0;
                    fixed[i][j] = false;
               }
     }

     private void paintCells (Graphics g) {
          int X = startingX;
          int Y = startingY;
          for (int i=0; i<NUMCELLS; i++)
               for (int j=0; j<NUMCELLS; j++) {
                    g.setColor(translateHeat2Color(cells[i][j]));
                    g.fillRect(X+j*CELLSIZE, Y+i*CELLSIZE, CELLSIZE, CELLSIZE);
                    if (fixed[i][j])
                         g.setColor(Color.white);
                         g.fillRect(X+j*CELLSIZE+CELLSIZE-2,
                                    Y+i*CELLSIZE+CELLSIZE-2,
                                    2,2);
               }
     }

     private void drawGrid(Graphics g) {
          int x = startingX;
          int Y = startingY;
          for (int i=0; i<NUMCELLS; i++) {
               g.drawLine(x,Y+0,x,Y+PIXELSIZE);
               x += CELLSIZE;
          }
          g.drawLine(x,Y+0,x,Y+PIXELSIZE);

          int y = 0;
          for (int i=0; i<NUMCELLS; i++) {
               g.drawLine(startingX,Y+y,PIXELSIZE+startingX,Y+y);
               y += CELLSIZE;
          }
          g.drawLine(startingX,Y+y,PIXELSIZE+startingX,Y+y);
     }

     public void actionPerformed (ActionEvent e) {
          if (e.getSource() == clearB) {
               clearCells();
               repaint();
          }
          else if (e.getSource() == onestepB) {
               muststop = false;
               simulate(1);
               repaint();
          }
          else if (e.getSource() == runB) {
               muststop = false;
               stopB.setEnabled(true);
               runB.setEnabled(false);
               runningStatus.setText("Running...");
               simulate(-1);
               repaint();
          }
          else if (e.getSource() == stopB) {
               muststop = true;
               stopB.setEnabled(false);
               runB.setEnabled(true);
               runningStatus.setText("");
          }
          else if (e.getSource() == exampleB) {
               makeExample(1);
               repaint();
          }
          else if (e.getSource() == example2B) {
               makeExample(2);
               repaint();
          }
          else if (e.getSource() == loadB) {
               loadPicture();
               repaint();
          }
          else if (e.getSource() == saveB) {
               savePicture();
          }
     }

     public void mouseClicked (MouseEvent me) {
          int x = me.getX();
          int y = me.getY();
          double temp = mouseInLegend (me.getX(), me.getY());
          if (temp > -1)
               currentTemp = temp;
          else
               touchCell(y-startingY, x-startingX);
          repaint();
     }
     public void mousePressed (MouseEvent me) {
     }
     public void mouseReleased (MouseEvent me) {
     }
     public void mouseEntered (MouseEvent me) {
     }
     public void mouseExited (MouseEvent me) {
     }

     private void touchCell (int xpos, int ypos) {
          int x = xpos / CELLSIZE;
          int y = ypos / CELLSIZE;
          if (x < 0 || x >= NUMCELLS) return;
          if (y < 0 || y >= NUMCELLS) return;
          cells[x][y] = currentTemp;
          fixed[x][y] = fixedCB.getState();
     }

     private int tempnumsteps;

     private void simulate(int numsteps) {
          tempnumsteps = numsteps;
          running = true;
          new Thread() {
               public void run() {
                    time = 0;
                    while ((tempnumsteps > 0 && !muststop) ||
                           (tempnumsteps == -1 && !muststop)) 
                    {
                         onestep();
                         time++;
                         repaint();
                         if (tempnumsteps > 0)
                              tempnumsteps--;
                         try {
                              Thread.sleep(100);
                         } catch (InterruptedException ie) {}
                    }
                    running = false;
               }
          }.start();
     }

     private void onestep() {
          for (int i=0; i<NUMCELLS; i++)
               for (int j=0; j<NUMCELLS; j++)
                    oldcells[i][j] = cells[i][j];

          for (int i=0; i<NUMCELLS; i++)
               for (int j=0; j<NUMCELLS; j++) 
                    if (!fixed[i][j])
                         cells[i][j] = applyRule (i, j);
     }

     private double applyRule (int x, int y) {
          double sum = 0.0;

          if (x != 0) 
               sum += oldcells[x-1][y] * .125;
          if (x != NUMCELLS-1) 
               sum += oldcells[x+1][y] * .125;
          if (y != 0) 
               sum += oldcells[x][y-1] * .125;
          if (y != NUMCELLS-1) 
               sum += oldcells[x][y+1] * .125;
          sum += oldcells[x][y] * .5;

          return sum;
     }

     private void makeExample(int which) {
          clearCells();
          if (which == 1) {
               for (int i=0; i<NUMCELLS; i++)
                    cells[8][i] = 1;
               for (int i=0; i<NUMCELLS; i++) {
                    cells[7][i] = .81;
                    cells[9][i] = .81;
               }
               for (int i=0; i<NUMCELLS; i++) {
                    cells[6][i]  = .61;
                    cells[10][i] = .61;
               }
               for (int i=0; i<NUMCELLS; i++) {
                    cells[i][8] = 1;
               }
          }
          if (which == 2) {               // Makes a flame pattern
               for (int i=0; i<5; i++) {
                    cells[NUMCELLS-1-i][9]  = 1;
                    cells[NUMCELLS-1-i][10] = 1;
                    fixed[NUMCELLS-1-i][9]  = true;
                    fixed[NUMCELLS-1-i][10] = true;
               }
          }
     }

     private void fillRow (int row) {
          for (int i=0; i<NUMCELLS; i++)
               cells[row][i] = 1;
     }

     private void fillColumn(int col) {
          for (int i=0; i<NUMCELLS; i++)
               cells[i][col] = 1;
     }

     public String loadPicture () {
          String text = "";

          FileDialog loadfile = new
               FileDialog(this, "Load Heat picture", FileDialog.LOAD);
          loadfile.setFile("*.txt");
          loadfile.show();

          String filename = loadfile.getFile();
          String directory = loadfile.getDirectory();
          if (filename == null)
               return "";
          if (!filename.endsWith(".txt")) {
               System.err.println ("A picture file must have an extension of .txt.");
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
                    StringTokenizer st = new StringTokenizer(line);
                    int i = U.atoi(st.nextToken());
                    int j = U.atoi(st.nextToken());
                    double temp = U.atod(st.nextToken());
                    int fixedvalue = U.atoi(st.nextToken());
                    cells[i][j] = temp;
                    fixed[i][j] = (fixedvalue == 1);
               }
               is.close();
          }
          catch(IOException ioe) {
               System.err.println("Load picture:  i/o exception.");
          }
          return text;
     }

     public void savePicture () {
          FileDialog savefile =
               new FileDialog(this,"Save Heat Picture",FileDialog.SAVE);
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

                    for (int i=0; i<NUMCELLS; i++)
                         for (int j=0; j<NUMCELLS; j++) {
                              os.write(i+" "+j+" "+cells[i][j]+" ");
                              if (fixed[i][j])
                                   os.write("1\n");
                              else
                                   os.write("0\n");
                         }
                    os.close();
               }
               catch(IOException ioe) {
                    System.err.println("Save Picture:  i/o exception.");
               }
          }
     }

     class ColorLevel {
          double toplevel;
          Color color;
          public ColorLevel (double level, Color c) {
               toplevel = level;
               color = c;
          }
     }

     int numLevels = 8;
     ColorLevel[] levels = new ColorLevel[numLevels];
     Rectangle[] legendbox = new Rectangle[numLevels];

     private void initializeLevels() {
          double increment = 1.0/numLevels;
          double cutoff = 0;
          int index = 0;
          levels[index++] = new ColorLevel(cutoff, new Color(0,0,0));
          cutoff += increment;
          levels[index++] = new ColorLevel(cutoff, new Color(138,0,115));
          cutoff += increment;
          levels[index++] = new ColorLevel(cutoff, new Color(0,0,255));
          cutoff += increment;
          levels[index++] = new ColorLevel(cutoff, new Color(0,158,0));
          cutoff += increment;
          levels[index++] = new ColorLevel(cutoff, new Color(0,255,0));
          cutoff += increment;
          levels[index++] = new ColorLevel(cutoff, new Color(255,170,0));
          cutoff += increment;
          levels[index++] = new ColorLevel(cutoff, new Color(255,130,0));
          cutoff += increment;
          levels[index++] = new ColorLevel(cutoff, new Color(255,0,0));
     }

     private void drawLegend (Graphics g) { 
          int x = startingX + CELLSIZE * NUMCELLS + 20;
          int y = startingY + 30;
          for (int i=0; i<numLevels; i++) {
               g.setColor(levels[i].color);
               g.fillRect(x,y,CELLSIZE,CELLSIZE);
               legendbox[i] = new Rectangle(x,y,CELLSIZE,CELLSIZE);
               g.setColor(Color.black);
               g.drawString(levels[i].toplevel+"", x+CELLSIZE+5, y+CELLSIZE-5);
               y += CELLSIZE;
          }
     }

     private double mouseInLegend (int x, int y) {
          for (int i=0; i<numLevels; i++) {
               int rx = legendbox[i].x;
               int ry = legendbox[i].y;
               int rw = legendbox[i].width;
               int rh = legendbox[i].height;
               if (x >= rx && x <= rx + rw &&
                   y >= ry && y <= ry + rh)
                    return levels[i].toplevel + 0.1;
          }
          return -1;
     }

     private Color translateHeat2Color (double heat) {
          for (int i=numLevels-1; i>=0; i--)
               if (heat > levels[i].toplevel) 
                    return levels[i].color;
          return Color.black;
     }
}
