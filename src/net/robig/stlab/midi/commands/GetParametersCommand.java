package net.robig.stlab.midi.commands;

import net.robig.stlab.model.StPreset;

/**
 * Request dump of current parameters
 * @author robig
 *
 */
public class GetParametersCommand extends AbstractMidiCommand {

	StPreset preset=null;
	
	public GetParametersCommand() {
		functionCode="10";
		expectData=true;
		expectedReturnCode="40";
	}
	
	@Override
	public void run() {
		sendData("");
	}

	/**
	 * internal method for receiving the data and parsing for StPreset data
	 */
	@Override
	protected void receiveData(String resultData) {
		super.receiveData(resultData);
		preset=new StPreset();
		preset.parseData(getResultData());
		log.debug("parsed preset:"+preset);
	}
	
	/**
	 * gets the received parameters as StPreset data
	 * @return
	 */
	public StPreset getPreset(){
		return preset;
	}

}
