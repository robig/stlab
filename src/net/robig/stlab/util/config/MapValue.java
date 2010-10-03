package net.robig.stlab.util.config;

import java.util.LinkedHashMap;
import java.util.Set;
import net.robig.logging.Logger;

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
		Set<String> nameKeys=ObjectConfig.getInstance().filterProperties(prefix+".names").stringPropertyNames();
		for(String key: nameKeys){
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
	
	public void save() {
		ObjectConfig.writeConfig(); //Write changes and notify listeners
	}
	
	public synchronized Set<String> keySet() {
		return map.keySet();
	}
	
	public synchronized StringValue get(String name){
		StringValue value=map.get(name);
		return value;
	}
	
	public synchronized void add(String name, StringValue value){
		int i=map.size();
		Set<String> keys=ObjectConfig.getInstance().filterProperties(prefix+".values.").stringPropertyNames();
		value.key=prefix+".values."+i;
		while(keys.contains(value.key)) {
			value.key=prefix+".values."+i;
			i++;
		}
		ObjectConfig.getInstance().setValue(prefix+".names."+i, name);
		map.put(name, value);
		save();
	}
	
	public void add(String name, String value){
		add(name, new StringValue("",value));
	}

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
