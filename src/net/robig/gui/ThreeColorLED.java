package net.robig.gui;

import java.awt.Graphics;
import java.awt.Image;
import static net.robig.gui.ImagePanel.loadImage;

public class ThreeColorLED extends LED {
	int state=0;
	protected Image imgOrange=null;
	
	public ThreeColorLED() {
		init();
	}
	
	@Override
	protected void init() {
		super.init();
		imgOrange=loadImage("img/led_orange.png");
	}
	
	public int getState() {
		return state;
	}

	@Override
	public void activate() {
		state=1;
		super.activate();
	}
	
	@Override
	public void deActivate() {
		state=0;
		super.deActivate();
	}
	
	public void setState(int s) {
		if(s>2||s<0) return;
		state=s;
		repaint();
	}
	
	public void orange(){
		state=3;
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		//state == 0 -> Green back
		if(state==1){
			//Orange
			g.drawImage(imgOrange,0,offset,this);
		}else if(state==2){
			//Red
			g.drawImage(imgActive,0,offset,this);
		}
	}
}
