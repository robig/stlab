package net.robig.gui;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class MyJKnob extends JKnob implements MouseWheelListener {

	double startTheta = 0;
	int startx=0;
	int starty=0;
	
	double wheelSensitivity = 5;
	double mouseSensitivity = 150;
	
	public MyJKnob() {
		addMouseWheelListener(this);
		thetaBase=Math.PI+Math.PI/4;
		maxTheta=2*Math.PI-Math.PI/2;
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
		// TODO Auto-generated method stub
		super.processMouseMotionEvent(e);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
	   // int mx = startx-e.getX();
	    int my = starty-e.getY();

	    double t=startTheta+Math.PI*2*(double)(my)/mouseSensitivity;
	    setTheta(t);
	    System.out.println("Theta: "+getTheta()+" my="+my);
	}
	
	@Override
	public void setTheta(double theta) {
		super.setTheta(theta);
		fireChange();
	}
	
    @Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int clicks=e.getWheelRotation();
	
	    double t=getTheta()-clicks*(wheelSensitivity*Math.PI*2/100);
	    setTheta(t);
	    System.out.println("wheel: Theta: "+getTheta()+" clicks="+clicks);
	
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
	
	private synchronized void fireChange() {
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
