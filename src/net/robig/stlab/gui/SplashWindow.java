package net.robig.stlab.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;

public class SplashWindow extends JWindow{
    public SplashWindow(String filename,JFrame parent){
        super(parent);
        JLabel l = new JLabel(new ImageIcon(filename));
        l.setText("Loading ...");
        getContentPane().add(l, BorderLayout.CENTER);
        pack();
        Dimension screenSize =
          Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = l.getPreferredSize();
        setLocation(screenSize.width/2 - (labelSize.width/2)-100,
                    screenSize.height/2 - (labelSize.height/2));
//        addMouseListener(new MouseAdapter()
//            {
//                public void mousePressed(MouseEvent e)
//                {
//                    setVisible(false);
//                    dispose();
//                }
//            });
        setVisible(true);
    }
    
    public void hide() {
    	setVisible(false);
    	dispose();
    }
    
    public static void main(String[] args) {
		new SplashWindow("img/display0.png", null);
	}
}

