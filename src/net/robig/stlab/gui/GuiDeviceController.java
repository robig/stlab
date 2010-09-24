package net.robig.stlab.gui;

import javax.swing.JOptionPane;
import net.robig.logging.ILogAppender;
import net.robig.logging.Level;
import net.robig.logging.LogEntry;
import net.robig.logging.Logger;
import net.robig.stlab.model.DeviceInfo;
import net.robig.stlab.model.StPreset;

/**
 * layer between GUI and device controller to catch errors from the GUI
 * @author robig
 *
 */
public class GuiDeviceController implements IDeviceController,ILogAppender,IDeviceListener{

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
			log.error("Error activating parameters! "+e.getMessage());
			e.printStackTrace(log.getDebugPrintWriter());
		}
	}

	public void findAndConnect()  {
		try {
			device.findAndConnect();
			connected=true;
		} catch (Exception e) {
			log.error("Cannot find VOX device!");
			e.printStackTrace(log.getWarnPrintWriter());
		}
	}

	public StPreset getPresetParameters(int number){
		try {
			StPreset p=device.getPresetParameters(number);
			return p;
		} catch (Exception e) {
			log.error("Error preset from device! "+e.getMessage());
			e.printStackTrace(log.getWarnPrintWriter());
		}
		return new StPreset();
	}
	
	public synchronized StPreset getCurrentParameters()  {
		try {
			preset=device.getCurrentParameters();
			return preset;
		} catch (Exception e) {
			log.error("Error getting current parameters from device! "+e.getMessage());
			e.printStackTrace(log.getWarnPrintWriter());
		}
		return new StPreset();
	}

	public StPreset initialize() {
		try {
			preset=device.initialize();
		} catch (Exception e) {
			log.error("Error initializing Gui Controller!"+e.getMessage());
			e.printStackTrace(log.getDebugPrintWriter());
			preset = new StPreset();
		}
		//Listen for commands from device
		addDeviceListener(this);
		return preset;
	}

	public synchronized void nextPreset() {
		try {
			selectPreset(preset.getNumber()+1);
		} catch (Exception e) {
			e.printStackTrace(log.getDebugPrintWriter());
		}
	}

	public synchronized void prevPreset() {
		try {
			selectPreset(preset.getNumber()-1);
		} catch (Exception e) {
			e.printStackTrace(log.getDebugPrintWriter());
		}
	}

	@Override
	public synchronized void selectPreset(int i)  {
//		if(i<0)i=99;
//		if(i>99)i=0;
		try {
			// send to device:
			device.selectPreset(i);
			// get parameters:
			//getCurrentParameters();
		} catch (Exception e) {
			log.error("Error setting Preset! "+e.getMessage());
			e.printStackTrace(log.getDebugPrintWriter());
		}
	}


	public synchronized void savePreset(StPreset preset, int pid) {
		try {
			device.savePreset(preset, pid);
		} catch (Exception e) {
			criticalError("Preset coult not be saved! error text: "+e.getMessage());
			e.printStackTrace(log.getWarnPrintWriter());
		}
	}

	public void disconnect() {
		device.disconnect();
		connected=false;
	}
	
	private void criticalError(String message){
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	
	/**
	 * from LogAppender
	 */
	@Override
	public void append(String formatedMessage, LogEntry log) {
		if(log.level.equals(Level.ERROR)){
			gui.outputError(log.level+": "+log.message);
		}
	}

	@Override
	public void init() throws Exception {
		// nothing to initialize for logging
	}

	/*     ============ from IDeviceListener */
	@Override
	public void savePreset(int presetNumber) {
		log.info("Got DeviceListener event: savePreset: not implemented yet");
	}

	@Override
	public void switchPreset(int p) {
		log.debug("Got DeviceListener event: preset Switch to "+p);
		gui.setCurrentPreset(getPresetParameters(p));
	}

	@Override
	public void addDeviceListener(IDeviceListener l) {
		device.addDeviceListener(l);
	}

	@Override
	public DeviceInfo getDeviceInfo() {
		return device.getDeviceInfo();
	}

}
