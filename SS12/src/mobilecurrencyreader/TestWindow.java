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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.image.DataBuffer;

/**
 *
 * @author Kevin
 */
public class TestWindow extends JFrame implements ActionListener {
    
    JPanel mainPanel = new JPanel(new BorderLayout());
    JPanel imagePanel = new JPanel();
    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JButton button = new JButton("Do Something!");
    BufferedImage leftImage;
    
    boolean tmp = false;
    
    /** Creates a new instance of TestWindow */
    public TestWindow() {
        super();
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.setSize(new Dimension(800,400));
        
        //this.setLayout(box);
        
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(button, BorderLayout.SOUTH);
        
        button.addActionListener(this);
        
        this.setContentPane(mainPanel);
    }
    
    public void setLeftImage(BufferedImage image) {
        setImagePanel(image, leftPanel);
        leftImage = image;
    }
    
    public void setRightImage(BufferedImage image) {
        setImagePanel(image, rightPanel);
    }
    
    private void setImagePanel(BufferedImage image, JPanel panel) {
        ImageIcon icon = new ImageIcon(image);
        JLabel label = new JLabel();
        label.setIcon(icon);
        panel.removeAll();
        panel.add(label);
        panel.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        /*BufferedImage gray = convertToGrayScale(leftImage);
        
        GeometryProcessorJ2SE gp = new GeometryProcessorJ2SE();
        ColorProcessorJ2SE cp = new ColorProcessorJ2SE();
        
        ByteBufferImage bbi = gp.bufferedToByte(gray);
        
        bbi = cp.expandDynamicRange(bbi, bbi.width/4, bbi.height/4, 3*bbi.width/4, 3*bbi.height/4, 0, 128);
            
        VertexPoint vp[] = gp.detectCornerVertices(bbi);
        gp.drawPoints(bbi, vp);
        
        bbi = gp.extractImage(bbi, vp);
        
        
        
        BufferedImage new_buf = gp.byteToBuffered(bbi);
        
        setRightImage(new_buf);
        
        rightPanel.invalidate();*/
    }
    

    
    public BufferedImage convertToGrayScale(BufferedImage img){
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
