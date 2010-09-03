package net.robig.gui;

public class IntToggleButton extends ImageButton {
	private static final long serialVersionUID = 1L;

	int currentValueOffset=0;
	int[] values = {0,1};
	String[] displayedValues={"0","1"};
	
	public IntToggleButton() {
		imageFile="img/button.png";
		init();
	}

	public synchronized int getValue(){
		return values[currentValueOffset];
	}
	
	public synchronized void setValue(int value) {
		for(int i=0;i<values.length;i++){
			if(values[i]==value) {
				currentValueOffset=i;
				return;
			}
		}
		log.warn("trying to set a value that does not exist: "+value);
	}
	
	public synchronized void setPossibleValues(int[] list){
		if(list.length<1){
			log.warn("possible valus must contain at least one value!");
			return;
		}
		values=list;
		displayedValues=new String[values.length];
		for(int i=0;i<values.length;i++) displayedValues[i]=""+values[i];
		currentValueOffset=0;
	}
	
	public synchronized void setDisplayedValues(String[] dispValues){
		if(dispValues.length != values.length) {
			log.warn("Cannot assign less display values than possible values!");
			return;
		}
		displayedValues=dispValues;
	}
	
	@Override
	protected void onMouseDown() {
		super.onMouseDown();
		toggle();
	}
	
	public synchronized void toggle() {
		currentValueOffset++;
		if(currentValueOffset>=values.length) currentValueOffset=0;
		log.debug("toggeld to value: "+getValue());
		setToolTipText(getName()+": "+getDisplayedValue());
		onToggle();
	}
	
	public void onToggle() {}
	
	public synchronized String getDisplayedValue() {
		return displayedValues[currentValueOffset];
	}
}
