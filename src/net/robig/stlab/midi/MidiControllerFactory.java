package net.robig.stlab.midi;

import net.robig.logging.Logger;
import net.robig.stlab.util.Config;

public class MidiControllerFactory {
	static Logger log = new Logger(MidiControllerFactory.class);
	static AbstractMidiController instance=null;
	
	public static AbstractMidiController create() {
		String os=System.getProperty("os.name");
		log.info("OS: "+os+" "+System.getProperty("os.version")+" "+System.getProperty("os.arch")+" java: "+System.getProperty("java.version"));
		String wish=Config.getInstance().getValue("midi.implementation", "default");
		
		if(wish.equals("Mac") || !wish.equals("PC") && os.startsWith("Mac")){
			log.info("Creating OSX midi controller");
			instance=new OSXMidiController();
		}else{
			log.info("Creating PC midi controller");
			instance= new PcMidiController();
		}
		return instance;
	}
	
	public static AbstractMidiController getInstance(){
		if(instance==null)create();
		return instance;
	}
}
