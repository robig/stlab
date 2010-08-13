package net.robig.stlab.midi.commands;

public class SwitchPresetCommand extends AbstractMidiCommand {

	int index = 0;
	
	public SwitchPresetCommand(int idx) {
		functionCode="4e";
		index = idx;
	}
	
	@Override
	public void run() {
		sendData("00"+toHexString(index));
	}

}
