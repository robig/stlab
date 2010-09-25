package net.robig.stlab.gui;

import java.util.ArrayList;
import java.util.List;
import net.robig.logging.Logger;
import net.robig.stlab.model.DeviceInfo;
import net.robig.stlab.model.PresetList;
import net.robig.stlab.model.StPreset;

public class DummyDeviceController implements IDeviceController {
	Logger log = new Logger(this.getClass());
	StPreset currentPreset = null;
	int currentPresetOffset=0;
	PresetList allPresets=null;
	List<IDeviceListener> listeners=new ArrayList<IDeviceListener>();

	@SuppressWarnings("deprecation")
	public void addPreset(String data) {
		StPreset p = new StPreset();
		p.parseParameters(data);
		p.setNumber(allPresets.size());
		allPresets.add(p);
	}
	
	public PresetList getPresetList(){
		return allPresets;
	}
	
	@Override
	public StPreset initialize() {
		log.info("initializing");
		allPresets=new PresetList();
		allPresets.add(new StPreset());
		addPreset("00770064 0d1c5a3a 002e2035 240a0219 08083232 22030000 00000000");
		addPreset("004f0400 08494450 00384451 2801011d 00033900 3d070400 00006400");
		addPreset("007f 01161714 59420035 414a2900 011a000a 59322a03 0c000000 6400");
		currentPreset=allPresets.get(currentPresetOffset);
		return currentPreset;
	}
	
	@Override
	public void activateParameters(StPreset preset) throws Exception {
		log.info("activatePreset: "+preset);
		currentPreset=preset;
	}

	@Override
	public void findAndConnect() throws Exception {
		log.info("findAndConnect");
	}

	@Override
	public StPreset getCurrentParameters() throws Exception {
		log.info("getCurrentParameters: "+currentPreset);
		return currentPreset;
	}
	
	@Override
	public void savePreset(StPreset preset, int pid) {
		log.info("savePreset: "+preset);
		if(allPresets.size()>=pid){
			allPresets.add(preset);
		}else{
			StPreset my = preset.clone();
			my.setNumber(pid);
			allPresets.set(pid, my);
		}
		for(IDeviceListener l: listeners)
			l.presetSaved(preset, pid);
	}

	public void nextPreset() throws Exception {
		if(allPresets.size()>currentPresetOffset+1)
			selectPreset(currentPresetOffset+1);
		else
			selectPreset(0);
	}

	public void prevPreset() throws Exception {
		if(currentPresetOffset>0)
			selectPreset(currentPresetOffset-1);
		else
			selectPreset(allPresets.size()-1);
	}

	@Override
	public void selectPreset(int i) throws Exception {
		if(i>=allPresets.size()){
			i=0;
		}
		if(i<0)i=allPresets.size()-1;
		log.info("selectPreset: "+i);
		currentPresetOffset=i;
		currentPreset=allPresets.get(i);
		log.debug("select preset: "+currentPreset);
		notifyPresetSwitch();
	}

	@Override
	public void disconnect() {
		log.info("disconnect: not implemented");
	}
	
	
	private void notifyPresetSwitch() {
		synchronized (listeners) {
			for(IDeviceListener l: listeners)
				l.presetSwitched(currentPresetOffset);
		}
	}

	@Override
	public void addDeviceListener(IDeviceListener l) {
		log.info("addDeviceListener");
		listeners.add(l);
	}

	@Override
	public StPreset getPresetParameters(int number) throws Exception {
		if(allPresets.size()>number)
			return allPresets.get(number);
		return null;
	}

	@Override
	public DeviceInfo getDeviceInfo() {
		DeviceInfo info=new DeviceInfo();
		info.numPresets=allPresets.size();
		return info;
	}
	
}
