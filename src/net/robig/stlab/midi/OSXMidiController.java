package net.robig.stlab.midi;

import net.robig.logging.Logger;
import de.humatic.mmj.MidiListener;
import de.humatic.mmj.MidiSystem;

public class OSXMidiController extends AbstractMidiController {

	Logger log = new Logger(this.getClass());
	de.humatic.mmj.MidiOutput output=null;
	de.humatic.mmj.MidiInput input=null;
	
	
	/**
	 * wrapper class to get midiInput() private
	 * @author robig
	 */
	class MyMidiListener implements MidiListener {
		@Override
		public void midiInput(byte[] arg0) {
			instance.midiInput(arg0);
		}
	}

	/**
	 * get a string array of available output devices
	 * @return
	 */
	public static String[] getOutputDevices() {
		return de.humatic.mmj.MidiSystem.getOutputs();
	}
	
	/**
	 * get a string array of available input devices
	 * @return
	 */
	public static String[] getInputDevices(){
		return de.humatic.mmj.MidiSystem.getInputs();
	}
	
	@Override
	public void initialize(int outputDeviceIndex, int inputDeviceIndex) {
		output = de.humatic.mmj.MidiSystem.openMidiOutput(outputDeviceIndex);
		input=MidiSystem.openMidiInput(inputDeviceIndex);
		input.addMidiListener(new MyMidiListener());
	}
	
	@Override
	public void sendMessage(byte[] data){
		log.debug("sending message: "+toHexString(data));
		output.sendMidi(data);
	}
	
	public void closeConnection() {
		log.info("closing midi connection");
		input.close();
		output.close();
	}
}
