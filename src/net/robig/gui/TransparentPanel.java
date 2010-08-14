package net.robig.gui;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class TransparentPanel extends JPanel {
	 
    ImageIcon bgImage=null;
 
    public TransparentPanel(String img) {
    	bgImage=new ImageIcon(ImagePanel.loadImage(img));
    }
 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
 
        Graphics2D g2d = (Graphics2D) g;
 
        Composite oldComp = g2d.getComposite();
 
        Composite alphaComp = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.3f);
 
        g2d.setComposite(alphaComp);
 
        g2d.drawImage(bgImage.getImage(), 0, 0, this.getWidth(), this.getHeight(),
                this);
 
        g2d.setComposite(oldComp);
    }
}
