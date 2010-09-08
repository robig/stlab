package net.robig.stlab.gui.preferences;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.robig.logging.Logger;
import net.robig.stlab.StLab;

public class PreferencesFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private Logger log=new Logger(this);
	private JPanel jContentPane=null;
	private JPanel jSectionListPanel=null;
	private JScrollPane listScrollPane=null;
	private JList sectionList = null;
	private PreferencesModel preferences = null;
	private List<JPanel> sections=new ArrayList<JPanel>();
	private JPanel activeSection=null;
	
	public PreferencesFrame() {
		preferences=new PreferencesModel();
		initialize();
	}
	
	private void initialize(){
		this.setSize(500,500);
		this.setContentPane(getJContentPane());
		this.setTitle(StLab.applicationName+" Preferences");
		this.setName("Preferences");
		
		for(String section: preferences.getSections()){
			JPanel panel=preferences.getSectionPanel(section);
			sections.add(panel);
			panel.setVisible(true);
			
		}
		if(sections.size()>0)showSection(0);
	}

	private Container getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getSectionListPanel(), BorderLayout.WEST);
		}
		return jContentPane;
	}

	private Component getSectionListPanel() {
		if (jSectionListPanel == null) {
			jSectionListPanel = new JPanel();
			jSectionListPanel.setLayout(new BorderLayout());
			jSectionListPanel.add(getListScrollPane(), BorderLayout.CENTER);
		}
		return jSectionListPanel;
	}

	private Component getListScrollPane() {
		if (listScrollPane == null) {
			listScrollPane = new JScrollPane();
			listScrollPane.setViewportView(getSectionList());
		}
		return listScrollPane;
	}
	
	private void showSection(int index){
		//for(JPanel p: sections) p.setVisible(false);
		if(activeSection!=null)getContentPane().remove(activeSection);
		activeSection=sections.get(index);
		getContentPane().add(activeSection, BorderLayout.CENTER);
		//pack();
		log.debug("Showing section #"+index+" "+preferences.getElementAt(index));
	}

	private Component getSectionList() {
		if(sectionList==null){
			sectionList=new JList();
			sectionList.setModel(preferences);
			sectionList.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					showSection(sectionList.getSelectedIndex());
				}
			});
		}
		return sectionList;
	}
	
	
	
	public static void main(String[] args) {
		JFrame frame=new PreferencesFrame();
		frame.show();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
