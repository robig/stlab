package net.robig.stlab.gui.preferences;

import java.awt.Component;

import net.robig.stlab.util.config.AbstractValue;

public abstract class AbstractPreferenceControl {
	String name=null;
	AbstractValue<?> configValue=null;
	
	
	public AbstractPreferenceControl(String name,AbstractValue<?> config) {
		this.name=name;
		configValue=config;
	}

	public String getName() {
		return name;
	}
	
	public abstract void onChange();
	
	public abstract Component getComponent();
}
