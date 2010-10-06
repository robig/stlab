package net.robig.stlab.gui.preferences;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import net.robig.stlab.util.config.StringValue;

/**
 * A preference control for text input
 * @author robig
 *
 */
public class TextPreferenceControl extends AbstractPreferenceControl {

	private JTextField field=null;
	private StringValue configValue=null;

	public TextPreferenceControl(String name, StringValue config) {
		super(name, config);
		configValue=config;
		initialize();
	}

	private void initialize() {
		field=new JTextField(10);
		//field.setPreferredSize(new Dimension(300, 12));
		field.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onChange();
			}
		});
		configUpdated();
	}

	/**
	 * on Value change, set Textfield value
	 */
	@Override
	public void onChange() {
		configValue.setValue(field.getText());
	}

	/**
	 * get the Component
	 */
	@Override
	public Component getComponent() {
		return field;
	}

	@Override
	public void configUpdated() {
		field.setText(configValue.getValue());
	}
	
	
}
