package net.robig.stlab.midi.incoming;

import static net.robig.stlab.midi.commands.AbstractMidiCommand.command_start_data;
import static net.robig.stlab.midi.commands.AbstractMidiCommand.command_end_data;
import java.util.ArrayList;
import net.robig.logging.Logger;
import net.robig.stlab.gui.IDeviceListener;

public class IncomingCommandController {
	
	ArrayList<IDeviceListener> deviceListeners = new ArrayList<IDeviceListener>();
	Logger log=new Logger(this.getClass());
	IIncomingCommand[] registeredCommands = new IIncomingCommand[]{
			new IncomingPresetChangeCommand()
	};

	/**
	 * process incoming commands from device
	 * @param fullData
	 * @return true if command was identified as incoming and was already processed
	 */
	public synchronized boolean processIncomingCommand(String fullData) {
		if(!fullData.startsWith(command_start_data)) return false;
		int cmdStartLen=command_start_data.length();
		int cmdEndLen=command_end_data.length();
		String functionCode=fullData.substring(cmdStartLen,cmdStartLen+2).toLowerCase();
		for(IIncomingCommand cmd: registeredCommands){
			if(cmd.getFunctionCode().toLowerCase().equals(functionCode)){
				log.info("Incoming command: "+cmd.getClass().getName());
				String data=fullData.substring(cmdStartLen+2,fullData.length()-cmdEndLen);
				try {
					synchronized (cmd) {
						cmd.prepare(data,deviceListeners.toArray(new IDeviceListener[]{}));
						new Thread(cmd).start();	
					}
				} catch(Exception ex){
					log.error("Error in DeviceListener! "+ex.getMessage());
					ex.printStackTrace(log.getDebugPrintWriter());
				}
				return true;
			}
		}
		if(functionCode=="24"){
			log.error("Got Error Code 24!");
		}
		return false;
	}
	
	public synchronized void addDeviceListener(IDeviceListener l) {
		deviceListeners.add(l);
	}
}
