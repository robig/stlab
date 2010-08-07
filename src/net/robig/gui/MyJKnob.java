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


public class MyJKnob extends JKnob implements MouseWheelListener {
	
	double startTheta = 0;
	int startx=0;
	int starty=0;
	Image image = null;
	
	double wheelSensitivity = 5;
	double mouseSensitivity = 150;
	
	public MyJKnob() {
		addMouseWheelListener(this);
		thetaBase=Math.PI+Math.PI/8;
		maxTheta=2*Math.PI-Math.PI/4;
		image=loadImage(getImageFile());
		setSize(image.getWidth(null),image.getHeight(null));
	}
	
	protected String getImageFile() {
		return "img/knob.png";		
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
	public void mouseDragged(MouseEvent e) {
	   // int mx = startx-e.getX();
	    int my = starty-e.getY();

	    double t=startTheta+Math.PI*2*(double)(my)/mouseSensitivity;
	    setTheta(t);
	    //System.out.println("Theta: "+getTheta()+" my="+my);
	}
	
	@Override
	public void setTheta(double theta) {
		
		super.setTheta(theta);
		if(getTheta()!=theta) return;	
		onUpdate();
	}
	
	public void onUpdate(){
		fireChange();
	}
	
    @Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int clicks=e.getWheelRotation();
	
	    double t=getTheta()-clicks*(wheelSensitivity*Math.PI*2/100);
	    setTheta(t);
	    //System.out.println("wheel: Theta: "+getTheta()+" clicks="+clicks);
	
	    repaint();
	}

	public double getWheelSensitivity() {
		return wheelSensitivity;
	}

	public void setWheelSensitivity(double wheelSensitivity) {
		this.wheelSensitivity = wheelSensitivity;
	}

	public double getMouseSensitivity() {
		return mouseSensitivity;
	}

	public void setMouseSensitivity(double mouseSensitivity) {
		this.mouseSensitivity = mouseSensitivity;
	}
	

	List<ChangeListener> allChangeListeners = new ArrayList<ChangeListener>();
	public void addChangeListener(ChangeListener listener) {
		synchronized (allChangeListeners) {
			allChangeListeners.add(listener);
		}
	}
	
	protected synchronized void fireChange() {
		if(allChangeListeners==null)return;//in initialization
		ChangeEvent e=new ChangeEvent(getChangeObject());
		synchronized (allChangeListeners) {
			for(ChangeListener l:allChangeListeners){
				l.stateChanged(e);
			}
		}
	}
	
	protected Object getChangeObject(){
		return this.getAngle();
	}
	
	AffineTransform at = new AffineTransform();
    public void paint(Graphics g) {

//		// Draw the knob.
//		g.setColor(knobColor);
//		g.fillOval(0,0,2*radius,2*radius);
//	
		//  2*spotRadius, 2*spotRadius);
    	Graphics2D copy = (Graphics2D)g.create();
    	copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    	at.setToRotation(getAngle()+thetaBase, image.getWidth(null)/2, image.getHeight(null)/2);
    	copy.transform(at);
    	copy.drawImage(image, 0,0,this);
    	
    	
//    	// Find the center of the spot.
// 		Point pt = getSpotCenter();
// 		int xc = (int)pt.getX();
// 		int yc = (int)pt.getY();
// 	
// 		// Draw the spot.
// 		copy.setColor(spotColor);
// 		copy.transform(new AffineTransform());
// 		copy.fillOval(xc-spotRadius, yc-spotRadius, 2*spotRadius, 2*spotRadius);
 		
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
