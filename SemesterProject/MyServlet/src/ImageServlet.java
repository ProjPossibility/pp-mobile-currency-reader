import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;

import harriscornerdetector.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.net.*;


import org.projectpossibility.Base64;

/**
Copyright (C) 2006 Media Informatics Group (www.mimuc.de), 
University of Munich, Contact person: Enrico Rukzio
(Enrico.Rukzio@ifi.lmu.de)

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

2008, April 30.  This code has been adapted for use with the Mobile Currency Reader.
The ImageServlet class provides the interface to the mobile phone and receives and
processes the image with a bill using the Harris Corner Detection Algorithm.

Changes are mostly found in the function
	public void doPost(HttpServletRequest request, HttpServletResponse response)

There is also the addition of the DisplayImage function, which writes out an image to
server (mostly used as a debug to show image status)

*/
public class ImageServlet extends HttpServlet {

    static int M = 400;
    static int N = 200;
    
    /*
     * Display Image is used to write out a file to c:/Image.  This is used as a debug tool
     */
    public static void DisplayImage(int finalOut[],String filename, int displayImageWidth, int displayImageHeight,int flag)
    {
       
        BufferedImage output = new BufferedImage(displayImageWidth, displayImageHeight, BufferedImage.TYPE_INT_RGB);
        // update the image with pixels[]
        String ext = "jpg";
        File file = new File("C:/Image/"+filename + "." + ext);
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
    
    /*
     * Display Image is used to write out a file to c:/Image.  This is used as a debug tool
     */
    public String ProcessImage(String fileName)
    {

         // Initialization
         PixelGrabber grabber = null;
         int width = 0, height = 0;	       
         double threshold=(double)1;
         int orig[] = null, originalBuf[] = null, cornerOut[] = null, corners[][] = {{0,0},{0,0},{0,0},{0,0}};
         Image image = null, originalImg =null;
         harris harrisOp; 
         String returnFilename = null;

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
             DisplayImage(orig,"1Output_After_CornerDetection",width,height,0);

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
             DisplayImage(cornerOut,"2Rotated-Image",w,h,0);

             // Generate the new corners after rotation.
             corners  = hf.getNewCorners(corners, w,h,imgData.getXCenter(), imgData.getYCenter(),imgData.getRotAngle());

             // Extract the final bill from the background using the new corners.
             int[] finalOut = hf.extractImage(cornerOut, w, h, corners);
             DisplayImage(finalOut,"3extracted_preresizing", hf.ExtactedImageWidth, hf.ExtactedImageHeight,0);

             // Scale the bill to size M X N.
             output = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(hf.ExtactedImageWidth, hf.ExtactedImageHeight, finalOut, 0, hf.ExtactedImageWidth));
             output = output.getScaledInstance(M, N, Image.SCALE_SMOOTH);
             int temp [] = new int[M*N];
             grabber = new PixelGrabber(output, 0, 0,M,N, temp, 0,M);
             grabber.grabPixels();
             DisplayImage(temp,"4Final", M, N,1);

         }
         catch(Exception e2) {
                 System.out.println("error: " + e2);
         }
         returnFilename = "4Final.jpg";
         return returnFilename;
    }

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		InputStream in = request.getInputStream();
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		StringBuffer buf = new StringBuffer();
		String line;
		while ((line = r.readLine()) != null) {
			buf.append(line);
		}
		String s = buf.toString();
		// Write the image of the bill from the cell phone to a file
		FileOutputStream f1;
		File directory = new File("C:/Image/");
	    	directory.mkdirs();
	   	String imageName = "C:/Image/image.png";
		byte[] data = Base64.decode(s);
		try {
			f1 = new FileOutputStream(imageName);
			f1.write(data);
			f1.close();
		} catch (IOException e) {
			System.out.println("Problems creating the file");
		}
		
		/*
		 * ProcessImage takes in an arbitrary image and returns an MxN image of the bill.
		 * Currently it is set to return a 400x200 pixel image.
		 */
		
		String billFileName;
		
		billFileName = ProcessImage(imageName);

		/*
		 * ProcessBill should take in the filename of the bill (or raw data of the bill)
		 * and return the value of the bill.  This was intended to be the Neural Network,
		 * but we were unable to implement this block in Java.
		 */
		// String ImageValue;
		// ImageValue = ProcessBill (billFileName);

		/*
		 * Here, the return value in ImageValue is returned to the cell phone using sockets
		 * This part of the code has some issues (as stated in the CameraMIDlet section)
		 */			
		
 		/*
 		//start socket server
 		ServerSocket serverSocket = null;
 		try {
 		    serverSocket = new ServerSocket(4444,32,InetAddress.getByName("192.168.1.103"));
 		} catch (IOException e) {
 		    System.err.println("Could not listen on port: 4444.");
 		    System.exit(1);
 		}
 		
 		Socket clientSocket = null;
 		try {
 		    clientSocket = serverSocket.accept();
 		} catch (IOException e) {
 		    System.err.println("Accept failed.");
 		    System.exit(1);
 		}
 		
 		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
 		out.println(ImageValue);
 		out.close();
 		clientSocket.close();
 		serverSocket.close();
 		
		*/
		
		
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
		doPost(req, resp);
	}
}




