package net.robig.stlab;

import net.robig.stlab.gui.DeviceFrame;
import net.robig.stlab.midi.DeviceController;
import net.robig.stlab.midi.DeviceNotFoundException;
import net.robig.stlab.midi.MidiController;

public class StLab {
    public static void main(String[] args) throws DeviceNotFoundException {

    	//TODO: show loading screen
		MidiController.findAndConnectToVOX();
		final MidiController midiController=MidiController.getInstance();
		
		// main window:
		DeviceFrame deviceFrame = new DeviceFrame(new DeviceController());
		//TODO: hide loading screen
		deviceFrame.show();
		
    }
}
