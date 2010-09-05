package net.robig.stlab.util.config;

public class StringValue extends AbstractValue<String> {
	private String value = null;
	
	public synchronized String getValue(){
		return value;
	}
	
	public synchronized void setValue(String val) {
		value=val;
	}

	@Override
	public String toString() {
		return value;
	}
}
