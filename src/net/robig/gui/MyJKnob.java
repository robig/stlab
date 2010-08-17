package net.robig.gui;

import java.awt.Container;
import static net.robig.gui.ImagePanel.loadImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.robig.stlab.StLabConfig;
import net.robig.stlab.util.config.DoubleValue;
import net.robig.stlab.util.config.IntValue;

/**
 * extends JKnob and implements a better (imho) dragging handling and mouse wheel
 * also provides use of images for the Knob
 * @see JKnob
 * @author robig
 *
 */
public class MyJKnob extends JKnob implements MouseWheelListener {
	
	double startTheta = 0;
	int startx=0;
	int starty=0;
	Image image = null;
	boolean dragging=false;
	
	DoubleValue wheelSensitivity = StLabConfig.getMouseWheelSensitivity();
	IntValue mouseSensitivity = StLabConfig.getMouseSensitivity();
	
	public MyJKnob() {
		addMouseWheelListener(this);
		thetaBase=Math.PI+Math.PI/8;
		maxTheta=2*Math.PI-Math.PI/4;
		image=loadImage(getImageFile());
		if(image!=null)
			setSize(image.getWidth(null),image.getHeight(null));
	}
	
	/**
	 * get the Imagefile, overwrite this to change the image
	 * @return
	 */
	protected String getImageFile() {
		return "img/knob.png";		
	}
	
	public boolean isDragging() {
		return dragging;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		startTheta = getTheta();
		startx=e.getX();
		starty=e.getY();
		//System.out.println("MouseDown on x="+startx+" y="+starty);
		super.mousePressed(e);
	}
	
	@Override
	protected void processMouseMotionEvent(MouseEvent e) {
		super.processMouseMotionEvent(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		dragging=false;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		dragging=true;
	   // int mx = startx-e.getX();
	    int my = starty-e.getY();

	    double t=startTheta+Math.PI*2*(double)(my)/mouseSensitivity.getValue();
	    setTheta(t);
	    //System.out.println("Theta: "+getTheta()+" my="+my);
	}
	
	/**
	 * set the new angle of the Knob
	 */
	@Override
	public void setTheta(double theta) {
		
		super.setTheta(theta);
		if(getTheta()!=theta) return;	
		onUpdate();
	}
	
	/** 
	 * anything else to do on change? overwrite this method,
	 * but remenber to call fireChange() for informing the changeListeners
	 */
	public void onUpdate(){
		fireChange();
	}
	
    @Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int clicks=e.getWheelRotation();
	
	    double t=getTheta()-clicks*(wheelSensitivity.getValue()*Math.PI*2/100);
	    setTheta(t);
	    //System.out.println("wheel: Theta: "+getTheta()+" clicks="+clicks);
	
	    repaint();
	}

    /**
     * get current mouse wheel sensitivity setting
     * @return
     */
	public double getWheelSensitivity() {
		return wheelSensitivity.getValue();
	}

	/**
	 * set the mouse wheel sensitivity
	 * @param wheelSensitivity
	 */
	public void setWheelSensitivity(double wheelSensitivity) {
		this.wheelSensitivity.setValue(wheelSensitivity);
	}

	/**
	 * get current mouse sensitivity setting
	 * @return
	 */
	public double getMouseSensitivity() {
		return mouseSensitivity.getValue();
	}

	/**
	 * set the mouse sensitivity
	 * @param mouseSensitivity
	 */
	public void setMouseSensitivity(int mouseSensitivity) {
		this.mouseSensitivity.setValue(mouseSensitivity);
	}
	
	List<ChangeListener> allChangeListeners = new ArrayList<ChangeListener>();
	/**
	 * als a Listender for changes
	 * @param listener
	 */
	public void addChangeListener(ChangeListener listener) {
		synchronized (allChangeListeners) {
			allChangeListeners.add(listener);
		}
	}
	
	/**
	 * internal method that fires a change (inform the listeners)
	 */
	protected synchronized void fireChange() {
		if(allChangeListeners==null)return;//in initialization
		ChangeEvent e=new ChangeEvent(getChangeObject());
		synchronized (allChangeListeners) {
			for(ChangeListener l:allChangeListeners){
				l.stateChanged(e);
			}
		}
	}
	
	/**
	 * internal method. can be overwritten to set the object that is sent on change
	 * @return
	 */
	protected Object getChangeObject(){
		return this.getAngle();
	}
	
	AffineTransform at = new AffineTransform();
	/**
	 * draw this component
	 */
    public void paint(Graphics g) {
    	Graphics2D copy = (Graphics2D)g.create();
    	copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    	at.setToRotation(getAngle()+thetaBase, image.getWidth(null)/2, image.getHeight(null)/2);
    	copy.transform(at);
    	copy.drawImage(image, 0,0,this);
    	
 		copy.dispose();
 			 
    }

	/**
     * Here main is used simply as a test method.  If this file
     * is executed "java JKnob" then this main() method will be
     * run.  However, if another file uses a JKnob as a component
     * and that file is run then this main is ignored.
     */
    public static void main(String[] args) {

		JFrame myFrame = new JFrame("JKnob Test method");
		
		Container thePane = myFrame.getContentPane();
	
		// Add a JKnob to the pane.
		thePane.add(new MyJKnob());
	
		myFrame.addWindowListener(new WindowAdapter() {
	             public void windowClosing(WindowEvent e) {
	                 System.exit(0);
	             }
	         });
	
		myFrame.pack();
		myFrame.show();
    }
}
