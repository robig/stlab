package net.robig.stlab.midi.incoming;

import net.robig.stlab.gui.IDeviceListener;
import net.robig.stlab.util.HexConvertionUtil;

public class IncomingPresetChangeCommand implements IIncomingCommand {

	int presetNumber=0;
	IDeviceListener[] listeners=null;
	
	@Override
	public String getFunctionCode() {
		return "4E";
	}

	private void performAction(IDeviceListener listener) {
		listener.switchPreset(presetNumber);
	}

	public synchronized void prepare(String data, IDeviceListener[] listeners) {
		presetNumber=HexConvertionUtil.hex2int(data);
		this.listeners=listeners.clone();
	}

	@Override
	public void run() {
		for(IDeviceListener listener: listeners){
			performAction(listener);
		}
	}

}
