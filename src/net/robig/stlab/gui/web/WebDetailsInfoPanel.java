package net.robig.stlab.gui.web;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import net.robig.gui.LinkLabel;
import net.robig.stlab.gui.controls.WrappedLabel;
import net.robig.stlab.gui.events.MouseAdapter;
import net.robig.stlab.model.WebPreset;
import net.robig.stlab.util.Browser;

public class WebDetailsInfoPanel extends javax.swing.JPanel {
	private static final long serialVersionUID = -3010687202231958566L;
	private JTextPane webDetailsDescriptionLabel;
	private JLabel webDetailsVoteLabel;
	private LinkLabel webDetailsLink;

	{
		setLayout(new BorderLayout());
		webDetailsDescriptionLabel=new JTextPane();
		webDetailsDescriptionLabel.setContentType("text/html");
//		webDetailsDescriptionLabel.setLineWrap()
		webDetailsDescriptionLabel.setMaximumSize(new Dimension(236,9999));
		webDetailsDescriptionLabel.setEditable(false);

		webDetailsVoteLabel=new JLabel("votes");
		add(webDetailsDescriptionLabel, BorderLayout.NORTH);
		webDetailsLink=new LinkLabel();
		webDetailsLink.setVisible(false);
		webDetailsLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onClick();
			}
		});
		add(webDetailsLink, BorderLayout.CENTER);
		add(webDetailsVoteLabel, BorderLayout.SOUTH);
	}
	
	protected void onClick(){
		//Implement in overriding class
	}
	
	public void setVoteHtml(String html){
		webDetailsVoteLabel.setText(html);
	}
	
	public void setDesctiptionHtml(String text){
		webDetailsDescriptionLabel.setText(text);
	}
	
	public void setLink(String link){
		webDetailsLink.setLink(link);
	}
	
	public void update(WebPreset p){
		setDesctiptionHtml("<html>"+p.toTopPanelHtml(WebControlFrame.getInstance().isLoggedin())+"</html>");
		setVoteHtml(p.getTopPanelVotesHtml(WebControlFrame.getInstance().isLoggedin()));
		if(p.hasLink()){
			webDetailsLink.setVisible(true);
//			webDetailsLink.setText("<html><br/><u>"+currentWebPreset.getLink()+"</u><br></html>");
			webDetailsLink.setLink(p.getLink());
//			webDetailsLink.setToolTipText("Open "+currentWebPreset.getLink()+" in your Browser");
		}else
			webDetailsLink.setVisible(false);
	}
}
