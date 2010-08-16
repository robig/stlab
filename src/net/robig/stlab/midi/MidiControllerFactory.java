package net.robig.stlab.midi;

import net.robig.logging.Logger;

public class MidiControllerFactory {
	static Logger log = new Logger(MidiControllerFactory.class);
	public static AbstractMidiController create() {
		String os=System.getProperty("os.name");
		log.info("OS: "+os);
		
		if(os.startsWith("Mac")){
//			log.info("Creating OSX midi controller");
//			return new OSXMidiController();
		}
		log.info("Creating PC midi controller");
		return new PcMidiController();
	}
}
