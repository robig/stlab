package net.robig.stlab.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * Some utility methods for working with Properties
 * @author robegroe
 *
 */
public class PropertyUtil {
	class SortedProperties extends Properties {
		private static final long serialVersionUID = 4134918987975160736L;

		@Override
	    public Set<Object> keySet(){
	        return Collections.unmodifiableSet(new TreeSet<Object>(super.keySet()));
	    }

	    @Override
	    public synchronized Enumeration<Object> keys() {
	        return Collections.enumeration(new TreeSet<Object>(super.keySet()));
		    }
	}

	/**
	 * Filter Properties entries by given starting String
	 * 
	 * @param props
	 * @param starting
	 * @return new Properties object with filtered keys/values
	 */
	public static Properties filterProperties(Properties props, String starting){
		Properties ret = new Properties();
		for(String key: props.stringPropertyNames()){
			if(key.startsWith(starting))
				ret.setProperty(key, props.getProperty(key));
		}
		return ret;
	}

	/**
	 * load Properties from file
	 * 
	 * @param file
	 * @return
	 */
	public static Properties loadProperties(String file) throws IOException {
		Properties props = new Properties();
	    try {
	        props.load(new FileInputStream(file));
	    } catch(IOException ex) {
	        throw ex;
	    }
	    return props;
	}
	
	/**
	 * load Properties from file
	 * 
	 * @param file
	 * @return
	 */
	public static Properties loadProperties(File file) throws IOException{
		Properties props = new Properties();
	    try {
	        props.load(new FileInputStream(file));
	    } catch(IOException ex) {
	    	throw ex;
	    }
	    return props;
	}
	
	/**
	 * Write properties to a file.
	 * @param props
	 * @param file
	 * @throws IOException
	 */
	public static void saveProperties(Properties props, File file) throws IOException{
		try {
			props.store(new FileOutputStream(file), null); 
		} catch (IOException e) {
			throw e;
		} 
	}

	/**
	 * Write properties to a file.
	 * @param props
	 * @param file
	 * @throws IOException
	 */
	public static void saveProperties(Properties props, String file) throws IOException, FileNotFoundException{
		saveProperties(props, new File(file));
	}
	
	/**
	 * merge key/value pairs of two Properties together
	 * p2 can override keys of p1
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static Properties mergeProperties(Properties p1, Properties p2){
		Properties props=p1; //TODO: clone p1
		for(String name: p2.stringPropertyNames()){
			props.setProperty(name, p2.getProperty(name));
		}
		
		return props;
	}

}
