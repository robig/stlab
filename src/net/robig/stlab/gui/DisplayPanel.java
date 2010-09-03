package net.robig.stlab.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.Format;
import java.text.NumberFormat;

import static net.robig.gui.ImagePanel.loadImage;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.robig.gui.MyJKnob;
import net.robig.logging.Logger;

/**
 * Displays a two digit number. Is used to display the preset and Knob changes.
 * @author robig
 *
 */
public class DisplayPanel extends JPanel implements MouseListener, PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	Image[] digits=new Image[10];
	Image off=null;
	int value=0;
	int digitWidth=0;
	JFormattedTextField inputField=null;
	Logger log = new Logger(getClass());
	
	public DisplayPanel() {
		for(int i=0;i<10;i++){
			digits[i]=loadImage("img/display"+i+".png");
		}
		off=loadImage("img/display_off.png");
		digitWidth=digits[0].getWidth(null);
		setSize(digitWidth*2,digits[0].getHeight(null));
		
		initComponents();
	}
	
	private void initComponents() {
		this.setLayout(null);
		addMouseListener(this);
		
		Format format=NumberFormat.getNumberInstance();
		//format.
		inputField=new JFormattedTextField(format);
		inputField.setOpaque(true);
		inputField.setBounds(this.getBounds());
		inputField.setSize(getSize());
		inputField.setVisible(false);
		inputField.setSelectedTextColor(Color.RED);
		inputField.setSelectionColor(Color.DARK_GRAY);
		inputField.setForeground(Color.RED);
		inputField.setBackground(Color.BLACK);
		inputField.setColumns(2);
		Font font = new Font("Courier",inputField.getFont().getStyle(),48);
		inputField.setFont(font);
		inputField.addPropertyChangeListener("value",this);
		add(inputField);
	}

	private int get1stDigit(){
		return Math.round(value/10);
	}
	
	private int get2ndDigit(){
		return value-get1stDigit()*10;
	}
	
	/**
	 * Sets the value to display. Allowed range is 0-100.
	 * 100 is displayed as 00. Values <10 will have 1st digit switched off.
	 * @param val
	 */
	public void setValue(int val){
		if(val>100||val<0) return; //supported range to display
		value=val;
		repaint();
	}
	
	/**
	 * Gets the current value.
	 * @return
	 */
	public int getValue(){
		return value;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(value==100){ //display 100 as 00
			g.drawImage(digits[0],0,0,this);
		}else if(value>9){ // only display first digit when > 9
			g.drawImage(digits[get1stDigit()],0,0,this);
		}else{
			g.drawImage(off,0,0,this);
		}
		g.drawImage(digits[get2ndDigit()],digitWidth,0,this);
	}

	public void onClick() {
		inputField.setVisible(true);
		inputField.setText(getValue()+"");
		Runnable doRun = new Runnable() {
			 
            public void run() {
            	inputField.select(0,inputField.getText().length()); 
            }
        };
        SwingUtilities.invokeLater(doRun);

		log.debug("Ready for input");
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		onClick();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
	public static void main(String[] args) {
		JFrame myFrame = new JFrame("DisplayPanel Test method");
		
		Container thePane = myFrame.getContentPane();
		DisplayPanel panel = new DisplayPanel();
		// Add a DisplayPanel to the pane.
		thePane.add(panel);
		myFrame.setBounds(new Rectangle(0,0,100,100));
	
		myFrame.addWindowListener(new WindowAdapter() {
	             public void windowClosing(WindowEvent e) {
	                 System.exit(0);
	             }
	         });
	
		myFrame.pack();
		myFrame.show();
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		log.debug(arg0.toString());
		setValue(Integer.parseInt(inputField.getValue().toString()));
		inputField.setVisible(false);
		onChange();
	}
	
	public void onChange() {
		
	}
}
