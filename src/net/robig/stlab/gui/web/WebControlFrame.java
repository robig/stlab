package net.robig.stlab.gui.web;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import net.robig.gui.ImagePanel;
import net.robig.net.WebAccess;
import net.robig.stlab.StLabConfig;
import net.robig.stlab.gui.DeviceFrame;
import net.robig.stlab.gui.PersistentJFrame;
import net.robig.stlab.model.WebPreset;
import net.robig.stlab.model.WebPresetList;
import net.robig.stlab.util.config.IntValue;
import javax.swing.JTextArea;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class WebControlFrame extends PersistentJFrame {

	private static WebControlFrame instance=null;
	{
		instance=this;
	}
	public static WebControlFrame getInstance(){
		if(instance==null) new WebControlFrame();
		return instance;
	}
	
	private static final long serialVersionUID = 1L;
	private WebPresetList currentList=null;
	private WebAccess web=new WebAccess();  //  @jve:decl-index=0:
	private net.robig.stlab.util.config.StringValue savedUsername=StLabConfig.getWebUsername();
	private WebPreset selectedPreset=null;
	
	private JTabbedPane jTabbedPane = null;
	private JPanel searchPanel = null;
	private JScrollPane jScrollPane = null;
	private JTable presetTable = null;
	private JPanel searchControlsPanel = null;
	private JPanel loginTabBasePanel = null;
	private JLabel loginUsernameLabel = null;
	private JLabel loginPasswordLabel = null;
	private JTextField loginUsernameTextField = null;
	private JPasswordField loginPasswordField = null;
	private JButton loginButton = null;
	private JButton loginRegisterNewButton = null;
	private JLabel loginInfoLabel = null;
	private JPanel aPanel = null;
	private JPanel topPresetsPanel = null;
	private JPanel sharePanel = null;
	private ActionListener loginActionListener;  //  @jve:decl-index=0:
	private JLabel shareTopLabel = null;
	private JPanel sharePanel2 = null;
	private JLabel shareTitleLabel = null;
	private JTextField shareTitleTextField = null;
	private JLabel jLabel = null;
	private JTextArea shareDescriptionTextArea = null;
	private JLabel shareTagsLabel = null;
	private JTextArea shareTagsTextArea = null;
	private JComponent shareSetupPanel = null;
	private JLabel shareSetupLabel = null;
	private JButton sharePublishButton = null;
	private JPanel searchPresetDetailsPanel = null;
	private JLabel searchPresetDetailsLabel = null;
	private JPanel jPanel = null;
	private JLabel searchTextLabel = null;
	private JTextField searchTextField = null;
	private JButton startSearchButton = null;
	private JPanel extendedSearchPanel = null;
	private JLabel extendedSearchEnabledLabel = null;
	private JPanel extendedSearchInnerPanel = null;
	private JLabel searchByUserLabel = null;
	private JTextField searchByUsernameTextField = null;
	private JLabel extendedSearchSwitch = null;
	private JLabel searchPresetDetailsAuthorLabel = null;
	private JCheckBox searchPresetDetailsActivateCheckbox = null;
	private JButton searchPresetDetailsLoadButton = null;
	private JPanel shareBasePanel = null;
	private JPanel loginTabPanel = null;
	
	/**
	 * This method initializes 
	 * 
	 */
	public WebControlFrame() {
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	protected void initialize() {
        this.setSize(new Dimension(586, 620));
        this.setTitle("StLab Web");
        this.setContentPane(getJTabbedPane());
		super.initialize();
	}

	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab("Login", null, getLoginTabPanel(), "Login or registret");
			jTabbedPane.addTab("Search", null, getSearchPanel(), "Search for presets");
			jTabbedPane.addTab("Top 10", null, getTopPresetsPanel(), "not implemented yet"); //TODO
			jTabbedPane.addTab("Share", null, getSharePanel(), "Share current preset");
			jTabbedPane.setEnabledAt(3, false);
			jTabbedPane.setEnabledAt(2, false); //TODO enable top 10
			
			//remember last active tab:
			final IntValue activeTab=getIntValue("tabindex", 0);
			if((web==null || !isLoggedin()) && activeTab.getSimpleValue()==3) activeTab.setValue(0); //dont activate share tab
			jTabbedPane.setSelectedIndex(activeTab.getValue());
			log.debug("activating tab:"+activeTab.getValue());
			jTabbedPane.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					activeTab.setValue(jTabbedPane.getSelectedIndex());
				}
			});
			
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes searchPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSearchPanel() {
		if (searchPanel == null) {
			searchPanel = new JPanel();
			searchPanel.setLayout(new BorderLayout());
			searchPanel.setEnabled(false);
			searchPanel.add(getJScrollPane(), BorderLayout.CENTER);
			searchPanel.add(getSearchControlsPanel(), BorderLayout.NORTH);
			searchPanel.add(getSearchPresetDetailsPanel(), BorderLayout.SOUTH);
		}
		return searchPanel;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getPresetTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes presetTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getPresetTable() {
		if (presetTable == null) {
			presetTable = new JTable();
			presetTable.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if(e.getButton() == MouseEvent.BUTTON1)
						onPresetSelection();
				}
			});
		}
		return presetTable;
	}

	/**
	 * This method initializes searchControlsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSearchControlsPanel() {
		if (searchControlsPanel == null) {
			searchControlsPanel = new JPanel();
			searchControlsPanel.setLayout(new BorderLayout());
			searchControlsPanel.add(getJPanel(), BorderLayout.NORTH);
			searchControlsPanel.add(getExtendedSearchPanel(), BorderLayout.SOUTH);
		}
		return searchControlsPanel;
	}

	protected void onSearch() {
		currentList=null;
		selectedPreset=null;
		List<WebPreset> result=web.find(new TextSearchCondition(getSearchTextField().getText().trim()));
		if(result!=null){
			currentList=new WebPresetList(result);
			presetTable.setModel(currentList);
		}else{
			JOptionPane.showMessageDialog(this, "Search failed! "+web.getMessage(),"Fail", JOptionPane.WARNING_MESSAGE);
			log.error("Search failed "+web.getMessage());
		}
	}
	
	/**
	 * Set the username in the login username input field
	 * @param name
	 */
	public void setUserName(String name){
		loginUsernameTextField.setText(name);
	}

	/**
	 * This method initializes loginTabPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLoginTabPanel() {
		if (loginTabPanel == null) {
			loginTabPanel=new JPanel();
			loginTabPanel.setLayout(new BorderLayout());
			JLabel iconLabel=new JLabel();
			iconLabel.setIcon(ImagePanel.loadImageIcon("img/stlab-web_top.png"));
			loginTabPanel.add(iconLabel,BorderLayout.NORTH);
			loginTabPanel.add(getLoginTabBasePanel(),BorderLayout.CENTER);
		}
		return loginTabPanel;
	}
	
	/**
	 * This method initializes loginTabBasePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLoginTabBasePanel() {
		if (loginTabBasePanel == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.gridy = 2;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.gridy = 0;
			loginInfoLabel = new JLabel();
			loginInfoLabel.setText("Please login:");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 3;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 3;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 2;
			loginPasswordLabel = new JLabel();
			loginPasswordLabel.setText("Password:");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			loginUsernameLabel = new JLabel();
			loginUsernameLabel.setText("Username:");
			loginTabBasePanel = new JPanel();
			loginTabBasePanel.setLayout(new GridBagLayout());
			loginTabBasePanel.add(loginInfoLabel, gridBagConstraints7);
			loginTabBasePanel.add(loginUsernameLabel, gridBagConstraints1);
			loginTabBasePanel.add(loginPasswordLabel, gridBagConstraints2);
			loginTabBasePanel.add(getLoginUsernameTextField(), gridBagConstraints3);
			loginTabBasePanel.add(getLoginButton(), gridBagConstraints5);
			loginTabBasePanel.add(getLoginRegisterNewButton(), gridBagConstraints6);
			loginTabBasePanel.add(getAPanel(), gridBagConstraints8);
		}
		return loginTabBasePanel;
	}

	/**
	 * This method initializes loginUsernameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getLoginUsernameTextField() {
		if (loginUsernameTextField == null) {
			loginUsernameTextField = new JTextField();
			loginUsernameTextField.setPreferredSize(new Dimension(300, 20));
			if(savedUsername!=null)
				loginUsernameTextField.setText(savedUsername.getValue());
			loginUsernameTextField.addActionListener(getLoginActionListener());
		}
		return loginUsernameTextField;
	}

	/**
	 * This method initializes loginPasswordField	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	private JPasswordField getLoginPasswordField() {
		if (loginPasswordField == null) {
			loginPasswordField = new JPasswordField();
			loginPasswordField.setText("08150815");
			loginPasswordField.setPreferredSize(new Dimension(300,20));
			loginPasswordField.addActionListener(getLoginActionListener());
		}
		return loginPasswordField;
	}
	
	private ActionListener getLoginActionListener(){
		if(loginActionListener==null){
			loginActionListener=new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onLogin();
				}
			};
		}
		return loginActionListener;
	}

	/**
	 * This method initializes loginButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getLoginButton() {
		if (loginButton == null) {
			loginButton = new JButton();
			loginButton.setText("Login");
			loginButton.addActionListener(getLoginActionListener());
		}
		return loginButton;
	}

	protected void onLogin() {
		String user=getLoginUsernameTextField().getText().trim();
		String pass=new String(getLoginPasswordField().getPassword());
		boolean success=web.login(user, pass);
		if(success){
			getLoginButton().setEnabled(false);
			getLoginUsernameTextField().setEnabled(false);
			getLoginPasswordField().setEnabled(false);
			getLoginRegisterNewButton().setEnabled(false);
			savedUsername.setValue(user);
			loginInfoLabel.setText("Successfully logged in.");
			jTabbedPane.setEnabledAt(3, true);
		}else{
			loginInfoLabel.setText("Login failed! "+web.getMessage());
		}
	}

	/**
	 * This method initializes loginRegisterNewButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getLoginRegisterNewButton() {
		if (loginRegisterNewButton == null) {
			loginRegisterNewButton = new JButton();
			loginRegisterNewButton.setText("Register");
			loginRegisterNewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					WebRegistrationFrame.getInstance().setVisible(true);
				}
			});
		}
		return loginRegisterNewButton;
	}

	/**
	 * This method initializes aPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAPanel() {
		if (aPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints4.gridy = -1;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridx = -1;
			aPanel = new JPanel();
			aPanel.setLayout(new BorderLayout());
			aPanel.add(getLoginPasswordField(), BorderLayout.NORTH);
		}
		return aPanel;
	}

	/**
	 * This method initializes topPresetsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTopPresetsPanel() {
		if (topPresetsPanel == null) {
			topPresetsPanel = new JPanel();
			topPresetsPanel.setLayout(new GridBagLayout());
		}
		return topPresetsPanel;
	}

	/**
	 * This method initializes sharePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSharePanel() {
		if (sharePanel == null) {
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.ipady = 13;
			jLabel = new JLabel();
			jLabel.setText("Description:");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 1;
			shareTopLabel = new JLabel();
			shareTopLabel.setText("Share your current preset/settings with the world:");
			sharePanel = new JPanel();
			sharePanel.setLayout(new GridBagLayout());
			sharePanel.setEnabled(false);
			sharePanel.add(shareTopLabel, gridBagConstraints18);
			sharePanel.add(getSharePanel2(), gridBagConstraints9);
		}
		return sharePanel;
	}
	
	/**
	 * This method initializes sharePanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSharePanel2() {
		if (sharePanel2 == null) {
			GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
			gridBagConstraints28.gridx = 1;
			gridBagConstraints28.gridy = 5;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.ipadx = 1;
			gridBagConstraints19.ipady = 9;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 1;
			gridBagConstraints17.anchor = GridBagConstraints.EAST;
			gridBagConstraints17.gridy = 4;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.anchor = GridBagConstraints.NORTH;
			gridBagConstraints16.gridy = 3;
			shareSetupLabel = new JLabel();
			shareSetupLabel.setText("My Setup:");
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 1;
			gridBagConstraints15.gridy = 3;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.fill = GridBagConstraints.BOTH;
			gridBagConstraints14.gridy = 2;
			gridBagConstraints14.weightx = 1.0;
			gridBagConstraints14.weighty = 1.0;
			gridBagConstraints14.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints14.gridx = 1;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = GridBagConstraints.NORTH;
			gridBagConstraints13.gridy = 2;
			shareTagsLabel = new JLabel();
			shareTagsLabel.setText("Searchable tags:");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = GridBagConstraints.BOTH;
			gridBagConstraints12.gridy = 1;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.weighty = 1.0;
			gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints12.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.anchor = GridBagConstraints.NORTH;
			gridBagConstraints11.gridy = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = GridBagConstraints.BOTH;
			gridBagConstraints10.weightx = 1.0;
			shareTitleLabel = new JLabel();
			shareTitleLabel.setText("Preset Title/Name:");
			sharePanel2 = new JPanel();
			sharePanel2.setLayout(new GridBagLayout());
			sharePanel2.add(shareTitleLabel, gridBagConstraints19);
			sharePanel2.add(getShareTitleTextField(), gridBagConstraints10);
			sharePanel2.add(jLabel, gridBagConstraints11);
			sharePanel2.add(getShareDescriptionTextArea(), gridBagConstraints12);
			sharePanel2.add(shareTagsLabel, gridBagConstraints13);
			sharePanel2.add(getShareTagsTextArea(), gridBagConstraints14);
			sharePanel2.add(getShareSetupPanel(), gridBagConstraints15);
			sharePanel2.add(shareSetupLabel, gridBagConstraints16);
			sharePanel2.add(getSharePublishButton(), gridBagConstraints17);
		}
		return sharePanel2;
	}

	/**
	 * This method initializes shareTitleTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getShareTitleTextField() {
		if (shareTitleTextField == null) {
			shareTitleTextField = new JTextField();
			shareTitleTextField.setPreferredSize(new Dimension(390, 20));
		}
		return shareTitleTextField;
	}

	/**
	 * This method initializes shareDescriptionTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getShareDescriptionTextArea() {
		if (shareDescriptionTextArea == null) {
			shareDescriptionTextArea = new JTextArea();
			shareDescriptionTextArea.setTabSize(4);
			shareDescriptionTextArea.setRows(5);
		}
		return shareDescriptionTextArea;
	}

	/**
	 * This method initializes shareTagsTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getShareTagsTextArea() {
		if (shareTagsTextArea == null) {
			shareTagsTextArea = new JTextArea();
			shareTagsTextArea.setRows(4);
		}
		return shareTagsTextArea;
	}

	/**
	 * This method initializes shareAuthorPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JComponent getShareSetupPanel() {
		if (shareSetupPanel == null) {
			shareSetupPanel=new JPanel();
//TODO			shareSetupPanel = StLab.getSetupPreferences().getComponent();
		}
		return shareSetupPanel;
	}

	/**
	 * This method initializes sharePublishButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSharePublishButton() {
		if (sharePublishButton == null) {
			sharePublishButton = new JButton();
			sharePublishButton.setText("Publish Preset");
			sharePublishButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					onPublish();
				}
			});
		}
		return sharePublishButton;
	}
	
	protected void onPublish(){
		WebPreset preset = new WebPreset();
		preset.setTitle(getShareTitleTextField().getText().trim());
		preset.setDescription(getShareDescriptionTextArea().getText().trim());
		preset.setTags(getShareTagsTextArea().getText().trim());
		preset.setData(DeviceFrame.getInctance().requestPreset());
		boolean success=web.publish(preset);
		if(success){
			JOptionPane.showMessageDialog(this, "Published successfully");
		}else{
			JOptionPane.showMessageDialog(this, "sharing preset failed! "+web.getMessage());
		}
	}

	/**
	 * This method initializes searchPresetDetailsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSearchPresetDetailsPanel() {
		if (searchPresetDetailsPanel == null) {
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.gridx = 0;
			gridBagConstraints27.anchor = GridBagConstraints.WEST;
			gridBagConstraints27.gridy = 2;
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.gridx = 1;
			gridBagConstraints26.anchor = GridBagConstraints.EAST;
			gridBagConstraints26.gridy = 2;
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.gridx = 1;
			gridBagConstraints24.anchor = GridBagConstraints.NORTH;
			gridBagConstraints24.gridy = 0;
			searchPresetDetailsAuthorLabel = new JLabel();
			searchPresetDetailsAuthorLabel.setText("Author");
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints20.gridwidth = 1;
			gridBagConstraints20.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints20.insets = new Insets(2, 2, 2, 2);
			searchPresetDetailsLabel = new JLabel();
			searchPresetDetailsLabel.setText("Details");
			searchPresetDetailsPanel = new JPanel();
			searchPresetDetailsPanel.setLayout(new GridBagLayout());
			searchPresetDetailsPanel.setVisible(true);
			searchPresetDetailsPanel.add(searchPresetDetailsLabel, gridBagConstraints20);
			searchPresetDetailsPanel.add(searchPresetDetailsAuthorLabel, gridBagConstraints24);
			searchPresetDetailsPanel.add(getSearchPresetDetailsActivateCheckbox(), gridBagConstraints26);
			searchPresetDetailsPanel.add(getSearchPresetDetailsLoadButton(), gridBagConstraints27);
		}
		return searchPresetDetailsPanel;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.anchor = GridBagConstraints.CENTER;
			gridBagConstraints.weightx = 1.0;
			searchTextLabel = new JLabel();
			searchTextLabel.setText("Search for keyword:");
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(searchTextLabel, new GridBagConstraints());
			jPanel.add(getSearchTextField(), gridBagConstraints);
			jPanel.add(getStartSearchButton(), new GridBagConstraints());
		}
		return jPanel;
	}

	/**
	 * This method initializes searchTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSearchTextField() {
		if (searchTextField == null) {
			searchTextField = new JTextField();
			searchTextField.setPreferredSize(new Dimension(250, 20));
			searchTextField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onSearch();
				}
			});
		}
		return searchTextField;
	}

	/**
	 * This method initializes startSearchButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getStartSearchButton() {
		if (startSearchButton == null) {
			startSearchButton = new JButton();
			startSearchButton.setToolTipText("Find a preset by keyword");
			startSearchButton.setText("Find");
			startSearchButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					onSearch();
				}
			});
		}
		return startSearchButton;
	}

	/**
	 * This method initializes extendedSearchPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getExtendedSearchPanel() {
		if (extendedSearchPanel == null) {
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.gridx = 0;
			gridBagConstraints25.insets = new Insets(0, 2, 0, 2);
			gridBagConstraints25.gridy = 0;
			extendedSearchSwitch = new JLabel();
			extendedSearchSwitch.setText("+");
			extendedSearchSwitch.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					getExtendedSearchInnerPanel().setVisible(!getExtendedSearchInnerPanel().isVisible());
					log.debug("ExtendedSearch enabled: "+getExtendedSearchInnerPanel().isVisible());
				}
			});
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 0;
			gridBagConstraints22.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints22.gridy = 2;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.anchor = GridBagConstraints.WEST;
			gridBagConstraints21.gridx = 2;
			gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
			extendedSearchEnabledLabel = new JLabel();
			extendedSearchEnabledLabel.setText("Extended Search");
			extendedSearchEnabledLabel.setPreferredSize(new Dimension(350, 16));
			extendedSearchPanel = new JPanel();
			extendedSearchPanel.setLayout(new GridBagLayout());
			extendedSearchPanel.setPreferredSize(new Dimension(350, 16));
			extendedSearchPanel.add(extendedSearchEnabledLabel, gridBagConstraints21);
			extendedSearchPanel.add(getExtendedSearchInnerPanel(), gridBagConstraints22);
			extendedSearchPanel.add(extendedSearchSwitch, gridBagConstraints25);
		}
		return extendedSearchPanel;
	}

	/**
	 * This method initializes extendedSearchInnerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getExtendedSearchInnerPanel() {
		if (extendedSearchInnerPanel == null) {
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints23.gridy = 0;
			gridBagConstraints23.weightx = 1.0;
			gridBagConstraints23.gridx = 1;
			searchByUserLabel = new JLabel();
			searchByUserLabel.setText("Search by Username:");
			extendedSearchInnerPanel = new JPanel();
			extendedSearchInnerPanel.setLayout(new GridBagLayout());
			extendedSearchInnerPanel.setVisible(false);
			extendedSearchInnerPanel.add(searchByUserLabel, new GridBagConstraints());
			extendedSearchInnerPanel.add(getSearchByUsernameTextField(), gridBagConstraints23);
		}
		return extendedSearchInnerPanel;
	}

	/**
	 * This method initializes searchByUsernameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSearchByUsernameTextField() {
		if (searchByUsernameTextField == null) {
			searchByUsernameTextField = new JTextField();
			searchByUsernameTextField.setPreferredSize(new Dimension(240, 28));
		}
		return searchByUsernameTextField;
	}
	
	/**
	 * when selecting a preset from a search result
	 */
	protected void onPresetSelection(){
		if(currentList==null) return;
		int selected=getPresetTable().getSelectedRow();
		log.debug("selected "+selected);
		if(currentList.size()<=selected) return;
		selectedPreset=currentList.get(selected);
		searchPresetDetailsLabel.setText(selectedPreset.toHtml());
		searchPresetDetailsAuthorLabel.setText(selectedPreset.getOwner().toHtml("Author"));
		searchPresetDetailsAuthorLabel.setIcon(new ImageIcon("http://stlab.robig.net/style/images/player.jpg"));
//		searchPresetDetailsPanel.setEnabled(true);
//		searchPresetDetailsPanel.getParent().validate();
//		validate();
//		repaint();
		if(searchPresetDetailsActivateCheckbox.isSelected())
			onLoad();
	}
	
	/**
	 * shows the tab for a login
	 */
	public void showLogin() {
		jTabbedPane.setSelectedIndex(0);
		setVisible(true);
	}
	
	public void showFind() {
		jTabbedPane.setSelectedIndex(1);
		setVisible(true);
	}
	
	public void showTop10() {
		jTabbedPane.setSelectedIndex(2);
		setVisible(true);
	}
	
	public void showPublish() {
		jTabbedPane.setSelectedIndex(3);
		setVisible(true);
	}
	
	/**
	 * returns if the user has logged in to stlab web
	 * @return
	 */
	public boolean isLoggedin(){
		return web.isLoggedIn();
	}

	/**
	 * This method initializes searchPresetDetailsActivateCheckbox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getSearchPresetDetailsActivateCheckbox() {
		if (searchPresetDetailsActivateCheckbox == null) {
			searchPresetDetailsActivateCheckbox = new JCheckBox();
			searchPresetDetailsActivateCheckbox.setText("automatically load preset");
			searchPresetDetailsActivateCheckbox.setSelected(getBoolValue("autoload", false).getValue());
			searchPresetDetailsActivateCheckbox.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent arg0) {
					getBoolValue("autoload", false).setValue(searchPresetDetailsActivateCheckbox.isSelected());
				}
			});
		}
		return searchPresetDetailsActivateCheckbox;
	}

	/**
	 * This method initializes searchPresetDetailsLoadButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSearchPresetDetailsLoadButton() {
		if (searchPresetDetailsLoadButton == null) {
			searchPresetDetailsLoadButton = new JButton();
			searchPresetDetailsLoadButton.setText("Load");
			searchPresetDetailsLoadButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					onLoad();
				}
			});
		}
		return searchPresetDetailsLoadButton;
	}
	
	/**
	 * Load a Preset from the web onto the device
	 */
	protected void onLoad() {
		if(selectedPreset==null) return;
		log.info("Loading WebPreset: #"+selectedPreset.getId()+" "+selectedPreset.getTitle());
		DeviceFrame.getInctance().loadWebPreset(selectedPreset);
	}

	/**
	 * This method initializes shareBasePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getShareBasePanel() {
		if (shareBasePanel == null) {
			shareBasePanel = new JPanel();
			shareBasePanel.setLayout(new BorderLayout());
			shareBasePanel.add(getSharePanel(),BorderLayout.CENTER);
		}
		return shareBasePanel;
	}

	public static void main(String[] args) {
		JFrame frame=new WebControlFrame();
		frame.show();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
