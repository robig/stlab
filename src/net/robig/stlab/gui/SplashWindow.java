package net.robig.stlab.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileNotFoundException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;

import net.robig.gui.ImagePanel;
import net.robig.logging.Logger;

public class SplashWindow extends JWindow{
	Logger log = new Logger(this.getClass());
    public SplashWindow(String filename,JFrame parent) throws FileNotFoundException{
        super(parent);
        ImageIcon back=ImagePanel.loadImageIcon(filename);
        if(back==null) throw new FileNotFoundException("Image "+filename+" not found in Classpath!");
        JLabel l = new JLabel(back);
        getContentPane().add(l, BorderLayout.CENTER);
        JLabel l2 = new JLabel();
        l2.setText("Loading StLab ...");
        getContentPane().add(l2, BorderLayout.SOUTH);
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
    
    public void close() {
    	setVisible(false);
    	dispose();
    }
    
    public static void main(String[] args) throws Exception {
		new SplashWindow("img/display0.png", null);
	}
}

