package net.robig.stlab;

import net.robig.stlab.util.Config;

/**
 * Easy type save access to all used config parameters   
 *
 * @author robegroe
 *
 */
public class StLabConfig extends Config {
	/**
	 * Saves config changes to disk.
	 */
	public static void writeConfig() {
		getInstance().saveConfig();
	}
	
	public static int getMouseSensitivity() {
		return getInstance().getValue("knobs.mouse.sensitivity",150);
	}
	
	public static void setMouseSensitivity(int value) {
		getInstance().setValue("knobs.mouse.sensitivity",value);
		writeConfig();
	}
	
	//TODO: become long
	public static int getMouseWheelSensitivity() {
		return getInstance().getValue("knobs.mousewheel.sensitivity",1);
	}
	
	public static void setMouseWheelSensitivity(int value) {
		getInstance().setValue("knobs.mousewheel.sensitivity",value);
		writeConfig();
	}
}
