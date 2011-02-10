package net.robig.stlab.gui.preferences;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JComponent;
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
		/* action listener is performed when closing preferences: */
		field.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onChange();
			}
		});
		field.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				onChange();
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
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
	public JComponent getComponent() {
		return field;
	}

	@Override
	public void configUpdated() {
		if(!configValue.getValue().equals(field.getText()))
			field.setText(configValue.getValue());
	}
	
	
}
