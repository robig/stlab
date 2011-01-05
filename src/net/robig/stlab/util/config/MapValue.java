package net.robig.stlab.util.config;

import java.util.LinkedHashMap;
import java.util.Set;
import net.robig.logging.Logger;

/**
 * Saves a key-value map of StringValues
 * @author robig
 *
 */
public class MapValue extends AbstractValue<LinkedHashMap<String,StringValue>> {

	String prefix=null;
	Logger log=new Logger(this);
	LinkedHashMap<String,StringValue> map = null;
	
	public MapValue(String prefix) {
		super(prefix);
		map=new LinkedHashMap<String,StringValue>();
		
		this.prefix=prefix;
		load();
	}

	public void load() {
		log.debug("requesting config keys for "+prefix);
		Set<Object> nameKeys=ObjectConfig.getInstance().filterProperties(prefix+".names").keySet();
		for(Object k: nameKeys){
			String key=k.toString();
			log.debug("key: "+key);
			String keyBase=key.replace(prefix+".names","");
			String name=ObjectConfig.getInstance().getValue(key, "");
			log.debug("Requesting value for key: "+prefix+".values"+keyBase);
			StringValue value=ObjectConfig.getStringValue(prefix+".values"+keyBase, "");
			if(value!=null && !value.equals("")){
				map.put(name, value);
			}
		}
	}
	
	/** write the changes */
	public void save() {
		ObjectConfig.writeConfig(); //Write changes and notify listeners
	}
	
	/** get all keys */
	public synchronized Set<String> keySet() {
		return map.keySet();
	}
	
	/** Get a value for a key */
	public synchronized StringValue get(String name){
		StringValue value=map.get(name);
		return value;
	}
	
	/** add a StringValue for a Key */
	public synchronized void add(String name, StringValue value){
		int i=map.size();
		Set<Object> keys=ObjectConfig.getInstance().filterProperties(prefix+".values.").keySet();
		value.key=prefix+".values."+i;
		boolean keyExists=true;
		do {
			keyExists=false;
			for(Object k: keys){
				if(k.toString().equals(value.key)) {keyExists=true;break;}
			}
			if(keyExists){
				value.key=prefix+".values."+i;
				i++;
			}
		} while(keyExists);
		ObjectConfig.getInstance().setValue(prefix+".names."+i, name);
		map.put(name, value);
		save();
	}
	
	/** add a String for a value (and make a StringValue) */
	public void add(String name, String value){
		add(name, new StringValue("",value));
	}

	/** remove a key-value pair by key */
	public void remove(String name){
		StringValue v=get(name);
		if(v!=null){
			log.debug("Removing name: "+name);
			map.remove(name);
			ObjectConfig.remove(v.key);
			ObjectConfig.remove(v.key.replace(".values.", ".names."));
		}
	}
	
	public void postSetValue() {
		save();
	}
}
