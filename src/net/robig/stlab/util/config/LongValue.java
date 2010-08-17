package net.robig.stlab.util.config;

public class LongValue {
	long value=0;
	
	public synchronized long getValue(){
		return value;
	}
	
	public synchronized void setValue(long val) {
		value=val;
	}
}
