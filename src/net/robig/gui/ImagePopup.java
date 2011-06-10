package net.robig.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import net.robig.logging.Logger;
import net.robig.stlab.gui.controls.WrappableLabel;
import net.robig.stlab.gui.controls.WrappedLabel;

public class ImagePopup extends JPanel {

	private static final long serialVersionUID = 1L;
	String message="";
	WrappableLabel messageLabel = new WrappableLabel();
	ImagePanel iconPanel=new ImagePanel("img/info.png");
	JPanel cardPanel = new JPanel();
	ImagePanel basePanel;
	JPanel emptyPanel=new JPanel();
	Logger log=new Logger(this);
	private CardLayout layout;
	
	public ImagePopup() {
		init("img/popup2.png");
	}

	
	public ImagePopup(String image){
		init(image);
	}
	
	private void init(String back){
		basePanel=new ImagePanel(back);
		basePanel.fitToImage();
		setLayout(new FlowLayout());
//		setLayout(null);
		
//		setSize(cardPanel.getPreferredSize());
		cardPanel.setSize(basePanel.getPreferredSize());
		add(cardPanel);
		
		layout = new CardLayout();
		cardPanel.setLayout(layout);
		cardPanel.add(basePanel,"base");
		cardPanel.add(emptyPanel,"empty"); //Empty one
		
		cardPanel.setOpaque(false);
//		basePanel.setBackground(Color.RED);
		setOpaque(false);
		emptyPanel.setOpaque(false);
		
		basePanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gc2=new GridBagConstraints();
//		gc2.gridwidth=2;
		gc2.gridx=0;
		gc2.insets=new Insets(5, 5, 5, 5);
		basePanel.add(iconPanel, gc2);
		
		GridBagConstraints gc=new GridBagConstraints();
		gc.weightx=200;
		gc.gridx=1;
		gc.insets=new Insets(12, 5, 12, 5);
		gc.fill=GridBagConstraints.BOTH;
		JPanel temp=new JPanel(new BorderLayout());
		basePanel.add(temp, gc);
		temp.add(messageLabel, BorderLayout.CENTER);
//		messageLabel.setBackground(Color.BLACK);
//		messageLabel.setOpaque(true);
		
//		messageLabel.setMaximumSize(basePanel.getPreferredSize());
		hideMessage();
	}

	public void setMessage(String msg) {
		iconPanel.setImage(ImagePanel.loadImage("img/info.png"));
		message=msg;
		messageLabel.setText(message);
		messageLabel.setMaximumSize(getSize());
	}
	
	public void setErrorMessage(String msg){
		iconPanel.setImage(ImagePanel.loadImage("img/error.png"));
		message=msg;
		messageLabel.setText(message);
		messageLabel.setMaximumSize(getSize());
	}
	
	public void showMessage(String msg){
		setMessage(msg);
		log.debug("Showing Message: "+msg);
		refresh();
	}
	
	public void showErrorMessage(String msg){
		setErrorMessage(msg);
		refresh();
	}
	
	public void hideMessage(){
		layout.last(cardPanel);
	}
	
	private void refresh(){
		layout.first(cardPanel);
	}
	
	public String getMessage(){
		return message;
	}
	
	
}
