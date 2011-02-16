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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import net.robig.gui.CursorController;
import net.robig.gui.ImagePanel;
import net.robig.gui.LinkLabel;
import net.robig.net.LinkValidator;
import net.robig.net.WebAccess;
import net.robig.stlab.StLabConfig;
import net.robig.stlab.gui.DeviceFrame;
import net.robig.stlab.gui.PersistentJFrame;
import net.robig.stlab.model.WebPreset;
import net.robig.stlab.model.WebPresetList;
import net.robig.stlab.model.WebVote;
import net.robig.stlab.util.TableUtil;
import net.robig.stlab.util.ToolTipTableCellRenderer;
import net.robig.stlab.util.config.BoolValue;
import net.robig.stlab.util.config.IntValue;
import net.robig.stlab.util.config.ObjectConfig;
import javax.swing.JTextArea;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

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
	private WebPresetList currentSearchResultList=null;
	private WebAccess web=new WebAccess();  //  @jve:decl-index=0:
	private net.robig.stlab.util.config.StringValue savedUsername=StLabConfig.getWebUsername();
	private WebPreset selectedSearchPreset=null;
	private JTabbedPane jTabbedPane = null;
	private JPanel searchPanel = null;
	private JScrollPane searchScrollPane = null;
	private JTable searchPresetTable = null;
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
	private JLabel shareDescriptionLabel = null;
	private JTextArea shareDescriptionTextArea = null;
	private JLabel shareTagsLabel = null;
	private JTextArea shareTagsTextArea = null;
	private JComponent shareSetupPanel = null;
	private JLabel shareSetupLabel = null;
	private JButton sharePublishButton = null;
	private JPanel searchPresetDetailsPanel = null;
	private JLabel searchPresetDetailsLeftLabel = null;
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
	private JLabel searchPresetDetailsRightLabel = null;
	private JCheckBox searchPresetDetailsActivateCheckbox = null;
	private JCheckBox mySharesDetailsActivateCheckbox = null;
	private JButton searchPresetDetailsLoadButton = null;
	private JPanel loginTabPanel = null;
	private int searchOrderByIndex=2; //default ordering
	private boolean searchOrderDesc=true;
	private int mySharesOrderByIndex=2; //default ordering
	private boolean mySharesOrderDesc=false;
	private JPanel mySharesBasePanel;
	private JScrollPane mySharesScrollPane;
	private JLabel mySharesDetailsLabel;
	private JPanel mySharesPresetDetailsPanel;
	private JLabel mySharesDetailsAuthorLabel;
	private JTable mySharesPresetTable;
	private WebPresetList currentMySharesList;
	private WebPreset selectedMySharesPreset;
	private JTextField shareLinkTextField;
	private JLabel shareLinkLabel;
	private JLabel searchPresetDetailsDescriptionLabel;
	private JLabel searchPresetDetailsLinkLabel;
	private static BoolValue searchPresetsAutoload=ObjectConfig.getBoolValue("web.search.autoload", false);
	private static BoolValue mySharesAutoload=ObjectConfig.getBoolValue("web.myshares.autoload", false);
	private JButton mySharesDetailsLoadButton;
	private JLabel mySharesPresetDetailsDescriptionLabel;
	private LinkLabel mySharesPresetDetailsLinkLabel;
	
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
//        this.setSize(new Dimension(471, 691));
        this.setMinimumSize(new Dimension(470,300));
        defaultHeight= DeviceFrame.getInctance().getHeight();
        this.setTitle("StLab Web");
        super.initialize();
        this.setContentPane(getJTabbedPane());
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
			jTabbedPane.addTab("My shares", null, getMySharesBasePanel(), "Show my shared presets");
			jTabbedPane.setEnabledAt(3, false); // disable share tab
			jTabbedPane.setEnabledAt(2, false); //TODO enable top 10
			jTabbedPane.setEnabledAt(4, false); // disable my uploads
			
			jTabbedPane.setOpaque(false);
			
			//remember last active tab:
			final IntValue activeTab=getIntValue("tabindex", 0);
			if((web==null || !isLoggedin()) && 
					(activeTab.getSimpleValue()==3 || activeTab.getSimpleValue()==4)) activeTab.setValue(0); //dont activate share tab
			jTabbedPane.setSelectedIndex(activeTab.getValue());
			log.debug("activating tab:"+activeTab.getValue());
			ChangeListener l = new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					activeTab.setValue(jTabbedPane.getSelectedIndex());
					if(jTabbedPane.getSelectedIndex()==4){ // my shares activated
						onGetMyShares();
					}
				}
			};
			jTabbedPane.addChangeListener(CursorController.createListener(this, l));
			
		}
		return jTabbedPane;
	}

	private JPanel getMySharesBasePanel(){
		if(mySharesBasePanel==null){
			mySharesBasePanel = new JPanel();
			mySharesBasePanel.setLayout(new BorderLayout());
//			mySharesBasePanel.add(getSearchControlsPanel(), BorderLayout.NORTH);
			mySharesBasePanel.add(getMySharesScrollPane(), BorderLayout.CENTER);
			mySharesBasePanel.add(getMySharesPresetDetailsPanel(), BorderLayout.SOUTH);
		}
		return mySharesBasePanel;
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
			searchPanel.add(getSearchControlsPanel(), BorderLayout.NORTH);
			searchPanel.add(getSearchScrollPane(), BorderLayout.CENTER);
			searchPanel.add(getSearchPresetDetailsPanel(), BorderLayout.SOUTH);
//			searchPanel.setLayout(new GridBagLayout());
//			GridBagConstraints c0 = new GridBagConstraints();
//			c0.fill=GridBagConstraints.HORIZONTAL;
//			GridBagConstraints c1 = new GridBagConstraints();
//			c1.fill=GridBagConstraints.BOTH;
//			c1.weighty=100;
//			c1.gridy=1;
//			GridBagConstraints c2 = new GridBagConstraints();
//			c2.fill = GridBagConstraints.HORIZONTAL;
//			c2.gridy=2;
////			c2.weighty=50;
//			searchPanel.add(getSearchControlsPanel(), c0);
//			searchPanel.add(getSearchScrollPane(), c1);
//			searchPanel.add(getSearchPresetDetailsPanel(), c2);
		}
		return searchPanel;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getSearchScrollPane() {
		if (searchScrollPane == null) {
			searchScrollPane = new JScrollPane();
			searchScrollPane.setViewportView(getSearchPresetTable());
		}
		return searchScrollPane;
	}
	
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getMySharesScrollPane() {
		if (mySharesScrollPane == null) {
			mySharesScrollPane = new JScrollPane();
			mySharesScrollPane.setViewportView(getMySharesPresetTable());
		}
		return mySharesScrollPane;
	}

	/**
	 * This method initializes presetTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getSearchPresetTable() {
		if (searchPresetTable == null) {
			searchPresetTable = new JTable(){
				private static final long serialVersionUID = 1L;
				@Override
				public void setModel(TableModel dataModel) {
					super.setModel(dataModel);
					TableColumnModel colModel = getColumnModel();
					if(colModel==null)return;
					for(int j = 0; j < colModel.getColumnCount(); j++)
			            colModel.getColumn(j).setCellRenderer(new ToolTipTableCellRenderer() {
							private static final long serialVersionUID = 1L;
							@Override
							public	String getCellInfo(int r, int c) {
								return currentSearchResultList.getCellInfo(r, c);
							}
						});
				}
			};
			JTableHeader header = searchPresetTable.getTableHeader();
		    header.addMouseListener(new MouseAdapter() {
		    	@Override
		    	public void mouseClicked(MouseEvent e) {
		    		TableColumnModel colModel = searchPresetTable.getColumnModel();
		    	    int index = colModel.getColumnIndexAtX(e.getX());
		    		onSortSearch(index);
		    	}
			});
//		    //header.setReorderingAllowed(true);
//			presetTable.addMouseListener(new MouseAdapter() {
//				public void mouseClicked(MouseEvent e) {
//					if(e.getButton() == MouseEvent.BUTTON1)
//						onPresetSelection();
//				}
//			});
			searchPresetTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					log.debug("ListSelection changed: "+e);
					onSearchPresetSelection();
				}
			});
			searchPresetTable.addKeyListener(new KeyAdapter() {
		    	@Override
		    	public void keyReleased(KeyEvent e) {
		    		if(e.getKeyCode()==KeyEvent.VK_ENTER){
		    			onSearchPresetSelection();
		    		}
		    	}
			});
		}
		return searchPresetTable;
	}
	
	/**
	 * This method initializes presetTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getMySharesPresetTable() {
		if (mySharesPresetTable == null) {
			mySharesPresetTable = new JTable(){
				private static final long serialVersionUID = 1L;
				@Override
				public void setModel(TableModel dataModel) {
					super.setModel(dataModel);
					TableColumnModel colModel = getColumnModel();
					if(colModel==null)return;
					for(int j = 0; j < colModel.getColumnCount(); j++)
			            colModel.getColumn(j).setCellRenderer(new ToolTipTableCellRenderer() {
							private static final long serialVersionUID = 1L;
							@Override
							public	String getCellInfo(int r, int c) {
								return currentMySharesList.getCellInfo(r, c);
							}
						});
				}
			};
			JTableHeader header = mySharesPresetTable.getTableHeader();
		    header.addMouseListener(new MouseAdapter() {
		    	@Override
		    	public void mouseClicked(MouseEvent e) {
		    		TableColumnModel colModel = mySharesPresetTable.getColumnModel();
		    	    int index = colModel.getColumnIndexAtX(e.getX());
		    		onSortMyShares(index);
		    	}
			});
		    mySharesPresetTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					onMySharesSelection();
				}
			});
		    mySharesPresetTable.addKeyListener(new KeyAdapter() {
		    	@Override
		    	public void keyReleased(KeyEvent e) {
		    		if(e.getKeyCode()==KeyEvent.VK_ENTER){
		    			onMySharesSelection();
		    		}
		    	}
			});
		}
		return mySharesPresetTable;
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
//			searchControlsPanel.add(getExtendedSearchPanel(), BorderLayout.SOUTH);
		}
		return searchControlsPanel;
	}
	
	/**
	 * set ordering by
	 * @param index
	 */
	protected void onSortSearch(int index){
		log.debug("sort requesed: "+index);
		if(searchOrderByIndex==index) searchOrderDesc=!searchOrderDesc;
		searchOrderByIndex=index;
		onSearch();
	}


	/**
	 * search triggered. send request to server
	 */
	protected void onSearch() {
		currentSearchResultList=null;
		selectedSearchPreset=null;
		String orderBy=WebPresetList.getHeaderName(searchOrderByIndex);
		List<WebPreset> result=web.find(new TextSearchCondition(getSearchTextField().getText().trim(),orderBy,searchOrderDesc));
		if(result!=null){
			currentSearchResultList=new WebPresetList(result);
			currentSearchResultList.setOrderIndex(searchOrderByIndex);
			currentSearchResultList.setOrderDesc(searchOrderDesc);
			searchPresetTable.setModel(currentSearchResultList);
			TableUtil.packTable(searchPresetTable);
		}else{
			JOptionPane.showMessageDialog(this, "Search failed! "+web.getMessage(),"Fail", JOptionPane.WARNING_MESSAGE);
			log.error("Search failed "+web.getMessage());
		}
	}
	
	/**
	 * set ordering by
	 * @param index
	 */
	protected void onSortMyShares(int index){
		log.debug("sort requesed: "+index);
		if(mySharesOrderByIndex==index) mySharesOrderDesc=!mySharesOrderDesc;
		mySharesOrderByIndex=index;
		onGetMyShares();
	}

	/**
	 * search triggered. send request to server
	 */
	protected void onGetMyShares() {
		currentMySharesList=null;
		selectedMySharesPreset=null;
		String orderBy=WebPresetList.getHeaderName(mySharesOrderByIndex);
		List<WebPreset> result=web.getMyShares(new TextSearchCondition(getSearchTextField().getText().trim(),orderBy,mySharesOrderDesc));
		if(result!=null){
			currentMySharesList=new WebPresetList(result);
			currentMySharesList.setOrderIndex(mySharesOrderByIndex);
			currentMySharesList.setOrderDesc(mySharesOrderDesc);
			getMySharesPresetTable().setModel(currentMySharesList);
			TableUtil.packTable(getMySharesPresetTable());
		}else{
			JOptionPane.showMessageDialog(this, "Failed to get shares! "+web.getMessage(),"Fail", JOptionPane.WARNING_MESSAGE);
			log.error("Failed to get Shares! "+web.getMessage());
		}
	}
	
	/**
	 * send a vote to the server
	 * @param p
	 * @param message
	 * @param v
	 * @return
	 */
	public boolean vote(WebPreset p,String message, int v){
		if(!isLoggedin()){
			JOptionPane.showMessageDialog(this, "Need to login first!","Fail", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		log.debug("Vote: #"+p.getId()+" ("+p.getTitle()+") vote:"+v);
		return web.vote(p, v, message);
	}
	
	public List<WebVote> loadVotes(WebPreset p, int page){
		return web.loadVotes(p, page);
	}
	
	public WebPreset load(int id){
		return web.load(id);
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
//			loginPasswordField.setText("08150815"); //FIXME
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
			loginButton.addActionListener(CursorController.createListener(this, getLoginActionListener()));
//			loginButton.addActionListener(getLoginActionListener());
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
			jTabbedPane.setEnabledAt(4, true);
			DeviceFrame.getInctance().showMySharesButton();
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
			shareTopLabel = new JLabel();
			shareTopLabel.setText("Share your current preset/settings with the world:");
			sharePanel = new JPanel();
			sharePanel.setLayout(new BorderLayout());
			sharePanel.setEnabled(false);
			sharePanel.add(shareTopLabel, BorderLayout.NORTH);
			sharePanel.add(getSharePanel2(), BorderLayout.CENTER);
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
			GridBagConstraints gridBagConstraints19titleLabel = new GridBagConstraints();
			gridBagConstraints19titleLabel.ipadx = 1;
			gridBagConstraints19titleLabel.ipady = 9;
			GridBagConstraints gridBagConstraints17publish = new GridBagConstraints();
			gridBagConstraints17publish.gridx = 1;
			gridBagConstraints17publish.anchor = GridBagConstraints.EAST;
			gridBagConstraints17publish.gridy = 4;
			GridBagConstraints gridBagConstraints16shareLabel = new GridBagConstraints();
			gridBagConstraints16shareLabel.gridx = 0;
			gridBagConstraints16shareLabel.anchor = GridBagConstraints.NORTH;
			gridBagConstraints16shareLabel.gridy = 3;
			shareSetupLabel = new JLabel();
			shareSetupLabel.setText("My Setup:");
			GridBagConstraints gridBagConstraints15setup = new GridBagConstraints();
			gridBagConstraints15setup.gridx = 1;
			gridBagConstraints15setup.gridy = 3;
			GridBagConstraints gridBagConstraintsLink = new GridBagConstraints();
			gridBagConstraintsLink.gridx = 1;
			gridBagConstraintsLink.gridy = 3;
			gridBagConstraintsLink.fill=GridBagConstraints.BOTH;
			GridBagConstraints gridBagConstraints14tags = new GridBagConstraints();
			gridBagConstraints14tags.fill = GridBagConstraints.BOTH;
			gridBagConstraints14tags.gridy = 2;
			gridBagConstraints14tags.weightx = 1.0;
			gridBagConstraints14tags.weighty = 1.0;
			gridBagConstraints14tags.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints14tags.gridx = 1;
			GridBagConstraints gridBagConstraints13tagsLabel = new GridBagConstraints();
			gridBagConstraints13tagsLabel.gridx = 0;
			gridBagConstraints13tagsLabel.anchor = GridBagConstraints.NORTH;
			gridBagConstraints13tagsLabel.gridy = 2;
			shareTagsLabel = new JLabel();
			shareTagsLabel.setText("<html>Searchable tags:<br/>" +
					"Give some word groups/tags<br/>" +
					"that match to your preset<br/>" +
					"for categorizing.<br/>"+
					"(one tag per line)</html>");
			GridBagConstraints gridBagConstraints12Description = new GridBagConstraints();
			gridBagConstraints12Description.fill = GridBagConstraints.BOTH;
			gridBagConstraints12Description.gridy = 1;
			gridBagConstraints12Description.weightx = 1.0;
			gridBagConstraints12Description.weighty = 1.0;
			gridBagConstraints12Description.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints12Description.gridx = 1;
			GridBagConstraints gridBagConstraints11descriptionLabel = new GridBagConstraints();
			gridBagConstraints11descriptionLabel.gridx = 0;
			gridBagConstraints11descriptionLabel.anchor = GridBagConstraints.NORTH;
			gridBagConstraints11descriptionLabel.gridy = 1;
			GridBagConstraints gridBagConstraints10title = new GridBagConstraints();
			gridBagConstraints10title.fill = GridBagConstraints.BOTH;
			gridBagConstraints10title.weightx = 1.0;
			shareDescriptionLabel = new JLabel();
			shareDescriptionLabel.setText("Description:");
			shareTitleLabel = new JLabel();
			shareTitleLabel.setText("Preset Title/Name:");
			shareLinkLabel=new JLabel("Sound Demo Link:");
			shareLinkLabel.setToolTipText("Add a Link to Youtube or to an MP3 of the sound.");
			sharePanel2 = new JPanel();
			sharePanel2.setLayout(new GridBagLayout());
			sharePanel2.add(shareTitleLabel, gridBagConstraints19titleLabel);
			sharePanel2.add(getShareTitleTextField(), gridBagConstraints10title);
			sharePanel2.add(shareDescriptionLabel, gridBagConstraints11descriptionLabel);
			sharePanel2.add(getShareDescriptionTextArea(), gridBagConstraints12Description);
			sharePanel2.add(shareTagsLabel, gridBagConstraints13tagsLabel);
			sharePanel2.add(getShareTagsTextArea(), gridBagConstraints14tags);
			//sharePanel2.add(getShareSetupPanel(), gridBagConstraints15setup);
			sharePanel2.add(getShareLinkTextField(), gridBagConstraintsLink);
			sharePanel2.add(shareLinkLabel, gridBagConstraints16shareLabel);
			sharePanel2.add(getSharePublishButton(), gridBagConstraints17publish);
		}
		return sharePanel2;
	}

	/**
	 * This method initializes shareTitleTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getShareTitleTextField() {
		if (shareTitleTextField == null) {
			shareTitleTextField = new JTextField();
			shareTitleTextField.setPreferredSize(new Dimension(390, 20));
		}
		return shareTitleTextField;
	}
	
	private JTextField getShareLinkTextField(){
		if(shareLinkTextField==null){
			shareLinkTextField = new JTextField();
			shareLinkTextField.setPreferredSize(new Dimension(390, 20));
		}
		return shareLinkTextField;
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
			shareDescriptionTextArea.setColumns(40);
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
			shareTagsTextArea.setColumns(20);
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
			ActionListener l = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					onPublish();
				}
			};
			sharePublishButton.addActionListener(CursorController.createListener(this, l));

		}
		return sharePublishButton;
	}
	
	/**
	 * on Publish button press
	 */
	protected void onPublish(){
		String link=getShareLinkTextField().getText();
		if(link.length()>0){
			try {
				new URL(link);
			} catch (MalformedURLException e) {
				log.debug("Invalid link: "+e.getMessage());
				JOptionPane.showMessageDialog(this, "Invalid Link: "+link,"Invalid link", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if(!LinkValidator.isUrl(link)){
				log.info("given link "+link+" could not be validated!");
				JOptionPane.showMessageDialog(this, "Invalid Link: "+link,"Invalid link", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		
		WebPreset preset = new WebPreset();
		preset.setTitle(getShareTitleTextField().getText().trim());
		preset.setDescription(getShareDescriptionTextArea().getText().trim());
		preset.setTags(getShareTagsTextArea().getText().trim());
		preset.setData(DeviceFrame.getInctance().requestPreset());
//		preset.setCreated(new Date());
		preset.setLink(link);
		boolean success=web.publish(preset);
		if(success){
			JOptionPane.showMessageDialog(this, "Published successfully","Success", JOptionPane.INFORMATION_MESSAGE);
			getShareTitleTextField().setText("");
			getShareDescriptionTextArea().setText("");
			getShareTagsTextArea().setText("");
			getShareLinkTextField().setText("");
		}else{
			JOptionPane.showMessageDialog(this, "sharing preset failed! "+web.getMessage(),"Fail", JOptionPane.WARNING_MESSAGE);
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
			gridBagConstraints27.gridy = 3;
			
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.gridx = 1;
			gridBagConstraints26.anchor = GridBagConstraints.EAST;
			gridBagConstraints26.gridy = 3;
			
			GridBagConstraints leftGridBagConstraints24 = new GridBagConstraints();
			leftGridBagConstraints24.gridx = 0;
			leftGridBagConstraints24.anchor = GridBagConstraints.NORTHWEST;
			leftGridBagConstraints24.fill = GridBagConstraints.HORIZONTAL;
			leftGridBagConstraints24.gridy = 2;
			leftGridBagConstraints24.weightx=1;
			leftGridBagConstraints24.insets = new Insets(2, 2, 2, 2);
			
			GridBagConstraints rightGridBagConstraints24 = new GridBagConstraints();
			rightGridBagConstraints24.gridx = 1;
			rightGridBagConstraints24.anchor = GridBagConstraints.NORTHEAST;
			rightGridBagConstraints24.fill = GridBagConstraints.HORIZONTAL;
			rightGridBagConstraints24.gridy = 2;
			rightGridBagConstraints24.weightx=2;

			GridBagConstraints topGridBagConstraints20 = new GridBagConstraints();
			topGridBagConstraints20.anchor = GridBagConstraints.NORTH;
			topGridBagConstraints20.gridwidth = 2;
			topGridBagConstraints20.fill = GridBagConstraints.HORIZONTAL;
			topGridBagConstraints20.insets = new Insets(2, 2, 2, 2);
			
			GridBagConstraints topGridBagConstraints25 = new GridBagConstraints();
			topGridBagConstraints25.anchor = GridBagConstraints.NORTH;
			topGridBagConstraints25.gridwidth = 2;
			topGridBagConstraints25.gridy = 1;
			topGridBagConstraints25.fill = GridBagConstraints.HORIZONTAL;
			
			searchPresetDetailsRightLabel = new JLabel();
			searchPresetDetailsRightLabel.setText("");
			searchPresetDetailsDescriptionLabel=new JLabel();
			searchPresetDetailsDescriptionLabel.setText("");
			searchPresetDetailsLinkLabel=new LinkLabel();
			searchPresetDetailsLinkLabel.setText("");
			
			searchPresetDetailsLeftLabel = new JLabel();
			searchPresetDetailsLeftLabel.setText("");
			searchPresetDetailsLeftLabel.setMinimumSize(new Dimension(280,0));
			searchPresetDetailsPanel = new JPanel();
			searchPresetDetailsPanel.setLayout(new GridBagLayout());
			searchPresetDetailsPanel.setVisible(true);
			searchPresetDetailsPanel.add(searchPresetDetailsDescriptionLabel, topGridBagConstraints20);
			searchPresetDetailsPanel.add(searchPresetDetailsLinkLabel, topGridBagConstraints25);
			searchPresetDetailsPanel.add(searchPresetDetailsRightLabel, rightGridBagConstraints24);
			searchPresetDetailsPanel.add(searchPresetDetailsLeftLabel, leftGridBagConstraints24);
			searchPresetDetailsPanel.add(getSearchPresetDetailsActivateCheckbox(), gridBagConstraints26);
			searchPresetDetailsPanel.add(getSearchPresetDetailsLoadButton(), gridBagConstraints27);
		}
		return searchPresetDetailsPanel;
	}
	
	/**
	 * This method initializes searchPresetDetailsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMySharesPresetDetailsPanel() {
		if (mySharesPresetDetailsPanel == null) {
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
			gridBagConstraints24.weightx=1;
			
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.anchor = GridBagConstraints.NORTH;
			gridBagConstraints20.gridwidth = 1;
			gridBagConstraints20.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints20.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints20.weightx=1;
			
			GridBagConstraints topGridBagConstraints20 = new GridBagConstraints();
			topGridBagConstraints20.anchor = GridBagConstraints.NORTH;
			topGridBagConstraints20.gridwidth = 2;
			topGridBagConstraints20.fill = GridBagConstraints.HORIZONTAL;
			topGridBagConstraints20.insets = new Insets(2, 2, 2, 2);
			
			GridBagConstraints topGridBagConstraints25 = new GridBagConstraints();
			topGridBagConstraints25.anchor = GridBagConstraints.NORTH;
			topGridBagConstraints25.gridwidth = 2;
			topGridBagConstraints25.gridy = 1;
			topGridBagConstraints25.fill = GridBagConstraints.HORIZONTAL;
			
			mySharesDetailsAuthorLabel = new JLabel();
			mySharesDetailsAuthorLabel.setText("");
			mySharesDetailsLabel = new JLabel();
			mySharesDetailsLabel.setText("");
//			mySharesDetailsLabel.setMinimumSize(new Dimension(255,0));
			mySharesPresetDetailsPanel = new JPanel();
			mySharesPresetDetailsPanel.setLayout(new GridBagLayout());
			mySharesPresetDetailsPanel.setVisible(true);
			mySharesPresetDetailsDescriptionLabel=new JLabel();
			mySharesPresetDetailsDescriptionLabel.setText("");
			mySharesPresetDetailsLinkLabel=new LinkLabel();
			mySharesPresetDetailsLinkLabel.setText("");
			mySharesPresetDetailsPanel.add(mySharesPresetDetailsDescriptionLabel, topGridBagConstraints20);
			mySharesPresetDetailsPanel.add(mySharesPresetDetailsLinkLabel, topGridBagConstraints25);
			mySharesPresetDetailsPanel.add(mySharesDetailsLabel, gridBagConstraints20);
			mySharesPresetDetailsPanel.add(mySharesDetailsAuthorLabel, gridBagConstraints24);
			mySharesPresetDetailsPanel.add(getMySharesDetailsActivateCheckbox(), gridBagConstraints26);
			mySharesPresetDetailsPanel.add(getMySharesDetailsLoadButton(), gridBagConstraints27);
		}
		return mySharesPresetDetailsPanel;
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
			searchTextField.setToolTipText("Enter a keyword to search for in title, description, tags and preset settings.");
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
			ActionListener l = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					onSearch();
				}
			};
			startSearchButton.addActionListener(CursorController.createListener(this, l));

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
					extendedSearchPanel.revalidate();
					searchControlsPanel.revalidate();
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
	protected void onSearchPresetSelection(){
		if(currentSearchResultList==null) return;
		int selected=getSearchPresetTable().getSelectedRow();
		if(currentSearchResultList.size()<=selected || selected<0) return;
		log.debug("selected preset #"+selected);
		selectedSearchPreset=currentSearchResultList.get(selected);
		setDescription(selectedSearchPreset.getDescription());
		setPresetDetails(selectedSearchPreset.toHtml());
		setAutorDetails(selectedSearchPreset.toBasicHtml(isLoggedin()));
		setLink(selectedSearchPreset.getLink());
		//TODO: image from bytes:
//		searchPresetDetailsAuthorLabel.setIcon(new ImageIcon("http://stlab.robig.net/style/images/player.jpg"));
		if(searchPresetDetailsActivateCheckbox.isSelected())
			onLoad(selectedSearchPreset);
	}
	
	protected void setDescription(String d){
		searchPresetDetailsDescriptionLabel.setText(d);
	}
	
	protected void setLink(String link){
		searchPresetDetailsLinkLabel.setText(link);
	}
	
	protected void setPresetDetails(String details){
		searchPresetDetailsLeftLabel.setText(details);
	}
	
	protected void setAutorDetails(String a){
		searchPresetDetailsRightLabel.setText(a);
	}
	
	/**
	 * when selecting a preset from a search result
	 */
	protected void onMySharesSelection(){
		if(currentMySharesList==null) return;
		int selected=getMySharesPresetTable().getSelectedRow();
		if(currentMySharesList.size()<=selected || selected<0) return;
		log.debug("selected my shared preset #"+selected);
		selectedMySharesPreset=currentMySharesList.get(selected);
		mySharesDetailsLabel.setText(selectedMySharesPreset.toHtml());
		mySharesDetailsAuthorLabel.setText(selectedMySharesPreset.toBasicHtml(isLoggedin()));
		mySharesPresetDetailsDescriptionLabel.setText(selectedMySharesPreset.getDescription());
		mySharesPresetDetailsLinkLabel.setLink(selectedMySharesPreset.getLink());
		if(mySharesDetailsActivateCheckbox.isSelected())
			onLoad(selectedMySharesPreset);
	}
	
	/**
	 * shows the tab for a login
	 */
	public void showLogin() {
		jTabbedPane.setSelectedIndex(0);
		setVisible(true);
		if(getLoginUsernameTextField().getText().isEmpty())
			getLoginUsernameTextField().requestFocus();
		else
			getLoginPasswordField().requestFocus();
	}
	
	public void showFind() {
		jTabbedPane.setSelectedIndex(1);
		setVisible(true);
		getSearchTextField().requestFocus();
	}
	
	public void showMyShares() {
		jTabbedPane.setSelectedIndex(4);
		setVisible(true);
	}
	
	public void showTop10() {
		jTabbedPane.setSelectedIndex(2);
		setVisible(true);
	}
	
	public void showPublish() {
		jTabbedPane.setSelectedIndex(3);
		setVisible(true);
		getShareTitleTextField().requestFocus();
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
			searchPresetDetailsActivateCheckbox.setSelected(searchPresetsAutoload.getValue());
			searchPresetDetailsActivateCheckbox.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent arg0) {
					searchPresetsAutoload.setValue(getSearchPresetDetailsActivateCheckbox().isSelected());
				}
			});
		}
		return searchPresetDetailsActivateCheckbox;
	}
	
	/**
	 * This method initializes searchPresetDetailsActivateCheckbox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getMySharesDetailsActivateCheckbox() {
		if (mySharesDetailsActivateCheckbox == null) {
			mySharesDetailsActivateCheckbox = new JCheckBox();
			mySharesDetailsActivateCheckbox.setText("Automatically load preset");
			mySharesDetailsActivateCheckbox.setSelected(mySharesAutoload.getValue());
			mySharesDetailsActivateCheckbox.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent arg0) {
					log.debug("myShares selected: "+getMySharesDetailsActivateCheckbox().isSelected());
					mySharesAutoload.setValue(getMySharesDetailsActivateCheckbox().isSelected());
				}
			});
		}
		return mySharesDetailsActivateCheckbox;
	}

	/**
	 * This method initializes searchPresetDetailsLoadButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getMySharesDetailsLoadButton() {
		if (mySharesDetailsLoadButton == null) {
			mySharesDetailsLoadButton = new JButton();
			mySharesDetailsLoadButton.setText("Load");
			mySharesDetailsLoadButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					onLoad(selectedMySharesPreset);
				}
			});
		}
		return mySharesDetailsLoadButton;
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
					onLoad(selectedSearchPreset);
				}
			});
		}
		return searchPresetDetailsLoadButton;
	}
	
	/**
	 * Load a Preset from the web onto the device
	 */
	protected void onLoad(WebPreset p) {
		if(p==null) return;
		log.info("Loading WebPreset: #"+p.getId()+" "+p.getTitle());
		DeviceFrame.getInctance().loadWebPreset(p);
	}

	@Override
	protected void onMouseReleased() {
//		this.repaint();
		getJTabbedPane().revalidate();
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		JFrame frame=new WebControlFrame();
		frame.show();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
