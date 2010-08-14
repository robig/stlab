package net.robig.stlab.gui;

import java.awt.BorderLayout;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;

import net.robig.gui.BlinkableLED;
import net.robig.gui.HoldableImageSwitch;
import net.robig.gui.ImageButton;
import net.robig.gui.ImagePanel;
import net.robig.gui.ImageSwitch;
import net.robig.gui.IntegerValueKnob;
import net.robig.gui.LED;
import net.robig.gui.TapButton;
import net.robig.gui.ThreeColorLED;
import net.robig.gui.ThreeWaySwitch;
import net.robig.gui.TransparentPanel;
import net.robig.logging.Logger;
import net.robig.stlab.model.StPreset;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;

public class DeviceFrame extends JFrame implements KeyListener{

	protected Logger log = new Logger(this.getClass());
	private StPreset currentPreset=new StPreset();
	private GuiDeviceController device=null;
	private Boolean receiving = false;
	long lastUpdate = 0;
	int maxChangesPerSecond=1;
	private boolean optionMode=false;
	
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private ImagePanel devicePanel = null;
	private JToolBar toolBar = null;
	private JMenuBar menu = null;
	private JMenu fileMenu = null;
	private JMenuItem optionsMenuItem = null;
	private JMenuItem exitMenuItem = null;
	private ImagePanel back = devicePanel;
	private JPanel optionPanel = null;
	
	//Controls:
	private IntegerValueKnob volumeKnob = new IntegerValueKnob();
	private IntegerValueKnob bassKnob = new IntegerValueKnob();
	private IntegerValueKnob middleKnob = new IntegerValueKnob();
	private IntegerValueKnob trebleKnob = new IntegerValueKnob();
	private IntegerValueKnob gainKnob = new IntegerValueKnob();
	private IntegerValueKnob ampKnob = new IntegerValueKnob();
	
	//Option Controls:
	private IntegerValueKnob cabinetKnob = new IntegerValueKnob();
	private IntegerValueKnob presenceKnob = new IntegerValueKnob();
	private IntegerValueKnob noiseReductionKnob = new IntegerValueKnob();
	
	
	private class LittleKnob extends IntegerValueKnob {
		@Override
		protected String getImageFile() {
			return "img/lknob.png";
		}
	}
	
	private class LongButton extends ImageButton {
		public LongButton() {
			imageFile="img/button_long.png";
//			setBorder(new LineBorder(new Color(0,0,255)));
			init();
		}
	}
	
	private class SmallButton extends ImageButton {
		public SmallButton() {
			imageFile="img/button.png";
//			setBorder(new LineBorder(new Color(0,0,255)));
			init();
		}
	}
	
	private class PresetSwitch extends ImageButton {
		public PresetSwitch(){
			//setBorder(new LineBorder(new Color(0,0,255)));
			init();
		}
	}
	
	private LittleKnob pedalKnob = new LittleKnob();
	private LittleKnob pedalEditKnob = new LittleKnob();
	private LittleKnob delayKnob = new LittleKnob();
	private LittleKnob delayEditKnob = new LittleKnob();
	private LittleKnob delayEdit2Knob = new LittleKnob();
	private LittleKnob reverbKnob = new LittleKnob();
	
	private PresetSwitch prevPreset = new PresetSwitch(){
		public void onClick() {
			device.prevPreset();
		};
	};
	private PresetSwitch nextPreset = new PresetSwitch(){
		public void onClick() {
			device.nextPreset();
		};
	};
	
	private ThreeColorLED ampModeLed = new ThreeColorLED();
	private ThreeWaySwitch ampTypeSwitch = new ThreeWaySwitch(ampModeLed);
	
	private BlinkableLED cabinetLed = new BlinkableLED();
	private HoldableImageSwitch cabinetOptionSwitch = new HoldableImageSwitch(cabinetLed){
		public void onClick() {
			if(isOptionMode())
				setOptionMode(false);
			else {
				updatePreset();
				sendPresetChange(true);
			}
		};
		protected void onHold() {
			setOptionMode(!isOptionMode());
		};
		protected void onUnHold() {
			setOptionMode(!isOptionMode());
		};
	};
	
	private LED pedalLed = new LED();
	private LED delayLed = new LED();
	private LED reverbLed = new LED(); 
	private ImageSwitch pedalSwitch = new ImageSwitch(pedalLed){
		public void onClick() {
			updatePreset();
			sendPresetChange(true);
		};
	};
	private ImageSwitch delaySwitch = new ImageSwitch(delayLed){
		public void onClick() {
			updatePreset();
			sendPresetChange(true);
		};
	};
	private ImageSwitch reverbSwitch = new ImageSwitch(reverbLed){
		public void onClick() {
			updatePreset();
			sendPresetChange(true);
		};
	};
	
	//TODO:
	private BlinkableLED tapLed = new BlinkableLED();
	private TapButton tapButton = new TapButton(){
		public void onClick() {
			super.onClick();
			int delay=(int) Math.floor(getMean(10));
			setTapDelay(delay);
			sendPresetChange(false);
		};
	}; 
	
	/**
	 * internal method that sets the delay time in tapLed
	 * @param delay
	 */
	private void setTapDelay(int delay){
		if(delay<=0) return;
		log.debug("setting Tap delay: "+delay+"ms");
		tapLed.setDelay(delay);
		tapLed.blink();
		tapLed.setToolTipText(tapLed.getName()+": "+delay+"ms");
		tapButton.setToolTipText(tapButton.getName()+": "+delay+"ms");
	}
	
	//Display:
	private DisplayPanel display = new DisplayPanel();
	private JTextArea output;

	/**
	 * This is the default constructor
	 */
	public DeviceFrame(IDeviceController ctrl) {
		super();
		device=new GuiDeviceController(ctrl,this);
		initialize();
		initDevice();
	}
	
	public void initDevice(){
		setCurrentPreset(device.initialize());
	}
	
	/**
	 * update the preset data and the correnspondign GUI elements
	 * @param preset
	 */
	public void setCurrentPreset(StPreset preset){
		synchronized (currentPreset) {
			currentPreset=preset;
			updateGui();
		}
	}
	
	/**
	 * is receiving mode enabled?
	 */
	private boolean isReceiving() {
		synchronized (receiving) {
			return receiving;
		}
	}
	
	/**
	 * switch to Receive-mode where no control changes were processed
	 * @param r
	 */
	private void setReceiving(boolean r){
		synchronized (receiving) {
			receiving=r;
		}
	}
	
	/**
	 * Returns if option Mode is enabled, where Cabinet, Presence and NR Knobs are visible
	 * @return
	 */
	public boolean isOptionMode() {
		return optionMode;
	}

	public void setOptionMode(boolean optionMode) {
		this.optionMode = optionMode;
		if(optionMode){
			gainKnob.setVisible(false);
			trebleKnob.setVisible(false);
			middleKnob.setVisible(false);
			delayEditKnob.setVisible(false);
			optionPanel.setVisible(true);
		}else{
			gainKnob.setVisible(true);
			trebleKnob.setVisible(true);
			middleKnob.setVisible(true);
			delayEditKnob.setVisible(true);
			optionPanel.setVisible(false);
		}
	}

	/** 
	 * Gui contolls have changed, update the preset for later submitting to the device
	 */
	public void updatePreset() {
		setReceiving(true);
		currentPreset.setAmp(ampKnob.getValue());
		currentPreset.setGain(gainKnob.getValue());
		currentPreset.setTreble(trebleKnob.getValue());
		currentPreset.setMiddle(middleKnob.getValue());
		currentPreset.setBass(bassKnob.getValue());
		currentPreset.setVolume(volumeKnob.getValue());
		currentPreset.setPedalEffect(pedalKnob.getValue());
		currentPreset.setPedalEdit(pedalEditKnob.getValue());
		currentPreset.setDelayEffect(delayKnob.getValue());
		currentPreset.setDelayDepth(delayEditKnob.getValue());
		currentPreset.setDelayFeedback(delayEdit2Knob.getValue());
		currentPreset.setDelaySpeed(tapLed.getDelay());
		currentPreset.setReverbEffect(reverbKnob.getValue());
		currentPreset.setAmpType(ampTypeSwitch.getState());
		currentPreset.setCabinetEnabled(cabinetOptionSwitch.isActive());
		currentPreset.setCabinet(cabinetKnob.getValue());
		currentPreset.setPresence(presenceKnob.getValue());
		currentPreset.setNoiseReduction(noiseReductionKnob.getValue());
		currentPreset.setDelayEnabled(delaySwitch.isActive());
		currentPreset.setPedalEnabled(pedalSwitch.isActive());
		currentPreset.setReverbEnabled(reverbSwitch.isActive());
		int rt=reverbKnob.getValue()/40;
		currentPreset.setReverbType(rt);
		currentPreset.setReverbEffect(reverbKnob.getValue()-rt*40);
		setReceiving(false);
	}
	
	public void updateGui(){
		setReceiving(true);
		log.debug("receiving GUI update");
		ampTypeSwitch.setState(currentPreset.getAmpType());
		ampKnob.setValue(currentPreset.getAmp());
		gainKnob.setValue(currentPreset.getGain());
		trebleKnob.setValue(currentPreset.getTreble());
		middleKnob.setValue(currentPreset.getMiddle());
		bassKnob.setValue(currentPreset.getBass());
		volumeKnob.setValue(currentPreset.getVolume());
		pedalKnob.setValue(currentPreset.getPedalEffect());
		pedalEditKnob.setValue(currentPreset.getPedalEdit());
		delayKnob.setValue(currentPreset.getDelayEffect());
		delayEditKnob.setValue(currentPreset.getDelayDepth());
		delayEdit2Knob.setValue(currentPreset.getDelayFeedback());
		setTapDelay(currentPreset.getDelaySpeed());
		reverbKnob.setValue(
				currentPreset.getReverbType()*40+
				currentPreset.getReverbEffect());
		
		cabinetOptionSwitch.setActive(currentPreset.isCabinetEnabled());
		cabinetKnob.setValue(currentPreset.getCabinet());
		presenceKnob.setValue(currentPreset.getPresence());
		noiseReductionKnob.setValue(currentPreset.getNoiseReduction());
		
		pedalSwitch.setActive(currentPreset.isPedalEnabled());
		delaySwitch.setActive(currentPreset.isDelayEnabled());
		reverbSwitch.setActive(currentPreset.isReverbEnabled());
		
		display.setValue(currentPreset.getNumber());
		setReceiving(false);
		log.debug("GUI updated.");
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
		pedalKnob.setBounds(new Rectangle(62,308,70,70));
		pedalEditKnob.setBounds(new Rectangle(157,308,70,70));
		delayKnob.setBounds(new Rectangle(274,308,70,70));
		delayEditKnob.setBounds(new Rectangle(370,308,70,70));
		delayEdit2Knob.setBounds(new Rectangle(370,308,70,70));
		reverbKnob.setBounds(new Rectangle(466,308,70,70));
		
		cabinetKnob.setBounds(new Rectangle(194, 165, 100, 100));
		presenceKnob.setBounds(new Rectangle(292, 165, 100, 100));
		noiseReductionKnob.setBounds(new Rectangle(390, 165, 100, 100));
		
		prevPreset.setBounds(new Rectangle(201,543,32,32));
		nextPreset.setBounds(new Rectangle(465,543,32,32));
		
		ampModeLed.setBounds(new Rectangle(120,136,12,12));
		ampTypeSwitch.setBounds(new Rectangle(89,135,24,12));
		cabinetOptionSwitch.setBounds(new Rectangle(175,135,24,12));
		pedalSwitch.setBounds(new Rectangle(51,405,24,12));
		delaySwitch.setBounds(new Rectangle(264,405,24,12));
		reverbSwitch.setBounds(new Rectangle(459,405,24,12));
		
		pedalLed.setBounds(new Rectangle(82,406,12,12));
		delayLed.setBounds(new Rectangle(294,406,12,12));
		reverbLed.setBounds(new Rectangle(489,406,12,12));
		cabinetLed.setBounds(new Rectangle(205,136,12,12));
		
		tapLed.setBounds(new Rectangle(384,373,12,12));
		tapButton.setBounds(new Rectangle(394,385,28,28));
		
		
		this.setJMenuBar(getMenu());
		this.setContentPane(getJContentPane());
		this.setSize(940, 691);
		this.setTitle("Tonelab Device");
		this.setName("StLab");
		getLogOutput();
		
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
		//TODO:
		reverbKnob.setMaxValue(40*3);
		reverbKnob.setName("Reverb");
		
		
		prevPreset.setName("Previous Preset");
		nextPreset.setName("Next Preset");
		
		ampModeLed.setName("AMP type");
		ampTypeSwitch.setName("Switch AMP Type");
		cabinetOptionSwitch.setName("Cabinet/Option");
		pedalSwitch.setName("Pedal effect");
		delaySwitch.setName("Delay");
		reverbSwitch.setName("Reverb");

		cabinetLed.setName("Cabinet/Option");
		pedalLed.setName("Pedal effect");
		delayLed.setName("Delay");
		reverbLed.setName("Reverb");
		tapLed.setName("Delay speed");
		tapButton.setName("Set delay speed by tapping.");
		
		cabinetKnob.setName("Cabinet");
		cabinetKnob.setMaxValue(11);
		presenceKnob.setName("Presence");
		noiseReductionKnob.setName("Noise reduction");
		
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
			delayEdit2Knob,
			reverbKnob,
			//option:
			cabinetKnob,
			presenceKnob,
			noiseReductionKnob
		}){
			k.addChangeListener(new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					if(isReceiving()) return;
					IntegerValueKnob knob = (IntegerValueKnob) e.getSource();
					log.debug("Knob changed: "+knob.getName()+" value="+knob.getValue());
					display.setValue(knob.getValue());
					updatePreset();
					sendPresetChange(!knob.isDragging());
				}
			});
		}
		
		// CLose Button of window:
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
		
		devicePanel.getInputMap().put(KeyStroke.getKeyStroke("F2"),
        	"doSomething");
		devicePanel.getActionMap().put("doSomething",
         new Action(){

			@Override
			public void addPropertyChangeListener(
					PropertyChangeListener listener) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Object getValue(String key) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isEnabled() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void putValue(String key, Object value) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void removePropertyChangeListener(
					PropertyChangeListener listener) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setEnabled(boolean b) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				log.error("actionPerformed!");
			}
			
		});
//where anAction is a javax.swing.Action
	}

	private void sendPresetChange(boolean exclusive) {
		long now=System.currentTimeMillis();
		if(exclusive || (now-lastUpdate) > 1000/maxChangesPerSecond){
			lastUpdate=now;
			device.activateParameters(currentPreset);
		}
	}
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if(jContentPane==null){
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getDevicePanel(), BorderLayout.CENTER);
			devicePanel.add(getToolBar(), BorderLayout.NORTH);
		}
		return jContentPane;
	}
	
	private JPanel getDevicePanel() {
		if (devicePanel == null) {
			devicePanel = new ImagePanel(ImagePanel.loadImage("img/TonelabST.png"));
			devicePanel.setSize(940, 671);
			// Controls:
			devicePanel.setLayout(null);
			
			devicePanel.add(ampKnob, null);
			devicePanel.add(volumeKnob, null);
			devicePanel.add(bassKnob, null);
			devicePanel.add(middleKnob, null);
			devicePanel.add(trebleKnob, null);
			devicePanel.add(gainKnob, null);
			devicePanel.add(display, null);
			devicePanel.add(pedalKnob, null);
			devicePanel.add(pedalEditKnob, null);
			devicePanel.add(delayKnob, null);
			devicePanel.add(delayEditKnob, null);
			devicePanel.add(reverbKnob, null);
			devicePanel.add(nextPreset, null);
			devicePanel.add(prevPreset, null);
			devicePanel.add(ampTypeSwitch, null);
			devicePanel.add(cabinetOptionSwitch, null);
			
			devicePanel.add(ampModeLed, null);
			devicePanel.add(cabinetLed, null);
			devicePanel.add(pedalLed, null);
			devicePanel.add(delayLed, null);
			devicePanel.add(reverbLed, null);
			devicePanel.add(pedalSwitch, null);
			devicePanel.add(delaySwitch, null);
			devicePanel.add(reverbSwitch, null);
			
			devicePanel.add(tapLed, null);
			devicePanel.add(tapButton, null);
			
			devicePanel.add(getOptionPanel(), null);
		}
		return devicePanel;
	}

	private JPanel getOptionPanel() {
		if(optionPanel==null){
			optionPanel=new TransparentPanel("img/TonelabSTopt.png");
			optionPanel.setLayout(null);
			optionPanel.setSize(940, 671);
			optionPanel.setBounds(new Rectangle(0,0,940,671));
			optionPanel.setOpaque(false);
			optionPanel.add(cabinetKnob, null);
			optionPanel.add(presenceKnob, null);
			optionPanel.add(noiseReductionKnob, null);
			optionPanel.add(delayEdit2Knob, null);
			optionPanel.setVisible(false);
		}
		return optionPanel;
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
	 * quit the application
	 */
	public void quit() {
		device.disconnect();
		System.exit(0);
	}
	
	/**
	 * This method initializes the menu	
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
	
	/**
	 * initialize info/error output panel
	 * @return
	 */
	private JTextArea getLogOutput() {
		if(output==null){
			output = new JTextArea();
		    output.setEditable(false);
		    output.setColumns(2);
		    output.setBackground(new Color(200,200,200));
		    output.setForeground(new Color(255,20,20));
		    output.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		    JScrollPane scroller = new JScrollPane();
		    scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		    scroller.getViewport().setView(output);
		    add(scroller,BorderLayout.SOUTH);
		}
		return output;
	}
	
	/**
	 * Output status information
	 * @param text
	 */
	public void output(String text){
		if(output==null) return;
		output.append(text+"\n");
		if(output.getLineCount()>3)
			try {
				output.setText(output.getText().substring(output.getLineStartOffset(output.getLineCount()-3)));
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
  		output.setCaretPosition(output.getText().length()-1);
	}
	
	/**
	 * for testing with dummy controller (without device)
	 * @param args
	 */
	public static void main(String[] args) {
		System.getProperties().setProperty("apple.laf.useScreenMenuBar", "true");
		System.getProperties().setProperty("com.apple.macos.useScreenMenuBar","true");
		//TODO:System.getProperties().setProperty("com.apple.mrj.application.apple.menu.about.name","StLab");
		new DeviceFrame(new DummyDeviceController()).show();
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		log.debug(e.toString());
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
