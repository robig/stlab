package net.robig.stlab.gui.preferences;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class LabelPreferenceControl extends AbstractPreferenceControl {
	String text=null;
	JLabel label=null;
	
	public LabelPreferenceControl(String text) {
		super("", null);
		this.text=text;
		this.label=new JLabel(text);
	}

	
	@Override
	public JComponent getComponent() {
		return label;
	}

	@Override
	public void onChange() {
	}

	@Override
	public void configUpdated() {

	}

}
