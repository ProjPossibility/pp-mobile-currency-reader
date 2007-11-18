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
}
