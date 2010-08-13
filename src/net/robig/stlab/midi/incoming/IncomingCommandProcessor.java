package net.robig.stlab.midi.incoming;

import static net.robig.stlab.midi.commands.AbstractMidiCommand.command_start_data;
import static net.robig.stlab.midi.commands.AbstractMidiCommand.command_end_data;
import java.util.ArrayList;
import java.util.LinkedList;

import net.robig.logging.Logger;
import net.robig.stlab.gui.IDeviceListener;

public class IncomingCommandProcessor extends Thread {
	
	boolean running=true;
	LinkedList<String> incomingData=new LinkedList<String>();
	
	ArrayList<IDeviceListener> deviceListeners = new ArrayList<IDeviceListener>();
	Logger log=new Logger(this.getClass());
	IIncomingCommand[] registeredCommands = new IIncomingCommand[]{
			new IncomingPresetChangeCommand(),
			new IncomingWritePresetCommand()
	};

	/**
	 * process incoming commands from device
	 * @param fullData
	 * @return true if command was identified as incoming and was already processed
	 */
	public synchronized void processIncomingCommand(String fullData) {
		if(!fullData.startsWith(command_start_data)) {
			log.error("unknown format! incoming command starts not as usual!");
			return;
		}
		incomingData.push(fullData);
		notify();
	}
	
	private void process(String fullData){
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
						cmd.run();	
					}
				} catch(Exception ex){
					log.error("Error in DeviceListener! "+ex.getMessage());
					ex.printStackTrace(log.getDebugPrintWriter());
				}
				return;
			}
		}
		if(functionCode.equals("24")){
			log.error("Got ERROR code 24 (DATA LOAD ERROR) !");
		}else if(functionCode.equals("26")){
			log.error("Got ERROR code 26 (DATA FORMAT ERROR) !");
		}else if(functionCode.equals("22")){
			log.error("Got ERROR code 22 (WRITE ERROR) !");
		} else
			log.debug("No command implementation for data: "+fullData);
		return;
	}
	
	public synchronized void addDeviceListener(IDeviceListener l) {
		deviceListeners.add(l);
	}
	
	@Override
	public synchronized void run() {
		while(running){
			while(incomingData.size()>0){
				String data=incomingData.pop();
				process(data);
			}
			try {
				log.debug("Waiting for incoming Commands");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace(log.getDebugPrintWriter());
			}
			log.debug("Woke up...");
		}
	}
}
