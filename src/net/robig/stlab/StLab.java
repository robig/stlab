package net.robig.stlab;

import javax.swing.JOptionPane;
import net.robig.stlab.gui.DeviceFrame;
import net.robig.stlab.midi.DeviceController;
import net.robig.stlab.midi.DeviceNotFoundException;
import net.robig.stlab.midi.AbstractMidiController;
import net.robig.stlab.midi.MidiControllerFactory;

public class StLab {
    public static void main(String[] args) throws Exception {

    	//TODO: show loading screen
    	try {
			final AbstractMidiController midiController=MidiControllerFactory.create();
			midiController.findAndConnectToVOX();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error initializing Midi system: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			throw e;
		}
		
		// main window:
		DeviceFrame deviceFrame = new DeviceFrame(new DeviceController());
		//TODO: hide loading screen
		deviceFrame.show();
		
    }
}
