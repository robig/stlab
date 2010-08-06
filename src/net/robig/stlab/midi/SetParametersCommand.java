package net.robig.stlab.midi;

import net.robig.stlab.model.StPreset;

public class SetParametersCommand extends AbstratMidiCommand {
	
	StPreset preset=null;

	public SetParametersCommand(StPreset presentToLoad) {
		functionCode = "40";
		preset=presentToLoad;
	}
	
	@Override
	public void run() {
		String data=preset.getEncodedData();
		//log.debug("Present Data: "+data);
		sendData(data);
	}
	
	
}
