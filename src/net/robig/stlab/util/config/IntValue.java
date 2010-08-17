package net.robig.stlab.util.config;

public class IntValue {
	public int value = 0;
	
	public synchronized int getValue(){
		return value;
	}
	
	public synchronized void setValue(int val) {
		value=val;
	}
}
