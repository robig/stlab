package net.robig.stlab.gui.preferences;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import net.robig.stlab.util.config.BoolValue;

public class BoolPreferenceControl extends AbstractPreferenceControl {

	JCheckBox checkbox=null;
	BoolValue configValue=null;

	public BoolPreferenceControl(String name, BoolValue config) {
		super(name, config);
		configValue=config;
		initialize();
	}

	private void initialize() {
		checkbox=new JCheckBox();
		checkbox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				onChange();
			}
		});
		configUpdated();
	}

	@Override
	public void onChange() {
		configValue.setValue(checkbox.isSelected());
	}

	@Override
	public Component getComponent() {
		return checkbox;
	}

	@Override
	public void configUpdated() {
		checkbox.setSelected(configValue.getValue());
	}
	
	
}
