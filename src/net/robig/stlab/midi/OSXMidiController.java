package net.robig.stlab.midi;

import net.robig.logging.Logger;
import net.robig.stlab.util.HexConvertionUtil;
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
	public String[] getOutputDevices() {
		return de.humatic.mmj.MidiSystem.getOutputs();
	}
	
	/**
	 * get a string array of available input devices
	 * @return
	 */
	public String[] getInputDevices(){
		return de.humatic.mmj.MidiSystem.getInputs();
	}
	
	public void initialize(int outputDeviceIndex, int inputDeviceIndex) {
		output = de.humatic.mmj.MidiSystem.openMidiOutput(outputDeviceIndex);
		input=MidiSystem.openMidiInput(inputDeviceIndex);
		input.addMidiListener(new MyMidiListener());
	}
	
	@Override
	public void sendMessage(byte[] data){
		if(!isOutputConnected())return;
		log.debug("sending message: "+HexConvertionUtil.toHexString(data));
		output.sendMidi(data);
	}
	
	public void closeConnection() {
		log.info("closing midi connection");
		input.close();
		output.close();
	}

	@Override
	void connectInput(String name) throws DeviceNotFoundException {
		String[] devices=getInputDevices();
		for(int i=0;i<devices.length;i++){
			if(devices[i].equals(name)){
				input=MidiSystem.openMidiInput(i);
				input.addMidiListener(new MyMidiListener());
				inputConnected=true;
				break;
			}
		}
	}

	@Override
	void connectOutput(String name) throws DeviceNotFoundException {
		String[] devices=getOutputDevices();
		for(int i=0;i<devices.length;i++){
			if(devices[i].equals(name)){
				output = de.humatic.mmj.MidiSystem.openMidiOutput(i);
				outputConnected=true;
				break;
			}
		}
	}

	@Override
	public void sendNote(int key, int velocy, boolean on) {
		if(!isOutputConnected())return;
		log.error("Not supported yet");
	}
}
