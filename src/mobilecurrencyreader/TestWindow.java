package mobilecurrencyreader;

import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Dimension;

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
    
    
    /** Creates a new instance of TestWindow */
    public TestWindow() {
        super();
        
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
        //do test code here!
    }
    
}
