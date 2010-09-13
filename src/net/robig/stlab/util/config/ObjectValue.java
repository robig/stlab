package net.robig.stlab.util.config;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ObjectValue extends AbstractValue<Object> {

	static private JSONSerializer serializer = new JSONSerializer();
	static private JSONDeserializer<Object> deserializer = new JSONDeserializer<Object>();
	
	public ObjectValue(String key, Object value) {
		super(key, value);
	}

	@Override
	public String toString() {
		return serializer.deepSerialize(value);
	}
	
	@Override
	public void fromString(String s) {
		value=deserializer.deserialize(s);
	}

}
