package net.robig.stlab.midi.commands;

import net.robig.stlab.model.StPreset;

public class SetParametersCommand extends AbstractMidiCommand {
	
	StPreset preset=null;

	public SetParametersCommand(StPreset presentToLoad) {
		functionCode = "40";
		preset=presentToLoad;
	}
	
	@Override
	public void run() {
		String data=preset.getEncodedData();
		log.debug("set parameters: "+preset);
		sendData(data);
		
		//sanity check:
		StPreset test = new StPreset();
		test.parseData(data);
		if(!test.equals(preset)){
			log.error("sanity check failed!");
			log.debug("reparsed failed data:"+test);
		}
	}
}
