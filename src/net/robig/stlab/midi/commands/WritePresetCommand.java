package net.robig.stlab.midi.commands;

import net.robig.stlab.model.StPreset;
import net.robig.stlab.util.HexConvertionUtil;

public class WritePresetCommand extends AbstractMidiCommand {

	StPreset preset=null;
	int number=0;
	
	public WritePresetCommand(StPreset preset, int num) {
		functionCode="4C";
		expectData=false;
		this.preset=preset;
		this.number=num;
	}
	
	@Override
	public void run() {
		sendData("20"+HexConvertionUtil.toHexString(number)+preset.encodeData());
	}

}
