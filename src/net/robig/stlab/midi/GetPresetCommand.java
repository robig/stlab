package net.robig.stlab.midi;

import net.robig.stlab.model.StPreset;

public class GetPresetCommand extends AbstratMidiCommand {

	int index = 0;
	
	public GetPresetCommand(int idx) {
		index = idx;
		functionCode="1C";
	}
	
	@Override
	public void run() {
		sendData("20 "+toHexString(index));
	}

	@Override
	public void receive(String data) throws MidiCommunicationException{
		if(data.startsWith(command_start_data+"4c")){
			StPreset preset = new StPreset();
			//TODO: implememt inizialisation of Preset object
		}
		throw new MidiCommunicationException("Cannot get preset!", data);
	}

}
