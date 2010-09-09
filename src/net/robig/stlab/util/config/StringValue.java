package net.robig.stlab.util.config;

public class StringValue extends AbstractValue<String> {
	public StringValue(String key, String value) {
		super(key, value);
	}

	public synchronized String getValue(){
		return value;
	}
	
	public synchronized void setValue(String val) {
		value=val;
		postSetValue();
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public void fromString(String s) {
		// TODO Auto-generated method stub
		
	}
}
