package net.robig.stlab.gui;

import java.awt.Graphics;
import java.awt.Image;
import static net.robig.gui.ImagePanel.loadImage;
import javax.swing.JPanel;

public class DisplayPanel extends JPanel {

	Image[] digits=new Image[10];
	int value=0;
	int digitWidth=0;
	
	public DisplayPanel() {
		for(int i=0;i<10;i++){
			digits[i]=loadImage("img/display"+i+".png");
		}
		digitWidth=digits[0].getWidth(null);
		setSize(digitWidth*2,2*digits[0].getHeight(null));
	}
	
	private int get1stDigit(){
		return Math.abs(value/10);
	}
	
	private int get2ndDigit(){
		return value-get1stDigit()*10;
	}
	
	public void setValue(int val){
		if(val>100) return;
		value=val;
		repaint();
	}
	
	public int getValue(){
		return value;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(digits[get1stDigit()],0,0,this);
		g.drawImage(digits[get2ndDigit()],digitWidth,0,this);
		
	}
	
}
