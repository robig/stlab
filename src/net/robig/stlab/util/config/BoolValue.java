package net.robig.stlab.util.config;

public class BoolValue extends AbstractValue<Boolean>{
	public BoolValue(String key, Boolean value) {
		super(key, value);
	}
	
	public BoolValue(String key,boolean v) {
		super(key,new Boolean(v));
	}
	
	public synchronized boolean getSimpleValue(){
		return value.booleanValue();
	}
	
	public synchronized String toString() {
		return value+"";
	}

	@Override
	public void fromString(String s) {
		value=Boolean.parseBoolean(s);
	}
}
