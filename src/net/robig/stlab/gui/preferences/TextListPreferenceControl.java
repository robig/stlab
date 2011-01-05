package net.robig.stlab.gui.preferences;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.robig.logging.Logger;
import net.robig.stlab.util.config.ObjectConfig;
import net.robig.stlab.util.config.StringValue;

public class TextListPreferenceControl extends AbstractPreferenceControl {

	JPanel rootPane=null;
	JPanel listPane=null; 
	List<StringValue> list=new ArrayList<StringValue>();
	String prefix=null;
	Logger log=new Logger(this);
	
	public TextListPreferenceControl(String name, String prefix) {
		super(name, null);
		this.prefix=prefix;
		initialize();
	}

	private void initialize() {
		rootPane=new JPanel();
		rootPane.setLayout(new BorderLayout());
		rootPane.setBorder(BorderFactory.createTitledBorder("List "+getName()));
		listPane=new JPanel();
		listPane.setLayout(new FlowLayout());
		rootPane.add(listPane,BorderLayout.CENTER);
		JButton addButton = new JButton("Add");
		rootPane.add(addButton,BorderLayout.AFTER_LAST_LINE);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addButtonPressed();
			}
		});
		
		log.debug("requesting config keys for "+prefix);
		Set<Object> keys=ObjectConfig.getInstance().filterProperties(prefix).keySet();
		for(Object k: keys){
			String key=k.toString();
			log.debug("key: "+key);
			StringValue value=ObjectConfig.getStringValue(key, "");
			if(!value.equals("")){
				add(value);
			}
		}
		
	}
	
	private void add(StringValue value){
		list.add(value);
		TextPreferenceControl control = new TextPreferenceControl(value.key, value);
		listPane.add(new JLabel(control.getName()));
		listPane.add(control.getComponent());
	}
	
	public void addButtonPressed() {
		log.debug("Add button pressed");
		JTextField keyField=new JTextField();
		JTextField valueField=new JTextField();
		Object[] array={"Key:",
				keyField,
				"Value:",valueField
				};
		JOptionPane optionPane = new JOptionPane(
				array,JOptionPane.QUESTION_MESSAGE,JOptionPane.OK_OPTION,null);
	}

	@Override
	public Component getComponent() {
		return rootPane;
	}

	@Override
	public void onChange() {
		
	}

	@Override
	public void configUpdated() {

	}

}
