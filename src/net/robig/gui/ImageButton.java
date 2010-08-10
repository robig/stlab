package net.robig.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import static net.robig.gui.ImagePanel.loadImage;
import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

import net.robig.logging.Logger;

public class ImageButton extends JComponent
	implements MouseInputListener {

	private static final long serialVersionUID = 1L;
	Logger log = new Logger(this.getClass());
	protected String imageFile="img/switch.png"; 
	protected Image imgPressed=null;
	boolean pressed=false;
	protected int offset=3;
	
	public ImageButton() {
		init();
	}
	
	protected void init(){
		imgPressed=loadImage(imageFile);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void onClick() {
		
	}
	
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		pressed=false;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(pressed)return;
		onClick();
		log.debug("Button pressed: "+getName());
		pressed=true;
		repaint();
		onMouseDown();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(!pressed) return;
//		log.debug("Button released: "+getName());
		pressed=false;
		repaint();
		onMouseUp();
	}
	
	protected void onMouseDown() {} 
	protected void onMouseUp() {}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
	
	@Override
	public void paint(Graphics g) {
		if(pressed){
			g.drawImage(imgPressed,0,offset,this);
		}
		super.paint(g);
	}
	
	@Override
	public void setName(String name) {
		super.setName(name);
		setToolTipText(name);
	}

}
