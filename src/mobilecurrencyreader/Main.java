package mobilecurrencyreader;

import java.awt.Graphics2D;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.*;

/**
 *
 * @author Kevin
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestWindow window = new TestWindow();
        File imageFile = new File("testImage.jpg");
        GeometryProcessorJ2SE geom = new GeometryProcessorJ2SE();
        
        try {
            BufferedImage orig = ImageIO.read(imageFile);
            BufferedImage image = Main.convertToGraySclae(orig);
            ByteBufferImage bytes = geom.bufferedToByte(image);
            int newWidth = 332;
            int newHeight = 143;
            //bytes = geom.cropImage(bytes, 54, 71, newWidth, newHeight);
            bytes = geom.testFeature(bytes);
            BufferedImage converted = geom.byteToBuffered(bytes);
            window.setLeftImage(orig);
            if (converted != null)
                window.setRightImage(converted);
            else System.out.println("Converted image is null!");
            System.out.println("Loaded Image: " + imageFile.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        window.setVisible(true);
    }
    
    public static BufferedImage convertToGraySclae(BufferedImage img){
        int width=img.getWidth();
        int height = img.getHeight();
        int type = java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
        BufferedImage grayImage = new java.awt.image.BufferedImage(width, height, type);
        Graphics2D g = grayImage.createGraphics();
        g.drawRenderedImage(img, null);
        g.dispose();
        return grayImage;
    }
}
