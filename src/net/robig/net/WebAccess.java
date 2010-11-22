package net.robig.net;

import java.io.IOException;

import net.robig.logging.Logger;
import net.robig.stlab.StLabConfig;
import net.robig.stlab.model.StPreset;

public class WebAccess {
	
	
	Logger log= new Logger(this);
	
	public WebAccess() {
	}
	
	
	public StPreset load() throws IOException{
		HttpRequest http=new HttpRequest(StLabConfig.getWebUrl()+"view.php");
		try {
			http.requestXml();
		} catch(Exception ex){
			log.error("Cannot parse page "+ex);
			return null;
		}
		http.findXmlTags("data");
		String title=http.findXmlTags("title").get(0).getText();
		
		return null;
	}
	
	
	public static void main(String[] args) throws IOException {
		System.getProperties().put( "proxySet", "true" );
		System.getProperties().put( "proxyHost", "192.168.100.2" );
		System.getProperties().put( "proxyPort", "8080" );
		new WebAccess().load();
	}
}
