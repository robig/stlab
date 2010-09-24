package net.robig.stlab.gui.preferences;

import java.awt.Component;

import javax.swing.JFrame;

import net.robig.stlab.util.config.AbstractValue;
import net.robig.stlab.util.config.IConfigListener;
import net.robig.stlab.util.config.ObjectConfig;

public abstract class AbstractPreferenceControl implements IConfigListener {
	String name=null;
	AbstractValue<?> configValue=null;
	JFrame parent=null;
	
	public void setParent(JFrame p){
		parent=p;
	}
	
	public AbstractPreferenceControl(String name,AbstractValue<?> config) {
		this.name=name;
		configValue=config;
		ObjectConfig.addConfigListener(this);
	}

	public String getName() {
		return name;
	}
	
	public abstract void onChange();
	
	public abstract Component getComponent();
}
