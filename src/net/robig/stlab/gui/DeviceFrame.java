package net.robig.stlab.gui;

import java.awt.BorderLayout;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.robig.gui.ImageButton;
import net.robig.gui.ImagePanel;
import net.robig.gui.IntegerValueKnob;
import net.robig.gui.MyJKnob;
import net.robig.logging.Logger;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DeviceFrame extends JFrame {

	protected Logger log = new Logger(this.getClass());
	
	private static final long serialVersionUID = 1L;
	private ImagePanel jContentPane = null;
	private JToolBar toolBar = null;
	private JMenuBar menu = null;
	private JMenu fileMenu = null;
	private JMenuItem optionsMenuItem = null;
	private JMenuItem exitMenuItem = null;
	private ImagePanel back = jContentPane;
	
	//Controls:
	private IntegerValueKnob volumeKnob = new IntegerValueKnob();
	private IntegerValueKnob bassKnob = new IntegerValueKnob();
	private IntegerValueKnob middleKnob = new IntegerValueKnob();
	private IntegerValueKnob trebleKnob = new IntegerValueKnob();
	private IntegerValueKnob gainKnob = new IntegerValueKnob();
	private IntegerValueKnob ampKnob = new IntegerValueKnob();
	
	private class LittleKnob extends IntegerValueKnob {
		@Override
		protected String getImageFile() {
			return "img/lknob.png";
		}
	}
	
	private class PresetSwitch extends ImageButton {
		public PresetSwitch(){
			//imageFile="img/red.png";
			//setBorder(new LineBorder(new Color(0,0,255)));
			init();
		}
	}
	
	private LittleKnob pedalKnob = new LittleKnob();
	private LittleKnob pedalEditKnob = new LittleKnob();
	private LittleKnob delayKnob = new LittleKnob();
	private LittleKnob delayEditKnob = new LittleKnob();
	private LittleKnob reverbKnob = new LittleKnob();
	
	private PresetSwitch prevPreset = new PresetSwitch();
	private PresetSwitch nextPreset = new PresetSwitch();
	
	//Display:
	private DisplayPanel display = new DisplayPanel();

	/**
	 * This is the default constructor
	 */
	public DeviceFrame() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		ampKnob.setBounds(new Rectangle(64, 165, 100, 100));
		gainKnob.setBounds(new Rectangle(194, 165, 100, 100));
		trebleKnob.setBounds(new Rectangle(292, 165, 100, 100));
		middleKnob.setBounds(new Rectangle(390, 165, 100, 100));
		bassKnob.setBounds(new Rectangle(489, 165, 100, 100));
		volumeKnob.setBounds(new Rectangle(587, 165, 100, 100));
		display.setBounds(new Rectangle(588,275,29*2,49));
		pedalKnob.setBounds(new Rectangle(65,311,65,65));
		pedalEditKnob.setBounds(new Rectangle(160,311,65,65));
		delayKnob.setBounds(new Rectangle(277,311,65,65));
		delayEditKnob.setBounds(new Rectangle(373,311,65,65));
		reverbKnob.setBounds(new Rectangle(469,311,65,65));
		nextPreset.setBounds(new Rectangle(201,543,32,32));
		prevPreset.setBounds(new Rectangle(465,543,32,32));
		this.setSize(940, 671);
		this.setJMenuBar(getMenu());
		this.setContentPane(getJContentPane());
		this.setTitle("Tonelab Device");
		
		volumeKnob.setName("Volume");
		bassKnob.setName("Bass");
		middleKnob.setName("Middle");
		trebleKnob.setName("Treble");
		gainKnob.setName("Gain");
		ampKnob.setName("AMP");
		ampKnob.setMaxValue(11);
		
		pedalKnob.setName("Pedal Effect");
		pedalEditKnob.setName("Pedal Edit");
		delayKnob.setName("Mod/Delay Effect");
		delayEditKnob.setName("Mod/Delay Edit");
		reverbKnob.setName("Reverb");
		
		prevPreset.setToolTipText("Previous Preset");
		prevPreset.setName("Previous Preset");
		nextPreset.setToolTipText("Next Preset");
		nextPreset.setName("Next Preset");
		
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
		
		initListeners();
	}
	
	// Init control listeners:
	private void initListeners(){
		//Listeners for the display change:
		for(IntegerValueKnob k: new IntegerValueKnob[]{
			ampKnob,
			gainKnob,
			trebleKnob,
			middleKnob,
			bassKnob,
			volumeKnob,
			//mini's:
			pedalKnob,
			pedalEditKnob,
			delayKnob,
			delayEditKnob,
			reverbKnob
		}){
			k.addChangeListener(new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					//k.getValue();
					IntegerValueKnob knob = (IntegerValueKnob) e.getSource();
					log.debug("Knob changed: "+knob.getName()+" value="+knob.getValue());
					display.setValue(knob.getValue());
				}
			});
		}
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new ImagePanel(ImagePanel.loadImage("img/TonelabST.png"));
			// Controls:
			jContentPane.setLayout(null);
			jContentPane.add(getToolBar(), null);
			jContentPane.add(ampKnob, null);
			jContentPane.add(volumeKnob, null);
			jContentPane.add(bassKnob, null);
			jContentPane.add(middleKnob, null);
			jContentPane.add(trebleKnob, null);
			jContentPane.add(gainKnob, null);
			jContentPane.add(display, null);
			jContentPane.add(pedalKnob, null);
			jContentPane.add(pedalEditKnob, null);
			jContentPane.add(delayKnob, null);
			jContentPane.add(delayEditKnob, null);
			jContentPane.add(reverbKnob, null);
			jContentPane.add(nextPreset, null);
			jContentPane.add(prevPreset, null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes toolBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getToolBar() {
		if (toolBar == null) {
			toolBar = new JToolBar();
			toolBar.setBounds(new Rectangle(0, 0, 940, 4));
		}
		return toolBar;
	}

	/**
	 * This method initializes menu	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getMenu() {
		if (menu == null) {
			menu = new JMenuBar();
			fileMenu=new JMenu("File");
			menu.add(fileMenu);
			optionsMenuItem = new JMenuItem("Options");
			optionsMenuItem.setMnemonic(KeyEvent.VK_O);
			fileMenu.add(optionsMenuItem);
			exitMenuItem = new JMenuItem("Exit");
			exitMenuItem.setMnemonic(KeyEvent.VK_W);
			fileMenu.add(exitMenuItem);
		}
		return menu;
	}
	
	public static void main(String[] args) {
		new DeviceFrame().show();
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
