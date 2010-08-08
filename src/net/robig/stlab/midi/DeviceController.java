package net.robig.stlab.midi;

import net.robig.logging.Logger;
import net.robig.stlab.gui.IDeviceController;
import net.robig.stlab.model.StPreset;

public class DeviceController implements IDeviceController {

	Logger log=new Logger(this.getClass());
	MidiController midi = null;
	
	@Override
	public void activateParameters(StPreset preset) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void findAndConnect() throws Exception {
		MidiController.findAndConnectToVOX();
	}

	@Override
	public StPreset getCurrentParameters() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StPreset initialize() throws Exception {
		midi=MidiController.getInstance();
		
		// get current activated preset:
		PresetRequestCommand cmd=new PresetRequestCommand();
		midi.runCommandBlocking(cmd);
		int presetNum=cmd.getCurrentPresetNumber();
		
		GetParametersCommand cmdp = new GetParametersCommand();
		midi.runCommandBlocking(cmdp);
		StPreset currentPreset = cmdp.getPreset();
		currentPreset.setNumber(presetNum);
		
		return currentPreset;
	}

	@Override
	public void nextPreset() throws Exception {
		//TODO: midi.runCommand(new SwitchPresetCommand)

	}

	@Override
	public void prevPreset() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void savePreset(StPreset preset, int pid) {
		log.error("not implemented yet :(");
	}

	@Override
	public void selectPreset(int num) throws Exception {
		if(num<0 || num>99) throw new IllegalArgumentException("preset number must be between 0 and 99!");
		midi.runCommand(new SwitchPresetCommand(num));
	}

	@Override
	public void disconnect() {
		midi.closeConnection();
	}

}
