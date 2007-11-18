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
import java.util.ArrayList;
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
                out.bytes[dest] = orig.getPixel(i, j);
            }
        }
        
        return out;
    }

    public ByteBufferImage extractImage(ByteBufferImage origt, Point[] verticeArray) {
        return null;
    }
    
    public boolean isFeature(ByteBufferImage img, int iIn, int jIn) {
        if (img.getPixelInt(iIn, jIn) < 128) {
            int num = 0;
            int total = 0;
            for (int i=iIn-1; i<=iIn+1; i++) {
                for (int j=jIn-1; j<=jIn+1; j++) {
                    if (!(j == jIn && i == iIn) && i>=0 && j >=0 && i<img.height && j < img.width) {
                        total += img.getPixelInt(i, j);
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
        for (int i=0; i<img.height; i++) {
            for (int j=0; j<img.width; j++) {
                if (isFeature(img, i, j)) {
                    out.setPixel(i, j, (byte)0);
                } else out.setPixel(i, j, (byte)255);
            }
        }
        return out;
    }
    
    
    
    private int[] getMinMaxFeature(ByteBufferImage img, int row) {
        int min_col = -1, max_col = -1;
        for (int j = 0; j < img.width; j++) {
            if (isFeature(img, row, j)) {
                if (min_col < 0)
                    min_col = j;
                max_col = j;
            }
        }
        int ret[] = new int[2];
        ret[0] = min_col;
        ret[1] = max_col;
        return ret;
    }
    
    private ArrayList<Point> findVerticeRecurse(ByteBufferImage img, int min_row, int max_row, int last_row) {
        System.out.println("min_row=" + min_row + " max_row=" + max_row + " last_row=" + last_row);
        if (Math.abs(max_row-min_row) <= 1) {
            int col_range[] = getMinMaxFeature(img, max_row);
            ArrayList<Point> list = new ArrayList<Point>();
            if (col_range[0] < 0) {
                System.out.println("no vertex found");
            } else {
                System.out.println("min_row=" + min_row + " max_row=" + max_row + " min_col=" + col_range[0] + " max_col=" + col_range[1]);
                list.add(new Point((col_range[0]+col_range[1])/2, (min_row+max_row)/2));
            }
            return list;
        } else {
            int middle = (max_row+min_row)/2;
            int col_range[] = getMinMaxFeature(img, middle);
            
            ArrayList newlist;
            if (col_range[0] < 0) {
                // no features on middle
                if (middle < last_row)
                    newlist = findVerticeRecurse(img, middle, max_row, last_row);
                else
                    newlist = findVerticeRecurse(img, min_row, middle, last_row);
            } else {
                // feature found
                if (middle < last_row)
                    newlist = findVerticeRecurse(img, min_row, middle, middle);
                else
                    newlist = findVerticeRecurse(img, middle, max_row, middle);
                newlist.add(new Point(col_range[0], middle));
                newlist.add(new Point(col_range[1], middle));
            }
            return newlist;
        }
    }
    
    private ArrayList<Point> getVertices(ByteBufferImage img) {
        ArrayList<Point> points = new ArrayList<Point>();
        points.addAll(findVerticeRecurse(img, 0,            img.height/2,   img.height/2));
        points.addAll(findVerticeRecurse(img, img.height/2, img.height,     img.height/2));
        
        return points;
    }
    
    private ArrayList<Point> swapPoints(ArrayList<Point> points) {
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            int tmp = p.x;
            p.x = p.y;
            p.y = tmp;
        }
        return points;
    }
    
    public ByteBufferImage detectVertices(ByteBufferImage img) {
        ArrayList<Point> points = new ArrayList<Point>();
        
        points.addAll(getVertices(img));
        points.addAll(swapPoints(getVertices(swapIJ(img))));
        
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            img.drawBox(p.x, p.y, 5, (byte)0xFF); 
        }
        
        return img;
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
    
    public ByteBufferImage swapIJ(ByteBufferImage img) {
        ByteBufferImage out = new ByteBufferImage(img.height, img.width);
        for (int i=0; i<img.height; i++) {
            for (int j=0; j<img.width; j++) {
                out.setPixel(j, i, img.getPixel(i, j));
            }
        }
        return out;
    }
}
