package net.robig.stlab.gui;

/**
 * what events of the device are interesting for the GUI?
 * @author robig
 *
 */
public interface IDeviceListener {
	public void switchPreset(int p);
	public void savePreset(int presetNumber);
}
