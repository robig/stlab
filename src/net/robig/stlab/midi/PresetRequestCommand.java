package net.robig.stlab.midi;

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
		return AbstractMidiController.hex2int(getResultData().substring(2));
	}

}
