package net.robig.stlab.gui.controls;

import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.PageAttributes.OriginType;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.text.BreakIterator;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import net.robig.logging.Logger;

public class WrappableLabel extends JLabel {
	private static final long serialVersionUID = -404832329901666287L;
	private Logger log = new Logger(this);
	private String originalText="";
	
	public WrappableLabel() {
		this.addContainerListener(new ContainerListener() {
			
			@Override
			public void componentRemoved(ContainerEvent e) {
			}
			
			@Override
			public void componentAdded(ContainerEvent e) {
				wrapText(originalText);
			}
		});
	}
	
	private void wrapText(String text) {
		if(text==null)return;
		FontMetrics fm = getFontMetrics(getFont());


		BreakIterator boundary = BreakIterator.getWordInstance();
		boundary.setText(text);

		StringBuffer trial = new StringBuffer();
		StringBuffer real = new StringBuffer(text.contains("<html>")?"":"<html>");

		Container container = getParent();
		if(container!=null){
			int containerWidth = container.getWidth();
			if(containerWidth!=0){
				int start = boundary.first();
				for (int end = boundary.next(); end != BreakIterator.DONE;
					start = end, end = boundary.next()) {
					String word = text.substring(start,end);
					trial.append(word);
					int trialWidth = SwingUtilities.computeStringWidth(fm,
						trial.toString());
					if (trialWidth > containerWidth) {
						trial = new StringBuffer(word);
						real.append("<br/>");
					}
					real.append(word);
				}
			}else{
				real.append(text);
			}
		}else{
			if(log!=null)log.warn("Initializing WrappableLabel text without being in a container! Wrapping wont work! Text: "+text);
			real.append(text);
		}

		real.append(text.contains("</html>")?"":"</html>");

		super.setText(real.toString());
	}
	
	public void setText(String text){
		originalText=text;
		if(text!=null && text.length()>0)
			wrapText(text);
	}
	
	@Override
	public void revalidate() {
		if(originalText!=null)wrapText(originalText);
		super.revalidate();
	}
	
//	@Override
//	public void repaint() {
//		if(originalText!=null)wrapText(originalText);
//		super.repaint();
//	}
}
