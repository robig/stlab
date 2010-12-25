package net.robig.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import net.robig.logging.Logger;
import net.robig.net.XmlParser.XmlElement;
import net.robig.stlab.StLabConfig;
import net.robig.stlab.model.InvalidXmlException;
import net.robig.stlab.model.StPreset;
import net.robig.stlab.model.WebPreset;

public class WebAccess {
	
	public interface SearchCondition{
		
	}
	
	private String user="";
	private String pass="";
	private String session="";
	private boolean loggedIn=false;
	private String message="";
	
	Logger log= new Logger(this);
	
	public WebAccess() {
	}
	
	public String getMessage(){
		return message;
	}
	
	public boolean isLoggedIn(){
		return loggedIn;
	}
	
	private void findError(XmlElement xml){
		List<XmlElement> errs=xml.find("error");
		if(errs.size()==1){
			message=errs.get(0).getText();
			log.error("Web error: "+message);
		}
	}
	
	public StPreset load(int id) throws IOException{
		HttpRequest http=new HttpRequest(StLabConfig.getWebUrl()+"view.php?id="+id+"&session="+session);
		try {
			http.requestXml();
		} catch(Exception ex){
			log.error("Cannot parse page "+ex.getLocalizedMessage());
			return null;
		}
		try {
			XmlElement presetElement = http.findXmlTags("preset").get(0);
			if(presetElement!=null){
				String title=presetElement.getAttribute("title");
				String data=presetElement.find("data").get(0).getText();
				StPreset p=new StPreset();
				p.parseParameters(data);
				p.setName(title);
				return p;
					
			}
//			findError(http.findXmlTags("response"));
		}catch(IndexOutOfBoundsException ex){
			log.error("Document parse error "+ex.getLocalizedMessage());
		}catch(NullPointerException ex){
			log.error("Document parse error "+ex.getLocalizedMessage());
		}
		
		return null;
	}
	
	
	public List<WebPreset> list(){
		return find(null);
	}
	
	public List<WebPreset> find(SearchCondition c){
		List<WebPreset> result=new ArrayList<WebPreset>();
		HttpRequest http=new HttpRequest(StLabConfig.getWebUrl()+"find.php");
		try {
			http.requestXml();
		} catch(Exception ex){
			log.error("Cannot parse page "+ex.getLocalizedMessage());
			return null;
		}
		try {
			List<XmlElement> presets = http.findXmlTags("preset");
			for(XmlElement presetElement: presets){
				if(presetElement!=null){
					WebPreset wp=WebPreset.fromXml(presetElement);
					result.add(wp);
				}
			}
			return result;
		}catch(InvalidXmlException ex){
			log.error("Document parse error "+ex.getMessage());
		}
		return null;
	}
	
	public boolean register(String username, String pass, String mail) {
		HttpRequest http=new HttpRequest(StLabConfig.getWebUrl()+"register.php");
		Hashtable<String,String> table=new Hashtable<String, String>();
		table.put("username", username);
		table.put("password", pass);
		table.put("email", mail);
		try {
			http.postXmlRequest(table);
			if(http.findXmlTags("success").size()==1){
				this.user=username;
				this.pass=pass;
				//TODO: save/login
				return true;
			}else{
				List<XmlElement> errs=http.findXmlTags("error");
				if(errs.size()==1){
					message=errs.get(0).getText();
					log.error("Login failed: "+message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace(log.getWarnPrintWriter());
		}
		return false;
	}
	
	public boolean publish(WebPreset preset){
		HttpRequest http=new HttpRequest(StLabConfig.getWebUrl()+"publish.php");
		Hashtable<String,String> table=new Hashtable<String, String>();
//		table.put("username", user);
		table.put("session", session);
		table.put("title", preset.getTitle());
		table.put("preset.data", preset.getData().encodeData());
		table.put("preset.dataversion", preset.getData().getDataVersion()+"");
		Properties author=preset.getData().getAuthorInfo();
		for(Object k: author.keySet()){
			table.put("preset.author."+k.toString(), author.getProperty((String) k));
		}
		table.put("descrition",preset.getDescription());
		table.put("tags", preset.getTags());
		
		try {
			http.postXmlRequest(table);
			if(http.findXmlTags("success").size()==1){
				return true;
			}else{
				List<XmlElement> errs=http.findXmlTags("error");
				if(errs.size()==1){
					message=errs.get(0).getText();
					log.error("Login failed: "+message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace(log.getWarnPrintWriter());
		}
		return false;
	}
	
	public boolean login(String user, String pass){
		this.user=user;
		this.pass=pass;
		return relogin();
	}
	
	public boolean relogin() {
		HttpRequest http=new HttpRequest(StLabConfig.getWebUrl()+"login.php");
		Hashtable<String,String> table=new Hashtable<String, String>();
		table.put("username", user);
		table.put("password", pass);
		try {
			http.postXmlRequest(table);
			if(http.findXmlTags("success").size()==1){
				String sess=http.findXmlTags("session").get(0).getText();
				log.info("Logged in as "+user);
				this.session=sess;
				log.debug("Using session "+session);
				loggedIn=true;
				return true;
			}else{
				List<XmlElement> errs=http.findXmlTags("error");
				if(errs.size()==1){
					message=errs.get(0).getText();
					log.error("Login failed: "+message);
				}
			}
		} catch (Exception e) {
			log.error("Login failed with an exception!");
			e.printStackTrace(log.getWarnPrintWriter());
		}
		return false;
	}
	
	public boolean vote(WebPreset preset, int vote, String comment) {
		HttpRequest http=new HttpRequest(StLabConfig.getWebUrl()+"vote.php");
		Hashtable<String,String> table=new Hashtable<String, String>();
		table.put("preset_id", preset.getId()+"");
		table.put("session", session);
		table.put("vote", ""+vote);
		table.put("comment", comment);
		try {
			http.postXmlRequest(table);
			if(http.findXmlTags("success").size()==1){
				return true;
			}else{
				List<XmlElement> errs=http.findXmlTags("error");
				if(errs.size()==1){
					message=errs.get(0).getText();
					log.error("Login failed: "+message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace(log.getWarnPrintWriter());
		}
		return false;
	}
	
	public static void main(String[] args) throws IOException {
//		System.getProperties().put( "proxySet", "true" );
//		System.getProperties().put( "proxyHost", "192.168.100.2" );
//		System.getProperties().put( "proxyPort", "8080" );
		
//		System.out.println(new WebAccess().load(1));
//		new WebAccess().register("robig","08150815","bla@blub");
		WebAccess wa=new WebAccess();
		System.out.println(wa.login("robig","08150815"));
//		StPreset p1=new StPreset();
//		p1.setAmp(9); p1.setVolume(99); p1.setTreble(70);
//		p1.setName("Test preset2");
//		wa.publish(p1);
//		WebPreset wp=wa.find(null).get(1);
//		wa.vote(wp, 4, "Ein Test");
		System.out.println(wa.find(null));
	}
}
