package net.robig.stlab;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.robig.gui.IntegerValueKnob;
import net.robig.gui.MyJKnob;
import net.robig.stlab.midi.DeviceNotFoundException;
import net.robig.stlab.midi.MidiController;
import net.robig.stlab.midi.SetParametersCommand;
import net.robig.stlab.model.StPreset;


public class StLab {
    public static void main(String[] args) throws DeviceNotFoundException {

		JFrame myFrame = new JFrame("StLab Test");
		
		Container thePane = myFrame.getContentPane();
	
		MidiController.findAndConnectToVOX();
		final MidiController midiController=MidiController.getInstance();
		
		// Add a JKnob to the pane.
		final StPreset preset=new StPreset();
		preset.volume=50;
		final IntegerValueKnob knob = new IntegerValueKnob();
		preset.gain=knob.getValue();
		thePane.add(knob);
		knob.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				preset.gain=knob.getValue();
				midiController.runCommand(new SetParametersCommand(preset));
			}
		});
	
		myFrame.addWindowListener(new WindowAdapter() {
	             public void windowClosing(WindowEvent e) {
	                 System.exit(0);
	             }
	         });
	
		myFrame.pack();
		myFrame.show();
    }
}
