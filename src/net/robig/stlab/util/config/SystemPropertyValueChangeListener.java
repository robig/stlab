package net.robig.stlab.util.config;

import net.robig.logging.Logger;

public class SystemPropertyValueChangeListener implements IValueChangeListener {
	private String propertyName=null;
	private Logger log = new Logger(this);
	public SystemPropertyValueChangeListener(String property) {
		propertyName=property;
	}
	
	@Override
	public void valueChanged(AbstractValue v) {
		log.info("Setting proxy port: "+v.toString());
		System.getProperties().put( propertyName, v.toString() );
	}

}
