package net.robig.stlab.gui.preferences;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.SpringLayout;
import javax.swing.event.ListDataListener;

import net.robig.stlab.StLab;
import net.robig.stlab.StLabConfig;
import net.robig.stlab.util.SpringUtilities;

public class PreferencesModel implements ListModel{

	Map<String, List<AbstractPreferenceControl>> dataMap=new HashMap<String, List<AbstractPreferenceControl>>();
	List<String> sections=new ArrayList<String>();
	
	public PreferencesModel() {
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
			for(AbstractPreferenceControl control: controls){
				JLabel label=new JLabel(control.getName());
				Component c=control.getComponent();
				panel.add(label);
				panel.add(c);
			}
			panel.add(new JLabel("1"));
			panel.add(new JLabel("2"));
			SpringUtilities.makeGrid(panel, controls.size(), 2, 5, 5, 5, 5);
			return panel;
		}
		return new JPanel();
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
