package net.robig.stlab.gui;

import net.robig.gui.ImagePanel;
import net.robig.stlab.StLab;
import net.robig.stlab.util.Browser;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;;

public class AboutDlg extends JDialog {
	private static final long serialVersionUID = 1L;
	private ImagePanel icon = null;
	private JPanel jContent = null;
	private JButton closeButton = null;
	private JLabel aboutText = null;
	private JLabel webLink = null;
	

	/**
	 * This method initializes 
	 * 
	 */
	public AboutDlg() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		
		icon=new ImagePanel("img/icon.png");
        icon.setBounds(new Rectangle(13, 14, 64, 64));
        this.setSize(new Dimension(334, 157));
        this.setContentPane(getJContent());
        this.setName("About StLab");
        this.setTitle("About StLab");
        this.setResizable(false);
        
		// CLose Button of window:
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
	}

	public void close(){
		setVisible(false);
		dispose();
	}
	
	/**
	 * This method initializes jContent	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContent() {
		if (jContent == null) {
			webLink = new JLabel();
			webLink.setBounds(new Rectangle(98, 81, 133, 22));
			webLink.setText("<html><a href=\"http://robig.net/wiki/?wiki=EnStLab\">http://robig.net</a></html>");
			webLink.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					try {
						Browser.getInstance().browse(new URI("http://robig.net/wiki/?wiki=EnStLab"));
						//Desktop.getDesktop().browse(new URI("http://robig.net/wiki/?wiki=EnStLab"));
					} catch (URISyntaxException e1) {
					}
				}
			});
			webLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			aboutText = new JLabel();
			aboutText.setBounds(new Rectangle(95, 14, 196, 59));
			aboutText.setText("<html>StLab v"+StLab.applicationVersion+"<br>by Robert Gr�ber (robig)<br>Use at your own risk!</html>");
			jContent = new JPanel();
			jContent.setLayout(null);
			jContent.add(icon, null);
			jContent.add(getCloseButton(), null);
			jContent.add(aboutText, null);
			jContent.add(webLink, null);
		}
		return jContent;
	}

	/**
	 * This method initializes closeButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setBounds(new Rectangle(239, 97, 80, 30));
			closeButton.setToolTipText("Close");
			closeButton.setText("OK");
			closeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					close();
				}
			});
		}
		return closeButton;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
