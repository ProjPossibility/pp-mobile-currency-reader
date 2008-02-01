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

import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.imageio.*;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
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
            new RefImage("ref10.jpg", "10.aiff", 10),
            new RefImage("ref20.jpg", "20.aiff", 20)
        };
        
        GeometryProcessorJ2SE geom = new GeometryProcessorJ2SE();
        ColorProcessorJ2SE color = new ColorProcessorJ2SE();
        
        
        try {
            BufferedImage testBI = ImageIO.read(new File((args.length > 0) ? args[0]:"test10.jpg"));
            
            ByteBufferImage testForVertex = geom.bufferedToByte(testBI);
            ByteBufferImage test = geom.bufferedToByte(testBI);
            
            testForVertex = color.expandDynamicRange(testForVertex, testForVertex.width/4, testForVertex.height/4, 3*testForVertex.width/4, 3*testForVertex.height/4, 0, 128);
            testForVertex = color.quantize(testForVertex, 2);
            
            VertexPoint vp[] = geom.detectCornerVertices(testForVertex);
            test = geom.extractImage(test, vp);
            
            test = color.expandDynamicRange(test);
            test = color.quantize(test, 2);
                
            for (int i = 0; i < refs.length; i++) {
                BufferedImage refBI = ImageIO.read(new File(refs[i].filename));
                refs[i].bbi = geom.bufferedToByte(refBI);
                refs[i].bbi = color.expandDynamicRange(refs[i].bbi);
                refs[i].bbi = color.quantize(refs[i].bbi, 2);
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
                test = geom.cropImage(test,0,0,refs[0].bbi.width,refs[0].bbi.height);
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
            
            String message = "THIS IS A $" + refs[smallest_index].value + " BILL! asdf";
            playClip(refs[smallest_index].audio_filename);
            //playAudioFile("10.aiff");
            
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
    
    
    private static void playClip(String filename) {
        try {
            // From file
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
            
            // At present, ALAW and ULAW encodings must be converted
            // to PCM_SIGNED before it can be played
            AudioFormat format = stream.getFormat();
            if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                format = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        format.getSampleRate(),
                        format.getSampleSizeInBits()*2,
                        format.getChannels(),
                        format.getFrameSize()*2,
                        format.getFrameRate(),
                        true);        // big endian
                stream = AudioSystem.getAudioInputStream(format, stream);
            }
            
            
            // Create the clip
            DataLine.Info info = new DataLine.Info(
                    Clip.class, stream.getFormat(), ((int)stream.getFrameLength()*format.getFrameSize()));
            
            Clip clip = (Clip) AudioSystem.getLine(info);
            
            // This method does not return until the audio file is completely loaded
            clip.open(stream);
            
            // Start playing
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("sound error");
        }
    }
}
