/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cornerdetection;
import harriscornerdetector.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

/**
 *
 * @author Nimish
 */


public class Main {

    static int M = 400;
    static int N = 200;
    
    public static void DisplayImage(int finalOut[],String filename, int displayImageWidth, int displayImageHeight,int flag)
    {
       
        BufferedImage output = new BufferedImage(displayImageWidth, displayImageHeight, BufferedImage.TYPE_INT_RGB);
        // update the image with pixels[]
        String ext = "jpg";
        File file = new File(filename + "." + ext);
        output.setRGB(0, 0, displayImageWidth, displayImageHeight, finalOut, 0, displayImageWidth);
        try
        {
            ImageIO.write(output, "jpg",file);
        }
        catch(IOException e2) 
        {
                System.out.println("error: " + e2);
        }
       

    }

    
    /**
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        int index = 1;
        
            // Initialization
            PixelGrabber grabber = null;
            int width = 0, height = 0;	
            String fileName =  index + ".jpg";        
            double threshold=(double)1;
            int orig[] = null, originalBuf[] = null, cornerOut[] = null, corners[][] = {{0,0},{0,0},{0,0},{0,0}};
            Image image = null, originalImg =null;
            harris harrisOp; 

            try
            {            
                // Keep a copy of original and original buffer.
                BufferedImage frame = ImageIO.read(new File(fileName));
                int w = frame.getWidth(null);
                int h = frame.getHeight(null);
                originalImg = ImageIO.read(new File(fileName));
                originalBuf=new int[w*h];
                grabber = new PixelGrabber(originalImg, 0, 0, w, h, originalBuf, 0, w);
                grabber.grabPixels();
                cornerOut=new int[w*h];
                //DisplayImage(originalBuf,"OriginalImage",w,h,0);

                // Image scaled to 256 X 256 fro Harris Corner Detection Algo.
                image = Toolkit.getDefaultToolkit().getImage(fileName);            
                image = image.getScaledInstance(256, 256, Image.SCALE_SMOOTH);	
                width = 256;
                height = 256;
                orig=new int[width*height];
                grabber = new PixelGrabber(image, 0, 0, width, height, orig, 0, width);
                grabber.grabPixels();

                // Apply Harric Corner Detection Algo. over the image.
                harrisOp = new harris();
                harrisOp.init(orig,width, height, threshold / 100);
                orig=harrisOp.process(); 
               // DisplayImage(orig,"Output_After_CornerDetection",width,height,0);

                // Store the corners of the original image and discard the 256 X 256 image.
                Image output = null;//Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(256, 256, orig, 0, 256));
                corners = harrisOp.findCorners(orig, width, height);
                for(int count = 0; count < 4; count++)
                {
                    corners[count][0] = corners[count][0]*w/256;
                    corners[count][1] = corners[count][1]*h/256;
                }

                // Actual processing on the image begins.

                // Set the corners.
                HelperFunctions hf = new HelperFunctions();
                ImageData imgData = new ImageData();            
                imgData.setCorners(corners);

                // Find the center of the image and the angle of rotation.
                hf.findAngleAndCenter(imgData);


                // Rotate the original image about the center by the angle of rotation.
                cornerOut = hf.rotateImage(originalBuf,h,w,imgData.getXCenter(),imgData.getYCenter(),imgData.getRotAngle());
                //DisplayImage(cornerOut,"Rotated-Image",w,h,0);

                // Generate the new corners after rotation.
                corners  = hf.getNewCorners(corners, w,h,imgData.getXCenter(), imgData.getYCenter(),imgData.getRotAngle());

                // Extract the final bill from the background using the new corners.
                int[] finalOut = hf.extractImage(cornerOut, w, h, corners);
                // DisplayImage(finalOut,"p2", hf.ExtactedImageWidth, hf.ExtactedImageHeight,0);

                // Scale the bill to size M X N.
                output = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(hf.ExtactedImageWidth, hf.ExtactedImageHeight, finalOut, 0, hf.ExtactedImageWidth));
                output = output.getScaledInstance(M, N, Image.SCALE_SMOOTH);
                int temp [] = new int[M*N];
                grabber = new PixelGrabber(output, 0, 0,M,N, temp, 0,M);
                grabber.grabPixels();
                DisplayImage(temp,"output" +index, M, N,1);

            }
            catch(Exception e2) {
                    System.out.println("error: " + e2);
            }
            

    }
    
    
}
