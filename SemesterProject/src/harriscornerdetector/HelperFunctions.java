/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package harriscornerdetector;
import java.lang.*;
/**
 *
 * @author Sandhya
 */
public class HelperFunctions {
    Math m;
    public int ExtactedImageWidth = -1;
    public int ExtactedImageHeight = -1;
   
    private ImageData findLargestEdge(ImageData image)
    {
        int corners[][] = image.getCorners();
        int index = 0,j=-1;
        double temp = -1;        
        double max = m.sqrt((corners[0][1]-corners[1][1]) * (corners[0][1]-corners[1][1]) +
                (corners[0][0]-corners[1][0]) * (corners[0][0]-corners[1][0]));
        
        for(int i=1;i<=3;i++)
        {
            if(i==3)
            {
                j = 0;
            }
            else
            {
                j = i+1;
            }
            temp = m.sqrt((corners[i][1]-corners[j][1]) * (corners[i][1]-corners[j][1]) + (corners[i][0]-corners[j][0]) * (corners[i][0]-corners[j][0]));
                if(max < temp)
                {
                    max = temp;
                    index = i;
                }
        }
        image.setLongestDistance(max);
        image.setLongestEdge(index);
        return image;
    }
    
    private ImageData findRotationAngle(ImageData image)
    {
        double slope = -1;                
        double theta = m.atan(90);
        image = findLargestEdge(image);
        int corners[][] = image.getCorners(); 
        int index = image.getLongestEdge();
        int nextIndex = index + 1;        
        
        if(nextIndex >3)
        {
            nextIndex = 0;
        }
               
        if( (corners[index][0]-corners[nextIndex][0]) != 0)        
        {
            slope = (1.0 * (corners[index][1]-corners[nextIndex][1]) / (corners[index][0]-corners[nextIndex][0]));
            theta = m.atan(slope);
        }        
        image.setRotAngle(theta);
        image.setSlope(slope);
        return image;
    }
    
    private ImageData findRotCenter(ImageData image)
    {
       int corners [][] = image.getCorners();
       double slope1 = (1.0 * (corners[2][1] - corners[0][1]) / (corners[2][0] - corners[0][0]));
       double slope2 = (1.0 * (corners[3][1] - corners[1][1]) / (corners[3][0] - corners[1][0]));
       double yintercep1 = (corners[2][1] - (slope1 * corners[2][0])) ;
       double yintercep2 = (corners[3][1] - (slope2 * corners[3][0])) ;
       image.setXCenter((int)(((yintercep2 - yintercep1)/(slope1 - slope2))+ 0.5));
       image.setYCenter((int)(slope2 * image.getXCenter() + yintercep2 + 0.5));
       return image;
    }
    
    public ImageData findAngleAndCenter(ImageData image)
    {
        image = findRotationAngle(image);
        image = findRotCenter(image);
        return image;
    }
    
   // @SuppressWarnings("static-access")
    public int [] rotateImage(int inputBuffer [],int height,int width, int xCenter, int yCenter, double angle)
    {
       int outputBuffer [] =new int[height * width];
       double xin = 0, yin = 0;
       for(int i=0;i<height;i++)
           for(int j=0;j<width;j++)
           outputBuffer[i * width + j] = 0;
                
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++)
            {   
                xin = ((int)(((j - xCenter) * m.cos(angle) - (i - yCenter) * m.sin(angle)) + xCenter + 0.5));
                yin = ((int)(((i - yCenter) * m.cos(angle) + (j - xCenter) * m.sin(angle)) + yCenter + 0.5));
                if(xin> 0 && yin > 0 && xin < width && yin < height)
                {
                   outputBuffer[i*width + j] = inputBuffer[(int)(yin * width + xin)];
                }
            }
       return outputBuffer;
    }
    
    public int [][] getNewCorners(int oldCorners[][],int newWidth, int newHeight, int xCenter, int yCenter, double angle)
    {
        int x=-1,y=-1;
        for(int i=0;i<4;i++)
        {
             System.out.println("Old Corner " + i+1 + " = X: "  + oldCorners[i][0]  + " Y: " + oldCorners[i][1] );
             x = ((int)(((oldCorners[i][0] - xCenter) * m.cos(angle) + (oldCorners[i][1] - yCenter) * m.sin(angle)) + xCenter + 0.5));
             y = ((int)(((-oldCorners[i][1] + yCenter) * m.cos(angle) + (oldCorners[i][0] - xCenter) * m.sin(angle)) + yCenter + 0.5));             
             if(x>0 && y>0 && x< newWidth && y<newHeight)
             {
                oldCorners[i][0] = x;
                oldCorners[i][1] = y;
             }
             System.out.println("New Corner " + i+1 + " = X: "  + x + " Y: " + y);
        }
        return oldCorners;
    }
   
    public int [] extractImage(int inputBuffer[],int width, int height, int corners[][])
    {
        
        int xmin=corners[0][0],xmax=corners[0][0],ymin=corners[0][1],ymax=corners[0][1];
        for(int i=0;i<4;i++)
        {
            if(corners[i][0]>xmax)
            {
                xmax = corners[i][0];
            }
            
            if(corners[i][0]<xmin)
            {
                xmin = corners[i][0];
            }
            
              if(corners[i][1]>ymax)
            {
                ymax = corners[i][1];
            }
            
              if(corners[i][1]<ymin)
            {
                ymin = corners[i][1];
            }
        }
        int outputBuffer [] = new int [(xmax-xmin) *(ymax-ymin)];
        ExtactedImageWidth = xmax - xmin;
        ExtactedImageHeight = ymax - ymin;
        for(int j=ymin;j<ymax;j++)
        {
            for(int i=xmin;i<xmax;i++)
            {
                outputBuffer[(j-ymin)*(xmax-xmin)+(i-xmin)] = inputBuffer[j*width + i];
            }
        }
        return outputBuffer;
    }
   
}

