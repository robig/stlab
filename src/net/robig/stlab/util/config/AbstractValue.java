package net.robig.stlab.util.config;

import java.lang.reflect.Field;

import net.robig.logging.Logger;

public abstract class AbstractValue<E extends Object> {
	public E value=null;
	public String key=null;
	private Logger log=new Logger(this);
	
	public AbstractValue(String key, E value) {
		this.value=value;
		this.key=key;
	}
	
	public void setValue(E v){
		this.value=v;
		postSetValue();
	}
	
	public E getValue(){
		return value;
	}
	
	public String toString(){
		return serializeMemberVariables(value);
	}
	public abstract void fromString(String s);
	
	public void postSetValue() {
		ObjectConfig.setAbstractValue(key, this);
	}
	
	private static String serializeMemberVariables(Object o){
		String ret=o.getClass().getName();
		for(Field f: o.getClass().getDeclaredFields()){
			try{
				ret+=";"+f.getName()+"="+
					f.toGenericString()+":"+
					f.get(o);
			}catch(Exception e){
				
			}
		}
		return ret;
	}
	
	private static Object deserializeMemberVariables(String dataString){
		String[] data=dataString.split(";");
		if(data.length>0){
			String type=data[0];
			try{
				Class<?> clazz=AbstractValue.class.getClassLoader().loadClass(type);
				Object o=clazz.newInstance();
				for(int i=1;i<data.length;i++){
					String[] mv=data[i].split("=",2);
					if(mv.length==2){
						String methodName=mv[0];
						String value=mv[1];
						clazz.getField(methodName);
					}
				}
				
			}catch(Exception e){
				
			}
		}
		return null;
	}
}
