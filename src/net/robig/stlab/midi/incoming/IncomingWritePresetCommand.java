package net.robig.stlab.midi.incoming;

import net.robig.logging.Logger;
import net.robig.stlab.gui.IDeviceListener;
import net.robig.stlab.midi.AbstractMidiController;

public class IncomingWritePresetCommand implements IIncomingCommand {
	Logger log = new Logger(this.getClass());
	
	int presetNumber=0;
	IDeviceListener[] listeners=null;
	
	@Override
	public String getFunctionCode() {
		return "21";
	}

	@Override
	public synchronized void prepare(String data, IDeviceListener[] listeners) {
		presetNumber=AbstractMidiController.hex2int(data);
		this.listeners=listeners.clone();
	}

	@Override
	public void run() {
		for(IDeviceListener l: listeners){
			l.savePreset(presetNumber);
		}
	}

}
