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
    public void setPixel(int x, int y, byte val) {
        bytes[y * width + x] = val;
    }
    public void setPixel(int i, byte val) {
        bytes[i] = val;
    }
    public byte getPixel(int x, int y) {
        return bytes[y * width + x];
    }
    public int getPixelInt(int x, int y) {
        return (int)(bytes[y * width + x]&0xff);
    }
    public double getPixelDouble(int x, int y) {
        return (double)(bytes[y * width + x]&0xff);
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
}
