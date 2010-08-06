package net.robig.stlab.midi;

import net.robig.logging.Logger;

public class DeviceNotFoundException extends Exception {
	Logger log = new Logger(this.getClass());
	
	public DeviceNotFoundException(String errortext) {
		super(errortext);
		log.error(errortext);
	}
}
