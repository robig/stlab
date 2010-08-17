package net.robig.stlab.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import net.robig.logging.Logger;

/**
 * Provides a easy configuration based on properties files
 * @author robegroe
 */
public class Config {
	private static String configFile="config.properties";
	private Logger log=new Logger(this);
	private static Config instance = null;
	private Properties config = new Properties();

	/**
	 * Used in tests to write to another config file
	 * @param filename
	 */
	public static void setConfigFile(String filename){
		configFile=filename;
	}
	
	/**
	 * THis is a singleton class. Us this method to get the instance.
	 * Will create a new instance if none exists.
	 * @return
	 */
	public static Config getInstance() {
		if(instance==null) new Config();
		return instance;
	}
	
	/**
	 * constructs a new <code>Config</code> object.
	 * Do not instantiate manually! Use getInstance() class method.
	 */
	public Config() {
		instance=this;
		initialize();
	}

	/**
	 * private initialization of Config object
	 * tries to load all files (defined by configFile) in classpath and in working directory
	 */
	private void initialize() {
		String path = configFile;
		log.debug("Loading config from: "+path);
		Enumeration<URL> e;
		try {
			e = getClass().getClassLoader().getResources("/"+path);
			int count = 0;
			while (e.hasMoreElements()) {
				URL file=e.nextElement();
				log.debug(" found config file: "+file);
				addConfigFile(file);
				count++;
			}
			if(count>0){
				log.debug("Loaded "+count+" config files");
			}else{
				log.debug("No config file found. trying backup");
				File backup = new File(configFile);
				if(backup.canRead())
					addConfigFile(configFile);
			}
		} catch (IOException e1) {
			log.warn("Exception when loading config file(s)! "+e1.getMessage());
			e1.printStackTrace(log.getDebugPrintWriter());
		}
	}
	
	/**
	 * Parse another config file (in properties format) and merge the existing properties with it.
	 * (existing values will be overwritten by later added files)
	 * @param file
	 */
	public void addConfigFile(String file){
		Properties newProps=null;
		try {
			newProps = PropertyUtil.loadProperties(file);
			log.info("Config file: "+file+" successfully loaded");
		} catch (IOException e) {
			log.error("Loading config file failed: "+file+" "+e.getMessage());
			e.printStackTrace(log.getDebugPrintWriter());
			return;
		}
		config=PropertyUtil.mergeProperties(config, newProps);
	}
	
	public void addConfigFile(URL u){
		Properties newProps=new Properties();
		try {
			newProps.load(u.openStream());
			log.info("Config from URL: "+u+" successfully loaded");
		} catch (IOException e) {
			log.error("Loading config from URL failed: "+u+" "+e.getMessage());
			e.printStackTrace(log.getDebugPrintWriter());
			return;
		}
		config=PropertyUtil.mergeProperties(config, newProps);
	}
	
	/**
	 * Saves current config to given file.
	 * @param file
	 */
	public void saveConfig(String file) {
		log.info("Saving config to: "+file);
		try {
			PropertyUtil.saveProperties(config, file);
		} catch (Exception e) {
			log.error("Cannot save config to "+file+"! "+e.getMessage());
			e.printStackTrace(log.getDebugPrintWriter());
		}
	}
	
	/**
	 * saves current config to default config file
	 */
	public void saveConfig() {
		saveConfig(configFile);
	}
	
	/**
	 * Gets a value from the current config.
	 * @param key
	 * @param def
	 * @return
	 */
	public String getValue(String key,String dflt){
		return config.getProperty(key,dflt);
	}
	
	/**
	 * Gets a value from the current config and parses for int.
	 * @param key
	 * @param def
	 * @return
	 */
	public int getValue(String key, int def){
		String plain=config.getProperty(key);
		if(plain==null) return def;
		int value=Integer.parseInt(plain);
		return value;
	}
	
	/**
	 * Gets a value from the current config and parses for boolean.
	 * @param key
	 * @param def
	 * @return
	 */
	public boolean getValue(String key, boolean def){
		String plain=config.getProperty(key);
		if(plain==null) return def;
		if(plain.toLowerCase().equals("true")) 	return true;
		if(plain.toLowerCase().equals("false")) return false;
		if(plain.toLowerCase().equals("on")) 	return true;
		if(plain.toLowerCase().equals("off")) 	return false;
		int value=Integer.parseInt(plain);
		return value==1;
	}
	
	/**
	 * Sets a value in the current config.
	 * @param key
	 * @param value
	 */
	public void setValue(String key, String value){
		config.setProperty(key, value);
	}
	
	/**
	 * Sets a boolean value in the config.
	 * @param key
	 * @param value
	 */
	public void setValue(String key, boolean value){
		config.setProperty(key, value?"true":"false");
	}
	
	/**
	 * Sets an int value to config.
	 * @param key
	 * @param value
	 */
	public void setValue(String key, int value){
		config.setProperty(key, value+"");
	}
	
	/**
	 * for testing only!
	 * @param args
	 */
	public static void main(String[] args) {
		Config.getInstance();
	}
}
