package net.robig.stlab.midi.commands;

public class ParameterChangeCommand extends AbstractMidiCommand {

	public ParameterChangeCommand() {
		functionCode="41";
		expectData=true;
		expectedReturnCode="23";
	}
	
	
	@Override
	public void run() {
		sendData("00010000");
	}

}
