package net.robig.stlab;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;
import net.robig.logging.Logger;
import net.robig.net.UpdateChecker;
import net.robig.stlab.gui.DeviceFrame;
import net.robig.stlab.gui.SplashWindow;
import net.robig.stlab.gui.preferences.AbstractPreferenceControl;
import net.robig.stlab.gui.preferences.BoolPreferenceControl;
import net.robig.stlab.gui.preferences.IntSliderPreferenceControl;
import net.robig.stlab.gui.preferences.IntegerTextPreferenceControl;
import net.robig.stlab.gui.preferences.LabelPreferenceControl;
import net.robig.stlab.gui.preferences.PreferencesModel;
import net.robig.stlab.gui.preferences.TablePreferenceCrontrol;
import net.robig.stlab.gui.preferences.TextListPreferenceControl;
import net.robig.stlab.gui.preferences.TextPreferenceControl;
import net.robig.stlab.gui.web.WebControlFrame;
import net.robig.stlab.midi.DeviceController;
import net.robig.stlab.midi.AbstractMidiController;
import net.robig.stlab.midi.MidiControllerFactory;
import net.robig.stlab.util.Config;

public class StLab {
	
	public static final Color BUTTON_BACKGROUND=new Color(189,189,189);
	public static final Color BUTTON_FOREGROUND=Color.BLACK;
	public static final Color FOREGROUND=new Color(187,154,77);
	public static final Color BACKGROUND=new Color(44,45,48);
	public static final Color SELECTION=new Color(204,75,73);
	public static final Color GRID=new Color(92,77,38);
	public static final Color ALT_BACK=Color.BLACK;
	public static final Color CARET=Color.WHITE;
	public static final Color LINK=Color.YELLOW;
	
	public static final String applicationName="StLab";
	public static final String applicationVersion="0.4";
	
	static Logger log = new Logger(StLab.class); 
	
	/** Preferences Dialog callback: */
	public static void initializePreferences(PreferencesModel model){
		model.addSection("Preset Author", new AbstractPreferenceControl[]{
			new LabelPreferenceControl("Enter the information that is stored in saved presets:"),
			new TextPreferenceControl("Author Name",StLabConfig.getAuthor()),
			new TablePreferenceCrontrol("Author Infos", "preset.author", new String[]{"Web","eMail"}),
			getSetupPreferences()
		});
		model.addSection("Application Updates", new AbstractPreferenceControl[]{
			new BoolPreferenceControl("Enable check for program updates on startup", StLabConfig.getCheckForUpdates()),
			//new BoolPreferenceControl("Enable check for updates2", StLabConfig.getCheckForUpdates())
		});
		model.addSection("Controls",new AbstractPreferenceControl[]{
			new IntSliderPreferenceControl("Knob mouse sensitivity", StLabConfig.getMouseSensitivity(), 50, 500),
			//new IntSliderPreferenceControl("Knob mouse sensitivity", StLabConfig.getMouseSensitivity(), 50, 500),
			new BoolPreferenceControl("Use space to show/hide preset list", StLabConfig.isSpaceSwitchesPresetListEnabled())
		});
		model.addSection("Open/Save Dialog",new AbstractPreferenceControl[]{
			new BoolPreferenceControl("Activate Preset on Selection",StLabConfig.getOpenDialogActivatePresetOnSelection()),
			new TextPreferenceControl("Last Directory", StLabConfig.getPresetsDirectory())
		});
		model.addSection("StLab-Web",new AbstractPreferenceControl[]{
			new TextPreferenceControl("Username", StLabConfig.getWebUsername()),
			new BoolPreferenceControl("Enable connection using proxy", StLabConfig.isWebProxyEnabled()),
			new TextPreferenceControl("Proxy Host", StLabConfig.getWebProxyHost()),
			new IntegerTextPreferenceControl("Proxy Port", StLabConfig.getWebProxyPort())
		});
	}
	
	public static AbstractPreferenceControl getSetupPreferences() {
		return new TablePreferenceCrontrol("Setup Used", "setup.list", new String[]{"AMP","Headphones","Speakers","Guitar","Pickup","Tone"});
	}
	
	/**
	 * Main method for application startup
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
    	
    	// Add my own log file appender:
    	net.robig.logging.SimpleFileLogAppender la=new net.robig.logging.SimpleFileLogAppender(){
    		{
    			// get configured value:
    			logFile=Logger.getProperty("log_file");
    			// write logfile into App on MacOS
    			File isApp = new File(applicationName+".app");
    			if(isApp.isDirectory() && DeviceFrame.MAC_OS_X){
    				logFile=isApp.getName()+"/"+logFile;
    				// Also set config file path:
    				Config.setConfigFile(isApp.getName()+"/"+Config.getConfigFile());
    			}
    		}
    	};
    	Logger.addAppender(la);

    	//Display Menu in MacOS Menubar:
		System.getProperties().setProperty("apple.laf.useScreenMenuBar", "true");
		System.getProperties().setProperty("com.apple.macos.useScreenMenuBar","true");

		//Native look and feel in windows:
		DeviceFrame.setNativeLookAndFeel();
		
    	//show loading screen:
		SplashWindow splash=null;
		try { 
			splash = new SplashWindow("img/stlab.png",null);
		} catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "Error loading Images: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
    	
    	// Initialize Conifg:
    	new StLabConfig();
    	
    	// Check for updates in the background:
    	if(StLabConfig.isUpdateCheckEnabled())
    		new Thread(new UpdateChecker()).start();
    	
    	// Initialize midi subsystem:
    	splash.setText("Initialize midi...");
    	try {
			final AbstractMidiController midiController=MidiControllerFactory.create();
			midiController.findAndConnectToVOX();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error initializing Midi system: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace(log.getErrorPrintWriter());
			System.exit(1);
		}
		
		// open window:
		splash.setText("Building GUI...");
		DeviceController controller=new DeviceController();
		DeviceFrame deviceFrame = new DeviceFrame(controller);
		
		// get all data from the device:
		splash.setText("Retrieving presets...");
		deviceFrame.initDevice();
		
		deviceFrame.setVisible(true);
		// Close splash screen
		splash.close();
		
		// will Initialize Stlab Web JFrame:
		deviceFrame.registerWebControlListeners();
//		WebControlFrame.getInstance();
		
    }
}
