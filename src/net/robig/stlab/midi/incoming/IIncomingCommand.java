package net.robig.stlab.midi.incoming;

import net.robig.stlab.gui.IDeviceListener;

public interface IIncomingCommand extends Runnable {
	public String getFunctionCode();
	public void prepare(String data, IDeviceListener[] listeners);
}
