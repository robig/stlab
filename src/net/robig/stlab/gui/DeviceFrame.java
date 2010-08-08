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
import net.robig.gui.ImageSwitch;
import net.robig.gui.IntegerValueKnob;
import net.robig.gui.LED;
import net.robig.gui.MyJKnob;
import net.robig.logging.Logger;
import net.robig.stlab.model.StPreset;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DeviceFrame extends JFrame {

	protected Logger log = new Logger(this.getClass());
	private StPreset currentPreset=new StPreset();
	private IDeviceController device=null;
	
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
	
	private class LongSwitch extends ImageButton {
		public LongSwitch() {
			imageFile="img/button_long.png";
			setBorder(new LineBorder(new Color(0,0,255)));
			init();
		}
	}
	
	private class SmallButton extends ImageButton {
		public SmallButton() {
			imageFile="img/button.png";
			setBorder(new LineBorder(new Color(0,0,255)));
			init();
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
	
	private LongSwitch ampModeSwitch = new LongSwitch();
	private LongSwitch cabinetOptionSwitch = new LongSwitch();
	
	private LED pedalLed = new LED();
	private LED delayLed = new LED();
	private LED reverbLed = new LED(); 
	private ImageSwitch pedalSwitch = new ImageSwitch(pedalLed);
	private ImageSwitch delaySwitch = new ImageSwitch(delayLed);
	private ImageSwitch reverbSwitch = new ImageSwitch(reverbLed);
	
	private LED tapLed = new LED();
	private SmallButton tapButton = new SmallButton(); 
	
	//Display:
	private DisplayPanel display = new DisplayPanel();

	/**
	 * This is the default constructor
	 */
	public DeviceFrame(IDeviceController ctrl) {
		super();
		device=ctrl;
		initialize();
		initDevice();
	}
	
	public void initDevice(){
//		device.findAndConnect();
		device.initialize();
		
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
		
		prevPreset.setBounds(new Rectangle(201,543,32,32));
		nextPreset.setBounds(new Rectangle(465,543,32,32));
		
		ampModeSwitch.setBounds(new Rectangle(89,135,24,12));
		cabinetOptionSwitch.setBounds(new Rectangle(175,135,24,12));
		pedalSwitch.setBounds(new Rectangle(51,405,24,12));
		delaySwitch.setBounds(new Rectangle(264,405,24,12));
		reverbSwitch.setBounds(new Rectangle(459,405,24,12));
		
		pedalLed.setBounds(new Rectangle(82,406,12,12));
		delayLed.setBounds(new Rectangle(294,406,12,12));
		reverbLed.setBounds(new Rectangle(489,406,12,12));
		
		tapLed.setBounds(new Rectangle(382,364,12,12));
		tapButton.setBounds(new Rectangle(394,385,28,28));
		
		
		this.setJMenuBar(getMenu());
		this.setContentPane(getJContentPane());
		//this.setSize(940, 671);
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
		pedalKnob.setMaxValue(11);
		delayKnob.setName("Mod/Delay Effect");
		delayEditKnob.setName("Mod/Delay Edit");
		reverbKnob.setMaxValue(11);
		reverbKnob.setName("Reverb");
		
		prevPreset.setName("Previous Preset");
		nextPreset.setName("Next Preset");
		
		ampModeSwitch.setName("Switch AMP Type");
		cabinetOptionSwitch.setName("Cabinet/Option");
		pedalSwitch.setName("Pedal effect");
		delaySwitch.setName("Delay");
		reverbSwitch.setName("Reverb");

		pedalLed.setName("Pedal effect");
		delayLed.setName("Delay");
		reverbLed.setName("Reverb");
		
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
		
		// CLose Button of window:
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new ImagePanel(ImagePanel.loadImage("img/TonelabST.png"));
			jContentPane.setSize(940, 671);
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
			jContentPane.add(ampModeSwitch, null);
			jContentPane.add(cabinetOptionSwitch, null);
			
			jContentPane.add(pedalLed, null);
			jContentPane.add(delayLed, null);
			jContentPane.add(reverbLed, null);
			jContentPane.add(pedalSwitch, null);
			jContentPane.add(delaySwitch, null);
			jContentPane.add(reverbSwitch, null);
			
			jContentPane.add(tapLed, null);
			jContentPane.add(tapButton, null);
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

	public void quit() {
		System.exit(0);
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
			
			exitMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					quit();
				}
			});
		}
		return menu;
	}
	
	public static void main(String[] args) {
		new DeviceFrame(new DummyDeviceController()).show();
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
