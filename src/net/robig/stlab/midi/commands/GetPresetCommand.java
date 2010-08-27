package net.robig.stlab.midi.commands;

import net.robig.stlab.model.StPreset;

/**
 * get the parameters of a specific preset
 * @author robegroe
 *
 */
public class GetPresetCommand extends AbstractMidiCommand {

	int index = 0;
	StPreset preset=null;
	
	/**
	 * 
	 * @param idx preset number 0-99
	 */
	public GetPresetCommand(int idx) {
		index = idx>99?0:idx;
		functionCode="1C";
		expectData=true;
		expectedReturnCode="4c";
	}
	
	@Override
	public void run() {
		sendData("20 "+toHexString(index));
	}

	@Override
	protected void receiveData(String resultData) {
		super.receiveData(resultData);
		preset=new StPreset();
		preset.parseData(getResultData().substring(2));
		log.debug("parsed preset:"+preset);
	}
	
	/**
	 * get the received StPeset data
	 * @return
	 */
	public StPreset getPreset(){
		return preset;
	}

}
