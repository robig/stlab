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

	private static GuiDeviceController instance=null;
	public static GuiDeviceController getInstance() { return instance; }
	
	private Logger log=new Logger(this.getClass());
	private boolean connected=false;
	private IDeviceController device=null;
	private StPreset preset=null;
	private DeviceFrame gui=null;
	
	public GuiDeviceController(IDeviceController deviceController,DeviceFrame gui) {
		instance=this;
		device=deviceController;
		this.gui=gui;
		//Also collect ERRORs from logging Framework
		Logger.addAppender(this);
	}
	
	/** @returns if the we're connected to the device */
	public synchronized boolean isConnected() {
		return connected;
	}

	/** Activate preset parameters on the device */
	public synchronized void activateParameters(StPreset preset) {
		try {
			device.activateParameters(preset);
		} catch (Exception e) {
			log.error("Error activating parameters! "+e.getMessage());
			e.printStackTrace(log.getDebugPrintWriter());
		}
	}

	/** Find and connect to the VOX device */
	public void findAndConnect()  {
		try {
			device.findAndConnect();
			connected=true;
		} catch (Exception e) {
			log.error("Cannot find VOX device!");
			e.printStackTrace(log.getWarnPrintWriter());
		}
	}

	/** Get the parameters of a preset */
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
	
	/** Get current activated parameters on the device */
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

	/** Initialize the device and gets active parameters */
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

	/** Switch to the next preset */
	public synchronized void nextPreset() {
		try {
			selectPreset(preset.getNumber()+1);
		} catch (Exception e) {
			e.printStackTrace(log.getDebugPrintWriter());
		}
	}

	/** Switch to the previous preset */
	public synchronized void prevPreset() {
		try {
			selectPreset(preset.getNumber()-1);
		} catch (Exception e) {
			e.printStackTrace(log.getDebugPrintWriter());
		}
	}

	/**
	 * switch to a preset by number
	 */
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


	/** transfer a preset onto the device */
	public synchronized void savePreset(StPreset preset, int pid) {
		try {
			device.savePreset(preset, pid);
		} catch (Exception e) {
			criticalError("Preset coult not be saved! error text: "+e.getMessage());
			e.printStackTrace(log.getWarnPrintWriter());
		}
	}

	/** disconnect */
	public void disconnect() {
		device.disconnect();
		connected=false;
	}
	
	private void criticalError(String message){
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	
	/**
	 * from LogAppender to fetch all ERROR messages and output in the gui log
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
	public void presetSaved(StPreset preset, int presetNumber) {
		// nothing to do here
		log.info("Got DeviceListener event: savePreset: "+presetNumber);
	}

	/**
	 * Got a preset switch from the device, send parameters to the GUI
	 */
	@Override
	public void presetSwitched(int p) {
		log.debug("Got DeviceListener event: preset Switch to "+p);
		gui.setCurrentPreset(getPresetParameters(p));
	}

	/** add other device listeners */
	@Override
	public void addDeviceListener(IDeviceListener l) {
		device.addDeviceListener(l);
	}

	/** get informations about the device */
	@Override
	public DeviceInfo getDeviceInfo() {
		return device.getDeviceInfo();
	}

}
