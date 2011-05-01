package net.robig.stlab.gui.preferences;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.robig.stlab.util.config.StringValue;

public class PasswordTextPreferenceControl extends TextPreferenceControl {

	public PasswordTextPreferenceControl(String name, StringValue config) {
		super(name, config);
	}
	
	protected JTextField getField(){
		return new JPasswordField(10);
	}

	
}
