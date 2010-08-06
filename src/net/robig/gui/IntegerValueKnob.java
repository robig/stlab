package net.robig.gui;

import javax.swing.event.ChangeListener;

public class IntegerValueKnob extends MyJKnob {

	int maxValue = 99;
	int minValue = 0;

	public IntegerValueKnob() {
		setValue(minValue);
	}
	
	public IntegerValueKnob(int initialValue) {
		super();
		setValue(initialValue);
	}
	
	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		if(maxValue==0) maxValue=1;
		this.maxValue = maxValue;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getValue() {
		// max = maxT-minT
		// ? = theta
		return (int) Math.round(getTheta()*maxValue/(maxTheta-minTheta));
	}
	
	public void setValue(int newval) {
		// max = maxT-minT
		// newval = ?
		setTheta(newval*(maxTheta-minTheta)/maxValue);
	}

	protected Object getChangeObject(){
		return this.getValue();
	}
	
	
	
}
