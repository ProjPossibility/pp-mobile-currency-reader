/*
 * ColorProcessorJ2SE.java
 *
 * Created on November 17, 2007, 1:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mobilecurrencyreader;

import javax.crypto.NullCipher;

/**
 *
 * @author Kevin
 */
public class ColorProcessorJ2SE implements ColorProcessor {
    
    /** Creates a new instance of ColorProcessorJ2SE */
    public ColorProcessorJ2SE() {
    }

    public byte[] convertToGrayscale(byte[] original, int height, int width) {
        return null;
    }

    public byte[] expandDynamicRange(byte[] original, int height, int width) {
        return null;
    }

    public byte[] quanize(byte[] original, int height, int width, int N) {
        return null;
    }

    public double subtractImage(byte[] a, byte[] b, int height, int width) {
        return 0;
    }
    
}
