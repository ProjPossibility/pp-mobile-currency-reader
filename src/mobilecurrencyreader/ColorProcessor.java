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
    public byte[] convertToGrayscale(byte original[], int height, int width);
    public byte[] expandDynamicRange(byte original[], int height, int width);
    public byte[] quantize(byte original[],  int N);
    public double subtractImage(byte a[], byte b[], int height, int width);
}
