package net.robig.stlab.midi.commands;

/**
 * direct Parameter changes are not supported by Tonelab ST
 * @author robegroe
 *
 */
@Deprecated
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
