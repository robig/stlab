package net.robig.stlab.gui;

import java.awt.Graphics;
import java.awt.Image;
import static net.robig.gui.ImagePanel.loadImage;
import javax.swing.JPanel;

/**
 * Displays a two digit number. Is used to display the preset and Knob changes.
 * @author robig
 *
 */
public class DisplayPanel extends JPanel {

	Image[] digits=new Image[10];
	Image off=null;
	int value=0;
	int digitWidth=0;
	
	public DisplayPanel() {
		for(int i=0;i<10;i++){
			digits[i]=loadImage("img/display"+i+".png");
		}
		off=loadImage("img/display_off.png");
		digitWidth=digits[0].getWidth(null);
		setSize(digitWidth*2,2*digits[0].getHeight(null));
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
	public void paint(Graphics g) {
		super.paint(g);
		
		if(value==100){ //display 100 as 00
			g.drawImage(digits[0],0,0,this);
		}else if(value>9){ // only display first digit when > 9
			g.drawImage(digits[get1stDigit()],0,0,this);
		}else{
			g.drawImage(off,0,0,this);
		}
		g.drawImage(digits[get2ndDigit()],digitWidth,0,this);
		
	}
	
}
