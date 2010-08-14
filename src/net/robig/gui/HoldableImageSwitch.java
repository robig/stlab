package net.robig.gui;

public class HoldableImageSwitch extends ImageSwitch {

	BlinkableLED led=null;
	long mouseDownTime=0;
	protected long mouseHoldTime=1000;
	boolean holdMode=false;
	
	public HoldableImageSwitch(BlinkableLED led) {
		super(led);
		this.led=led;
	}

	@Override
	protected void onMouseDown() {
		mouseDownTime=System.currentTimeMillis();
	}
	
	private boolean isHoldInternal() {
		return System.currentTimeMillis()-mouseDownTime >= mouseHoldTime;
	}
	
	public boolean isHold(){
		return holdMode;
	}
	
	protected void onHold() {
		
	}
	
	protected void onUnHold() {
		
	}
	
	@Override
	protected void onMouseUp() {
		if(isHold()){
			log.debug("Hold mode disabled: "+getName());
			holdMode=false;
			led.blinkOff();
			onUnHold();
			return;
		}
		if(!isHoldInternal()){
			active=!active;
			doUpdate();
			onClick();
			holdMode=false;
		}else{
			log.debug("Hold mode enabled: "+getName());
			holdMode=true;
			led.blink();
			onHold();
		}
	}
}
