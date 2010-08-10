package net.robig.stlab.midi;

import net.robig.logging.Logger;

public class PcMidiController extends AbstractMidiController {

	Logger log = new Logger(this.getClass());
	
	@Override
	void closeConnection() {
		log.error("Not implemented yet");
	}

	@Override
	void initialize(int outputDeviceIndex, int inputDeviceIndex) {
		log.error("Not implemented yet");
	}

	@Override
	void sendMessage(byte[] data) {
		log.error("Not implemented yet");
	}

	@Override
	public String[] getOutputDevices() {
		log.error("Not implemented yet");
		return null;
	}
	
	@Override
	public String[] getInputDevices(){
		log.error("Not implemented yet");
		return null;
	}

}
