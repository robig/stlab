package net.robig.stlab.midi.incoming;

import net.robig.logging.Logger;
import net.robig.stlab.gui.IDeviceListener;

public class IncomingWritePresetCommand implements IIncomingCommand {
	Logger log = new Logger(this.getClass());
	
	@Override
	public String getFunctionCode() {
		return "21";
	}

	@Override
	public synchronized void prepare(String data, IDeviceListener[] listeners) {
		log.error("Write Preset command not implemented yet!");
		// TODO implement this!

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
