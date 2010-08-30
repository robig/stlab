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
		int delay=0;
		int ledOnTime=100;
		int ledOffTime=200-ledOnTime;
		long lastSwitch=0;
		
		public Blinker(BlinkableLED led) {
			this.led=led;
			timer=new Timer(ledOnTime,this);
			timer.setRepeats(false);
		}
		
		public synchronized void setDelay(int delay){
			if(delay<=ledOnTime){
				log.warn("Invalid delay time! "+delay);
				return;
			}
			boolean running=isAlive();
			if(running) stop();
			this.delay=delay;
			ledOffTime=delay-ledOnTime;
			timer.setDelay(ledOnTime);
			led.active=true;
			if(running) start();
		}
		
		public int getDelay(){
			return delay;
		}
		
		public void run() {
			if(!led.active){
				timer.setDelay(ledOffTime);
			}else{
				timer.setDelay(ledOnTime);
			}
			led.active=!led.active;
			//log.debug("switched led: "+led.active+" time:"+(System.currentTimeMillis()-lastSwitch));
			led.repaint();
			lastSwitch=System.currentTimeMillis();
			start();
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
