package net.robig.stlab.midi;

import net.robig.logging.Logger;
import net.robig.stlab.util.Config;

public class MidiControllerFactory {
	static Logger log = new Logger(MidiControllerFactory.class);
	public static AbstractMidiController create() {
		String os=System.getProperty("os.name");
		log.info("OS: "+os);
		String wish=Config.getInstance().getValue("midi.implementation", "default");
		
		if(wish.equals("Mac") || !wish.equals("PC") && os.startsWith("Mac")){
			log.info("Creating OSX midi controller");
			return new OSXMidiController();
		}
		log.info("Creating PC midi controller");
		return new PcMidiController();
	}
}
