/*
 * GeometryProcessorJ2SE.java
 *
 * Created on November 17, 2007, 12:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mobilecurrencyreader;

import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Kevin
 */
public class GeometryProcessorJ2SE implements GeometryProcessor {
    
    /** Creates a new instance of GeometryProcessorJ2SE */
    public GeometryProcessorJ2SE() {
        
    }

    public ByteBufferImage translateImage(ByteBufferImage orig, int tx, int ty) {
        return null;
    }

    public ByteBufferImage rotateImage(ByteBufferImage orig, float angleOfRotation) {
        return null;
    }

    public ByteBufferImage scaleImage(ByteBufferImage orig, float sx, float sy) {
        return null;
    }
    
    public ByteBufferImage cropImage(ByteBufferImage orig, int tx, int ty, int newWidth, int newHeight) {
        ByteBufferImage out = new ByteBufferImage(newWidth, newHeight);
        int dest=0;
        for (int i=ty; i<ty+newHeight; i++) {
            for (int j=tx; j<tx+newWidth; j++, dest++) {
                out.bytes[dest] = orig.getPixel(j, i);
            }
        }
        
        return out;
    }

    public ByteBufferImage extractImage(ByteBufferImage origt, Point[] verticeArray) {
        return null;
    }
    
    public boolean isFeature(ByteBufferImage img, int x, int y) {
        if (img.getPixelInt(x, y) < 128) {
            int num = 0;
            int total = 0;
            for (int i=y-1; i<=y+1; i++) {
                for (int j=x-1; j<=x+1; j++) {
                    if (!(j == x && i == y) && i>=0 && j >=0 && i<img.height && j < img.width) {
                        total += img.getPixelInt(j, i);
                        num++;
                    }
                }
            }
            float avg = (float)total / (float) num;
            if (avg < 128)
                return true;
            else
                return false;
        } else return false;
    }
    
    public ByteBufferImage testFeature(ByteBufferImage img) {
        ByteBufferImage out = new ByteBufferImage(img.width, img.height);
        for (int x=0; x<img.width; x++) {
            for (int y=0; y<img.height; y++) {
                if (isFeature(img, x, y)) {
                    out.setPixel(x, y, (byte)0);
                } else out.setPixel(x, y, (byte)255);
            }
        }
        return out;
    }

    public Point[] detectVertices(ByteBufferImage image) {
        
        return null;
    }
    
    // convert a greyscale byte array to a buffered image
    public BufferedImage byteToBuffered(ByteBufferImage img) {
        DataBuffer buffer = new DataBufferByte(img.bytes, img.width*img.height);
        
        int pixelStride = 1; //assuming r, g, b, skip, r, g, b, skip...
        int scanlineStride = img.width; //no extra padding
        int[] bandOffsets = {0};
        WritableRaster raster = Raster.createInterleavedRaster(buffer, img.width, img.height, scanlineStride, pixelStride, bandOffsets, null);
        //WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, 1, null);
        
        ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        boolean hasAlpha = false;
        boolean isAlphaPremultiplied = false;
        int transparency = Transparency.OPAQUE;
        int transferType = DataBuffer.TYPE_BYTE;
        ColorModel colorModel = new ComponentColorModel(colorSpace, hasAlpha, isAlphaPremultiplied, transparency, transferType);
        
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }
    
    // converts a buffered image to a byte array
    public ByteBufferImage bufferedToByte(BufferedImage img) {
        DataBuffer buf = img.getData().getDataBuffer();
        ByteBufferImage newImg = new ByteBufferImage(img.getWidth(), img.getHeight());
        for (int i = 0; i < buf.getSize(); i++)
            newImg.bytes[i] = (byte)buf.getElem(i);
        return newImg;
    }
}
