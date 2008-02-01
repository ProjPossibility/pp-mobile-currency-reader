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
