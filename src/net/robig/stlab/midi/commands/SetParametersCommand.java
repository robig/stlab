package net.robig.stlab.midi.commands;

import net.robig.stlab.model.StPreset;

/**
 * Midi command for setting the current preset's parameters
 * @author robegroe
 *
 */
public class SetParametersCommand extends AbstractMidiCommand {
	
	StPreset preset=null;

	public SetParametersCommand(StPreset presentToLoad) {
		functionCode = "40";
		preset=presentToLoad;
	}
	
	@Override
	public void run() {
		String data=preset.encodeData();
		log.debug("set parameters: "+preset);
		sendData(data);
		
		//sanity check:
		StPreset test = new StPreset();
		test.parseData(data);
		if(!test.equals(preset)){
			log.warn("sanity check failed!");
			log.debug("reparsed failed data:"+test);
		}
	}
}
