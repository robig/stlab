package net.robig.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class BlinkableLED extends LED {
	
	public BlinkableLED() {
		blinker=new Blinker(this);
	}
	
	private class Blinker implements ActionListener{

		BlinkableLED led=null;
		Timer timer= null;
		
		public Blinker(BlinkableLED led) {
			this.led=led;
			timer=new Timer(400,this);
		}
		
		public synchronized void setDelay(int delay){
			timer.setDelay(delay);
		}
		
		public int getDelay(){
			return timer.getDelay();
		}
		
		public void run() {
			led.active=!led.active;
			led.repaint();
		}
		
		public void start() {
			if(!isAlive())
				timer.start();
		}
		
		public void stop(){
			if(!isAlive())return;
			timer.stop();
		}
		private boolean isAlive() {
			return timer.isRunning();
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			run();
		}
		
	}
	
	Blinker blinker=null;
	int state=0;
	
	public int getState() {
		return state;
	}

	@Override
	public void activate() {
		state=1;
		blinker.stop();
		super.activate();
	}
	
	@Override
	public void deActivate() {
		state=0;
		blinker.stop();
		super.deActivate();
	}
	
	public void blinkOff() {
		if(isActive()){
			state=1;
		}else{
			state=0;
		}
		blinker.stop();
		repaint();
	}
	
	public void blink(){
		state=3;
		blinker.start();
	}
	
	public void setDelay(int delay){
		blinker.setDelay(delay/2);
	}
	
	public int getDelay(){
		return blinker.getDelay()*2;
	}
	
}
