// Riley Jenkins

/* This program loads an image from a source and then runs different edits to the image. This is all based on
 * using 3D arrays, random numbers, and nested loops.
*/

// For running an application
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

// For reading images from files.
import javax.imageio.*;
import java.io.*;
import java.net.*;

// For the dialog box:
import javax.swing.*;
import javax.swing.event.*;

public class ImageEditor extends Frame implements WindowListener 
{
        String selection;
        double brightnessValue;
        double image[][][]; 
        
        // main is called when the program starts
        public static void main(String args[]) 
        {
                // setup our application window
                ImageEditor app = new ImageEditor();

                // Ask for filename.  Fill in a default value.
                String filename = JOptionPane.showInputDialog("Enter filename or URL:", 
                                                              "http://www.eng.utah.edu/~cs1021/images/maui-wave.jpg");
                app.image = app.readImageFile(filename);
                        
                
                // Create a drop-down dialog box
                Object[] selectionValues = { "Normal", "Brightness", "Invert", "Add noise" };
                String initialSelection = "Normal";
                String dialogText = "How should I draw the image?";
                String dialogTitle = "Answer my question!";
                app.selection = (String) JOptionPane.showInputDialog(null, dialogText,
                                                                     dialogTitle, JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialSelection);
            
                if(app.selection.equals("Brightness"))
                {
                        JOptionPane optionPane = new JOptionPane();
                        JSlider slider = getSlider(optionPane);
                        optionPane.setMessage(new Object[] { "Select a value: ", slider });
                        optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
                        optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
                        JDialog dialog = optionPane.createDialog(null, "My Slider");
                        dialog.setVisible(true);
                        app.brightnessValue = ((Integer)optionPane.getInputValue()).intValue() / 100.0;
                        System.out.println(app.brightnessValue);
                }
            
                app.setSize(600, 600);
                app.setVisible(true);
                app.addWindowListener(app);
        }
        
        

        
        // paint is called when Java needs to repaint the window
        public void paint(Graphics g) 
        {       
                //Used to show where to draw the image
                int imageCoord[] = { 0, 50 };
                int textCoord[] = { 5, 40 };
                
                //This section of code draws the image as it was loaded from the source when Normal is selected
                if(selection.equals("Normal"))
                {
                        g.drawString("Normal", textCoord[0], textCoord[1]);
                        g.drawImage(createImage(image),imageCoord[0],imageCoord[1], null);              
                }
                
                
                //This Section of Code adjusts the brightness
                if(selection.equals("Brightness"))
                {
                 for(int x=0;x<image.length;x++)
        			for(int y=0;y<image[0].length;y++)
        			{
        				image[x][y][0]=Math.pow(image[x][y][0], brightnessValue);
        				image[x][y][1]=Math.pow(image[x][y][1], brightnessValue);
        				image[x][y][2]=Math.pow(image[x][y][2], brightnessValue); 
        			}
                		g.drawString("Brightness", textCoord[0], textCoord[1]);
                        g.drawImage(createImage(image),imageCoord[0],imageCoord[1], null);              
                }

                
                //This Section of Code inverts all the values in the image
                if(selection.equals("Invert"))
                {
                 for(int x=0;x<image.length;x++)
        			for(int y=0;y<image[0].length;y++)
        			{
        				image[x][y][0]=1 - image[x][y][0];
        				image[x][y][1]=1 - image[x][y][1];
        				image[x][y][2]=1 - image[x][y][2]; 
        			}
                		g.drawString("Invert", textCoord[0], textCoord[1]);
                        g.drawImage(createImage(image),imageCoord[0],imageCoord[1], null);              
                }
        				
                /*This Section of Code adds a little noise to the image(I did a little more than
                 was required to make the change more noticable.*/
                if(selection.equals("Add noise"))
                {
                 for(int x=0;x<image.length;x++)
        			for(int y=0;y<image[0].length;y++)
        			{
        				image[x][y][0]= image[x][y][0] + (Math.random()/1);
        				image[x][y][1]= image[x][y][1]+ (Math.random()/1);;
        				image[x][y][2]= image[x][y][2]+ (Math.random()/1);; 
        			}
                		g.drawString("Add noise", textCoord[0], textCoord[1]);
                        g.drawImage(createImage(image),imageCoord[0],imageCoord[1], null);              
                }
                
        }
        
        
        // **************************************************
        // **** DON'T CHANGE THE CODE BELOW THIS POINT! *****
        // **************************************************

        public void windowActivated(WindowEvent e) {}
        public void windowDeactivated(WindowEvent e) {}
        public void windowOpened(WindowEvent e) {}
        public void windowClosed(WindowEvent e) {}
        public void windowClosing(WindowEvent e) {System.exit(0);}
        public void windowIconified(WindowEvent e) {}
        public void windowDeiconified(WindowEvent e) {}
                
        // Given a filename, reads in the image and stores it in an array of doubles.
        public double[][][] readImageFile(String filename)
        {
                BufferedImage image = null;
                System.out.println("Please wait...loading image...");
                System.out.flush();
                try
                {
                        image = ImageIO.read(new File(filename));
                }
                catch(Exception e)
                {
                        try
                        {
                                image = ImageIO.read(new URL(filename));
                        }
                        catch(Exception ex)
                        {
                                // Return an image with one black pixel if error.
                                System.out.println("Couldn't read file " + filename );
                                double ret[][][]= new double[1][1][3];
                                ret[0][0][0]=0;
                                ret[0][0][1]=0;
                                ret[0][0][2]=0;
                                return ret;
                                // System.exit(1);      
                        }
                }
                        
                System.out.println("Finished reading a " + image.getWidth() + "x" + image.getHeight());
                
                double ret[][][] = new double[image.getWidth()][image.getHeight()][3];
                int oneD[] = image.getRGB(0,0,image.getWidth(),image.getHeight(),null,0,image.getWidth());
                for(int i=0; i<image.getHeight(); i++)
                        for(int j=0; j<image.getWidth(); j++)
                                for(int k=2;k>=0; k--)
                                {
                                        ret[j][i][k] = (oneD[i*image.getWidth()+j] & 0x000000FF)/255.0;
                                        oneD[i*image.getWidth()+j] = oneD[i*image.getWidth()+j] >> 8; 
                                }
                                
                
                return ret;
        }
        
        
        /* Creates a grayscale image from a 2D array of doubles.  0=black, 1=white */
        public BufferedImage createImage(double image[][])
        {
                // Create the 2D array of ints that we will fill in             
                int imageInt[][] = new int[image.length][];
                
                // For each row
                for(int i=0; i<image.length; i++)
                {
                        // Create the row
                        imageInt[i]=new int[image[i].length];
                        // For each pixel in the row
                        for(int j=0; j<image[i].length; j++)
                                // Convert it to an int 0--255.
                                imageInt[i][j]=(int)Math.round(image[i][j]*255);
                }
                
                // Create an image from the integer array we made and return the image.
                return createImage(imageInt);
        }
        
        
        /* Creates an image from a 3D array of doubles.  0=black, 1=white */
        public BufferedImage createImage(double image[][][])
        {
                // Create the 2D array of ints that we will fill in             
                int imageInt[][][] = new int[image.length][][];
                
                // For each row
                for(int i=0; i<image.length; i++)
                {
                        // Create the row
                        imageInt[i]=new int[image[i].length][];

                        // For each pixel in the row
                        for(int j=0; j<image[i].length; j++)
                        {
                                // Create the pixel
                                imageInt[i][j]=new int[3];
                                
                                // for each color in the pixel
                                for(int k=0; k<3; k++)
                                        // Convert it to an int 0--255.
                                        imageInt[i][j][k]=(int)Math.round(image[i][j][k]*255);
                        }
                }
                
                // Create an image from the integer array we made and return the image.
                return createImage(imageInt);
        }
        
        // Creates a grayscale image from an array of ints.  0=black, 255=white.
        public BufferedImage createImage(int image[][])
        {
                int width = image.length;
                int height = image[0].length;
                                
                // Convert the 2D data into a 1D array:
                int oneD[] = new int[width*height];
                // For each row
                for(int i=0; i<width; i++)
                {
                        // Check if this row has the correct number of pixels
                        if(image[i].length != height)
                        {
                                System.out.println("Image is not rectangular");
                                return null;
                        }

                        // for each column
                        for(int j=0; j<height; j++)
                        {
                                // check for invalid values
                                if(image[i][j] > 255)
                                        image[i][j]=255;
                                else if(image[i][j] < 0)
                                        image[i][j]=0;
                                
                                // Combine each pixel into one int.
                                // First 8 bits are alpha value (255=opaque)
                                // Then red, green, and blue (8-bits each)
                                oneD[j*width+i] = 0xFF000000 |
                                        (0x000000FF & image[i][j]) << 16 |
                                        (0x000000FF & image[i][j]) << 8 |
	                                (0x000000FF & image[i][j]);
                        }
                }
                
                
                BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
                img.setRGB(0,0,width,height,oneD,0,width);
                return img;
        }
        
        // Creates a color image from an array of ints.  0=off, 255=on.
        public BufferedImage createImage(int image[][][])
        {
                int width = image.length;
                int height = image[0].length;
                                                
                // Convert the 2D data into a 3D array:
                int oneD[] = new int[width*height];
                // For column
                for(int i=0; i<width; i++)
                {
                        // Check if column has the correct number of pixels
                        if(image[i].length != height)
                        {
                                System.out.println("Image is not rectangular");
                                return null;
                        }

                        // for each row
                        for(int j=0; j<height; j++)
                        {
                                if(image[i][j].length!=3)
                                {
                                        System.out.println("Image does not have 3 colors");
                                        return null;
                                }

                                // Check for invalid values.
                                for(int k=0; k<3; k++)
                                        if(image[i][j][k] > 255)
                                                image[i][j][k] = 255;
                                        else if(image[i][j][k] < 0)
                                                image[i][j][k] = 0;

                                
                                // Combine each pixel into one int.
                                // First 8 bits are alpha value (255=opaque)
                                // Then red, green, and blue (8-bits each)
                                oneD[j*width+i] = 0xFF000000 |
                                        (0x000000FF & image[i][j][0]) << 16 |
                                        (0x000000FF & image[i][j][1]) << 8 |
                                        (0x000000FF & image[i][j][2]);
                        }
                }
                
                
                BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
                img.setRGB(0,0,width,height,oneD,0,width);
                return img;
        }

        // This method is used to create a dialog box
        // that has a slider on it.
        static JSlider getSlider(final JOptionPane optionPane) 
        {
                JSlider slider = new JSlider();
                slider.setMinimum(0);
                slider.setMaximum(200);
                slider.setValue(100);
                slider.setMajorTickSpacing(50);
                slider.setPaintTicks(true);
                slider.setPaintLabels(false);
                optionPane.setInputValue(new Integer(100)); 

                ChangeListener changeListener = new ChangeListener() {
                        public void stateChanged(ChangeEvent changeEvent) {
                                JSlider theSlider = (JSlider) changeEvent.getSource();
                                if (!theSlider.getValueIsAdjusting()) {
                                        optionPane.setInputValue(new Integer(theSlider.getValue()));
                                }
                        }
                };
                slider.addChangeListener(changeListener);
                return slider;
        }

}