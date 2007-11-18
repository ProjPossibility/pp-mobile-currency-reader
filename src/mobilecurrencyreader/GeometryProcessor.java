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
    public ByteBufferImage translateImage(ByteBufferImage orig, int tx, int ty);
    public ByteBufferImage rotateImage(ByteBufferImage orig, float angleOfRotation);
    public ByteBufferImage cropImage(ByteBufferImage orig, int tx, int ty, int newWidth, int newHeight);
    public ByteBufferImage scaleImage(ByteBufferImage orig, int sx, int sy);
    public ByteBufferImage extractImage(ByteBufferImage orig, Point verticeArray[]);   
    public ByteBufferImage detectVertices(ByteBufferImage img);
}
