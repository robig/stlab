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
		test.setNumber(preset.getNumber());
		test.parseParameters(data);
		if(!data.equals(test.encodeData())){
			log.error("sanity check failed!");
			log.debug("preset: "+data+";");
			log.debug("test:   "+test.encodeData()+";");
			log.warn("reparsed failed data:"+test);
		}
	}
}
