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
 * GeometryProcessor.java
 *
 * Created on November 17, 2007, 12:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mobilecurrencyreader;

/**
 *
 * @author binarygame
 */
public interface ColorProcessor {
        public ByteBufferImage expandDynamicRange(ByteBufferImage original);
    public ByteBufferImage expandDynamicRange(ByteBufferImage original, int x0, int y0, int x1, int y1);
    public ByteBufferImage expandDynamicRange(ByteBufferImage original, int m, int n);
    public ByteBufferImage expandDynamicRange(ByteBufferImage original, int x0, int y0, int x1, int y1, int m, int n);
    public ByteBufferImage quantize(ByteBufferImage original,  int N);
    public double subtractImage(ByteBufferImage a, ByteBufferImage b);
}
