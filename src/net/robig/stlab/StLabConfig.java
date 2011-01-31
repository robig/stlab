package net.robig.stlab;

import net.robig.logging.Logger;
import net.robig.stlab.util.config.AbstractValue;
import net.robig.stlab.util.config.BoolValue;
import net.robig.stlab.util.config.DoubleValue;
import net.robig.stlab.util.config.IValueChangeListener;
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
	
	static final Logger log = new Logger(StLabConfig.class);

	public static String getWebUrl(){
		return "http://stlab.robig.net/";
	}
	
//	System.getProperties().put( "proxySet", "true" );
//	System.getProperties().put( "proxyHost", "192.168.100.2" );
//	System.getProperties().put( "proxyPort", "8080" );
	
	public static StringValue getWebProxyHost(){
		StringValue v=getStringValue("web.proxy.host", "");
		v.addChangeListener(new IValueChangeListener() {
			@Override
			public void valueChanged(AbstractValue av) {
				log.info("Setting proxy host: "+av.toString());
				System.getProperties().put( "proxyHost", av.toString() );
			}
		});
		return v;
	}
	
	public static IntValue getWebProxyPort() {
		IntValue v=getIntValue("web.proxy.port", 8080);
		v.addChangeListener(new IValueChangeListener() {
			@Override
			public void valueChanged(AbstractValue av) {
				log.info("Setting proxy port: "+av.toString());
				System.getProperties().put( "proxyPort", av.toString() );
			}
		});
		return v;
	}
	
	public static BoolValue isWebProxyEnabled(){
		final BoolValue v=getBoolValue("web.proxy.enabled", false);
		v.addChangeListener(new IValueChangeListener() {
			@Override
			public void valueChanged(AbstractValue av) {
				boolean e=v.getSimpleValue();
				log.info((e?"Enabled":"Disabled")+" proxy");
				System.getProperties().put( "proxySet", (e?"true":"false") );
				System.getProperties().remove( "proxyPort" );
				System.getProperties().remove( "proxyHost" );
			}
		});
		return v;
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
