package net.robig.stlab.gui.preferences;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
	private JPanel buttonPanel=null;
	private JPanel jSectionListPanel=null;
	private JScrollPane listScrollPane=null;
	private JList sectionList = null;
	private PreferencesModel preferences = null;
	private JButton closeButton = null;
	
	private JPanel cards=null;
	private List<JPanel> sections=new ArrayList<JPanel>();
	private JPanel activeSection=null;
	
	public PreferencesFrame() {
		preferences=new PreferencesModel();
		initialize();
	}
	
	private void initialize(){
		this.setSize(600,500);
		this.setContentPane(getJContentPane());
		this.setTitle(StLab.applicationName+" Preferences");
		this.setName("Preferences");

	}

	private Container getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getSectionListPanel(), BorderLayout.WEST);
			jContentPane.add(getCardPanel(), BorderLayout.CENTER);
			jContentPane.add(getButtonPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}
	
	private Component getButtonPanel() {
		if(buttonPanel==null){
			buttonPanel=new JPanel(new BorderLayout());
			closeButton=new JButton("Close");
			closeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					close();
				}
			});
			buttonPanel.add(closeButton,BorderLayout.AFTER_LINE_ENDS);
		}
		return buttonPanel;
	}

	private Container getCardPanel() {
		if(cards==null){
			cards=new JPanel(new CardLayout());
			
			for(String section: preferences.getSections()){
				JPanel parent=new JPanel();
				//TODO: scrollbar
				parent.setBorder(BorderFactory.createTitledBorder("Configure "+section));
				JPanel panel=preferences.getSectionPanel(section);
				parent.add(panel);
				parent.setName(section);
				cards.add(parent,section);
				//panel.setVisible(true);
				sections.add(parent);
			}
			if(sections.size()>0)showSection(0);
		}
		return cards;
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
		//if(activeSection!=null)getContentPane().remove(activeSection);
		activeSection=sections.get(index);
		sectionList.setSelectedIndex(index);
		//getContentPane().add(activeSection, BorderLayout.CENTER);
		//pack();
		CardLayout cl=(CardLayout)cards.getLayout();
		cl.show(cards, activeSection.getName());
		log.debug("Showing section #"+index+" name="+activeSection.getName()+"; "+preferences.getElementAt(index));
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
	
	public void close() {
		this.setVisible(false);
	}
	
	public static void main(String[] args) {
		JFrame frame=new PreferencesFrame(){
			@Override
			public void close() {
				super.close();
				System.exit(0);
			}
		};
		frame.show();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
