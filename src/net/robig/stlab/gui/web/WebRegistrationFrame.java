package net.robig.stlab.gui.web;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.robig.gui.CursorController;
import net.robig.gui.ImagePanel;
import net.robig.net.WebAccess;
import net.robig.stlab.StLabConfig;
import net.robig.stlab.util.Browser;

public class WebRegistrationFrame extends JFrame {

	private static final long serialVersionUID = -6031358763556036551L;
	
	private static WebRegistrationFrame instance=null;
	public static WebRegistrationFrame getInstance() {
		if(instance==null){
			new WebRegistrationFrame();
		}
		return instance;
	}
	

	private WebAccess web=new WebAccess();
	
	private JPanel basePanel = null;
	private JLabel topLabel = null;
	private JPanel registrationFormPanel = null;
	private JLabel usernameLabel = null;
	private JTextField usernameTextField = null;
	private JLabel emailLabel = null;
	private JTextField emailTextField = null;
	private JLabel password1Label = null;
	private JPasswordField password1Field = null;
	private JLabel password2Label = null;
	private JPasswordField password2Field = null;
	private JCheckBox termsCheckBox = null;
	private JLabel acceptTermsLabel = null;
	private JButton registerButton = null;
	private JLabel iconLabel = null;

	/**
	 * This method initializes 
	 * 
	 */
	public WebRegistrationFrame() {
		super();
		instance=this;
		initialize();
	}
	
	public WebRegistrationFrame(WebAccess w) {
		super();
		instance=this;
		web=w;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new Dimension(445, 361));
        this.setTitle("StLab Web Registration");
        this.setContentPane(getBasePanel());
			
	}

	/**
	 * This method initializes basePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getBasePanel() {
		if (basePanel == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints13.gridy = 0;
			iconLabel = new JLabel();
			iconLabel.setText("");
			iconLabel.setIcon(ImagePanel.loadImageIcon("img/stlab-web_top.png"));
			usernameLabel = new JLabel();
			usernameLabel.setText("Choose username:");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 3;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new Insets(12, 0, 12, 0);
			gridBagConstraints.gridy = 1;
			topLabel = new JLabel();
			topLabel.setText("Setup a new StLab web account here:");
			basePanel = new JPanel();
			basePanel.setLayout(new GridBagLayout());
			basePanel.add(topLabel, gridBagConstraints);
			basePanel.add(getRegistrationFormPanel(), gridBagConstraints1);
			basePanel.add(iconLabel, gridBagConstraints13);
		}
		return basePanel;
	}

	/**
	 * This method initializes registrationFormPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getRegistrationFormPanel() {
		if (registrationFormPanel == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.gridy = 5;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 4;
			acceptTermsLabel = new JLabel("<html><u>accept terms and conditions</u></html>");
			acceptTermsLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			acceptTermsLabel.setForeground(Color.yellow);
			acceptTermsLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Browser.getInstance().browse(StLabConfig.getTaCUrl());
				}
			});
//			acceptTermsLabel.setText("<html>accept <a href=\""+
//					StLabConfig.getTaCUrl()+
//					"\">terms and conditions</a>:</html>");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 1;
			gridBagConstraints10.gridy = 4;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints9.gridy = 3;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.gridy = 3;
			password2Label = new JLabel();
			password2Label.setText("retype password:");
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints7.gridy = 2;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 2;
			password1Label = new JLabel();
			password1Label.setText("Your password:");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridy = 1;
			emailLabel = new JLabel();
			emailLabel.setText("Valid e-Mail address:");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = -1;
			registrationFormPanel = new JPanel();
			registrationFormPanel.setLayout(new GridBagLayout());
			registrationFormPanel.add(usernameLabel, gridBagConstraints2);
			registrationFormPanel.add(getUsernameTextField(), gridBagConstraints3);
			registrationFormPanel.add(emailLabel, gridBagConstraints4);
			registrationFormPanel.add(getEmailTextField(), gridBagConstraints5);
			registrationFormPanel.add(password1Label, gridBagConstraints6);
			registrationFormPanel.add(getPassword1Field(), gridBagConstraints7);
			registrationFormPanel.add(password2Label, gridBagConstraints8);
			registrationFormPanel.add(getPassword2Field(), gridBagConstraints9);
			registrationFormPanel.add(getTermsCheckBox(), gridBagConstraints10);
			registrationFormPanel.add(acceptTermsLabel, gridBagConstraints11);
			registrationFormPanel.add(getRegisterButton(), gridBagConstraints12);
		}
		return registrationFormPanel;
	}

	/**
	 * This method initializes usernameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getUsernameTextField() {
		if (usernameTextField == null) {
			usernameTextField = new JTextField();
			usernameTextField.setPreferredSize(new Dimension(150, 28));
		}
		return usernameTextField;
	}

	/**
	 * This method initializes emailTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getEmailTextField() {
		if (emailTextField == null) {
			emailTextField = new JTextField();
			emailTextField.setPreferredSize(new Dimension(150, 28));
		}
		return emailTextField;
	}

	/**
	 * This method initializes password1Field	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	private JPasswordField getPassword1Field() {
		if (password1Field == null) {
			password1Field = new JPasswordField();
			password1Field.setPreferredSize(new Dimension(150, 28));
		}
		return password1Field;
	}

	/**
	 * This method initializes password2Field	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	private JPasswordField getPassword2Field() {
		if (password2Field == null) {
			password2Field = new JPasswordField();
			password2Field.setPreferredSize(new Dimension(150, 28));
		}
		return password2Field;
	}

	/**
	 * This method initializes termsCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getTermsCheckBox() {
		if (termsCheckBox == null) {
			termsCheckBox = new JCheckBox();
			termsCheckBox.setText("");
			termsCheckBox.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					registerButton.setEnabled(termsCheckBox.isSelected());
				}
			});
		}
		return termsCheckBox;
	}

	/**
	 * This method initializes registerButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRegisterButton() {
		if (registerButton == null) {
			registerButton = new JButton();
			registerButton.setText("Register account");
			registerButton.setEnabled(false);
			ActionListener l = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					onRegister();
				}
			};
			registerButton.addActionListener(CursorController.createListener(this, l));
		}
		return registerButton;
	}
	
	protected void onRegister() {
		String user=usernameTextField.getText().trim();
		if(user.length()<5){
			JOptionPane.showMessageDialog(this, "Username is too short!", "Fail", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(password1Field.getPassword().equals(password2Field.getPassword())){
			JOptionPane.showMessageDialog(this, "Passwords must be equal!", "Fail", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(password1Field.getPassword().length<6){
			JOptionPane.showMessageDialog(this, "Passwords too short!", "Fail", JOptionPane.WARNING_MESSAGE);
			return;
		}
		String mail=emailTextField.getText().trim();
		if(!mail.contains("@")){
			JOptionPane.showMessageDialog(this, "Email must be valid!", "Fail", JOptionPane.WARNING_MESSAGE);
			return;
		}
		boolean ok=web.register(user, new String(password1Field.getPassword()), mail);
		if(ok){
			WebControlFrame.getInstance().setUserName(user);
			JOptionPane.showMessageDialog(this, "Registration of "+user+" successful!");
			this.setVisible(false);
		}else{
			JOptionPane.showMessageDialog(this, "Failure message from server: "+web.getMessage(), "Fail", JOptionPane.WARNING_MESSAGE);
		}
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
