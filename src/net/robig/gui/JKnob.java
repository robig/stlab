package net.robig.gui;

// Imports for the GUI classes.
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * JKnob.java - 
 *   A knob component.  The knob can be rotated by dragging 
 *   a spot on the knob around in a circle.
 *   The knob will report its position in radians when asked.
 *
 * @author Grant William Braught
 * @author Dickinson College
 * @author robig
 * @version 12/4/2000
 */

class JKnob 
    extends JComponent
    implements MouseListener, MouseMotionListener {

    private static final int radius = 50;
    protected static final int spotRadius = 10;
    protected double minTheta = 0;
    protected double maxTheta = 2*Math.PI;
    protected double thetaBase = 0;

    private double theta;
    public double getTheta() {
		return theta;
	}

	public void setTheta(double theta) {
		if(this.theta==theta)return;
		if(theta>maxTheta) theta=maxTheta;
		if(theta<minTheta) theta=minTheta;
		this.theta = theta;
		repaint();
	}
	
	public static int getRadius() {
		return radius;
	}
	
	public boolean isThetaValid(double theta){
		return theta>=minTheta && theta<=maxTheta;
	}

	public boolean isPressedOnSpot() {
		return pressedOnSpot;
	}

	private Color knobColor;
    protected Color spotColor;

    private boolean pressedOnSpot;

    /**
     * No-Arg constructor that initializes the position
     * of the knob to 0 radians (Up).
     */
    public JKnob() {
    	this(0);
    }

    /**
     * Constructor that initializes the position
     * of the knob to the specified angle in radians.
     *
     * @param initAngle the initial angle of the knob.
     */
    public JKnob(double initTheta) {
    	this(initTheta, Color.gray, Color.black);
    }
    
    /**
     * Constructor that initializes the position of the
     * knob to the specified position and also allows the
     * colors of the knob and spot to be specified.
     *
     * @param initAngle the initial angle of the knob.
     * @param initColor the color of the knob.
     * @param initSpotColor the color of the spot.
     */
    public JKnob(double initTheta, Color initKnobColor, 
		 Color initSpotColor) {

		setTheta(initTheta);
		pressedOnSpot = false;
		knobColor = initKnobColor;
		spotColor = initSpotColor;
	
		this.addMouseListener(this);	
		this.addMouseMotionListener(this);
    }

    /**
     * Paint the JKnob on the graphics context given.  The knob
     * is a filled circle with a small filled circle offset 
     * within it to show the current angular position of the 
     * knob.
     *
     * @param g The graphics context on which to paint the knob.
     */
    public void paint(Graphics g) {

		// Draw the knob.
		g.setColor(knobColor);
		g.fillOval(0,0,2*radius,2*radius);
	
		// Find the center of the spot.
		Point pt = getSpotCenter();
		int xc = (int)pt.getX();
		int yc = (int)pt.getY();
	
		// Draw the spot.
		g.setColor(spotColor);
		g.fillOval(xc-spotRadius, yc-spotRadius,
			   2*spotRadius, 2*spotRadius);
    }

    /**
     * Return the ideal size that the knob would like to be.
     *
     * @return the preferred size of the JKnob.
     */
    public Dimension getPreferredSize() {
    	return new Dimension(2*radius,2*radius);
    }

    /**
     * Return the minimum size that the knob would like to be.
     * This is the same size as the preferred size so the
     * knob will be of a fixed size.
     *
     * @return the minimum size of the JKnob.
     */
    public Dimension getMinimumSize() {
    	return new Dimension(2*radius,2*radius);
    }

    /**
     * Get the current anglular position of the knob.
     *
     * @return the current anglular position of the knob.
     */
    public double getAngle() {
    	return theta;
    }


    /** 
     * Calculate the x, y coordinates of the center of the spot.
     *
     * @return a Point containing the x,y position of the center
     *         of the spot.
     */ 
    protected Point getSpotCenter() {

		// Calculate the center point of the spot RELATIVE to the
		// center of the of the circle.
	
		int r = radius - spotRadius;
	
		int xcp = (int)(r * Math.sin(theta+thetaBase));
		int ycp = (int)(r * Math.cos(theta+thetaBase));
	
		// Adjust the center point of the spot so that it is offset
		// from the center of the circle.  This is necessary because
		// 0,0 is not actually the center of the circle, it is  the 
	        // upper left corner of the component!
		int xc = radius + xcp;
		int yc = radius - ycp;
	
		// Create a new Point to return since we can't  
		// return 2 values!
		return new Point(xc,yc);
    }

    /**
     * Determine if the mouse click was on the spot or
     * not.  If it was return true, otherwise return 
     * false.
     *
     * @return true if x,y is on the spot and false if not.
     */
    private boolean isOnSpot(Point pt) {
    	return (pt.distance(getSpotCenter()) < spotRadius);
    }

    // Methods from the MouseListener interface.

    /**
     * Empy method because nothing happens on a click.
     *
     * @param e reference to a MouseEvent object describing 
     *          the mouse click.
     */
    public void mouseClicked(MouseEvent e) {}

    /**
     * Empty method because nothing happens when the mouse
     * enters the Knob.
     *
     * @param e reference to a MouseEvent object describing
     *          the mouse entry.
     */
    public void mouseEntered(MouseEvent e) {}

    /**
     * Empty method because nothing happens when the mouse
     * exits the knob.
     *
     * @param e reference to a MouseEvent object describing
     *          the mouse exit.
     */
    public void mouseExited(MouseEvent e) {}

    /**
     * When the mouse button is pressed, the dragging of the
     * spot will be enabled if the button was pressed over
     * the spot.
     *
     * @param e reference to a MouseEvent object describing
     *          the mouse press.
     */
    public void mousePressed(MouseEvent e) {

		Point mouseLoc = e.getPoint();
		pressedOnSpot = isOnSpot(mouseLoc);
    }

    /**
     * When the button is released, the dragging of the spot
     * is disabled.
     *
     * @param e reference to a MouseEvent object describing
     *          the mouse release.
     */
    public void mouseReleased(MouseEvent e) {
    	pressedOnSpot = false;
    }
    
    // Methods from the MouseMotionListener interface.

    /**
     * Empty method because nothing happens when the mouse
     * is moved if it is not being dragged.
     *
     * @param e reference to a MouseEvent object describing
     *          the mouse move.
     */
    public void mouseMoved(MouseEvent e) {}

    /**
     * Compute the new angle for the spot and repaint the 
     * knob.  The new angle is computed based on the new
     * mouse position.
     *
     * @param e reference to a MouseEvent object describing
     *          the mouse drag.
     */
    public void mouseDragged(MouseEvent e) {
		if (pressedOnSpot) {
	
		    int mx = e.getX();
		    int my = e.getY();
	
		    // Compute the x, y position of the mouse RELATIVE
		    // to the center of the knob.
		    int mxp = mx - radius;
		    int myp = radius - my;
	
		    // Compute the new angle of the knob from the
		    // new x and y position of the mouse.  
		    // Math.atan2(...) computes the angle at which
		    // x,y lies from the positive y axis with cw rotations
		    // being positive and ccw being negative.
		    setTheta(Math.atan2(mxp, myp));
	
		    
		}
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
	thePane.add(new JKnob());

	myFrame.addWindowListener(new WindowAdapter() {
             public void windowClosing(WindowEvent e) {
                 System.exit(0);
             }
         });

	myFrame.pack();
	myFrame.show();
    }
}
