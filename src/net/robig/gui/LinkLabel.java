package net.robig.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import net.robig.stlab.StLab;
import net.robig.stlab.util.Browser;


public class LinkLabel extends javax.swing.JLabel {
	
	private static final long serialVersionUID = -6857760395341424886L;
	
	protected String link = null;
	
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setForeground(StLab.LINK);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Browser.getInstance().browse(link);
			}
		});
	}
	
	@Override
	public void setText(String text) {
		if(link==null) link = text; // JLabel compatibility
		super.setText("<html><u>"+text+"</u></html>");
		setToolTipText("Opens "+link+" in your Browser");
	}
	
	public void setLink(String url){
		this.link=url;
		setText(url);
	}
}
