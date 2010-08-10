package net.robig.stlab;

import net.robig.stlab.gui.DeviceFrame;
import net.robig.stlab.midi.DeviceController;
import net.robig.stlab.midi.DeviceNotFoundException;
import net.robig.stlab.midi.AbstractMidiController;
import net.robig.stlab.midi.MidiControllerFactory;

public class StLab {
    public static void main(String[] args) throws DeviceNotFoundException {

    	//TODO: show loading screen
		final AbstractMidiController midiController=MidiControllerFactory.create();
		midiController.findAndConnectToVOX();
		
		// main window:
		DeviceFrame deviceFrame = new DeviceFrame(new DeviceController());
		//TODO: hide loading screen
		deviceFrame.show();
		
    }
}
