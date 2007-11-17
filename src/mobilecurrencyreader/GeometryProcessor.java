/*
 * GeometryProcessor.java
 *
 * Created on November 17, 2007, 12:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mobilecurrencyreader;

/**
 *
 * @author binarygame
 */
public interface GeometryProcessor {
    public  byte [] translateImage(byte[], int height,int width, int tx, int ty);
     public byte [] rotateImage(byte[], int height,int width, int tx, int ty);
    public byte [] scaleImage(byte[], int height,int width, int tx, int ty);
     
    
    
    
    
}
