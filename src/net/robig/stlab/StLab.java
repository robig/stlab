package net.robig.stlab;

import java.io.FileNotFoundException;

import javax.swing.JOptionPane;
import net.robig.logging.Logger;
import net.robig.net.UpdateChecker;
import net.robig.stlab.gui.DeviceFrame;
import net.robig.stlab.gui.PresetListFrame;
import net.robig.stlab.gui.SplashWindow;
import net.robig.stlab.midi.DeviceController;
import net.robig.stlab.midi.AbstractMidiController;
import net.robig.stlab.midi.MidiControllerFactory;

public class StLab {
	
	public static final String applicationName="StLab";
	public static final String applicationVersion="0.2";
	
	static Logger log = new Logger(StLab.class); 
    public static void main(String[] args) throws Exception {

    	//Display Menu in MacOS Menubar:
		System.getProperties().setProperty("apple.laf.useScreenMenuBar", "true");
		System.getProperties().setProperty("com.apple.macos.useScreenMenuBar","true");
    	
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
		
    }
}
