package net.robig.gui;

import java.awt.Graphics;
import java.awt.Image;
import static net.robig.gui.ImagePanel.loadImage;
import javax.swing.JComponent;
import net.robig.logging.Logger;

public class LED  extends JComponent {

	private static final long serialVersionUID = 1L;
	Logger log = new Logger(this.getClass());

	protected String imageFile="img/led_on.png"; 
	protected Image imgActive=null;
	boolean active=false;
	protected int offset=0;
	
	public LED() {
		init();
	}
	
	protected void init(){
		imgActive=loadImage(imageFile);
	}
	
	public boolean isActive() {
		return active;
	}

	public void activate() {
		if(active)return;
		log.debug("LED switched on: "+getName());
		active=true;
		repaint();
	}

	public void deActivate() {
		if(!active) return;
		log.debug("LED switched off: "+getName());
		active=false;
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		if(active){
			g.drawImage(imgActive,0,offset,this);
		}
		super.paint(g);
	}
	
	@Override
	public void setName(String name) {
		super.setName(name);
		setToolTipText(name);
	}

}
