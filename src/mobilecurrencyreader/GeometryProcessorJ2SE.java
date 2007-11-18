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

    public byte[] translateImage(byte[] orig, int width, int height, int tx, int ty) {
        return null;
    }

    public byte[] rotateImage(byte[] orig, int width, int height, float angleOfRotation) {
        return null;
    }

    public byte[] scaleImage(byte[] orig, int width, int height, float sx, float sy) {
        return null;
    }
    
    public byte [] cropImage(byte orig[], int width,int height, int tx, int ty, int newWidth, int newHeight) {
        byte out[] = new byte[newWidth*newHeight];
        int dest=0;
        for (int i=ty; i<ty+newHeight; i++) {
            for (int j=tx; j<tx+newWidth; j++, dest++) {
                //int dest = (i-ty)*newWidth + (j-tx);
                out[dest] = getPixel(orig, i, j, width);
            }
        }
        
        return out;
    }

    public byte[] extractImage(byte[] orig, int width, int height, Point[] verticeArray) {
        return null;
    }

    public Point[] detectVertices(byte[] img, int width, int height) {
        return null;
    }
    
    // convert a greyscale byte array to a buffered image
    public BufferedImage byteToBuffered(byte[] img, int width, int height) {
        DataBuffer buffer = new DataBufferByte(img, width*height);
        
        int pixelStride = 1; //assuming r, g, b, skip, r, g, b, skip...
        int scanlineStride = width; //no extra padding
        int[] bandOffsets = {0};
        WritableRaster raster = Raster.createInterleavedRaster(buffer, width, height, scanlineStride, pixelStride, bandOffsets, null);
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
    public byte[] bufferedToByte(BufferedImage img) {
        DataBuffer buf = img.getData().getDataBuffer();
        byte bytes[] = new byte[buf.getSize()];
        for (int i = 0; i < buf.getSize(); i++)
            bytes[i] = (byte)buf.getElem(i);
        return bytes;
    }
    
    public byte getPixel(byte[] img, int i, int j, int width) {
        return img[i * width + j];
    }
}
