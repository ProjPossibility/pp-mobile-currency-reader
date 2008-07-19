/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package harriscornerdetector;

/**
 *
 * @author Sandhya
 */
public class ImageData
{
    
    // Private members.
    private int corners[][];
    private int longestEdge = -1;
    private double longestDistance = -1;
    private double slope = -1;
    private double rotAngle = -1;
    private int yCenter = -1;
    private int xCenter = -1;

// Getters and Setters
    public int[][] getCorners() {
        return corners;
    }

    public void setCorners(int[][] corners) {
        this.corners = corners;
    }

    public int getLongestEdge() {
        return longestEdge;
    }

    public void setLongestEdge(int longestEdge) {
        this.longestEdge = longestEdge;
    }

    public double getLongestDistance() {
        return longestDistance;
    }

    public void setLongestDistance(double longestDistance) {
        this.longestDistance = longestDistance;
    }

    public double getSlope() {
        return slope;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }

    public double getRotAngle() {
        return rotAngle;
    }

    public void setRotAngle(double rotAngle) {
        this.rotAngle = rotAngle;
    }


    public int getYCenter() {
        return yCenter;
    }

    public void setYCenter(int yCenter) {
        this.yCenter = yCenter;
    }

    public int getXCenter() {
        return xCenter;
    }

    public void setXCenter(int xCenter) {
        this.xCenter = xCenter;
    }
   
        
}
