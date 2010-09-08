package net.robig.stlab.gui.preferences;

import java.awt.Component;

import javax.swing.JCheckBox;
import net.robig.stlab.util.config.BoolValue;

public class BoolPreferenceControl extends AbstractPreferenceControl {

	JCheckBox checkbox=null;
	BoolValue configValue=null;

	public BoolPreferenceControl(String name, BoolValue config) {
		super(name, config);
		initialize();
	}

	private void initialize() {
		checkbox=new JCheckBox();
	}

	@Override
	public void onChange() {
		configValue.setValue(checkbox.isSelected());
	}

	@Override
	public Component getComponent() {
		return checkbox;
	}
	
	
}
