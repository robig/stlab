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
		Timer onTimer = null;
		Timer timer = null;
		int delay=0;
		int ledOnTime=100;
		int cycleTime=200;
		long lastSwitch=0;
		
		public Blinker(BlinkableLED led) {
			this.led=led;
			timer=new Timer(cycleTime,this);
			onTimer=new Timer(ledOnTime, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					fireOnTimer();
				}
			});
			onTimer.setRepeats(false);
		}
		
		public synchronized void setDelay(int delay){
			this.delay=delay;
			ledOnTime=100;
			if(delay<=ledOnTime){
				ledOnTime=delay/2;
			}
//			log.debug("using ledOnTime: "+ledOnTime+" delay: "+delay);
			
			boolean running=isAlive();
			if(running) stop();
			
			cycleTime=delay;
			timer.setDelay(cycleTime);
			
			run();
			
			if(running) start();
		}
		
		public int getDelay(){
			return delay;
		}
		
		private synchronized void fireOnTimer(){
			led.active=false;
			led.repaint();
		}
		
		private void startOnTimer() {
			onTimer.start();
		}
		
		public synchronized void run() {
			led.active=true;
			led.repaint();
			
			startOnTimer();
			
			log.debug("switched led: "+led.active+" time:"+(System.currentTimeMillis()-lastSwitch));
			lastSwitch=System.currentTimeMillis();
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
		blinker.setDelay(delay);
	}
	
	public int getDelay(){
		return blinker.getDelay();
	}
	
	public void setFrequency(double f) {
		blinker.setDelay((int)(1000/f));
	}
	
}
