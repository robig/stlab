package net.robig.stlab.midi;

import java.util.ArrayList;
import java.util.List;

import net.robig.logging.Logger;
import net.robig.stlab.gui.IDeviceController;
import net.robig.stlab.gui.IDeviceListener;
import net.robig.stlab.midi.commands.GetParametersCommand;
import net.robig.stlab.midi.commands.GetPresetCommand;
import net.robig.stlab.midi.commands.PresetRequestCommand;
import net.robig.stlab.midi.commands.SetParametersCommand;
import net.robig.stlab.midi.commands.SwitchPresetCommand;
import net.robig.stlab.midi.commands.WritePresetCommand;
import net.robig.stlab.model.DeviceInfo;
import net.robig.stlab.model.StPreset;

public class DeviceController implements IDeviceController {

	Logger log=new Logger(this.getClass());
	AbstractMidiController midi = null;
	List<IDeviceListener> listeners= new ArrayList<IDeviceListener>();
	DeviceInfo info=new DeviceInfo();
	
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
		//log.error("not implemented yet :( use save button on the unit.");
		if(pid<0 || pid>49) throw new IllegalArgumentException("preset number must be between 0 and 49!");
		midi.runCommand(new WritePresetCommand(preset, pid));
	}

	@Override
	public void selectPreset(int num) throws Exception {
		if(num<0 || num>99) throw new IllegalArgumentException("preset number must be between 0 and 99!");
		String ret=midi.runCommandBlocking(new SwitchPresetCommand(num));
		if(ret!=null){
			//StPreset preset=getPresetParameters(num);
			for(IDeviceListener l: listeners)
				l.switchPreset(num);
		}
	}
	
	@Override
	public void disconnect() {
		midi.closeConnection();
	}

	@Override
	public synchronized void addDeviceListener(IDeviceListener l) {
		midi.addDeviceListener(l);
		listeners.add(l);
	}

	@Override
	public DeviceInfo getDeviceInfo() {
		info.numPresets=100;
		return info;
	}


}
