package net.robig.gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class ThreeWaySwitch extends ImageButton {

	ThreeColorLED led = null;
	int state=0;
	
	public ThreeWaySwitch(ThreeColorLED led) {
		this.led=led;
		imageFile="img/button_long.png";
		init();
	}
	
	public void setState(int s){
		if(s>2 || s<0 )return;
		state=s;
		doUpdate();
	}

	public int getState(){
		return state;
	}
	
	public void onClick() {
		state++;
		if(state>2)state=0;	
		doUpdate();
	}
	
	private void doUpdate() {
		if(led!=null){
			led.setState(state);
			log.debug("Setting led state="+state);
		}
		setToolTipText(getName()+": "+state);
	}
}
