/*
    This file is part of Mobile Currency Reader.

    Mobile Currency Reader is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Mobile Currency Reader is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Mobile Currency Reader.  If not, see <http://www.gnu.org/licenses/>. 
    
    This software was developed by members of Project:Possibility, a software 
    collaboration for the disabled.
    
    For more information, visit http://projectpossibility.org
*/


/*
 * ColorProcessorJ2SE.java
 *
 * Created on November 17, 2007, 1:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mobilecurrencyreader;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.crypto.NullCipher;

/**
 *
 * @author Kevin
 */
public class ColorProcessorJ2SE implements ColorProcessor {
    
    /** Creates a new instance of ColorProcessorJ2SE */
    public ColorProcessorJ2SE() {
    }


     
    public ByteBufferImage quantize(ByteBufferImage original, int Colors)
    {
        int i, c;
        int N=256/Colors;
        
        for(i=0;i<original.bytes.length;i++) {
            c=(int)(original.getPixelInt(i));
            
            if(c>(N*(Colors-1))&&c<256)
                c=255;
            else
                c=(c/N)*N;
            
           //copy value back to original array
           original.bytes[i]=(byte)c;
        }
        
        return original;
    }
        

    public double subtractImage(ByteBufferImage orig, ByteBufferImage compare)
    {
        
        int sumOfSquares=0,i=0;
        double rmsVal;
        if (orig.bytes.length!=compare.bytes.length)
        {
            return 5000;
        }
        for(i=0;i<orig.bytes.length;i++)
        {
            
            sumOfSquares+= (orig.getPixelInt(i)-compare.getPixelInt(i))*(orig.getPixelInt(i)-compare.getPixelInt(i));
        }
        rmsVal=((double)sumOfSquares)/((double)orig.bytes.length);
        rmsVal=Math.sqrt(rmsVal);
        return rmsVal;
       
    }
    public double[] calculateHistogram(ByteBufferImage original) {
        double hist[] = new double[256];
        
        // zero out all elements of histogram array
        for (int i = 0; i < 256; i++)
            hist[i] = 0.0f;
        
        // increment histogram "bucket" for each pixel
        for (int i = 0; i < original.bytes.length; i++)
            hist[original.getPixelInt(i)]++;
        
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

    private static double calculateStdDev(ByteBufferImage image, int x0, int y0, int x1, int y1, double mean) {
        double sum = 0.0;
        for (int i = y0; i < y1; i++)
            for (int j = x0; j < x1; j++)
                sum += Math.pow(image.getPixelDouble(i,j) - mean, 2);
        return Math.sqrt(sum/((x1-x0)*(y1-y0)));
    }

    public byte[] quantize(byte[] original, int N) {
        return null;
    }

    public double subtractImage(byte[] original, byte[] compare) {
        return 0D;
    }
    
    private static double calculateMean(ByteBufferImage image, int x0, int y0, int x1, int y1) {
        double sum = 0.0;
        for (int i = y0; i < y1; i++)
            for (int j = x0; j < x1; j++)
                sum += image.getPixelDouble(i,j);
        return sum / ((x1-x0)*(y1-y0));
    }
    
    public ByteBufferImage expandDynamicRange(ByteBufferImage original) {
        return expandDynamicRange(original, 0, 0, original.width-1, original.height-1);
    }
    
    public ByteBufferImage expandDynamicRange(ByteBufferImage original, int x0, int y0, int x1, int y1) {
        return expandDynamicRange(original, x0, y0, x1, y1, 0, 255);
    }
    public ByteBufferImage expandDynamicRange(ByteBufferImage original, int m, int n) {
        return expandDynamicRange(original, 0, 0, original.width-1, original.height-1, m, n);
    }
    
    public ByteBufferImage expandDynamicRange(ByteBufferImage original, int x0, int y0, int x1, int y1, int m, int n) {
        double mean = calculateMean(original, x0, y0, x1, y1);
        double sd = calculateStdDev(original, x0, y0, x1, y1, mean);
        
        System.out.println("mean: " + mean + " sd: " + sd);
        
        double min = mean - sd;
        
        double scale_factor = (n+1-m) / (2 * sd);
        for (int i = 0; i < original.bytes.length; i++) {
            long scaled = Math.round((original.getPixelDouble(i) - min) * scale_factor) + m;
            if (scaled < m)
                original.bytes[i] = 0;
            else if (scaled > n)
                original.bytes[i] = (byte)255;
            else
                original.bytes[i] = (byte)scaled;
        }
        
        return original;
    }
    
    public ByteBufferImage invert(ByteBufferImage img) {
        ByteBufferImage out = new ByteBufferImage(img.width, img.height);
        for (int i=0; i<img.height; i++) {
            for (int j=0; j<img.width; j++) {
                int old = img.getPixelInt(i, j);
                int diff = old - 127;
                out.setPixel(i, j, (byte)(128 - diff));
            }
        }
        return out;
    }
}
