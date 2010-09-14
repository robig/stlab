package net.robig.stlab.gui.preferences;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import net.robig.stlab.util.config.StringValue;

public class TextPreferenceControl extends AbstractPreferenceControl {

	JTextField field=null;
	StringValue configValue=null;

	public TextPreferenceControl(String name, StringValue config) {
		super(name, config);
		configValue=config;
		initialize();
	}

	private void initialize() {
		field=new JTextField();
		//field.setPreferredSize(new Dimension(300, 12));
		field.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onChange();
			}
		});
		configUpdated();
	}

	@Override
	public void onChange() {
		configValue.setValue(field.getText());
	}

	@Override
	public Component getComponent() {
		return field;
	}

	@Override
	public void configUpdated() {
		field.setText(configValue.getValue());
	}
	
	
}
