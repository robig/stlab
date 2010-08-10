package net.robig.gui;

public class HoldableImageSwitch extends ImageSwitch {

	long mouseDownTime=0;
	protected long mouseHoldTime=2000;
	
	public HoldableImageSwitch(LED led) {
		super(led);
	}

	@Override
	protected void onMouseDown() {
		mouseDownTime=System.currentTimeMillis();
	}
	
	public boolean isHold() {
		return System.currentTimeMillis()-mouseDownTime >= mouseDownTime;
	}
	
	protected void onHold() {
		
	}
	
	@Override
	protected void onMouseUp() {
		if(!isHold()){
			onUpdate();
			onClick();
		}else
			onHold();
	}
}
