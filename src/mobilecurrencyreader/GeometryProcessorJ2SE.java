/*
 * GeometryProcessorJ2SE.java
 *
 * Created on November 17, 2007, 12:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mobilecurrencyreader;

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
import java.util.Vector;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.Graphics2D;

/**
 *
 * @author Kevin
 */
public class GeometryProcessorJ2SE implements GeometryProcessor {
    
    /** Creates a new instance of GeometryProcessorJ2SE */
    public GeometryProcessorJ2SE() {
        
    }

    public ByteBufferImage translateImage(ByteBufferImage bytes, int tx, int ty) {
      int size=bytes.width*bytes.height;
     byte newBytes[]=new byte[size];
     int i,j=0;
     int newI,newJ;
     int boundI,boundJ,InitJ;
     if(tx<0)
     {
        i=-tx;
        boundI=bytes.height;
      }
     else
     {
         i=0;
         boundI=bytes.height-tx;
     }
     if(ty<0)
     {
        boundJ=bytes.width;
        InitJ=-ty;
     }
  
     else
     {
        InitJ=0;
         boundJ=bytes.width-ty;
     }
         
     for( ;i<boundI;i++)
     {
           newI=i+((tx));
         for(j=InitJ;j<boundJ;j++){
           {
               newJ=j+((ty));
               newBytes[newI*bytes.width+newJ]=bytes.bytes[i*bytes.width+j];  
           }
      }
   }
     return new ByteBufferImage(newBytes,bytes.width,bytes.height);
}
    
public VertexPoint rotatePoint(VertexPoint orig, double angle, double i0, double j0) {
    double sin = Math.sin(angle);
    double cos = Math.cos(angle);

    double transI = orig.y - i0;
    double transJ = orig.x - j0;
    double rotJ = transJ * cos - transI * sin;
    double rotI = transJ * sin + transI * cos;
    int finalI = (int)Math.round(rotI + i0);
    int finalJ = (int)Math.round(rotJ + j0);

    return new VertexPoint(finalJ, finalI, orig.end);
}

public ByteBufferImage rotateImage(ByteBufferImage orig, float angle,double i0,double j0) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
	byte[] source = orig.bytes;
        byte[] newBytes = new byte[source.length];
        int[] dest = new int[source.length];
        
        for (int i = 0; i < orig.height; ++i) {
            for (int j = 0; j < orig.width; ++j) {
                dest[i*orig.width+j]=-1;
                double transI = i - i0;
                double transJ = j - j0;
                double rotJ = transJ * cos - transI * sin;
                double rotI = transJ * sin + transI * cos;
                int finalI = (int)Math.round(rotI + i0);
                int finalJ = (int)Math.round(rotJ + j0);
                if (finalI >= 0 && finalI <orig.height && finalJ >=0 && finalJ < orig.width) {
                    dest[finalI * orig.width + finalJ] = source[i * orig.width + j];
                }
            }
        }
        
        angle *= -1;
        sin = Math.sin(angle);
        cos = Math.cos(angle);
        for (int i = 0; i < orig.height; ++i) {
            for (int j = 0; j < orig.width; ++j) {
                if(dest[i * orig.width + j] < 0){
                    double transI = i - i0;
                    double transJ = j - j0;
                    double rotJ = transJ * cos - transI * sin;
                    double rotI = transJ * sin + transI * cos;
                    int finalI = (int)Math.round(rotI + i0);
                    int finalJ = (int)Math.round(rotJ + j0);
                    if (finalI >= 0 && finalI <orig.height && finalJ >=0 && finalJ < orig.width) {
                        dest[i * orig.width + j] = orig.bytes[finalI * orig.width + finalJ];
                    }
                }
                newBytes[i * orig.width + j] = (byte)dest[i * orig.width + j];
            }
        }
        return new ByteBufferImage(newBytes,orig.width,orig.height);
    }
        public ByteBufferImage resizeImage(ByteBufferImage orig, int height,int width){
            
            ByteBufferImage newBuff= new ByteBufferImage(height,width);
            for(int i =0;i<height;i++)
                for(int j=0;j<width;j++)
                {
                newBuff.setPixel(i,j,orig.getPixel(i,j));
                    
                }
            return newBuff;
        }
    
    public ByteBufferImage scaleImage(ByteBufferImage orig, double tx, double ty) {
        int scaleBuf[][] = new int[orig.height][orig.width];
        int TscaleBuf[][] = new int[orig.height][orig.width];
        
        int i,j,x,y,x0,y0,x1,y1;
        double xReal,yReal,dx,dy,omdx,omdy,bilinear;
        
        tx=1/tx;
        ty=1/ty;
  
       for(i=0;i<orig.height;i++)
            {
                for(j=0;j<orig.width;j++)
                {
                   scaleBuf[i][j]=orig.getPixelInt(i,j);
                }
            }
        
       
        for(x=0;x<orig.height;x++)
            {
                for(y=0;y<orig.width;y++)
                {

                xReal=x*tx;  // 0 <= xReal <= N-1
                yReal=y*ty;  // 0 <= yReal <= M-1

                     x0 = (int)(xReal); y0 = (int)(yReal);
                    if((x0<orig.height)&&(y0<orig.width))
                    {
                        dx = xReal-x0;
                        dy = yReal-y0; 
                        omdx = 1-dx; omdy = 1-dy;
                        y1=(y0+1 >=orig.width)?orig.width-1:y0+1;
                        x1=(x0+1 >=orig.height)?orig.height-1:x0+1;

                        bilinear = omdx*omdy*scaleBuf[x0][y0] +omdx*dy*scaleBuf[x0][y1] +dx*omdy*scaleBuf[x1][y0] + dx*dy*scaleBuf[x1][y1];
                        TscaleBuf[x][y]=(int)bilinear;

                    }
                }
        }
             
         for(i=0;i<orig.height;i++)
        {
                    
            for(j=0;j<orig.width;j++)
            {
                orig.setPixel(i,j,(byte)TscaleBuf[i][j]);
            }
         }
        
            
       return orig;
    }
    
    
    public ByteBufferImage cropImage(ByteBufferImage orig, int tx, int ty, int newWidth, int newHeight) {
        ByteBufferImage out = new ByteBufferImage(newWidth, newHeight);
        int dest=0;
        for (int i=ty; i<ty+newHeight; i++) {
            for (int j=tx; j<tx+newWidth; j++, dest++) {
                if((i>=orig.height)||(j>=orig.width))
                {
                 out.bytes[dest]=0;   
                }
                else
                {
                out.bytes[dest] = orig.getPixel(i, j);
                }
            }
        }
        
        return out;
    }

    public ByteBufferImage extractImage(ByteBufferImage img, VertexPoint[] points) {
        
        // figure out which line segments are the long ones:
        double short_edge_dist = Double.MAX_VALUE,
               long_edge_dist = Double.MIN_VALUE,
               diagonal_edge_dist = Double.MIN_VALUE;
        
        for (int k = 0; k < points.length; k++) {
            for (int l = 0; l < k; l++) {
                double dist = VertexPoint.distance(points[k], points[l]);
                if (dist < short_edge_dist)
                    short_edge_dist = dist;
                if (dist > diagonal_edge_dist)
                    diagonal_edge_dist = dist;
            }
        }
        
        // for keeping track of angle / direction
        int direction = 0;
        double sum_rad = 0;
        int num_lines = 0;
        
        // use the long ones 
        for (int k = 0; k < points.length; k++) {
            for (int l = 0; l < k; l++) {
                double dist = VertexPoint.distance(points[k], points[l]);
                if (Math.abs(dist - short_edge_dist) < 10) {
                    //System.out.println("Short ("+k+","+l+") " + dist);
                } else if (Math.abs(dist - diagonal_edge_dist) < 10) {
                    //System.out.println("Diagonal ("+k+","+l+") " + dist);
                } else {
                    System.out.println("Long ("+k+","+l+") " + dist);
                    
                    double rad = Math.acos(Math.abs(points[k].x-points[l].x)/dist);
                    sum_rad += rad;
                    num_lines++;
                    
                    //System.out.println("Degrees: " + (rad/Math.PI*180));
                    
                    VertexPoint upper = (points[k].y < points[l].y) ? points[k] : points[l];
                    VertexPoint lower = (points[k].y < points[l].y) ? points[l] : points[k];
                    int new_direction = (upper.x > lower.x) ? 1 : -1;
                    if (direction != 0 && direction != new_direction) {
                        System.out.println("WARNING: direction of rotation not same for both lines, using last");
                        sum_rad = rad;
                        num_lines = 1;
                    }
                    direction = new_direction;
                    
                    //System.out.println("Direction: " + direction);
                }
            }
        }
        // calculate average angle
        float rotation_radians = (float)(sum_rad / num_lines) * direction;
        
        System.out.println("Final rotation: " + rotation_radians + " rad (" + rotation_radians/Math.PI*180);
        
        // find extents to calculate center of bill
        int x0 = Integer.MAX_VALUE, x1 = Integer.MIN_VALUE, y0 = Integer.MAX_VALUE, y1 = Integer.MIN_VALUE;
        for (int k = 0; k < points.length; k++) {
            if (points[k].x < x0) x0 = points[k].x;
            if (points[k].x > x1) x1 = points[k].x;
            if (points[k].y < y0) y0 = points[k].y;
            if (points[k].y > y1) y1 = points[k].y;
        }
        double origin_x = x0 + (((double)x1)-((double)x0))/2.0;
        double origin_y = y0 + (((double)y1)-((double)y0))/2.0;
        
        // rotate image about center by the given degrees
        ByteBufferImage bbi = rotateImage(img, rotation_radians, origin_y, origin_x);
        
        // rotate vertices about center by the given degrees, and find extents
        x0 = y0 = Integer.MAX_VALUE;
        x1 = y1 = Integer.MIN_VALUE;
        for (int k = 0; k < points.length; k++) {
            points[k] = rotatePoint(points[k], rotation_radians, origin_y, origin_x);
            bbi.drawBox(points[k].x, points[k].y, 10, (byte)0x7f);
            if (points[k].x < x0) x0 = points[k].x;
            if (points[k].x > x1) x1 = points[k].x;
            if (points[k].y < y0) y0 = points[k].y;
            if (points[k].y > y1) y1 = points[k].y;
        }
        int w = x1 - x0;
        int h = y1 - y0;
        bbi = cropImage(bbi, x0, y0, w, h);
        /*
        // calculate scaling factor
        double sx = ((double)bbi.width)  / ((double)(x1-x0));
        double sy = ((double)bbi.height) / ((double)(y1-y0));
        
        // translate
        bbi = translateImage(bbi,-y0,-x0);
        */
        // scale
//        bbi = scaleImage(bbi,0.5,0.5);
//        bbi = cropImage(bbi, x0, y0, x1/2, y1/2);
        
        return bbi;
    }
    
    public boolean isFeature(ByteBufferImage img, int iIn, int jIn) {
        if (jIn >= img.width || iIn >= img.height) {
            System.out.println("It happened!");
            return false;
        }
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
        return (min_col < 0) ? null : new int[] {min_col, max_col};
    }
    
    private Vector findVerticesRecursive(ByteBufferImage img, int min_row, int max_row, int last_row) {
        System.out.println("min_row=" + min_row + " max_row=" + max_row + " last_row=" + last_row);
        if (Math.abs(max_row-min_row) <= 1) {
            Vector list = new Vector();
            
            int col_range[] = (min_row == last_row) ? getMinMaxFeature(img, max_row) : getMinMaxFeature(img, min_row);
            if (col_range == null)
                col_range = (min_row == last_row) ? getMinMaxFeature(img, min_row) : getMinMaxFeature(img, max_row);
              
            if (col_range == null) {
                System.out.println("WARNING: no vertex found (should not occur)");
            } else {
                VertexPoint p = new VertexPoint((col_range[0]+col_range[1])/2, (min_row+max_row)/2, true);
                System.out.println("x=" + p.x + " y=" + p.y + " (min_row=" + min_row + " max_row=" + max_row + " min_col=" + col_range[0] + " max_col=" + col_range[1] + ")");
                list.addElement(p);
            }
            return list;
        } else {
            int middle = (max_row+min_row)/2;
            int col_range[] = getMinMaxFeature(img, middle);
            
            Vector newlist;
            if (col_range == null) {
                // no features on middle
                if (middle < last_row)
                    newlist = findVerticesRecursive(img, middle, max_row, last_row);
                else
                    newlist = findVerticesRecursive(img, min_row, middle, last_row);
            } else {
                // feature found
                if (middle < last_row)
                    newlist = findVerticesRecursive(img, min_row, middle, middle);
                else
                    newlist = findVerticesRecursive(img, middle, max_row, middle);
                newlist.addElement(new VertexPoint(col_range[0], middle, false));
                newlist.addElement(new VertexPoint(col_range[1], middle, false));
            }
            return newlist;
        }
    }
    
    private Vector getVertices(ByteBufferImage img) {
        Vector points = new Vector();
        Vector firstRecurse = findVerticesRecursive(img, 0,            img.height/2,   img.height/2);
        for (int i=0; i<firstRecurse.size(); i++) {
            points.addElement(firstRecurse.elementAt(i));
        }
        Vector secondRecurse = findVerticesRecursive(img, img.height/2, img.height,     img.height/2);
        for (int i=0; i<secondRecurse.size(); i++) {
            points.addElement(secondRecurse.elementAt(i));
        }
        
        return points;
    }
    
    private Vector swapPoints(Vector points) {
        for (int i = 0; i < points.size(); i++) {
            VertexPoint p = (VertexPoint)points.elementAt(i);
            int tmp = p.x;
            p.x = p.y;
            p.y = tmp;
        }
        return points;
    }
    
    public VertexPoint[] detectAllVertices(ByteBufferImage img) {
        Vector result = new Vector();
        
        Vector verts1 = getVertices(img);
        Vector verts2 = swapPoints(getVertices(swapIJ(img)));
        for (int i=0; i<verts1.size(); i++) {
            result.addElement(verts1.elementAt(i));
        }
        for (int i=0; i<verts2.size(); i++) {
            result.addElement(verts2.elementAt(i));
        }
        
        VertexPoint[] array = new VertexPoint[result.size()];
        
        result.copyInto(array);
        
        return array;
    }
    
    public VertexPoint[] detectCornerVertices(ByteBufferImage img) {
        Vector result = new Vector();
        
        VertexPoint all[] = detectAllVertices(img);
        for (int i = 0; i < all.length; i++) {
            VertexPoint p = all[i];
            if (p.end)
                result.addElement(p);
        }
        
        VertexPoint[] array = new VertexPoint[result.size()];
        
        result.copyInto(array);
        
        return array;
    }
    
    public ByteBufferImage drawPoints(ByteBufferImage img, VertexPoint points[]) {
        for (int i = 0; i < points.length; i++) {
            VertexPoint p = points[i];
            if (!p.end)
                img.drawBox(p.x, p.y, 5, (byte)0xFF);
        }
        for (int i = 0; i < points.length; i++) {
            VertexPoint p = points[i];
            if (p.end)
                img.drawBox(p.x, p.y, 5, (byte)(0x3F*i));
        }
        return img;
    }
    
    // convert a greyscale byte array to a buffered image
    public BufferedImage byteToBuffered(ByteBufferImage img) {
        ByteBufferImage copy = new ByteBufferImage(img.width, img.height);
        for(int i=0;i< img.height; i++) {
            for(int j=0; j<img.width; j++) {
                copy.setPixel(i, j, img.getPixel(i, j));
            }
        }
        DataBuffer buffer = new DataBufferByte(copy.bytes, img.width*img.height);
        
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
    public ByteBufferImage bufferedToByte(BufferedImage orig) {
        byte[] newBytes = new byte[orig.getWidth()*orig.getHeight()];
        for(int i=0;i<orig.getHeight();i++)
            for(int j=0;j<orig.getWidth();j++) {
            int k1=orig.getRGB(j,i);
            double bb=(double)(0x00ff & k1);
            double gg=(double)((0x00ff)&(k1 >> 8));
            double rr=(double)((0x00ff)&((k1 >> 16)));
            newBytes[i*(orig.getWidth())+j]=(byte)((0.3*rr+0.59*gg+0.11*bb));
            }
        return new ByteBufferImage(newBytes,orig.getWidth(),orig.getHeight());
    }
    
    public int[] bufferedToInt(BufferedImage img) {
        DataBuffer buf = img.getData().getDataBuffer();
        int [] newImg = new int[buf.getSize()];
        for (int i = 0; i < buf.getSize(); i++)
            newImg[i] = buf.getElem(i);
        return newImg;
    }
    
    public  BufferedImage getBufferedImage(int[] pic,int WIDTH,int HEIGHT) {
	BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);
	for (int i = 0; i < WIDTH; ++i) {
		for (int j = 0; j < HEIGHT; ++j) {
			bi.setRGB(i, j, pic[j * HEIGHT + i]);
		}
	}
	return bi;
}

    
    public BufferedImage createThumbnail(BufferedImage image, int thumbHeight, int thumbWidth)
{
   int sourceWidth = image.getWidth();
   int sourceHeight = image.getHeight();
 
   BufferedImage thumb = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_BGR);
   Graphics2D g = thumb.createGraphics();
 
   for (int y = 0; y < thumbHeight; y++)
   {
      for (int x = 0; x < thumbWidth; x++)
      {
        g.setClip(x, y, 1, 1);
        int dx = x * sourceWidth / thumbWidth;
        int dy = y * sourceHeight / thumbHeight;
        g.drawImage(image, null,x,y);
      }
   }
 
   BufferedImage immutableThumb = image;
 
   return immutableThumb;
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
