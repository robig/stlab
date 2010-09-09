package net.robig.stlab.util.config;

public class IntValue extends AbstractValue<Integer>{

	public IntValue(String key, Integer value) {
		super(key, value);
	}

	public synchronized int getSimpleValue(){
		return value.intValue();
	}
	
	public synchronized void setValue(int val) {
		value=val;
		postSetValue();
	}
	
	@Override
	public String toString() {
		return value+"";
	}

	@Override
	public void fromString(String s) {
		// TODO Auto-generated method stub
		
	}
}
