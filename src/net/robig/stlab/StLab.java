package net.robig.stlab;

import javax.swing.JOptionPane;

import net.robig.logging.Logger;
import net.robig.stlab.gui.DeviceFrame;
import net.robig.stlab.gui.SplashWindow;
import net.robig.stlab.midi.DeviceController;
import net.robig.stlab.midi.DeviceNotFoundException;
import net.robig.stlab.midi.AbstractMidiController;
import net.robig.stlab.midi.MidiControllerFactory;

public class StLab {
	static Logger log = new Logger(StLab.class); 
    public static void main(String[] args) throws Exception {

    	//show loading screen:
    	SplashWindow splash = new SplashWindow("img/display0.png",null);
    	
    	Thread.sleep(5000);
    	try {
			final AbstractMidiController midiController=MidiControllerFactory.create();
			midiController.findAndConnectToVOX();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error initializing Midi system: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace(log.getErrorPrintWriter());
			System.exit(1);
		}
		
		// main window:
		DeviceFrame deviceFrame = new DeviceFrame(new DeviceController());
		//TODO: hide loading screen
		deviceFrame.show();
		splash.hide();
		
    }
}
