package net.robig.stlab.gui;

import net.robig.logging.ILogAppender;
import net.robig.logging.Level;
import net.robig.logging.LogEntry;
import net.robig.logging.Logger;
import net.robig.stlab.model.StPreset;

public class GuiDeviceController implements IDeviceController,ILogAppender{

	Logger log=new Logger(this.getClass());
	boolean connected=false;
	IDeviceController device=null;
	StPreset preset=null;
	DeviceFrame gui=null;
	
	public GuiDeviceController(IDeviceController deviceController,DeviceFrame gui) {
		device=deviceController;
		this.gui=gui;
		//Also collect ERRORs from logging Framework
		Logger.addAppender(this);
	}
	
	public synchronized boolean isConnected() {
		return connected;
	}

	public synchronized void activateParameters(StPreset preset) {
		try {
			device.activateParameters(preset);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void findAndConnect()  {
		try {
			device.findAndConnect();
			connected=true;
		} catch (Exception e) {
			log.error("Cannot find VOX device!");
		}
	}

	public synchronized StPreset getCurrentParameters()  {
		try {
			return device.getCurrentParameters();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new StPreset();
	}

	public StPreset initialize() {
		try {
			preset=device.initialize();
			return preset;
		} catch (Exception e) {
			log.error("Error initializing device!");
			e.printStackTrace(log.getErrorPrintWriter());
		}
		return new StPreset();
	}

	public synchronized void nextPreset() {
		try {
			selectPreset(preset.getNumber()+1);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public synchronized void prevPreset() {
		try {
			selectPreset(preset.getNumber()-1);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public synchronized void selectPreset(int i)  {
		try {
			device.selectPreset(i);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}


	public synchronized void savePreset(StPreset preset, int pid) {
		try {
			device.savePreset(preset, pid);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void disconnect() {
		device.disconnect();
		connected=false;
	}
	
	/**
	 * from LogAppender
	 */
	@Override
	public void append(String formatedMessage, LogEntry log) {
		if(log.level.equals(Level.ERROR)){
			
		}
		gui.output(log.level+": "+log.message);
	}

	@Override
	public void init() throws Exception {
		// nothing to initialize for logging
	}

}
