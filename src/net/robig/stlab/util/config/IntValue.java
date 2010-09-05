package net.robig.stlab.util.config;

public class IntValue implements IValue{
	public int value = 0;
	public String key="";
	
	public IntValue(String key,int value) {
		this.value=value;
		this.key=key;
	}
	
	public synchronized int getValue(){
		return value;
	}
	
	public synchronized void setValue(int val) {
		value=val;
		ObjectConfig.setIntValue(key, val);
	}
	
	@Override
	public String toString() {
		return value+"";
	}
}
