package net.robig.stlab.gui;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import net.robig.logging.Logger;
import net.robig.stlab.StLabConfig;
import net.robig.stlab.util.config.BoolValue;
import net.robig.stlab.util.config.DoubleValue;
import net.robig.stlab.util.config.IntValue;
import net.robig.stlab.util.config.StringValue;

public abstract class PersistentJFrame extends JFrame {
	
	public interface IVisibleChangeListener {
		public void visibilityChanged(boolean value);
	}
	
	private ArrayList<IVisibleChangeListener> visibilityChangeListeners=new ArrayList<IVisibleChangeListener>();
	public synchronized void addVisibleChangeListener(IVisibleChangeListener l){
		visibilityChangeListeners.add(l);
	}
	
	protected synchronized void visibilityChanged(){
		for(IVisibleChangeListener l:visibilityChangeListeners)
			l.visibilityChanged(isVisible());
		visible.setValue(isVisible());
	}
	
	protected static final Color FOREGROUND=new Color(187,154,77);
	protected static final Color BACKGROUND=new Color(44,45,48);
	
	private static final long serialVersionUID = -5669742303445861069L;
	
	protected String keybase=this.getClass().getName().toLowerCase();
	IntValue width=null;
	protected int defaultWidth=300;
	IntValue height=null;
	protected int defaultHeight=300;
	IntValue x=null;
	protected int defaultX=0;
	IntValue y=null;
	protected int defaultY=0;
	BoolValue visible=null;
	boolean defaultVisible=false;
	protected Logger log=new Logger(this);
	
	public PersistentJFrame() {
		initialize();
	}
	
	protected void initialize() {
		width=StLabConfig.getIntValue(keybase+".width", defaultWidth);
		height=StLabConfig.getIntValue(keybase+".height", defaultHeight);
		x=StLabConfig.getIntValue(keybase+".x", defaultX);
		y=StLabConfig.getIntValue(keybase+".y", defaultY);
		this.setBounds(x.getValue(), y.getValue(), width.getValue(),height.getValue());
		visible=StLabConfig.getBoolValue(keybase+".visible", defaultVisible);
		this.setVisible(visible.getValue());
		initListeners();
	}

	private void initListeners() {
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				x.setValue(getX());
				y.setValue(getY());
				onWindowMove();
			}

			@Override
			public void componentResized(ComponentEvent e) {
				width.setValue(getWidth());
				height.setValue(getHeight());
				onWindowResize();
			}
		});
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				setVisible(false);
				onWindowClose();
			}

			@Override
			public void windowClosing(WindowEvent e) {
			}
		});
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseReleased(MouseEvent e) {
				log.debug("Mouse released "+e);
				onMouseReleased();
			}
		});
	}
	
	protected void onMouseReleased() {
	}

	@Override
	public void setVisible(boolean b) {
		log.debug("setting visibility: "+b);
		super.setVisible(b);
		visibilityChanged();
	}
	
	protected void onWindowClose(){
		
	}
	
	protected void onWindowResize() {
		
	}
	
	protected void onWindowMove(){
		
	}
	
	public BoolValue getBoolValue(String key, boolean def){
		return StLabConfig.getBoolValue(keybase+"."+key, def);
	}
	
	public StringValue getStringValue(String key, String def){
		return StLabConfig.getStringValue(keybase+"."+key, def);
	}
	
	public DoubleValue getBoolValue(String key, double def){
		return StLabConfig.getDoubleValue(key, def);
	}
	
	public IntValue getIntValue(String key, int def){
		return StLabConfig.getIntValue(key, def);
	}
}
