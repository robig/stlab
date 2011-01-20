package net.robig.stlab.gui.web;

import java.util.List;
import javax.swing.JPanel;
import net.robig.stlab.model.WebVote;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import java.awt.FlowLayout;
import java.awt.BorderLayout;

public class WebVotesPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private List<WebVote> votes=null;

	private JScrollPane voteScrollPane = null;

	private JPanel innerPanel = null;

	private JLabel topLabel = null;

	private JPanel contentPanel = null;

	public WebVotesPanel() {
		initialize();
	}
	
	public void clear() {
//		if(this.votes!=null && this.votes.size()>0){
//		}
		if(votes!=null)votes.clear();
		contentPanel.removeAll();
//		contentPanel.revalidate();
	}
	
	public void loadVotes(List<WebVote> votes){
		clear();
		this.votes=votes;
		for(WebVote vote: votes){
			contentPanel.add(vote); // vote.getText()
		}
		if(votes.size()>0)setVisible(true);
		else setVisible(false);
		contentPanel.revalidate();
	}

	private void initialize() {
		setLayout(new BorderLayout());
        this.add(getVoteScrollPane(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes voteScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getVoteScrollPane() {
		if (voteScrollPane == null) {
			voteScrollPane = new JScrollPane();
			voteScrollPane.setViewportView(getInnerPanel());
			voteScrollPane.setBorder(new EmptyBorder(10,10,10,10));
		}
		return voteScrollPane;
	}

	/**
	 * This method initializes innerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getInnerPanel() {
		if (innerPanel == null) {
			topLabel = new JLabel();
			topLabel.setText("<html><u>Votes:</u></html>");
			innerPanel = new JPanel();
			innerPanel.setLayout(new BorderLayout());
			innerPanel.add(topLabel, BorderLayout.NORTH);
			innerPanel.add(getContentPanel(), BorderLayout.CENTER);
		}
		return innerPanel;
	}

	/**
	 * This method initializes contentPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		}
		return contentPanel;
	}
}
