package net.robig.stlab.midi;

import net.robig.logging.Logger;
import net.robig.stlab.gui.IDeviceController;
import net.robig.stlab.gui.IDeviceListener;
import net.robig.stlab.midi.commands.GetParametersCommand;
import net.robig.stlab.midi.commands.GetPresetCommand;
import net.robig.stlab.midi.commands.PresetRequestCommand;
import net.robig.stlab.midi.commands.SetParametersCommand;
import net.robig.stlab.midi.commands.SwitchPresetCommand;
import net.robig.stlab.model.StPreset;

public class DeviceController implements IDeviceController {

	Logger log=new Logger(this.getClass());
	AbstractMidiController midi = null;
	
	@Override
	public void activateParameters(StPreset preset) throws Exception {
		midi.runCommand(new SetParametersCommand(preset));
	}

	@Override
	public void findAndConnect() throws Exception {
		midi.findAndConnectToVOX();
	}

	@Override
	public StPreset getPresetParameters(int number) throws Exception {
		GetPresetCommand cmdp = new GetPresetCommand(number);
		midi.runCommandBlocking(cmdp);
		StPreset preset = cmdp.getPreset();
		return preset;
	}

	@Override
	public StPreset getCurrentParameters() throws Exception {
		// get current activated preset:
		PresetRequestCommand cmd=new PresetRequestCommand();
		midi.runCommandBlocking(cmd);
		int presetNum=cmd.getCurrentPresetNumber();
		
		// But get current preset:
		GetParametersCommand cmdp = new GetParametersCommand();
		midi.runCommandBlocking(cmdp);
		StPreset currentPreset = cmdp.getPreset();
		currentPreset.setNumber(presetNum);
		
		return currentPreset;
	}

	@Override
	public StPreset initialize() throws Exception {
		midi=AbstractMidiController.getInstance();
	
		return getCurrentParameters();
	}

	@Override
	public void savePreset(StPreset preset, int pid) {
		log.error("not implemented yet :(");
	}

	@Override
	public void selectPreset(int num) throws Exception {
		if(num<0 || num>99) throw new IllegalArgumentException("preset number must be between 0 and 99!");
		midi.runCommandBlocking(new SwitchPresetCommand(num));
	}

	@Override
	public void disconnect() {
		midi.closeConnection();
	}

	@Override
	public void addDeviceListener(IDeviceListener l) {
		midi.addDeviceListener(l);
	}


}
