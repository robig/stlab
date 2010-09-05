package net.robig.stlab.gui;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JFrame;

abstract public class JFrameBase extends JFrame {
	
	// Check that we are on Mac OS X.  This is crucial to loading and using the OSXAdapter class.
    public static boolean MAC_OS_X = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));
	
    public void registerForMacOSXEvents() {
        if (MAC_OS_X) {
            try {
                // Generate and register the OSXAdapter, passing it a hash of all the methods we wish to
                // use as delegates for various com.apple.eawt.ApplicationListener methods
                OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("quit", (Class[])null));
                OSXAdapter.setAboutHandler(this, getClass().getDeclaredMethod("about", (Class[])null));
                OSXAdapter.setPreferencesHandler(this, getClass().getDeclaredMethod("preferences", (Class[])null));
//                OSXAdapter.setFileHandler(this, getClass().getDeclaredMethod("loadImageFile", new Class[] { String.class }));
            } catch (Exception e) {
                System.err.println("Error while loading the OSXAdapter:");
                e.printStackTrace();
            }
        }
    }
    abstract void quit();
    abstract void about();
    abstract void preferences();
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
    	Rectangle screen = getScreenBounds();
    	if(x<screen.x)x=0;
    	if(x>screen.width+screen.x)x=0;
    	if(y<screen.y)y=0;
    	if(y>screen.height-20)y=0;
    	super.setBounds(x, y, width, height);
    }
    
    public Rectangle getScreenBounds() {
    	GraphicsEnvironment ge;
    	ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

    	Rectangle vBounds = new Rectangle();

    	GraphicsDevice[] gdArray = ge.getScreenDevices();

    	for (int i = 0; i < gdArray.length; i++) {
    		GraphicsDevice gd = gdArray[i];

    		GraphicsConfiguration[] gcArray = gd.getConfigurations();

    		for (int j = 0; j < gcArray.length; j++)
    			vBounds = vBounds.union(gcArray[j].getBounds());
    	}

    	Point origin = vBounds.getLocation();
    	System.out.println("Virtual x = " + origin.x);
    	System.out.println("Virtual y = " + origin.y);

    	Dimension size = vBounds.getSize();
    	System.out.println("Virtual width = " + size.width);
    	System.out.println("Virtual height = " + size.height);
    	return vBounds;
    }
}
