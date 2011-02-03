package net.robig.stlab.util;

import java.net.URI;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;

public class Browser {
	
	private static Browser instance=null;
	
	public static Browser getInstance(){
		if(instance==null){
			new Browser();
		}
		return instance;
	}
	
	BrowserLauncher launcher = null;
	
	public Browser() {
		instance=this;
		
		try {
			launcher = new BrowserLauncher();
		} catch (BrowserLaunchingInitializingException e) {
			e.printStackTrace();
		} catch (UnsupportedOperatingSystemException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void browse(URI uri){
		if(launcher!=null){
			launcher.openURLinBrowser(uri.toString());
		}
	}
}
