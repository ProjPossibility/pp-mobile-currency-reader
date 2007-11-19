package mobilecurrencyreader;

import java.awt.Graphics2D;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.*;
import javax.swing.JOptionPane;

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
        
        RefImage refs[] = {
            new RefImage("ref10.jpg", 10),
            new RefImage("ref20.jpg", 20)
        };
        
        GeometryProcessorJ2SE geom = new GeometryProcessorJ2SE();
        ColorProcessorJ2SE color = new ColorProcessorJ2SE();
        
        
        try {
            BufferedImage testBI = ImageIO.read(new File("test20.jpg"));
            //testBI = Main.convertToGraySclae(testBI);
            ByteBufferImage test = geom.bufferedToByte(testBI);
            
            test = color.expandDynamicRange(test, test.width/4, test.height/4, 3*test.width/4, 3*test.height/4, 0, 128);
            
            //test = color.quantize(test, 2);
            
            VertexPoint vp[] = geom.detectCornerVertices(test);
            test = geom.extractImage(test, vp);
            
            
            for (int i = 0; i < refs.length; i++) {
                BufferedImage refBI = ImageIO.read(new File(refs[i].filename));
                //refBI = Main.convertToGraySclae(refBI);
                refs[i].bbi = geom.bufferedToByte(refBI);
                refs[i].bbi = color.expandDynamicRange(refs[i].bbi);
                //refs[i].bbi = color.quantize(refs[i].bbi, 2);
            }
            
            
            if (test.width < refs[0].bbi.width) {
                System.out.println("test is smaller");
                for (int i = 0; i < refs.length; i++) {
                    double sx = ((double)test.width) / ((double) refs[i].bbi.width);
                    double sy = ((double)test.height) / ((double) refs[i].bbi.height);
                    refs[i].bbi = geom.scaleImage(refs[i].bbi,sx,sy);
                    refs[i].bbi = geom.cropImage(refs[i].bbi,0,0,test.width,test.height);
                }
            } else {
                System.out.println("test is bigger");
                double sx = ((double) refs[0].bbi.width) / ((double)test.width);
                double sy = ((double) refs[0].bbi.height) / ((double)test.height);
                test = geom.scaleImage(test,sx,sy);
                test = geom.cropImage(test,0,0,test.width,test.height);
            }
            
            int smallest_index = -1;
            double min_difference = Double.MAX_VALUE;
            for (int i = 0; i < refs.length; i++) {
                refs[i].difference = color.subtractImage(test, refs[i].bbi);
                if (min_difference > refs[i].difference) {
                    min_difference = refs[i].difference;
                    smallest_index = i;
                }
                System.out.println("Value=$"+refs[i].value+ " Difference="+ refs[i].difference);
            }
            
            String message = "THIS IS A $" + refs[smallest_index].value + " BILL!";
            JOptionPane.showMessageDialog(window, message);
            System.out.println(message);
            
            
            BufferedImage converted = geom.byteToBuffered(test);
            window.setLeftImage(converted);
            
            converted = geom.byteToBuffered(refs[smallest_index].bbi);
            window.setRightImage(converted);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        window.setVisible(true);
    }
    /*
    public static BufferedImage convertToGraySclae(BufferedImage img) {
        int width=img.getWidth();
        int height = img.getHeight();
        int type = java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
        BufferedImage grayImage = new java.awt.image.BufferedImage(width, height, type);
        Graphics2D g = grayImage.createGraphics();
        g.drawRenderedImage(img, null);
        g.dispose();
        return grayImage;
    }*/
}
