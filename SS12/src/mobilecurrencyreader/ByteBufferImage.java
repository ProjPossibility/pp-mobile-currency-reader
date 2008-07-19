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

public class ByteBufferImage {
    
    byte[] bytes;
    int width;
    int height;
    
    /** Creates a new instance of ByteBufferImage */
    public ByteBufferImage(int width, int height) {
        this.bytes = new byte[width*height];
        this.width = width;
        this.height = height;
    }
    
    public ByteBufferImage(byte[] bytes, int width, int height) {
        this.bytes = bytes;
        this.width = width;
        this.height = height;
    }
    public void setPixel(int i, int j, byte val) {
        bytes[i * width + j] = val;
    }
    public void setPixel(int i, byte val) {
        bytes[i] = val;
    }
    public byte getPixel(int i, int j) {
        return bytes[i * width + j];
    }
    public int getPixelInt(int i, int j) {
        return (int)(bytes[i * width + j]&0xff);
    }
    public double getPixelDouble(int i, int j) {
        return (double)(bytes[i * width + j]&0xff);
    }
    public byte getPixel(int i) {
        return bytes[i];
    }
    public int getPixelInt(int i) {
        return (int)(bytes[i]&0xff);
    }
    public double getPixelDouble(int i) {
        return (double)(bytes[i]&0xff);
    }
    public void drawBox(int x, int y, int size, byte color) {
        for (int i = y-size/2; i < y+size/2; i++)
            for (int j = x-size/2; j < x+size/2; j++)
                if (i >= 0 && i < height && j >= 0 && j < width)
                    setPixel(i,j,color);
    }
}
