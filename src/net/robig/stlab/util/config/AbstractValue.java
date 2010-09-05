package net.robig.stlab.util.config;

public abstract class AbstractValue<E> {
	public abstract void setValue(E v);
	public abstract E getValue();
	public abstract String toString(); 
}
