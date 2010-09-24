package net.robig.stlab.util.config;

import java.util.HashMap;
import java.util.Set;

import net.robig.logging.Logger;
import net.robig.stlab.util.Config;

public class MapValue extends AbstractValue<HashMap<String,StringValue>> {

	String prefix=null;
	Logger log=new Logger(this);
	HashMap<String,StringValue> map = null;
	
	public MapValue(String prefix) {
		super(prefix);
		map=new HashMap<String,StringValue>();
		
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
				add(name,value);
			}
		}
	}
	
	public synchronized void save() {
		int i=0;
		for(String name: map.keySet()){
			String keyBase=prefix+".names."+i;
			StringValue value=map.get(name);
			if(value!=null){
				log.debug("name: "+name+" base: "+keyBase);
				ObjectConfig.getInstance().setValue(keyBase, name);
			}
			i++;
		}
		Config.getInstance().saveConfig();
	}
	
	public synchronized Set<String> keySet() {
		return map.keySet();
	}
	
	public synchronized StringValue get(String name){
		StringValue value=map.get(name);
		return value;
	}
	
	public synchronized void add(String name, StringValue value){
		if(!value.key.startsWith(prefix))
			value.key=prefix+".values."+map.size();
		map.put(name, value);
		save();
	}
	
	public void add(String name, String value){
		add(name, new StringValue("",value));
	}

	public void postSetValue() {
		save();
	}
}
