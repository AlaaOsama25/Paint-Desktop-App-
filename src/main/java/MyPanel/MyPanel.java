
package MyPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MyPanel extends JPanel {

    int x1 = 0;
    int y1 = 0;
    int x2;
    int y2;
    BufferedImage img;
    Vector<Shapes> AllShapes = new Vector<Shapes>();
    Vector<Shapes> FreeHandArr = new Vector<Shapes>();
     Vector<Shapes> erase_indexes = new Vector<Shapes>();
    String[] Shapes = {"Rectangle", "Line", "Oval"};
    String[] Colors = {"Black", "Blue", "Green", "Pink", "Yellow", "Red"};
    String[] Delete = {"ClearAll", "Erase"};
    String[] Options = {"Save", "Load", "Undo"};
    String SelectedShape = "Rectangle";
    String SelectedColor = "Black";
    String SelectedDeleteAction = "";
    String selectedOption = "";
    int IsdottedLine = 0;
    int IsFilled = 0;
    int IsFreeHand = 0;
    int undo = 0;
    Color FillColor;

    public MyPanel() {

        this.setBackground(Color.white);

        ///------------------------------------ DropDown Lists ------------------------------------------///
        JComboBox<String> jComboBoxShapes = new JComboBox<>(Shapes);
        JComboBox<String> jComboBoxColor = new JComboBox<>(Colors);
        JComboBox<String> jComboBoxDelete = new JComboBox<>(Delete);
        JComboBox<String> jComboBoxOptions = new JComboBox<>(Options);
        this.add(jComboBoxShapes);
        this.add(jComboBoxColor);
        this.add(jComboBoxDelete);
        this.add(jComboBoxOptions);
        this.setVisible(true);
        this.setVisible(true);
        this.setVisible(true);
        this.setVisible(true);

        jComboBoxShapes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectedDeleteAction = "";
                SelectedShape = jComboBoxShapes.getItemAt(jComboBoxShapes.getSelectedIndex());
            }
        });

        jComboBoxColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  SelectedDeleteAction = "";
                SelectedColor = jComboBoxColor.getItemAt(jComboBoxColor.getSelectedIndex());
            }
        });

        jComboBoxDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectedDeleteAction = jComboBoxDelete.getItemAt(jComboBoxDelete.getSelectedIndex());
                if (SelectedDeleteAction.equalsIgnoreCase("clearall")) {
                    AllShapes.removeAllElements();
                    FreeHandArr.removeAllElements();
                    erase_indexes.removeAllElements();
                    repaint();
                }
            }
        });

        jComboBoxOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedOption = jComboBoxOptions.getItemAt(jComboBoxOptions.getSelectedIndex());
                  SelectedDeleteAction = "";
                switch (selectedOption) {
                    case "Undo":
                        undo();
                        break;
                    case "Save":
                        saveImg();
                        break;
                    case "Load":
                        loadImg();
                        break;
                }

            }
        }
        );

        //----------------------------- Check Boxes -----------------------------//
        JCheckBox checkbox1 = new JCheckBox("Filled");
        JCheckBox checkbox2 = new JCheckBox("Dotted Line");
        JCheckBox checkbox3 = new JCheckBox("Free Hand");
        checkbox1.setBounds(200, 150, 50, 50);
        checkbox2.setBounds(150, 150, 50, 50);
        checkbox3.setBounds(150, 150, 50, 50);
        this.add(checkbox1);
        this.add(checkbox2);
        this.add(checkbox3);

        checkbox1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
            
                IsFilled = e.getStateChange();
            }
             
           
        });
        checkbox2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                
             
                IsdottedLine = e.getStateChange();
            }
        });
        checkbox3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                SelectedDeleteAction = "";
                IsFreeHand = e.getStateChange();
                
            }
        });

//--------------------------------- Mouse Action --------------------------//
        this.setFocusable(true);
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                x2 = e.getX();
                y2 = e.getY();
                if(!SelectedDeleteAction.equalsIgnoreCase("erase"))
                AllShapes.add(new Shapes(x1, y1, x2, y2, SelectedShape, SelectedColor, IsFilled, IsdottedLine));

            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if (IsFreeHand != 1) {
                    x2 = e.getX();
                    y2 = e.getY();
                    repaint();
                } else if(IsFreeHand==1 && !SelectedDeleteAction.equalsIgnoreCase("erase")) {

                    x2 = e.getX();
                    y2 = e.getY();
                    Graphics g = getGraphics();
                    g.setColor(getColor(SelectedColor));
                    g.drawLine(x1, y1, x2, y2);
                    FreeHandArr.add((new Shapes(x1, y1, x2, y2, "line", SelectedColor, 1, 0)));
                    x1 = x2;
                    y1 = y2;
                }
                if ( SelectedDeleteAction.equalsIgnoreCase("Erase") ) {
                    x1 = e.getX();
                    y1 = e.getY();
                    Graphics g = getGraphics();
                    g.setColor(Color.WHITE);
                    g.fillRect(x1, x1, 10, 10);
                    erase_indexes.add(new Shapes(x1, y1, x1 + 10, y1 + 10, "rectangle", "White", 1, 0));
                    repaint();
                    
                  
                }

            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }

        });

    }

    class Shapes {

        protected int x1;
        protected int y1;
        protected int x2;
        protected int y2;
        String Tname;
        String Tcolor;
        int Isfilled;
        int Isdotted;

        public Shapes(int x1, int y1, int x2, int y2, String Tname, String Tcolor, int Isfilled, int Isdotted) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.Tname = Tname;
            this.Tcolor = Tcolor;
            this.Isdotted = Isdotted;
            this.Isfilled = Isfilled;
        }

    }

    public void saveImg() {
        BufferedImage image_save = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image_save.createGraphics();
        paint(g2);
        try {
            ImageIO.write(image_save, "png", new File("gui" + (int) Math.random() * (10 - 1) + 1 + "." + "png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadImg() {
        BufferedImage image_load;
        try {
            image_load = ImageIO.read(new File("gui.png"));
            JLabel picLabel = new JLabel(new ImageIcon(image_load));
            add(picLabel);

            repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void undo() {
        undo = 1;
        if(IsFreeHand==1)
        {
           
            
            if(FreeHandArr.size()!=0)
                FreeHandArr.remove(FreeHandArr.size()-1);
            selectedOption = "";
            repaint();
        }
         else  
        {
            if (AllShapes.size() != 0) {
            AllShapes.remove(AllShapes.size() - 1);
            System.out.println("E "+AllShapes.size());
            selectedOption = "";
            repaint();
        } 
        }
        

    }

    public Color getColor(String color_string) {
        Color color_returned = Color.BLACK;
        switch (color_string) {
            case "Red":
                color_returned = Color.red;
                break;
            case "Blue":
                color_returned = Color.blue;
                break;

            case "Green":
                color_returned = Color.green;
                break;
            case "Yellow":
                color_returned = Color.yellow;
                break;
            case "Pink":
                color_returned = Color.pink;
                break;
            case "Black":
                color_returned = Color.black;
                break;
            case "White":
                color_returned = Color.WHITE;
                break;

        }

        return color_returned;
    }

    public void drawOldLine(Shapes shape, Graphics g, Graphics2D g2d) {

        FillColor = getColor(shape.Tcolor);
        if (shape.Isdotted == 1) {

            g2d.setColor(FillColor);
            g2d.drawLine(shape.x1, shape.y1, shape.x2, shape.y2);
        

        } else {

            g.setColor(FillColor);
            g.drawLine(shape.x1, shape.y1, shape.x2, shape.y2);

        }

    }

    public void drawOldOval(Shapes shape, Graphics g, Graphics2D g2d) {
       
        FillColor = getColor(shape.Tcolor);
        if (shape.Isdotted == 1) {

            g2d.setColor(FillColor);
            if (shape.Isfilled == 1) {
                g2d.fillOval(Math.min(shape.x1, shape.x2), Math.min(shape.y1, shape.y2), Math.max(shape.x1, shape.x2) - Math.min(shape.x1, shape.x2), Math.max(shape.y1, shape.y2) - Math.min(shape.y1, shape.y2));
            } else {
                g2d.drawOval(Math.min(shape.x1, shape.x2), Math.min(shape.y1, shape.y2), Math.max(shape.x1, shape.x2) - Math.min(shape.x1, shape.x2), Math.max(shape.y1, shape.y2) - Math.min(shape.y1, shape.y2));
            }
         

        } else if (shape.Isfilled == 1) {
            g.setColor(FillColor);
            g.fillOval(Math.min(shape.x1, shape.x2), Math.min(shape.y1, shape.y2), Math.max(shape.x1, shape.x2) - Math.min(shape.x1, shape.x2), Math.max(shape.y1, shape.y2) - Math.min(shape.y1, shape.y2));

        } else {
            g.setColor(FillColor);
            g.drawOval(Math.min(shape.x1, shape.x2), Math.min(shape.y1, shape.y2), Math.max(shape.x1, shape.x2) - Math.min(shape.x1, shape.x2), Math.max(shape.y1, shape.y2) - Math.min(shape.y1, shape.y2));

        }

    }

    public void drawOldRec(Shapes shape, Graphics g, Graphics2D g2d) {
        
        FillColor = getColor(shape.Tcolor);
        if (shape.Isdotted == 1) {

            g2d.setColor(FillColor);
            if (shape.Isfilled == 1) {

                g2d.fillRect(Math.min(shape.x1, shape.x2), Math.min(shape.y1, shape.y2), Math.max(shape.x1, shape.x2) - Math.min(shape.x1, shape.x2), Math.max(shape.y1, shape.y2) - Math.min(shape.y1, shape.y2));

            } else {
                g2d.drawRect(Math.min(shape.x1, shape.x2), Math.min(shape.y1, shape.y2), Math.max(shape.x1, shape.x2) - Math.min(shape.x1, shape.x2), Math.max(shape.y1, shape.y2) - Math.min(shape.y1, shape.y2));

            }

        } else if (shape.Isfilled == 1) {
            g.setColor(FillColor);
            g.fillRect(Math.min(shape.x1, shape.x2), Math.min(shape.y1, shape.y2), Math.max(shape.x1, shape.x2) - Math.min(shape.x1, shape.x2), Math.max(shape.y1, shape.y2) - Math.min(shape.y1, shape.y2));

        } else {
            g.setColor(FillColor);
            g.drawRect(Math.min(shape.x1, shape.x2), Math.min(shape.y1, shape.y2), Math.max(shape.x1, shape.x2) - Math.min(shape.x1, shape.x2), Math.max(shape.y1, shape.y2) - Math.min(shape.y1, shape.y2));

        }

    }

    public void drawNewLine(Graphics g, Graphics2D g2d) {
        FillColor = getColor(SelectedColor);
        if (IsdottedLine == 1) {

            g2d.setColor(FillColor);
            g2d.drawLine(x1, y1, x2, y2);
        

        } else {
            g.setColor(FillColor);
            g.drawLine(x1, y1, x2, y2);

        }

    }

    public void drawNewOval(Graphics g, Graphics2D g2d) {
        FillColor = getColor(SelectedColor);
      
        if (IsdottedLine == 1) {

            g2d.setColor(FillColor);

            if (IsFilled == 1) {
                g2d.fillOval(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2) - Math.min(x1, x2), Math.max(y1, y2) - Math.min(y1, y2));

            } else {
                g2d.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2) - Math.min(x1, x2), Math.max(y1, y2) - Math.min(y1, y2));
            }

        } else if (IsFilled == 1) {
            g.setColor(FillColor);
            g.fillOval(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2) - Math.min(x1, x2), Math.max(y1, y2) - Math.min(y1, y2));

        } else {
            g.setColor(FillColor);
            g.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2) - Math.min(x1, x2), Math.max(y1, y2) - Math.min(y1, y2));
        }

    }

    public void drawNewRec(Graphics g, Graphics2D g2d) {
        FillColor = getColor(SelectedColor);
      
        if (IsdottedLine == 1) {

            g2d.setColor(FillColor);
            if (IsFilled == 1) {
                g2d.fillRect(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2) - Math.min(x1, x2), Math.max(y1, y2) - Math.min(y1, y2));

            } else {
                g2d.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2) - Math.min(x1, x2), Math.max(y1, y2) - Math.min(y1, y2));
            }

        } else if (IsFilled == 1) {
            g.setColor(FillColor);
            g.fillRect(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2) - Math.min(x1, x2), Math.max(y1, y2) - Math.min(y1, y2));

        } else {
            g.setColor(FillColor);
            g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2) - Math.min(x1, x2), Math.max(y1, y2) - Math.min(y1, y2));
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);

        if (!SelectedDeleteAction.equalsIgnoreCase("ClearAll")) {

            ///draw old shapes
            for (int i = 0; i < AllShapes.size(); i++) {

                if (AllShapes.get(i).Tname.equalsIgnoreCase("Line")) {
                    drawOldLine(AllShapes.get(i), g, g2d);
                } else if (AllShapes.get(i).Tname.equalsIgnoreCase("Rectangle")) {
                    drawOldRec(AllShapes.get(i), g, g2d);
                } else if (AllShapes.get(i).Tname.equalsIgnoreCase("Oval")) {
                    drawOldOval(AllShapes.get(i), g, g2d);
                }

            }
              for(int i=0; i<FreeHandArr.size();i++)
            {
                drawOldLine(FreeHandArr.get(i), g, g2d);
            }

            for(int i=0; i<erase_indexes.size();i++)
            {
                
                drawOldRec(erase_indexes.get(i), g, g2d);
            }
            
          
            ///for new drawing
            if(!SelectedDeleteAction.equalsIgnoreCase("erase"))
            {
                  if (undo != 1) {

                if (SelectedShape.equalsIgnoreCase("rectangle")) {
                    drawNewRec(g, g2d);
                } else if (SelectedShape.equalsIgnoreCase("Oval")) {
                    drawNewOval(g, g2d);
                } else if (SelectedShape.equalsIgnoreCase("line")) {
                    drawNewLine(g, g2d);
                }

            }
            }
          
            undo = 0;
        } else {
            SelectedDeleteAction = "";
        }
    }
}
