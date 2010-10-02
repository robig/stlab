package net.robig.stlab.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;

import static net.robig.gui.ImagePanel.loadImage;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import net.robig.logging.Logger;

/**
 * Displays a two digit number. Is used to display the preset and Knob changes.
 * @author robig
 *
 */
public class DisplayPanel extends JPanel implements MouseListener, PropertyChangeListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private enum Mode { NORMAL, FILTRON, PITCH };
	private boolean enterValueMode=false;
	private IValueCallback enterValueCallback=null;
	private Image[] digits=new Image[17];
	private Image off=null;
	private int value=0;
	private int digitWidth=0;
	private Mode mode=Mode.NORMAL;
	private JFormattedTextField inputField=null;
	private Logger log = new Logger(getClass());
	private int max=99;
	private int min=0;
	
	public DisplayPanel(int min, int max) {
		this.min=min;
		setMax(max);
		init();
	}
	
	public DisplayPanel() {
		init();
	}
	
	private void init() {
		int i=0;
		for(i=0;i<10;i++){
			digits[i]=loadImage("img/display"+i+".png");
		}
		digits[10]=loadImage("img/display-.png");
		digits[11]=loadImage("img/display-1.png");
		digits[12]=loadImage("img/displayd.png");
		digits[13]=loadImage("img/displayn.png");
		digits[14]=loadImage("img/displayp.png");
		digits[15]=loadImage("img/displayt.png");
		digits[16]=loadImage("img/displayu.png");
		off=loadImage("img/display_off.png");
		digitWidth=digits[0].getWidth(null);
		setSize(digitWidth*2,digits[0].getHeight(null));
		
		initComponents();
	}
	
	private void initComponents() {
		this.setLayout(null);
		addMouseListener(this);
		
		Format format=NumberFormat.getIntegerInstance();
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
		Font font = new Font("Courier",inputField.getFont().getStyle(),42);
		inputField.setFont(font);
		inputField.addPropertyChangeListener("value",this);
		inputField.addKeyListener(this);
		inputField.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
		inputField.getInputMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
		add(inputField);
	}

	private int get1stDigit(){
		if(mode==Mode.FILTRON){
			if(value==0) return 16; //u
			else return 12; // d
		}
		if(mode==Mode.PITCH){
			if(value==0   ) return 11; // -1
			if(value==0x0d) return 12; // d
			if(value <8   ) return 10; // -
			if(value==0x19) return 1;  // 1
		}
		return Math.round(value/10);
	}
	
	private int get2ndDigit(){
		if(mode==Mode.FILTRON){
			if(value==0) return 14; // P
			else return 13; // n
		}
		if(mode==Mode.PITCH){
			if(value==   0) return 2;
			if(value==   5) return 7;
			if(value==   7) return 5;
			if(value==0x0d) return 15;
			if(value==0x12) return 5;
			if(value==0x14) return 7;
			if(value==0x19) return 2;
		}
		return value-get1stDigit()*10;
	}
	
	/**
	 * Sets the value to display. Allowed range is 0-100.
	 * 100 is displayed as 00. Values <10 will have 1st digit switched off.
	 * @param val
	 */
	public void setValue(int val){
		if(val>max||val<min) return; //supported range to display
		value=val;
		if(enterValueMode)enterValueMode=false;
		abort();
		repaint();
	}
	
	public void setMax(int max){
		this.max=max;
	}
	
	public void setFiltronMode() {
		mode=Mode.FILTRON;
	}
	
	public void setPitchMode() {
		mode=Mode.PITCH;
	}
	
	public void setNormalMode(){
		mode=Mode.NORMAL;
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
		
		if(mode!=Mode.NORMAL){
			if(value>0x0d && value!=0x19)g.drawImage(off,0,0,this);
			else g.drawImage(digits[get1stDigit()],0,0,this);
		}else if(value==100){ //display 100 as 00
			g.drawImage(digits[0],0,0,this);
		}else if(value>9){ // only display first digit when > 9
			g.drawImage(digits[get1stDigit()],0,0,this);
		}else{
			g.drawImage(off,0,0,this);
		}
		g.drawImage(digits[get2ndDigit()],digitWidth,0,this);
	}

	public void onClick() {
	}
	
	public void abort(){
		log.debug("Aborting enterValue Mode");
		inputField.setVisible(false);
		getParent().requestFocusInWindow();
		if(enterValueMode)enterValueMode=false;
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
		DisplayPanel panel = new DisplayPanel(0,99);
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
		inputField.setVisible(false);
		if(inputField.getValue()!=null){
			int newValue=getTextFieldValue();
			if(enterValueMode){
				setValue(newValue);
				if(enterValueCallback!=null)
					enterValueCallback.callback(newValue);
				//onChange();
			}else
				abort();
		}
		getParent().requestFocusInWindow();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		int kc=e.getKeyCode();
		if(kc == KeyEvent.VK_ESCAPE) abort();
		else if(kc == KeyEvent.VK_ENTER) abort();
		//else if(kc == 0) abort();
		else log.debug("keyPressed"+e.toString());
	}
	
	public void enterValue(IValueCallback callback){
		enterValueCallback=callback;
		enterValueMode=true;
		inputField.setVisible(true);
		inputField.setFocusable(true);
		inputField.setText(getValue()+"");
		inputField.selectAll();
		inputField.requestFocusInWindow();
		log.debug("Ready for input");
	}
	
	public int getTextFieldValue() {
		try {
			inputField.commitEdit();
			return Integer.parseInt(inputField.getValue().toString());
		} catch (ParseException e) {
			e.printStackTrace(log.getWarnPrintWriter());
		}
		return 0;
	}
	
	public boolean isEnterValueModeEnabled(){
		return enterValueMode;
	}
	
	public interface IValueCallback {
		public void callback(int value);
	}
}
