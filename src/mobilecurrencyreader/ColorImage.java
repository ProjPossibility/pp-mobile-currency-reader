/*
 * ColorImage.java
 *
 * Created on November 17, 2007, 1:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mobilecurrencyreader;
using java.math.*;

/**
 *
 * @author binarygame
 */
public class ColorImage implements ColorProcessor {
    
    /** Creates a new instance of ColorImage */
    public ColorImage() {
    }
    
   public byte[] convertToGrayscale(byte original[], int height, int width)
   {
       return original;
   }
    public byte[] expandDynamicRange(byte original[], int height, int width)
    {
        return original;
        
    }
    public byte[] quantize(byte original[], int N)
    {
        int i,c;
  
        for(i=0;i<original.length;i++)
        {
            //convert to integer
               c=(int)original[i];
               //divide by N to get base factor and multiply by N
               c=(c/N)*N;
               //copy value back to original array
               original[i]=(byte)c;
        }
        
        return original;
        
    }
    public double subtractImage(byte orig[], byte compare[])
    {
        
        int sumOfSquares=0,i=0;
        double rmsVal;
        if (orig.length!=compare.length)
        {
            return -1;
        }
        for(i=0;i<orig.length;i++)
        {
            sumOfSquares+= ((int)orig[i]-(int)compare[i])*((int)orig[i]-(int)compare[i]);
        }
        rmsVal=sumOfSquares/orig.length;
        rmsVal=Math.sqrt(rmsVal);
        return rmsVal;
       
    }
  
    
}
