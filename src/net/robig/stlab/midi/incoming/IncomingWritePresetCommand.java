package net.robig.stlab.midi.incoming;

import net.robig.logging.Logger;
import net.robig.stlab.gui.IDeviceListener;
import net.robig.stlab.midi.MidiControllerFactory;
import net.robig.stlab.midi.commands.GetPresetCommand;
import net.robig.stlab.util.HexConvertionUtil;

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
		presetNumber=HexConvertionUtil.hex2int(data);
		this.listeners=listeners.clone();
	}

	@Override
	public void run() {
		GetPresetCommand cmd=new GetPresetCommand(presetNumber);
		MidiControllerFactory.getInstance().runCommandBlocking(cmd);
		for(IDeviceListener l: listeners){
			l.presetSaved(cmd.getPreset(),presetNumber);
		}
	}

}
