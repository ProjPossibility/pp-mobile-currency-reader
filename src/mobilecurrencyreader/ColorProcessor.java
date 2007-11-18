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
public interface ColorProcessor {
    public ByteBufferImage convertToGrayscale(byte original[][], int height, int width);
    public ByteBufferImage expandDynamicRange(ByteBufferImage original);
    public ByteBufferImage expandDynamicRange(ByteBufferImage original, int x0, int y0, int x1, int y1);
    public ByteBufferImage expandDynamicRange(ByteBufferImage original, int x0, int y0, int x1, int y1, int m, int n);
    public ByteBufferImage quantize(ByteBufferImage original,  int N);
    public double subtractImage(ByteBufferImage a, ByteBufferImage b);
}
