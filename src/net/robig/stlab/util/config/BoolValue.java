package net.robig.stlab.util.config;

public class BoolValue {
	boolean value=false;
	
	public synchronized boolean getValue(){
		return value;
	}
	
	public synchronized void setValue(boolean val) {
		value=val;
	}
}
