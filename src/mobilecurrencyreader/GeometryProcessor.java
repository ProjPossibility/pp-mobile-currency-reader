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
    public ByteBufferImage translateImage(ByteBufferImage orig, int tx, int ty);
   public ByteBufferImage rotateImage(ByteBufferImage orig, float angleOfRotation,double i0,double j0);
    public ByteBufferImage cropImage(ByteBufferImage orig, int tx, int ty, int newWidth, int newHeight);

    public ByteBufferImage scaleImage(ByteBufferImage orig, double sx, double sy);

    public ByteBufferImage extractImage(ByteBufferImage orig, VertexPoint verticeArray[]);   
    public VertexPoint[] detectAllVertices(ByteBufferImage img);
    public VertexPoint[] detectCornerVertices(ByteBufferImage img);
    
}
