package net.robig.stlab;

import net.robig.stlab.util.config.BoolValue;
import net.robig.stlab.util.config.DoubleValue;
import net.robig.stlab.util.config.IntValue;
import net.robig.stlab.util.config.LongValue;
import net.robig.stlab.util.config.ObjectConfig;

/**
 * Easy type save access to all used config parameters   
 *
 * @author robegroe
 *
 */
public class StLabConfig extends ObjectConfig {

	public static boolean isUpdateCheckEnabled() {
		return getBoolValue("startup.checkforupdates", true).getValue();
	}

	public static IntValue getMouseSensitivity() {
		return getIntValue("knobs.mouse.sensitivity",150);
	}
	
	public static void setMouseSensitivity(int value) {
		setIntValue("knobs.mouse.sensitivity",value);
	}
	
	public static DoubleValue getMouseWheelSensitivity() {
		return getDoubleValue("knobs.mousewheel.sensitivity",1.0);
	}
	
	public static void setMouseWheelSensitivity(double value) {
		setDoubleValue("knobs.mousewheel.sensitivity",value);
	}

	/* TODO:
	public static Properties getAuthorInfo() {

		String keys=getInstance().getValue("preset.author.keys","name");
		for(String k: keys.split(" ")){
	*/

	
}
