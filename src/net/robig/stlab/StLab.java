package net.robig.stlab;

import net.robig.stlab.gui.DeviceFrame;
import net.robig.stlab.midi.DeviceNotFoundException;
import net.robig.stlab.midi.MidiController;

public class StLab {
    public static void main(String[] args) throws DeviceNotFoundException {

//    	DeviceFrame deviceFrame = new DeviceFrame();
	
		MidiController.findAndConnectToVOX();
		final MidiController midiController=MidiController.getInstance();
		
    }
}
