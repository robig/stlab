package net.robig.stlab.gui;

import net.robig.stlab.model.StPreset;

/**
 * what events of the device are interesting for the GUI?
 * @author robig
 *
 */
public interface IDeviceListener {
	public void presetSwitched(int p);
	public void presetSaved(StPreset preset, int presetNumber);
}
