package net.robig.stlab.util.config;

public class StringValue {
	private String value = null;
	
	public synchronized String getValue(){
		return value;
	}
	
	public synchronized void setValue(String val) {
		value=val;
	}
}
