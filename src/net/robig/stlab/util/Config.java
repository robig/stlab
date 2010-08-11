package net.robig.stlab.util;

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
	private static final String configFile="config.properties";
	private Logger log=new Logger(this);
	private static Config instance = null;
	private Properties config = null;

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

	private void initialize() {
		String path = configFile;
		log.debug("Loading config from: "+path+":");
		Enumeration<URL> e;
		try {
			e = getClass().getClassLoader().getResources(path);
			while (e.hasMoreElements()) {
				URL file=e.nextElement();
				log.debug(" found logfile: "+file);
				addConfigFile(file.toString());
			}
		} catch (IOException e1) {
			log.warn("Exception when loading config file(s)!");
			e1.printStackTrace(log.getDebugPrintWriter());
		}
	}
	
	/**
	 * Parse another config file (in properties format) and merge the existing properties with it.
	 * (existing values will be overwritten by later added files)
	 * @param file
	 */
	public void addConfigFile(String file){
		log.info("Loading config file:"+file);
		Properties newProps = PropertyUtil.loadProperties(file);
		if(config==null) config=newProps;
		else config=PropertyUtil.mergeProperties(config, newProps);
	}
	
	public String getValue(String key,String dflt){
		return config.getProperty(key,dflt);
	}
	
	public int getValue(String key, int def){
		String plain=config.getProperty(key);
		if(plain==null) return def;
		int value=Integer.parseInt(plain);
		return value;
	}
	
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
	 * for testing only!
	 * @param args
	 */
	public static void main(String[] args) {
		Config.getInstance();
	}
}
