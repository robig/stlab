package net.robig.gui;

import java.awt.Color;

import javax.swing.border.LineBorder;

public class ImageSwitch extends ImageButton {
	
	boolean active=false;
	LED indicator = null;
	
	public ImageSwitch(LED led) {
		indicator=led;
		imageFile="img/button_long.png";
		//setBorder(new LineBorder(new Color(0,0,255)));
		init();
	}
	
	public void setActive(boolean a){
		active=a;
		onUpdate();
	}
	
	@Override
	protected void onMouseDown() {
		onUpdate();
	}
	
	protected synchronized void onUpdate() {
		if(!active){
			activate();
			active=true;
		}else{
			deactivate();
			active=false;
		}
		setToolTipText(getName()+": "+(active?"active":"not active"));
	}
	
	private void activate() {
		if(indicator!=null){
			indicator.activate();
		}
		onActivate();
	}
	
	private void deactivate() {
		if(indicator!=null){
			indicator.deActivate();
		}
		onDeactivate();
	}
	
	public void onDeactivate() {}
	public void onActivate() {}
}
