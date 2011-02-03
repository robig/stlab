package net.robig.stlab.util;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import net.robig.logging.Logger;

public class Browser {
	
	private static Browser instance=null;
	
	public static Browser getInstance(){
		if(instance==null){
			new Browser();
		}
		return instance;
	}
	
	private Logger log = new Logger(this);
	
	public Browser() {
		instance=this;
	}
	
	public void browse(URI uri){
		try {
			Desktop.getDesktop().browse(uri);
		} catch (IOException e) {
			e.printStackTrace(log.getDebugPrintWriter());
		}
	}
	
	public void browse(String url){
		try {
			browse(new URI(url));
		} catch (URISyntaxException e) {
			e.printStackTrace(log.getWarnPrintWriter());
		}
	}
}
