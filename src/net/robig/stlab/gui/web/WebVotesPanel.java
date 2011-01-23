package net.robig.stlab.gui.web;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import net.robig.logging.Logger;
import net.robig.stlab.StLab;
import net.robig.stlab.model.WebPreset;
import net.robig.stlab.model.WebVote;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class WebVotesPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Logger log = new Logger(this);
	
	private List<WebVote> votes=new ArrayList<WebVote>();
	
	private int page=0;
	
	private WebPreset currentPreset=null;

	private JScrollPane voteScrollPane = null;

	private JPanel innerPanel = null;

	private JLabel topLabel = null;

	private JPanel contentPanel = null;

	public WebVotesPanel() {
		initialize();
	}
	
	public synchronized void clear() {
//		if(this.votes!=null && this.votes.size()>0){
//		}
		if(votes!=null)votes.clear();
		contentPanel.removeAll();
//		contentPanel.revalidate();
	}
	
	public boolean allLoaded(){
		return currentPreset.getVoteCount() <= votes.size();
	}
	
	public void showVotes(WebPreset p){
		clear();
		addVotes(p.getVotes());
		this.currentPreset=p;
	}
	
	private void onUpdate(){
		if(votes.size()>0)setVisible(true);
		else setVisible(false);
		
		contentPanel.validate();
		contentPanel.getParent().validate();
	}
	
	private synchronized void addVotes(List<WebVote> votes){
		for(WebVote vote: votes){
			contentPanel.add(vote); // vote.getText()
			this.votes.add(vote);
		}
		onUpdate();
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
			voteScrollPane.setBorder(BorderFactory.createLineBorder(StLab.FOREGROUND));
			voteScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					if(currentPreset==null) return;
//					int max=voteScrollPane.getVerticalScrollBar().getVisibleAmount();
					boolean isMax=voteScrollPane.getVerticalScrollBar().getVisibleAmount() + voteScrollPane.getVerticalScrollBar().getValue() == voteScrollPane.getVerticalScrollBar().getMaximum();
//					log.debug("vertical scrolling "+e.getValue()+" isMax "+isMax);
					if(isMax)onLoadMore();
				}
			});
		}
		return voteScrollPane;
	}

	protected synchronized void onLoadMore(){
		if(currentPreset==null) return;
		if(allLoaded()){
			log.debug("All votes loaded already");
			return;
		}
		page++;
		log.info("requesting votes page "+page+" of preset "+currentPreset);
		addVotes(WebControlFrame.getInstance().loadVotes(currentPreset, page));
	}
	
	/**
	 * This method initializes innerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getInnerPanel() {
		if (innerPanel == null) {
			topLabel = new JLabel();
			topLabel.setText("<html><u>Last Votes:</u></html>");
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
			contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.Y_AXIS));
		}
		return contentPanel;
	}
}
