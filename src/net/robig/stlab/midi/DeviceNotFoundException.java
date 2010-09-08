package net.robig.stlab.midi;

import net.robig.logging.Logger;

public class DeviceNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	Logger log = new Logger(this.getClass());
	
	public DeviceNotFoundException(String errortext) {
		super(errortext);
		log.error(errortext);
	}
}
