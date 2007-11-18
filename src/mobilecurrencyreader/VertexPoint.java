/*
 * VertexPoint.java
 *
 * Created on November 18, 2007, 10:44 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mobilecurrencyreader;


/**
 *
 * @author tlrobinson
 */
class VertexPoint {
    public int x;
    public int y;
    public boolean end;
    public VertexPoint(int x, int y, boolean end) {
        this.x = x;
        this.y = y;
        this.end = end;
    }
    public double distanceTo(VertexPoint b) {
        return distance(this, b);
    }
    public static double distance(VertexPoint a, VertexPoint b) {
        return Math.sqrt((a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y));
    }
}
