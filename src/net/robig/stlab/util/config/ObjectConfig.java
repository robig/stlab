package net.robig.stlab.util.config;

import java.util.HashMap;

import net.robig.stlab.util.Config;

public class ObjectConfig extends Config {
	private static HashMap<String,Object> objectMap = new HashMap<String,Object>();
	
	public static IntValue getIntValue(String key,int def) {
		int value=getInstance().getValue(key,def);
		IntValue object=(IntValue)objectMap.get(key);
		if(object != null){
			object.setValue(value);
		}else{
			object = new IntValue();
			object.setValue(value);
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
	
	public static void setIntValue(String key, int value){
		IntValue object=(IntValue)objectMap.get(key);
		if(object != null){
			object.setValue(value);
		}else{
			object = new IntValue();
			object.setValue(value);
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
			object.setValue(value);
		}else{
			object = new BoolValue();
			object.setValue(value);
			objectMap.put(key, object);
		}
		return object;
	}
	
	public static void setBoolValue(String key,boolean value) {
		BoolValue object=(BoolValue)objectMap.get(key);
		if(object != null){
			object.setValue(value);
		}else{
			object = new BoolValue();
			object.setValue(value);
			objectMap.put(key, object);
		}
		getInstance().setValue(key,value);
		writeConfig();
	}
	
	public static DoubleValue getDoubleValue(String key,double def) {
		double value=Double.parseDouble(getInstance().getValue(key,""+def));
		DoubleValue object=(DoubleValue)objectMap.get(key);
		if(object != null){
			object.setValue(value);
		}else{
			object = new DoubleValue();
			object.setValue(value);
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
}
