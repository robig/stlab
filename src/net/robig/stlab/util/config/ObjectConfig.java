package net.robig.stlab.util.config;

import java.util.HashMap;

import net.robig.logging.Logger;
import net.robig.stlab.util.Config;

public class ObjectConfig extends Config {
	private static HashMap<String,Object> objectMap = new HashMap<String,Object>();
	private static Logger log=new Logger(ObjectConfig.class);
	
	public static IntValue getIntValue(String key,int def) {
		int value=getInstance().getValue(key,def);
		IntValue object=(IntValue)objectMap.get(key);
		if(object != null){
			object.value=value;
		}else{
			object = new IntValue(key,value);
			objectMap.put(key, object);
		}
		return object;
	}
	
	/**
	 * Saves config changes to disk.
	 */
	public static void writeConfig() {
		getInstance().saveConfig();
	}
	
	@Override
	public void saveConfig() {
		for(String key: objectMap.keySet()){
			if(!getAllProperties().containsKey(key)){
				Object value=objectMap.get(key);
				setValue(key,value.toString());
			}
		}
		super.saveConfig();
	}
	
	public static void setIntValue(String key, int value){
		IntValue object=(IntValue)objectMap.get(key);
		if(object != null){
			object.value=value;
		}else{
			object = new IntValue(key,value);
			objectMap.put(key, object);
		}
		getInstance().setValue(key,value);
		writeConfig();
	}
	
	public static StringValue getStringValue(String key,String def) {
		String value=getInstance().getValue(key,def);
		StringValue object=(StringValue)objectMap.get(key);
		if(object != null){
			object.setValue(value);
		}else{
			object = new StringValue();
			object.setValue(value);
			objectMap.put(key, object);
		}
		return object;
	}
	
	public static void setStringValue(String key, String value) {
		StringValue object=(StringValue)objectMap.get(key);
		if(object != null){
			object.setValue(value);
		}else{
			object = new StringValue();
			object.setValue(value);
			objectMap.put(key, object);
		}
		getInstance().setValue(key,value);
		writeConfig();
	}
	
	public static LongValue getLongValue(String key,long def) {
		long value=Long.parseLong(getInstance().getValue(key,""+def));
		LongValue object=(LongValue)objectMap.get(key);
		if(object != null){
			object.setValue(value);
		}else{
			object = new LongValue();
			object.setValue(value);
			objectMap.put(key, object);
		}
		return object;
	}
	
	public static void setLongValue(String key, long value){
		LongValue object=(LongValue)objectMap.get(key);
		if(object != null){
			object.setValue(value);
		}else{
			object = new LongValue();
			object.setValue(value);
			objectMap.put(key, object);
		}
		getInstance().setValue(key,""+value);
		writeConfig();
	}
	
	public static BoolValue getBoolValue(String key,boolean def) {
		boolean value=getInstance().getValue(key,def);
		BoolValue object=(BoolValue)objectMap.get(key);
		if(object != null){
			object.value=value;
		}else{
			object = new BoolValue(key,value);
			objectMap.put(key, object);
		}
		return object;
	}
	
	public static void setBoolValue(String key,boolean value) {
		BoolValue object=(BoolValue)objectMap.get(key);
		if(object != null){
			object.value=value;
		}else{
			object = new BoolValue(key,value);
			objectMap.put(key, object);
		}
		getInstance().setValue(key,value);
		writeConfig();
	}
	
	public static DoubleValue getDoubleValue(String key,double def) {
		
		DoubleValue object=(DoubleValue)objectMap.get(key);
		if(object != null){
			return object;
		}else{
			double value=Double.parseDouble(getInstance().getValue(key,""+def));
			object = new DoubleValue();
			object.value=value;
			objectMap.put(key, object);
		}
		return object;
	}
	
	public static void setDoubleValue(String key,double value) {
		DoubleValue object=(DoubleValue)objectMap.get(key);
		if(object != null){
			object.setValue(value);
		}else{
			object = new DoubleValue();
			object.setValue(value);
			objectMap.put(key, object);
		}
		getInstance().setValue(key,""+value);
		writeConfig();
	}
	
	@SuppressWarnings("unchecked")
	public static AbstractValue getAbstractValue(String key, AbstractValue def){
		try{
			AbstractValue object=(AbstractValue) objectMap.get(key);
			if(object == null){
				object = def;
				String data=getInstance().getValue(key, null);
				if(data!=null)
					def.fromString(data);
				objectMap.put(key, object);
			}
			return object;
		}catch(Exception e){
			log.error("getting abstract value for key "
					+key+" failed! "+e.getMessage());
			e.printStackTrace(log.getWarnPrintWriter());
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static void setAbstractValue(String key, AbstractValue value){
		try{
			AbstractValue object=(AbstractValue) objectMap.get(key);
			if(object == null){
				object = value;
				objectMap.put(key, object);
			}
			object.value=value.value;
			getInstance().setValue(key, object.toString());
			writeConfig();
		}catch(Exception e){
			log.error("setting abstract value for key "
					+key+" failed! "+e.getMessage());
			e.printStackTrace(log.getWarnPrintWriter());
		}
	}
}
