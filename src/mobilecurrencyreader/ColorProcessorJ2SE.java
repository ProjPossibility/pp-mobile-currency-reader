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

    public ByteBufferImage quantize(ByteBufferImage original, int N) {
        return null;
    }

    public double subtractImage(ByteBufferImage a, ByteBufferImage b) {
        return 0;
    }

    public ByteBufferImage convertToGrayscale(byte[][] original, int height, int width) {
        return null;
    }
    
    public double[] calculateHistogram(ByteBufferImage original) {
        double hist[] = new double[256];
        
        // zero out all elements of histogram array
        for (int i = 0; i < 256; i++)
            hist[i] = 0.0f;
        
        // increment histogram "bucket" for each pixel
        for (int i = 0; i < original.bytes.length; i++)
            hist[(int)(original.bytes[i]&0xff)]++;
        
        // calculate max
        double max = 0.0;
        for (int i = 0; i < 256; i++)
            if (hist[i] > max)
                max = hist[i];
        
        // scale
        for (int i = 0; i < 256; i++)
            hist[i] /= max;
      
        return hist;
    }
    
    private static double calculateStdDev(ByteBufferImage image, double mean) {
        double sum = 0.0;
        for (int i = 0; i < image.bytes.length; i++)
            sum += Math.pow((double)(image.bytes[i]&0xff) - mean, 2);
        return Math.sqrt(sum/image.bytes.length);
    }
    
    private static double calculateMean(ByteBufferImage image) {
        double sum = 0.0;
        for (int i = 0; i < image.bytes.length; i++)
            sum += (double)(image.bytes[i]&0xff);
        return sum / image.bytes.length;
    }
    
    public ByteBufferImage expandDynamicRange(ByteBufferImage original) {
        double mean = calculateMean(original);
        double sd = calculateStdDev(original, mean);
        
        System.out.println("mean: " + mean + " sd: " + sd);
        
        double min = mean - sd;
        
        for (int i = 0; i < original.bytes.length; i++) {
            long scaled = Math.round(((double)(original.bytes[i]&0xff) - min) * (256 / (2 * sd)));
            if (scaled < 0)
                original.bytes[i] = 0;
            else if (scaled > 255)
                original.bytes[i] = (byte)255;
            else
                original.bytes[i] = (byte)scaled;
            //System.out.println("scaled: " + scaled + " new: " + original[i]);
        }
        
        return original;
    }
    
}
