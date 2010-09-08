package net.robig.stlab.gui.preferences;

import java.awt.Component;

import javax.swing.JTextField;

import net.robig.stlab.util.config.AbstractValue;
import net.robig.stlab.util.config.StringValue;

public class TextPreferenceControl extends AbstractPreferenceControl {

	JTextField field=null;
	StringValue configValue=null;

	public TextPreferenceControl(String name, StringValue config) {
		super(name, config);
		initialize();
	}

	private void initialize() {
		field=new JTextField();
	}

	@Override
	public void onChange() {
		configValue.setValue(field.getText());
	}

	@Override
	public Component getComponent() {
		return field;
	}
	
	
}
