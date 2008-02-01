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




package mobilecurrencyreader;

/**
 *
 * @author binarygame
 */
public class ConvolutionProcessorJ2SE implements ConvolutionProcessor {
    
    public ByteBufferImage convolution(ByteBufferImage img, int[][] dx, int[][] dy, int radius) {
        ByteBufferImage out = new ByteBufferImage(img.width, img.height);
        
        for (int i=0; i<img.height; i++) {
            for (int j=0; j<img.width; j++) {
                int num = 0;
                int total = 0;
                for (int i1=i-radius; i1<=i+radius; i1++) {
                    for (int j1=j-radius; j1<=j+radius; j1++) {
                        if (!(j1 == j && i1 == i) && i1>=0 && j1 >=0 && i1<img.height && j1 < img.width) {
                            total += img.getPixelInt(i1, j1) * dx[i1-i+radius][j1-j+radius] + img.getPixelInt(i1, j1) * dy[i1-i+radius][j1-j+radius];
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
        
        return convolution(img, dx, dy, 1);
    }
}
