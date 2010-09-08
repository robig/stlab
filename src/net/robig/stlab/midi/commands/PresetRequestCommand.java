package net.robig.stlab.midi.commands;

import net.robig.stlab.util.HexConvertionUtil;

/**
 * Midi command for getting current preset number
 * @author robegroe
 *
 */
public class PresetRequestCommand extends AbstractMidiCommand {

	public PresetRequestCommand() {
		functionCode = "12";
		expectData=true;
		expectedReturnCode="42";
	}
	
	@Override
	public void run() {
		sendData("");
	}
	
	public int getCurrentPresetNumber() {
		return HexConvertionUtil.hex2int(getResultData().substring(2));
	}

}
