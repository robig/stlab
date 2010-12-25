package net.robig.stlab.gui.preferences;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.event.ListDataListener;

import net.robig.stlab.StLab;
import net.robig.stlab.StLabConfig;
import net.robig.stlab.util.SpringUtilities;

public class PreferencesModel implements ListModel{

	static private PreferencesModel instance=null;
	static public PreferencesModel getInctance(JFrame parent) {
		if(instance==null) new PreferencesModel(parent);
		return instance;
	}
	
	private Map<String, List<AbstractPreferenceControl>> dataMap=new HashMap<String, List<AbstractPreferenceControl>>();
	private List<String> sections=new ArrayList<String>();
	private JFrame frame=null;
	
	public PreferencesModel(JFrame parent) {
		instance=this;
		frame=parent;
		initialize();
	}
	
	public void addSection(String name,AbstractPreferenceControl[] data){
		sections.add(name);
		List<AbstractPreferenceControl> list = new ArrayList<AbstractPreferenceControl>();
		for(AbstractPreferenceControl c: data)list.add(c);
		dataMap.put(name, list);
	}
	
	private void initialize() {
		StLab.initializePreferences(this);
	}

	public int getSectionSize(String section) {
		List<?> l=dataMap.get(section);
		if(l!=null) return l.size();
		return 0;
	}
	
	public String[] getSections() {
		return sections.toArray(new String[]{});
	}
	
	public JPanel getSectionPanel(String section) {
		List<AbstractPreferenceControl> controls=dataMap.get(section);
		if(controls!=null){
			JPanel panel=new JPanel();
			//panel.setBackground(Color.red);
			SpringLayout layout=new SpringLayout();
			panel.setLayout(layout);
			Component c=null;
			for(AbstractPreferenceControl control: controls){
				JLabel label=new JLabel(control.getName(),JLabel.TRAILING);
				c=control.getComponent();
				label.setLabelFor(c);
				panel.add(label);
				panel.add(c);
				control.setParent(frame);
			}
			SpringUtilities.makeCompactGrid(panel, controls.size(), 2, 5, 5, 5, 5);
			Spring.height(c).setValue(24);
			return panel;
		}
		return new JPanel();
	}
	
	public JPanel getSectionPanel(int idx){
		String[] sections=getSections();
		if(sections.length<=idx) return null;
		return getSectionPanel(sections[idx]);
	}
	
	
	
	@Override
	public void addListDataListener(ListDataListener l) {
	}

	@Override
	public Object getElementAt(int index) {
		return sections.get(index);
	}

	@Override
	public int getSize() {
		return sections.size();
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
	}

}
