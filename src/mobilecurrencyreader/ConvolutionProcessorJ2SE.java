

package mobilecurrencyreader;

/**
 *
 * @author binarygame
 */
public class ConvolutionProcessorJ2SE implements ConvolutionProcessor {
    
    public ByteBufferImage convolution(ByteBufferImage img, int[][] dx, int[][] dy) {
        ByteBufferImage out = new ByteBufferImage(img.width, img.height);
        
        for (int i=0; i<img.height; i++) {
            for (int j=0; j<img.width; j++) {
                int num = 0;
                int total = 0;
                for (int i1=i-1; i1<=i+1; i1++) {
                    for (int j1=j-1; j1<=j+1; j1++) {
                        if (!(j1 == j && i1 == i) && i1>=0 && j1 >=0 && i1<img.height && j1 < img.width) {
                            total += img.getPixelInt(i1, j1) * dx[i1-i+1][j1-j+1] + img.getPixelInt(i1, j1) * dy[i1-i+1][j1-j+1];
                            num++;
                        }
                    }
                }
                float avg =((float)total / (float) num);
                avg =  Math.abs(avg);
                
                out.setPixel(i, j, (byte)(avg + .5));
            }
        }
        
        return out;
    }
    
    public ByteBufferImage sobelKernelDerivative(ByteBufferImage img) {
        
        int[][] dx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] dy = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
        
        return convolution(img, dx, dy);
    }
}
