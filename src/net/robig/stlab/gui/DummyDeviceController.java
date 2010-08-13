package net.robig.stlab.gui;

import net.robig.logging.Logger;
import net.robig.stlab.model.StPreset;

public class DummyDeviceController implements IDeviceController {
	Logger log = new Logger(this.getClass());
	StPreset devicePreset = new StPreset();
	
	@Override
	public StPreset initialize() {
		log.info("initialize");
		return new StPreset();
	}
	
	@Override
	public void activateParameters(StPreset preset) throws Exception {
		log.info("activatePreset: "+preset);
		devicePreset=preset;
	}

	@Override
	public void findAndConnect() throws Exception {
		log.info("findAndConnect");
	}

	@Override
	public StPreset getCurrentParameters() throws Exception {
		log.info("getCurrentParameters: "+devicePreset);
		return devicePreset;
	}
	
	@Override
	public void savePreset(StPreset preset, int pid) {
		devicePreset=preset;
		log.info("savePreset: "+preset);
	}

	public void nextPreset() throws Exception {
		log.info("nextPreset: not implemented");
	}

	public void prevPreset() throws Exception {
		log.info("prevPreset: not implemented");
	}

	@Override
	public void selectPreset(int i) throws Exception {
		log.info("selectPreset: not implemented");
	}

	@Override
	public void disconnect() {
		log.info("disconnect: not implemented");
	}

	@Override
	public void addDeviceListener(IDeviceListener l) {
		log.info("addDeviceListener: not implemented");
	}
	
}
