package net.robig.stlab.util.config;

public class DoubleValue {
	double value = 0;
	
	public synchronized double getValue(){
		return value;
	}
	
	public synchronized void setValue(double val) {
		value=val;
	}
	
	@Override
	public String toString() {
		return value+"";
	}
}
