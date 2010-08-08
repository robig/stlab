package net.robig.stlab.midi;

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

	@Override
	protected void receiveData(String resultData) {
		super.receiveData(resultData);
		preset=new StPreset();
		preset.parseData(getResultData());
		log.debug("parsed preset:"+preset);
	}
	
	public StPreset getPreset(){
		return preset;
	}

}
