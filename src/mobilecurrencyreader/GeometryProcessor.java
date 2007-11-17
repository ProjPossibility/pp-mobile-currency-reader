/*
 * GeometryProcessor.java
 *
 * Created on November 17, 2007, 12:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mobilecurrencyreader;
import java.awt.Point;

/**
 *
 * @author binarygame
 */
public interface GeometryProcessor {
    public  byte [] translateImage(byte orig[], int width,int height, int tx, int ty);
    public byte [] rotateImage(byte orig[], int width,int height, float angleOfRotation);
    public byte []  scaleImage(byte orig[], int width,int height, float sx, float sy);
    public byte []  extractImage(byte orig[], int width, int height,Point  verticeArray[] );   
    public Point[] detectVertices(byte img[], int width, int height);
}
