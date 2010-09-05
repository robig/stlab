package net.robig.stlab.util.config;

public class BoolValue {
	protected boolean value=false;
	String key=null;
	
	public BoolValue(String key,boolean v) {
		value=v;
		this.key=key;
	}
	
	public synchronized boolean getValue(){
		return value;
	}
	
	public synchronized void setValue(boolean val) {
		value=val;
		ObjectConfig.setBoolValue(key, val);
	}
	
	public synchronized String toString() {
		return value+"";
	}
}
