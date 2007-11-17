/*
 * Main.java
 *
 * Created on November 17, 2007, 10:34 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ss12;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;

/**
 *
 * @author OM
 */
public class Main {
    
    static BufferedImage bufferedImage;
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
        File input = new File("");
        bufferedImage = ImageIO.read(input);
        }catch(IOException e){
            e.printStackTrace();
        }
       // bufferedImage.getRGB()
    }
    
    public void rotate(int degrees,int anchorX,int anchorY){
        
        BufferedImage sourceBi;/* here we have to convert*/
                                
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(degrees * Math.PI / 180.0,anchorX,anchorY); 
         BufferedImageOp bio = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
           BufferedImage destinationBI = bio.filter(sourceBi, null);
        
    }
    
    public void scale(){
        
    }
    
    public void translate(){
        
    }
    
    public BufferedImage convertToGraySclae(BufferedImage img){
       int width=img.getWidth();
       int height = img.getHeight();
       int type = java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
       BufferedImage grayImage = new java.awt.image.BufferedImage(width, height, type);
       Graphics2D g = grayImage.createGraphics();
       g.drawRenderedImage(img, null);
       g.dispose();
       return grayImage;
    }
    
    
    public  byte [] translateImage(byte orig[], int width,int height, int tx, int ty){
     BufferedImage sourceBi; /* have to initialise here */
     
     Graphics2D g = sourceBi.createGraphics();
     g.translate(tx,ty);
     
     
     
    }
    
    /**
     This functions return the buffered image given the co-ordinates.Expects that the image is properly aligned.
     */
    public BufferedImage getImage(int startX,int startY,int width,int height,File input){
        BufferedImage reader;
        try {
            reader = ImageIO.read(input);
            return reader.getSubimage(startX,startY,width,height);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    return null;  
    }
    
    public void cropImage(int startX,int startY,int width,int height){
       int[] rgbVal = bufferedImage.getRGB(startX,startY,width,height,null,0,0);
       int result[] = null;
       int j =0;
       for(int i=0;i<rgbVal.length;i++){
            if(rgbVal[i] == 0xFFFFFF){
                continue;
            }else{
                    result[j] =rgbVal[i];
                    j++;
              }
            }
       }
    }

