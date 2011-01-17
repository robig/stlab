package net.robig.stlab;

import net.robig.stlab.util.config.BoolValue;
import net.robig.stlab.util.config.DoubleValue;
import net.robig.stlab.util.config.IntValue;
import net.robig.stlab.util.config.LongValue;
import net.robig.stlab.util.config.MapValue;
import net.robig.stlab.util.config.StringValue;
import net.robig.stlab.util.config.ObjectConfig;

/**
 * Easy type save access to all used config parameters   
 *
 * @author robegroe
 *
 */
public class StLabConfig extends ObjectConfig {

	public static String getWebUrl(){
		return "http://stlab.robig.net/";
	}
	
	public static boolean isUpdateCheckEnabled() {
		return getCheckForUpdates().getValue();
	}
	
	public static BoolValue getCheckForUpdates() {
		return getBoolValue("startup.checkforupdates", true);
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

	public static MapValue getAuthorInfo() {
		return (MapValue) getAbstractValue("preset.author", new MapValue("preset.author"));
	}
	
	public static StringValue getAuthor(){
		return getStringValue("preset.author.name", System.getProperty("user.name"));
	}

	public static BoolValue getPresetListWindowVisible() {
		return getBoolValue("presetlist.visible", false);
	}
	
	public static IntValue getPresetListWindowWidth(){
		return getIntValue("presetlist.width", 650);
	}
	
	public static IntValue getPresetListWindowHeight(){
		return getIntValue("presetlist.height", 450);
	}
	
	public static IntValue getPresetListWindowX(){
		return getIntValue("presetlist.x", 0);
	}
	
	public static IntValue getPresetListWindowY(){
		return getIntValue("presetlist.y", 0);
	}
	
	public static IntValue getLiveWindowX(){
		return getIntValue("livewindow.x", 0);
	}
	
	public static IntValue getLiveWindowY(){
		return getIntValue("livewindow.y", 0);
	}
	
	/** Get last Directory  */
	public static StringValue getPresetsDirectory() {
		return getStringValue("directory.presets", "");
	}
	
	/** Should the selected preset transfered directly to the device? */
	public static BoolValue getOpenDialogActivatePresetOnSelection(){
		return getBoolValue("opendialog.activate.presets", true);
	}
	
	public static StringValue getWebUsername() {
		return getStringValue("stlab-web.username", "");
	}

	public static BoolValue isSpaceSwitchesPresetListEnabled(){
		return getBoolValue("presetlist.space.switch", true); 
	}

}
