package mobilecurrencyreader;

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
        try {
            BufferedImage image = ImageIO.read(imageFile);
            window.setLeftImage(image);
            System.out.println("Loaded Image: " + imageFile.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        window.setVisible(true);
    }
    
}
