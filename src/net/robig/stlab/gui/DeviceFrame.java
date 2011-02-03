package net.robig.stlab.gui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.text.BadLocationException;
import net.robig.gui.BlinkableLED;
import net.robig.gui.HoldableImageSwitch;
import net.robig.gui.ImageButton;
import net.robig.gui.ImagePanel;
import net.robig.gui.ImageSwitch;
import net.robig.gui.IntToggleButton;
import net.robig.gui.IntegerValueKnob;
import net.robig.gui.LED;
import net.robig.gui.TapButton;
import net.robig.gui.ThreeColorLED;
import net.robig.gui.ThreeWaySwitch;
import net.robig.gui.TransparentPanel;
import net.robig.logging.Logger;
import net.robig.stlab.StLab;
import net.robig.stlab.StLabConfig;
import net.robig.stlab.gui.controls.AmpKnob;
import net.robig.stlab.gui.controls.SmallButton;
import net.robig.stlab.gui.events.ComponentAdapter;
import net.robig.stlab.gui.events.MouseAdapter;
import net.robig.stlab.gui.preferences.PreferencesFrame;
import net.robig.stlab.gui.web.WebControlFrame;
import net.robig.stlab.gui.web.WebVotesPanel;
import net.robig.stlab.model.StPreset;
import net.robig.stlab.model.WebPreset;
import net.robig.stlab.util.Browser;
import net.robig.stlab.util.config.BoolValue;
import net.robig.stlab.util.config.IntValue;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Main (device) window of the StLab application.
 * Displays the device with its buttons, leds and knobs. 
 * @author robig
 *
 */
public class DeviceFrame extends JFrameBase implements KeyListener{

	static private DeviceFrame instance=null;
	static public DeviceFrame getInctance() { return instance; }
	
	protected Logger log = new Logger(this.getClass());
	private WebPreset currentWebPreset=null;
	private int currentVote=-1;
	private StPreset currentPreset=new StPreset();
	private GuiDeviceController device=null;
	private FileManagementController fileController = new FileManagementController(this){
		public void onPresetSelect(StPreset preset, File file) {
			if(device!=null && isOpenMode()){
				loadPreset(preset, file.toString());
			}
		};
	};
	private JToggleButton togglePresetListButton=null;
	private JToggleButton toggleWebButton=null;
	private JButton toggleSaveButton=null;
	private JButton togglePreferencesButton=null;
	private JPanel buttonPanel=null;
	
	private Boolean receiving = false;
	private long lastUpdate = 0;
	private int maxChangesPerSecond=1;
	private boolean optionMode=false;
	private PresetListFrame presetListFrame=null;
	private PreferencesFrame preferenceFrame=null;
	private IntValue x=null;
	private IntValue y=null;
	
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private ImagePanel devicePanel = null;
	private JMenuBar menu = null;
	private JMenu fileMenu = null;
	private JMenu windowMenu = null;
	private JMenuItem optionsMenuItem = null;
	private JMenuItem exitMenuItem = null;
	private JMenuItem saveMenuItem = null;
	private JMenuItem loadMenuItem = null;
	private JMenuItem presetListWindowMenuItem = null;
	private JPanel optionPanel = null;
	private JLabel bottomLabel = null;
	
	//Controls:
	private IntegerValueKnob volumeKnob = new IntegerValueKnob();
	private IntegerValueKnob bassKnob = new IntegerValueKnob();
	private IntegerValueKnob middleKnob = new IntegerValueKnob();
	private IntegerValueKnob trebleKnob = new IntegerValueKnob();
	private IntegerValueKnob gainKnob = new IntegerValueKnob();
	private IntegerValueKnob ampKnob = new AmpKnob();
	
	//Option Controls:
	private IntegerValueKnob cabinetKnob = new IntegerValueKnob(){
		private static final long serialVersionUID = 1L;
		public void onChange() {
			if(!isReceiving()) cabinetOptionSwitch.setActive(true);
		};
		//FIXME dont reimplement naming here!
		String [] cabNames={"TWEED 1x8","TWEED 1x12","TWEED 4x10", "BLACK 2x10","BLACK 2x12","VOX AC15", "VOX AC30","VOX AD120VTX","UK H30 4x12", "UK T75 4x12","US V30 4x12" };
		public String getDisplayedTextValue() {
			return "("+getDisplayedValue()+") "+cabNames[getValue()];
		};
	};
	private IntegerValueKnob presenceKnob = new IntegerValueKnob();
	private IntegerValueKnob noiseReductionKnob = new IntegerValueKnob();
	
	
	private class LittleKnob extends IntegerValueKnob {
		private static final long serialVersionUID = 1L;
		@Override
		protected String getImageFile() {
			return "img/lknob.png";
		}
	}
	
	/**
	 * Internal Class for Updating the display to show the current preset number
	 * after 2 seconds when a knob has changed.
	 * @author robig
	 */
	private class PresetNumberUpdater implements ActionListener{
		Timer timer=new Timer(2000,this);
		public PresetNumberUpdater() {
			timer.setRepeats(false);
		}
		
		public synchronized void start(){
			if(timer.isRunning())timer.stop();
			timer.start();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			display.setNormalMode();
			display.setValue(currentPreset.getNumber());
		}
	}
	
	private PresetNumberUpdater presetNumberUpdater = new PresetNumberUpdater();
	
	/**
	 * Internal class for the Reverb knob that has a range of 3x40
	 * @author robig
	 */
	private class ReverbKnob extends LittleKnob {
		private static final long serialVersionUID = 1L;
		public int getDisplayedValue(){
			//reverb has 3*40 range, so its display like that:
			return getValue()-(getValue()/40)*40;
		}
	}
	
	/** The Knob for pedal effect */
	private LittleKnob pedalKnob = new LittleKnob(){
		private static final long serialVersionUID = 1L;
		public void onChange() {
			if(!isReceiving())pedalSwitch.setActive(true);
		};
		String [] pedalNames={"COMP","ACOUSTIC","VOX WAH","U-VIBE","OCTAVE","TREBLE BOOST","TUBE OD","BOUTIQUE","ORANGE DIST","METAL DIST","FUZZ"};
		public String getDisplayedTextValue() {
			return "("+getDisplayedValue()+") "+pedalNames[getValue()];
		};
	};
	private LittleKnob pedalEditKnob = new LittleKnob();
	private LittleKnob delayKnob = new LittleKnob(){
		private static final long serialVersionUID = 1L;
		public void onChange() {
			if(!isReceiving()) delaySwitch.setActive(true);
		};
		String [] delayNames={"CLASSIC CHORUS","MULTITAP CHORUS","CLASSIC FLANGER","PHASER","TEXTREM","ROTARY","PITCH SHIFTER","FILTRON","ECHO PLUS","DELAY","CHORUS+DELAY"};
		public String getDisplayedTextValue() {
			return "("+getDisplayedValue()+") "+delayNames[getValue()];
		};
	};
	private LittleKnob delayEditKnob = new LittleKnob();
	private LittleKnob delayEdit2Knob = new LittleKnob();
	private ReverbKnob reverbKnob = new ReverbKnob(){
		private static final long serialVersionUID = 1L;
		public void onChange() {
			if(!isReceiving()) reverbSwitch.setActive(true);
		};
		public String getDisplayedTextValue() {
			String names[] = {"SPRING","ROOM","HALL"};
			return names[getValue()/40]+" "+getDisplayedValue();
		};
	};
	
	private ImageButton prevPreset = new ImageButton(){
		private static final long serialVersionUID = 1L;
		public void onClick() {
			device.selectPreset(currentPreset.getNumber()-1);
			onSave();
		};
	};
	private ImageButton nextPreset = new ImageButton(){
		private static final long serialVersionUID = 1L;
		public void onClick() {
			device.selectPreset(currentPreset.getNumber()+1);
			onSave();
		};
	};
	
	private ThreeColorLED ampModeLed = new ThreeColorLED();
	private ThreeWaySwitch ampTypeSwitch = new ThreeWaySwitch(ampModeLed){
		private static final long serialVersionUID = 1L;
		public void onClick() {
			super.onClick();
			updatePreset();
			sendPresetChange(true);
		};
	};
	
	private BlinkableLED cabinetLed = new BlinkableLED();
	private HoldableImageSwitch cabinetOptionSwitch = new HoldableImageSwitch(cabinetLed){
		private static final long serialVersionUID = 1L;
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
		private static final long serialVersionUID = 1L;
		public void onClick() {
			updatePreset();
			sendPresetChange(true);
		};
	};
	private ImageSwitch delaySwitch = new ImageSwitch(delayLed){
		private static final long serialVersionUID = 1L;
		public void onClick() {
			updatePreset();
			sendPresetChange(true);
			if(!currentPreset.isTapLedUsed()){
				tapLed.deActivate();
			}
		};
	};
	private ImageSwitch reverbSwitch = new ImageSwitch(reverbLed){
		private static final long serialVersionUID = 1L;
		public void onClick() {
			updatePreset();
			sendPresetChange(true);
		};
	};
	
	private BlinkableLED tapLed = new BlinkableLED();
	private TapButton tapButton = new TapButton(){
		private static final long serialVersionUID = 1L;
		public void onLeftClick() {
			super.onLeftClick();
			int delay=(int) Math.floor(getMean(3));
//			log.debug("Setting tapped delay speed: "+delay);
//			tapLed.setDelay(delay);
			setTapDelay(delay);
			sendPresetChange(false);
		};
		public void onRightClick() {
			if(currentPreset.delayIsFiltron()||currentPreset.delayIsPitch()) return;
			final JDialog d=new JDialog();
			d.setBackground(PresetListFrame.BACKGROUND);
			d.setForeground(PresetListFrame.FOREGROUND);
			d.setBounds(clickEvent.getXOnScreen(), clickEvent.getYOnScreen(), 250, 100);
			d.setLayout(new BorderLayout());
			d.setModal(true);
			JTextField t=new JTextField();
			JLabel l1=new JLabel("Enter tapping speed:");
			l1.setForeground(PresetListFrame.FOREGROUND);
			d.add(l1,BorderLayout.NORTH);
			d.add(t,BorderLayout.CENTER);
			t.setText(""+(currentPreset.delayIsFrequency()?currentPreset.getDelayFrequency():currentPreset.getDelaySpeed()));
			JButton b=new JButton("Set");
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					d.setVisible(false);
					d.dispose();
				}
			});
			JLabel l2=new JLabel(currentPreset.delayIsFrequency()?"Hz":"ms");
			l2.setForeground(PresetListFrame.FOREGROUND);
			d.add(l2,BorderLayout.EAST);
			d.add(b,BorderLayout.SOUTH);
//			d.setSize(250,100);
			d.setVisible(true);
			try {
				if(currentPreset.delayIsFrequency()){
					float value=Float.parseFloat(t.getText());
					log.debug("Entered value: "+value);
					int v=(int) (1000/value);
					setTapDelay(v);
				}else{
					int v=Integer.parseInt(t.getText());
					log.debug("Entered value: "+v);
					setTapDelay(v);
				}
				
				sendPresetChange(false);
			}catch(NumberFormatException ex){
				log.error("Entered invalid number");
			}
		}
	};
	
	private IntToggleButton pitchToggleButton = new IntToggleButton(){
		private static final long serialVersionUID = 1L;
		public void onToggle() {
			display.setPitchMode();
			display.setValue(getValue());
			currentPreset.setDelaySpeed(getValue());
			sendPresetChange(true);
			presetNumberUpdater.start();
		};
	};
	private IntToggleButton filtronToggleButton = new IntToggleButton(){
		private static final long serialVersionUID = 1L;
		public void onToggle() {
			display.setFiltronMode();
			display.setValue(getValue());
			currentPreset.setDelaySpeed(getValue());
			sendPresetChange(true);
			presetNumberUpdater.start();
		};
	};
	
	private SmallButton writeButton = new SmallButton(){
		private static final long serialVersionUID = 1L;
		public void onClick() {
			DisplayPanel.IValueCallback callback=new DisplayPanel.IValueCallback() {
				@Override
				public void callback(int value) {
					log.debug("Write current preset to num "+value);
					device.savePreset(currentPreset, value);
					output("Preset written to #"+value);
					onSave();
				}
			};
			if(!display.isEnterValueModeEnabled()){
				display.setValue(currentPreset.getNumber());
				output("Enter preset Number to write");
				display.enterValue(callback);
			}else{
				display.abort();
				callback.callback(display.getTextFieldValue());
			}
		};
	};
	
	//Display:
	private DisplayPanel display = new DisplayPanel(){
		private static final long serialVersionUID = 1L;
		DisplayPanel.IValueCallback valueChangeCallback=new DisplayPanel.IValueCallback() {
			@Override
			public void callback(int value) {
				log.debug("Display Value changed to "+value);
				device.selectPreset(getValue());					
			}
		};
		public void onClick() {
			display.enterValue(valueChangeCallback);
		};
	};
	private JTextArea output;
	private JPanel topWebPanel;
	private Star webStar1Label;
	private JLabel webStar3Label;
	private JLabel webStar2Label;
	private JLabel webStar4Label;
	private JLabel webStar5Label;
	private JLabel webDescriptionLabel;
	private JLabel webStar1grayLabel;
	private JLabel webStar2grayLabel;
	private JLabel webStar3grayLabel;
	private JLabel webStar4grayLabel;
	private JLabel webStar5grayLabel;
	private JPanel webVotePanel;
	private JPanel webDetailsPanel;
	private JTextArea webVoteMessageTextField;
	private JPanel buttonPanel2;
	private WebVotesPanel webVotesPanel;
	
	
	@Override
	public void setTitle(String arg0) {
		super.setTitle(getName()+" "+(arg0.length()>0?" - "+arg0:""));
	}
	
	/**
	 * internal method that sets the delay time in tapLed
	 * @param delay
	 */
	private void setTapDelay(int delay){
		if(delay<=0) return;
		//FIXME hack
		currentPreset.setDelaySpeed(delay);
		if(!currentPreset.isTapLedUsed()){
			tapLed.deActivate();
			return;
		}
		String textValue=currentPreset.getDelaySpeedString();
		log.debug("setting Tap delay: "+textValue);
		tapLed.setDelay(delay);
		tapLed.blink();
		tapLed.setToolTipText(tapLed.getName()+": "+textValue);
		tapButton.setToolTipText(tapButton.getName()+": "+textValue);
	}
	
	private void setModDelayEffect(StPreset preset){
		delayKnob.setValue(preset.getDelayEffect());
		if(preset.delayIsPitch()){
			tapButton.setVisible(false);
			pitchToggleButton.setVisible(true);
			filtronToggleButton.setVisible(false);
		}else if(preset.delayIsFiltron()){
			tapButton.setVisible(false);
			pitchToggleButton.setVisible(false);
			filtronToggleButton.setVisible(true);
		}else{
			tapButton.setVisible(true);
			pitchToggleButton.setVisible(false);
			filtronToggleButton.setVisible(false);
		}
	}

	/**
	 * This is the default constructor
	 */
	public DeviceFrame(IDeviceController ctrl) {
		super();
		instance=this;
		device=new GuiDeviceController(ctrl,this);
		initialize();
		//initDevice(); is called from StLab class
		registerForMacOSXEvents();
	}
	
	/**
	 * Intitializes the devices and gets current preset.
	 */
	public void initDevice(){
		StPreset p=device.initialize();
		presetListFrame.initializeData();
		presetListFrame.setSelectionIndex(p.getNumber());
		setCurrentPreset(p);
		display.setMax(device.getDeviceInfo().numPresets);
		setPresetListVisible(StLabConfig.getPresetListWindowVisible().getValue());
	}
	
	/**
	 * update the preset data and the correnspondign GUI elements
	 * @param preset
	 */
	public synchronized void setCurrentPreset(StPreset preset){
		if(preset==null){
			log.error("Got null preset!");
			return;
		}
		getTopWebPanel().setVisible(false);
		showLocalButtons();
		currentWebPreset=null;
		this.setTitle("#"+preset.getNumber()+" "+preset.getTitle());
		currentPreset=preset;
		updateGui();
	}
	
	/**
	 * sets current displayed preset, but only when already displayed (update)
	 * @see setCurrentPreset
	 * @param p
	 */
	public synchronized void updateIfCurrentPreset(StPreset p){
		if(p == currentPreset || p.getNumber() == currentPreset.getNumber() ){
			setCurrentPreset(p);
		}
	}
	
	/**
	 * Is receiving mode enabled?
	 * While receiving (data from device) no changes where sent to the device. 
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

	/**
	 * Sets the option mode on or off. If the option mode is enabled the Cabinet, Presence and NR Knobs are visible.
	 * @param optionMode
	 */
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
	 * GUI controls have changed, update the preset for later submitting to the device
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
		setModDelayEffect(currentPreset);
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
		onChange();
	}
	
	/**
	 * The device submitted an update, so we have to update the GUI elements from the current preset.
	 */
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
		setModDelayEffect(currentPreset);
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
		//has its own listener now: presetListFrame.setSelectionIndex(currentPreset.getNumber());
		currentPreset.setTitle(presetListFrame.getPresetName(currentPreset.getNumber()));
		setReceiving(false);
		log.debug("GUI updated.");
	}

	/**
	 * This method initializes this Window
	 * 
	 * @return void
	 */
	private void initialize() {
		initializeUIDefaults();
		
		presetListFrame=new PresetListFrame(this);
		preferenceFrame=new PreferencesFrame();
		
		int oy=9; // y offset
		ampKnob.setBounds(new Rectangle(64, 165-oy, 100, 100));
		gainKnob.setBounds(new Rectangle(194, 165-oy, 100, 100));
		trebleKnob.setBounds(new Rectangle(292, 165-oy, 100, 100));
		middleKnob.setBounds(new Rectangle(390, 165-oy, 100, 100));
		bassKnob.setBounds(new Rectangle(489, 165-oy, 100, 100));
		volumeKnob.setBounds(new Rectangle(587, 165-oy, 100, 100));
		display.setBounds(new Rectangle(588,275-oy,29*2,49));
		pedalKnob.setBounds(new Rectangle(62,308-oy,70,70));
		pedalEditKnob.setBounds(new Rectangle(157,308-oy,70,70));
		delayKnob.setBounds(new Rectangle(274,308-oy,70,70));
		delayEditKnob.setBounds(new Rectangle(370,308-oy,70,70));
		delayEdit2Knob.setBounds(new Rectangle(370,308-oy,70,70));
		reverbKnob.setBounds(new Rectangle(466,308-oy,70,70));
		
		cabinetKnob.setBounds(new Rectangle(194, 165-oy, 100, 100));
		presenceKnob.setBounds(new Rectangle(292, 165-oy, 100, 100));
		noiseReductionKnob.setBounds(new Rectangle(390, 165-oy, 100, 100));
		
		prevPreset.setBounds(new Rectangle(199,542-oy,32,32));
		nextPreset.setBounds(new Rectangle(463,542-oy,32,32));
		
		ampModeLed.setBounds(new Rectangle(119,136-oy,12,12));
		ampTypeSwitch.setBounds(new Rectangle(88,135-oy,24,12));
		cabinetOptionSwitch.setBounds(new Rectangle(175,135-oy,24,12));
		pedalSwitch.setBounds(new Rectangle(51,405-oy,24,12));
		delaySwitch.setBounds(new Rectangle(264,405-oy,24,12));
		reverbSwitch.setBounds(new Rectangle(459,405-oy,24,12));
		
		pedalLed.setBounds(new Rectangle(81,406-oy,12,12));
		delayLed.setBounds(new Rectangle(294,406-oy,12,12));
		reverbLed.setBounds(new Rectangle(489,406-oy,12,12));
		cabinetLed.setBounds(new Rectangle(205,136-oy,12,12));
		
		tapLed.setBounds(new Rectangle(383,374-oy,12,12));
		tapButton.setBounds(new Rectangle(394,385-oy,28,28));
		
		pitchToggleButton.setBounds(new Rectangle(394,385-oy,28,28));
		pitchToggleButton.setVisible(false);
		pitchToggleButton.setPossibleValues(new int[] {0,5,7,13,18,20,25});
		pitchToggleButton.setDisplayedValues(new String[] {"-12","-7","-5","DT","5","7","12"});
		
		filtronToggleButton.setBounds(new Rectangle(394,385-oy,28,28));
		filtronToggleButton.setVisible(false);
		filtronToggleButton.setDisplayedValues(new String[] {"Up","Down"});
		
		writeButton.setBounds(new Rectangle(567,386-oy,28,28));
		
		x=StLabConfig.getLiveWindowX();
		y=StLabConfig.getLiveWindowY();
		this.setJMenuBar(getMenu());
		this.setContentPane(getJContentPane());
		int height=705;
		if(MAC_OS_X) height-=24; //Menu bar is at screen top
		this.setBounds(x.getValue(), y.getValue(), 940, height);
		
		this.setName(StLab.applicationName+" Live");
		this.setTitle("");
		
		volumeKnob.setName("Volume");
		bassKnob.setName("Bass");
		middleKnob.setName("Middle");
		trebleKnob.setName("Treble");
		gainKnob.setName("Gain");
		ampKnob.setName("AMP");
		ampKnob.setMaxValue(10);
		
		pedalKnob.setName("Pedal Effect");
		pedalEditKnob.setName("Pedal Edit");
		pedalKnob.setMaxValue(10);
		delayKnob.setName("Mod/Delay Effect");
		delayKnob.setMaxValue(10);
		delayEditKnob.setName("Mod/Delay Depth");
		delayEdit2Knob.setName("Mod/Delay Feedback");
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
		tapButton.setName("Set delay speed by tapping, right click to enter speed value");
		
		pitchToggleButton.setName("Select pitch");
		filtronToggleButton.setName("Select filtron type");
		
		cabinetKnob.setName("Cabinet");
		cabinetKnob.setMaxValue(10);
		presenceKnob.setName("Presence");
		noiseReductionKnob.setName("Noise reduction");
		
		display.setToolTipText("Click to enter preset number to switch to.");
		writeButton.setName("Write preset");
		
		
		
		initListeners();
	}
	
	/** Init control listeners: */
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
					//log.debug("Knob changed: "+knob.getName()+" value="+knob.getValue());
					display.setNormalMode();
					display.setValue(knob.getDisplayedValue());
					updatePreset();
					sendPresetChange(!knob.isDragging());
					presetNumberUpdater.start();
				}
			});
		}
		
		// Close Button of window:
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
		
		// remenber window position:
		addComponentListener(new ComponentAdapter(){
			@Override
			public void componentMoved(ComponentEvent e) {
				x.setValue(getX());
				y.setValue(getY());
			}
		});
		
		// listen for key events:
		addKeyListener(this);
		setFocusable(true);
		
		togglePresetListButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				setPresetListVisible(!isPresetListVisible());
				requestFocus();
			}
		});
				
		
		toggleWebButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				WebControlFrame.getInstance().setVisible(!WebControlFrame.getInstance().isVisible());
				requestFocus();
			}
		});
		
		toggleSaveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fileController.openSavePresetDialog(currentPreset.clone());
				requestFocus();
			}
		});
		
		togglePreferencesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				preferences();
				requestFocus();
			}
		});
	}
	
//	public void loadPreset(StPreset preset){
//		setCurrentPreset(preset);
//		sendPresetChange(true);
//		onSave();
//	}

	/** Caches changes.
	 * Makes sure we dont send too much midi commands ;)
	 * @param exclusive: no caching when exclusive
	 */
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
			jContentPane.add(getLogOutput(),BorderLayout.SOUTH);
		}
		return jContentPane;
	}
	
	/**
	 * initializes the devicePanel
	 * @return
	 */
	private JPanel getDevicePanel() {
		if (devicePanel == null) {
			devicePanel = new ImagePanel("img/TonelabST.png");
			devicePanel.setSize(940, 621);
			devicePanel.setLayout(null);
			// Controls:			
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
			devicePanel.add(pitchToggleButton, null);
			devicePanel.add(filtronToggleButton, null);
			
			devicePanel.add(writeButton, null);
			
			devicePanel.add(getOptionPanel(), null);
			devicePanel.add(getBottomLabel(), null);
			
			devicePanel.add(getButtonPanel2(), null);
			devicePanel.add(getTopWebPanel(), null);
			devicePanel.add(getButtonPanel(), null);
		}
		return devicePanel;
	}
	
	/**
	 * internal class for drawing stars for voting 
	 * @author robig
	 */
	private class Star extends JLabel {
		private static final long serialVersionUID = 1L;
		float i=0;
		public Star(float v) {
			super();
			this.i=v;
			addMouseListener(new java.awt.event.MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent arg0) {
					if(!isWebPreset()) return;
					if(!WebControlFrame.getInstance().isLoggedin()){
						log.error("You need to Login to vote!");
						WebControlFrame.getInstance().showLogin();
						return;
					}
					if(currentWebPreset.hasAlreadyVoted()){
						log.error("Already voted for preset "+currentWebPreset.getTitle());
						onAlreadyVoted();
						return;
					}
					getWebDetailsPanel().setVisible(false);
					getTopWebPanel().add(getWebVotePanel(),BorderLayout.CENTER);
					getTopWebPanel().revalidate();
					webVoteMessageTextField.requestFocus();
				}
			});
			addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					if(!isWebPreset()) return;
					if(!WebControlFrame.getInstance().isLoggedin()) return;
					if(currentWebPreset.hasAlreadyVoted()) return;
					showRating(i);
				}
			});
			setToolTipText("Vote "+v);
		}
	}
	
	protected void onAlreadyVoted() {
		JOptionPane.showMessageDialog(this, "You can only vote once!","Already voted this preset", JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * initializes/gets top vote/web panel
	 * @return
	 */
	private JPanel getTopWebPanel() {
		if(topWebPanel==null){
			topWebPanel=new JPanel();
			topWebPanel.setVisible(false);
			topWebPanel.setBounds(690,0,240,600);
			topWebPanel.setBackground(StLab.BACKGROUND);
			Border b=BorderFactory.createLineBorder(StLab.GRID,2);
			topWebPanel.setBorder(new CompoundBorder(b,new EmptyBorder(10,10,10,10)));
			topWebPanel.setLayout(new BorderLayout());
			JPanel starsPanel=new JPanel();
			starsPanel.setLayout(new FlowLayout());
			
			
			webStar1Label = new Star(1);
			webStar1Label.setIcon(ImagePanel.loadImageIcon("img/star32.png"));
			starsPanel.add(webStar1Label);
			
			webStar1grayLabel = new Star(1);
			webStar1grayLabel.setIcon(ImagePanel.loadImageIcon("img/star32gray.png"));
			webStar1grayLabel.setVisible(false);
			starsPanel.add(webStar1grayLabel);
			
			webStar2Label = new Star(2);
			webStar2Label.setIcon(ImagePanel.loadImageIcon("img/star32.png"));
			starsPanel.add(webStar2Label);
			
			webStar2grayLabel = new Star(2);
			webStar2grayLabel.setIcon(ImagePanel.loadImageIcon("img/star32gray.png"));
			webStar2grayLabel.setVisible(false);
			starsPanel.add(webStar2grayLabel);
			
			webStar3Label = new Star(3);
			webStar3Label.setIcon(ImagePanel.loadImageIcon("img/star32.png"));
			starsPanel.add(webStar3Label);
			
			webStar3grayLabel = new Star(3);
			webStar3grayLabel.setIcon(ImagePanel.loadImageIcon("img/star32gray.png"));
			webStar3grayLabel.setVisible(false);
			starsPanel.add(webStar3grayLabel);
			
			webStar4Label = new Star(4);
			webStar4Label.setIcon(ImagePanel.loadImageIcon("img/star32.png"));
			starsPanel.add(webStar4Label);
			
			webStar4grayLabel = new Star(4);
			webStar4grayLabel.setIcon(ImagePanel.loadImageIcon("img/star32gray.png"));
			webStar4grayLabel.setVisible(false);
			starsPanel.add(webStar4grayLabel);
			
			webStar5Label = new Star(5);
			webStar5Label.setIcon(ImagePanel.loadImageIcon("img/star32.png"));
			starsPanel.add(webStar5Label);
			
			webStar5grayLabel = new Star(5);
			webStar5grayLabel.setIcon(ImagePanel.loadImageIcon("img/star32gray.png"));
			webStar5grayLabel.setVisible(false);
			starsPanel.add(webStar5grayLabel);
			
			topWebPanel.add(starsPanel,BorderLayout.NORTH);
			topWebPanel.add(getWebDetailsPanel(), BorderLayout.CENTER);
			//also initialize:
			getWebVotePanel();
		}
		return topWebPanel;
	}
	
	private JPanel getWebDetailsPanel(){
		if(webDetailsPanel==null){
			webDetailsPanel = new JPanel();
			webDetailsPanel.setLayout(new BorderLayout());
			webDescriptionLabel=new JLabel("description");
			webDetailsPanel.add(webDescriptionLabel, BorderLayout.NORTH);
			webDetailsPanel.add(getWebVotesPanel(), BorderLayout.CENTER);
		}
		return webDetailsPanel;
	}
	
	private WebVotesPanel getWebVotesPanel(){
		if(webVotesPanel==null){
			webVotesPanel=new WebVotesPanel();
		}
		return webVotesPanel;
	}
	
	private JPanel getWebVotePanel(){
		if(webVotePanel==null){
			webVotePanel=new JPanel();
			webVoteMessageTextField = new JTextArea();
			webVoteMessageTextField.setLayout(new GridBagLayout());
			webVoteMessageTextField.setRows(4);
			webVoteMessageTextField.setColumns(17);
			JLabel messageLabel=new JLabel("Comment:");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.anchor=GridBagConstraints.LINE_START;
			webVotePanel.add(messageLabel,gridBagConstraints2);
			
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			webVotePanel.add(webVoteMessageTextField,gridBagConstraints1);
			
			JButton doVoteButton=new JButton("Vote this preset");
			doVoteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onWebDoVote();
				}
			});
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.anchor=GridBagConstraints.LINE_END;
			webVotePanel.add(doVoteButton,gridBagConstraints3);
			
			JButton cancelButton=new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					onWebVoteCancel();
				}
			});
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 1;
			webVotePanel.add(cancelButton,gridBagConstraints4);
		}
		return webVotePanel;
	}
	
	protected void onWebDoVote(){
		if(currentWebPreset==null) return;
		if(currentVote<0){
			JOptionPane.showMessageDialog(this, "Please set vote value using the stars.","Fail", JOptionPane.WARNING_MESSAGE);
			return;
		}
		String comment=webVoteMessageTextField.getText().trim();
		boolean ok=WebControlFrame.getInstance().vote(currentWebPreset, comment, currentVote);
		if(ok) {
			currentWebPreset.setAlreadyVoted(true);
			onWebVoteCancel();
		}
		else 
			JOptionPane.showMessageDialog(this, "Sending Vote failed!","Fail", JOptionPane.WARNING_MESSAGE);
	}
	
	protected void onWebVoteCancel(){
		if(webVoteMessageTextField!=null)
			webVoteMessageTextField.setText("");
		webDescriptionLabel.setText(currentWebPreset.toTopPanelHtml(WebControlFrame.getInstance().isLoggedin()));
		getTopWebPanel().remove(getWebVotePanel());
		getTopWebPanel().add(getWebDetailsPanel(),BorderLayout.CENTER);
		getTopWebPanel().revalidate();
		currentVote=-1;
	}
	
	/**
	 * initializes the bottom Label
	 * @return
	 */
	private JLabel getBottomLabel() {
		if(bottomLabel==null){
			bottomLabel=new JLabel("Press ALT to switch option mode, LEFT and RIGHT keys will change presets,"+
					" SPACE to show/hide preset list or ENTER to enter preset number.");
//			bottomLabel.setOpaque(false);
			bottomLabel.setForeground(new Color(204,173,93));
			bottomLabel.setBounds(new Rectangle(15,590,890,20));
			bottomLabel.setVisible(true);
		}
		return bottomLabel;
	}
	
	/**
	 * gets the Panel with the Knobs displayed in Option mode
	 */
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
			
			saveMenuItem = new JMenuItem("Save Preset");
			saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, MENU_MASK));
			fileMenu.add(saveMenuItem);
			saveMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					fileController.openSavePresetDialog(currentPreset.clone());
				}
			});
			
			loadMenuItem = new JMenuItem("Open Preset");
			fileMenu.add(loadMenuItem);
			loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, MENU_MASK));
			loadMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					StPreset preset=fileController.openLoadPresetDialog();
					if(preset!=null){
						loadPreset(preset,fileController.getPresetFile().toString());
					}
				}
			});
			
			fileMenu.addSeparator();
			
			optionsMenuItem = new JMenuItem("Preferences");
			optionsMenuItem.setMnemonic(KeyEvent.VK_P);
			optionsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, MENU_MASK));
			fileMenu.add(optionsMenuItem);
			
			JMenuItem aboutItem = new JMenuItem("About");
			fileMenu.add(aboutItem);
			
			exitMenuItem = new JMenuItem("Exit");
			exitMenuItem.setMnemonic(KeyEvent.VK_W);
			exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, MENU_MASK));
			fileMenu.add(exitMenuItem);

			optionsMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					preferences();
				}
			});
			
			aboutItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					about();
				}
			});
			
			exitMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					quit();
				}
			});
			
			windowMenu=new JMenu("Window");
			menu.add(windowMenu);
			presetListWindowMenuItem=new JCheckBoxMenuItem("Preset List");
//			presetListWindowMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
			windowMenu.add(presetListWindowMenuItem);
			
			presetListWindowMenuItem.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					setPresetListVisible(presetListWindowMenuItem.isSelected());
				}
			});
			
			JMenu feedbackMenu = new JMenu("Feedback");
			menu.add(feedbackMenu);
			JMenuItem forumItem=new JMenuItem("Please give Feedback in the Forum");
			feedbackMenu.add(forumItem);
			forumItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Browser.getInstance().browse(StLabConfig.getFeedbackUrl());
				}
			});
			
			JMenuItem webItem=new JMenuItem("StLab Web");
			windowMenu.add(webItem);
			webItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					WebControlFrame.getInstance().setVisible(true);
				}
			});
		}
		return menu;
	}
	
	/** returns if preset list is visible */
	public boolean isPresetListVisible() {
		return presetListFrame.isVisible();
	}
	
	/** set preset list visibility */
	public void setPresetListVisible(boolean v) {
		final BoolValue value=StLabConfig.getPresetListWindowVisible();
		if(value.getValue() != v) value.setValue(v);
		presetListFrame.setVisible(v);
		presetListWindowMenuItem.setSelected(v);
		togglePresetListButton.setSelected(v);
		if(v){
			presetListWindowMenuItem.setText("Hide "+presetListFrame.getName());
		}else{
			presetListWindowMenuItem.setText("Show "+presetListFrame.getName());
		}
	}
	
	/**
	 * initialize info/error output panel
	 * @return
	 */
	private Component getLogOutput() {
		if(output==null){
			output = new JTextArea();
			output.setFocusable(false);
		    output.setEditable(false);
		    output.setColumns(2);
		    output.setText("\n\n");
		    output.setBackground(new Color(200,200,200));
		    output.setForeground(new Color(255,20,20));
		    output.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		    JScrollPane scroller = new JScrollPane();
		    scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		    scroller.getViewport().setView(output);
		    return scroller;
		}
		return output;
	}
	
	/**
	 * Output status information
	 * @param text
	 */
	public void output(String text){
		output(text,Color.BLACK);
	}
	
	/** Output error information in red */
	public void outputError(String text){
		output(text,Color.RED);
	}
	
	/** Output information */ 
	public void output(String text, Color color){
		if(output==null) return;
		output.setForeground(color);
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
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		System.getProperties().setProperty("apple.laf.useScreenMenuBar", "true");
		System.getProperties().setProperty("com.apple.macos.useScreenMenuBar","true");
		net.robig.stlab.util.Config.setConfigFile("testconfig.properties");
		//TODO:System.getProperties().setProperty("com.apple.mrj.application.apple.menu.about.name","StLab");
		DeviceFrame d=new DeviceFrame(new DummyDeviceController());
		d.initDevice();
		d.show();
		d.registerWebControlListeners();
	}

	/**
	 * Listener method for key press in the application (window)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ALT){
			setOptionMode(true);
		}else if(e.getKeyCode()==KeyEvent.VK_LEFT){
			prevPreset.onClick();
		}else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
			nextPreset.onClick();
		}else if(e.getKeyCode()==KeyEvent.VK_ENTER){
			display.onClick(); //Enter preset number
// disabled in menu item:
		}else if(e.getKeyCode()==KeyEvent.VK_SPACE && StLabConfig.isSpaceSwitchesPresetListEnabled().getSimpleValue()){
			setPresetListVisible(!isPresetListVisible());
		}else if(e.getKeyCode()==KeyEvent.VK_TAB){
			if(isPresetListVisible()){
				presetListFrame.toFront();
				presetListFrame.requestFocus();
			}else
				setPresetListVisible(true);
		}else if(e.getKeyCode()==0){
			log.warn("Unknown Keycode 0x00");
			display.abort();
		}else if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
			display.abort();
		}else
			log.debug("keyPressed"+e.toString());
	}

	/**
	 * Listener method for key releases in the application (window)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ALT){
			setOptionMode(false);
		}else
			log.debug("keyTyped"+e.toString());
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	/** Open AboutDialog */
	@Override
	void about() {
		new AboutDlg().setVisible(true);
	}

	/** Open prferences dialog */
	@Override
	void preferences() {
		preferenceFrame.setVisible(true);
	}

	/** get the Device controller for device operations */
	public GuiDeviceController getDeviceController() {
		return device;
	}
	
	/** is called when a preset is saved (on the unit or in a file) */
	public void onSave() {
		this.setTitle(getCurrentPresetTitle());
	}
	
	private String getCurrentPresetTitle(){
		if(isWebPreset())return currentWebPreset.getTitle();
		return currentPreset.getTitle();
	}
	
	/** is called when a preset got changed */
	public void onChange() {
		this.setTitle(getCurrentPresetTitle()+" *modified*");
	}
	
	/**
	 * Load a WebPreset (from the internet)
	 * @param selectedPreset
	 */
	public void loadWebPreset(WebPreset selectedPreset){
		selectedPreset.getData().setTitle(selectedPreset.getTitle());
		loadPreset(selectedPreset.getData(), selectedPreset.getTitle()+" (web)");
		currentWebPreset=selectedPreset;
		getTopWebPanel().setVisible(true);
		webDescriptionLabel.setText(selectedPreset.toTopPanelHtml(WebControlFrame.getInstance().isLoggedin()));
		showRating(selectedPreset.getVoteAvg());
		hideLocalButtons();
		getWebVotesPanel().showVotes(selectedPreset);
		onWebVoteCancel();
	}
	
	public boolean isWebPreset(){
		return currentWebPreset!=null;
	}
	
	/**
	 * show star rating
	 * @param v - value (0-5)
	 */
	public void showRating(float v){
		currentVote=Math.round(v);
		if(v>0.5){
			webStar1Label.setVisible(true);
			webStar1grayLabel.setVisible(false);
		}else{
			webStar1Label.setVisible(false);
			webStar1grayLabel.setVisible(true);
		}
		if(v>1.5){
			webStar2Label.setVisible(true);
			webStar2grayLabel.setVisible(false);
		}else{
			webStar2Label.setVisible(false);
			webStar2grayLabel.setVisible(true);
		}
		if(v>2.5){
			webStar3Label.setVisible(true);
			webStar3grayLabel.setVisible(false);
		}else{
			webStar3Label.setVisible(false);
			webStar3grayLabel.setVisible(true);
		}
		if(v>3.5){
			webStar4Label.setVisible(true);
			webStar4grayLabel.setVisible(false);
		}else{
			webStar4Label.setVisible(false);
			webStar4grayLabel.setVisible(true);
		}
		if(v>4.5){
			webStar5Label.setVisible(true);
			webStar5grayLabel.setVisible(false);
		}else{
			webStar5Label.setVisible(false);
			webStar5grayLabel.setVisible(true);
		}
	}
	
	/** Open a Preset: sets preset in GUI and transfers it to the device */
	public void loadPreset(StPreset preset, String source){
		getTopWebPanel().setVisible(false);
		currentWebPreset=null;
		setCurrentPreset(preset);
		device.activateParameters(preset);
		this.setTitle(source);
	}

	/**
	 * reloads current settings from the deviceand displays them
	 * TODO: watch changes
	 */
	private void reloadPreset(){
		setCurrentPreset(device.getCurrentParameters());
	}
	
	/**
	 * request current program from device
	 * @return current preset/program
	 */
	public StPreset requestPreset(){
		reloadPreset();
		return currentPreset;
	}
	
	/**
	 * gets the top button panel, toolbar replacement
	 * @return
	 */
	private JPanel getButtonPanel(){
		if(buttonPanel==null){
			buttonPanel=new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			buttonPanel.setBounds(0,0,980,50);
			buttonPanel.setBackground(StLab.BACKGROUND);

			togglePresetListButton=new JToggleButton("Presets");
			togglePresetListButton.setIcon(ImagePanel.loadImageIcon("img/list.png"));
			toggleWebButton=new JToggleButton("Web");
			toggleWebButton.setIcon(ImagePanel.loadImageIcon("img/world.png"));
			toggleSaveButton=new JButton("Save");
			toggleSaveButton.setIcon(ImagePanel.loadImageIcon("img/save.png"));
			toggleSaveButton.setForeground(StLab.FOREGROUND);
			togglePreferencesButton=new JButton("Preferences");
			togglePreferencesButton.setIcon(ImagePanel.loadImageIcon("img/preferences.png"));
			togglePreferencesButton.setForeground(StLab.FOREGROUND);
			
			buttonPanel.add(toggleSaveButton);
			buttonPanel.add(togglePresetListButton);
			buttonPanel.add(toggleWebButton);
			buttonPanel.add(togglePreferencesButton);
			
		}
		return buttonPanel;
	}
	
	public void showLocalButtons(){
		getButtonPanel2().setVisible(true);
	}
	
	public void hideLocalButtons(){
		getButtonPanel2().setVisible(false);
	}
	
	private JPanel getButtonPanel2(){
		if(buttonPanel2 == null){
			buttonPanel2 = new JPanel();
//			buttonPanel2.setVisible(false);
			buttonPanel2.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPanel2.setBounds(690,0,240,50);
			buttonPanel2.setBackground(StLab.BACKGROUND);
			JButton shareButton = new JButton("Share");
			shareButton.setIcon(ImagePanel.loadImageIcon("img/upload.png"));
			shareButton.setFocusable(false);
			shareButton.setForeground(StLab.FOREGROUND);
			shareButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(WebControlFrame.getInstance().isLoggedin()){
						WebControlFrame.getInstance().showPublish();
						WebControlFrame.getInstance().getShareTitleTextField().setText(currentPreset.getTitle());
					}else
						WebControlFrame.getInstance().showLogin();
				}
			});
			buttonPanel2.add(shareButton);
		}
		return buttonPanel2;
	}
	
	/**
	 * gets/creates an instance of WebControlFrame, so start it <b>after</b> initialization
	 */
	public void registerWebControlListeners() {
		// Register to get changes
		WebControlFrame.getInstance().addVisibleChangeListener(new PersistentJFrame.IVisibleChangeListener() {
			@Override
			public void visibilityChanged(boolean value) {
				toggleWebButton.setSelected(value);
			}
		});
	}
	
	/**
	 * setup general gui colors
	 */
	private void initializeUIDefaults(){
		UIManager.put("Button.background",  StLab.BACKGROUND);  
//		UIManager.put("Button.foreground",  StLab.FOREGROUND);
		UIManager.put("ToggleButton.background",  StLab.BACKGROUND);  
		UIManager.put("ToggleButton.foreground",  StLab.FOREGROUND);
		UIManager.put("Panel.background",  StLab.BACKGROUND);
		UIManager.put("Panel.foreground",  StLab.FOREGROUND);
		UIManager.put("Label.foreground",  StLab.FOREGROUND);
		UIManager.put("List.background",  StLab.BACKGROUND);
		UIManager.put("List.foreground",  StLab.FOREGROUND);
		UIManager.put("List.selectionBackground",  StLab.ALT_BACK);
		UIManager.put("List.selectionForeground",  StLab.SELECTION);
		UIManager.put("Table.background",  StLab.BACKGROUND);
		UIManager.put("Table.foreground",  StLab.FOREGROUND);
		UIManager.put("Table.selectionBackground",  StLab.ALT_BACK);
		UIManager.put("Table.selectionForeground",  StLab.SELECTION);
		UIManager.put("TableHeader.background", StLab.GRID);
		UIManager.put("TableHeader.foreground", StLab.FOREGROUND);
		UIManager.put("Table.gridColor", StLab.FOREGROUND);
		UIManager.put("Viewport.background", StLab.BACKGROUND);
		UIManager.put("TextField.background",  StLab.BACKGROUND);
		UIManager.put("TextField.foreground",  StLab.FOREGROUND);
		UIManager.put("TextField.caretForeground", StLab.CARET);
		UIManager.put("PasswordField.background",  StLab.BACKGROUND);
		UIManager.put("PasswordField.foreground",  StLab.FOREGROUND);
		UIManager.put("PasswordField.caretForeground",  StLab.CARET);
		UIManager.put("TextArea.background",  StLab.BACKGROUND);
		UIManager.put("TextArea.foreground",  StLab.FOREGROUND);
		UIManager.put("TextArea.caretForeground", StLab.CARET);
		UIManager.put("Desktop.Background",  StLab.ALT_BACK);
		UIManager.put("Focus.color",  StLab.SELECTION);
		UIManager.put("OptionPane.background", StLab.BACKGROUND);
		UIManager.put("OptionPane.foreground", StLab.FOREGROUND);
		UIManager.put("OptionPane.messageForeground", StLab.CARET);
		UIManager.put("CheckBox.background", StLab.BACKGROUND);
		UIManager.put("CheckBox.foreground", StLab.FOREGROUND);
		UIManager.put("TabbedPane.contentBorderInsets", new InsetsUIResource(0,0,0,0));
		UIManager.put("TabbedPane.contentAreaColor", StLab.BACKGROUND);
		
		
		Border b=BorderFactory.createLineBorder(StLab.FOREGROUND);
		UIManager.put("TextArea.border", b);
		UIManager.put("TextField.border", b);
		UIManager.put("PasswordField.border", b);
		UIManager.put("Table.border", b);
		UIManager.put("ScrollPane.border", b);
		UIManager.put("List.border", b);
	}
}
