package net.robig.stlab.gui;

import net.robig.logging.ILogAppender;
import net.robig.logging.Level;
import net.robig.logging.LogEntry;
import net.robig.logging.Logger;
import net.robig.stlab.model.StPreset;

public class GuiDeviceController implements IDeviceController,ILogAppender{

	IDeviceController device=null;
	
	public GuiDeviceController() {
		//Also collect ERRORs from logging Framework
		Logger.addAppender(this);
	}
	
	@Override
	public void activateParameters(StPreset preset) {
		try {
			device.activateParameters(preset);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void findAndConnect() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StPreset getCurrentParameters() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextPreset() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prevPreset() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void savePreset(StPreset preset, int pid) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * from LogAppender
	 */
	@Override
	public void append(String formatedMessage, LogEntry log) {
		if(log.level.equals(Level.ERROR)){
			
		}
		
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
