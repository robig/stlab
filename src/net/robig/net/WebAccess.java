package net.robig.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import net.robig.logging.Logger;
import net.robig.net.XmlParser.XmlElement;
import net.robig.stlab.StLabConfig;
import net.robig.stlab.model.StPreset;

public class WebAccess {
	
	public class SearchCondition{
		
	}
	
	private String user="";
	private String pass="";
	private String session="";
	
	Logger log= new Logger(this);
	
	public WebAccess() {
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
		}catch(IndexOutOfBoundsException ex){
			log.error("Document parse error "+ex.getLocalizedMessage());
		}catch(NullPointerException ex){
			log.error("Document parse error "+ex.getLocalizedMessage());
		}
		return null;
	}
	
	
	public List<StPreset> list(){
		return find(null);
	}
	
	public List<StPreset> find(SearchCondition c){
		List<StPreset> result=new ArrayList<StPreset>();
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
					String title=presetElement.getAttribute("title");
					String data=presetElement.find("data").get(0).getText();
					StPreset p=new StPreset();
					p.parseParameters(data);
					p.setName(title);
					result.add(p);
				}
			}
			return result;
		}catch(ArrayIndexOutOfBoundsException ex){
			log.error("Document parse error "+ex.getLocalizedMessage());
		}catch(NullPointerException ex){
			log.error("Document parse error "+ex.getLocalizedMessage());
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
			}
		} catch (Exception e) {
			e.printStackTrace(log.getWarnPrintWriter());
		}
		return false;
	}
	
	public boolean publish(StPreset preset){
		HttpRequest http=new HttpRequest(StLabConfig.getWebUrl()+"publish.php");
		Hashtable<String,String> table=new Hashtable<String, String>();
//		table.put("username", user);
		table.put("session", session);
		table.put("title", preset.getName());
		table.put("preset.data", preset.encodeData());
		table.put("preset.dataversion", preset.getDataVersion()+"");
		Properties author=preset.getAuthorInfo();
		for(Object k: author.keySet()){
			table.put("preset.author."+k.toString(), author.getProperty((String) k));
		}
		table.put("session", session);
		try {
			http.postXmlRequest(table);
			if(http.findXmlTags("success").size()==1){
				return true;
			}else{
				List<XmlElement> errs=http.findXmlTags("error");
				if(errs.size()==1){
					log.error("Login failed: "+errs.get(0).getText());
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
				return true;
			}else{
				List<XmlElement> errs=http.findXmlTags("error");
				if(errs.size()==1){
					log.error("Login failed: "+errs.get(0).getText());
				}
			}
		} catch (Exception e) {
			log.error("Login failed with an exception!");
			e.printStackTrace(log.getWarnPrintWriter());
		}
		return false;
	}
	
	public void vote() {
		
	}
	
	public static void main(String[] args) throws IOException {
//		System.getProperties().put( "proxySet", "true" );
//		System.getProperties().put( "proxyHost", "192.168.100.2" );
//		System.getProperties().put( "proxyPort", "8080" );
		
//		System.out.println(new WebAccess().load(1));
//		new WebAccess().register("robig","08150815","bla@blub");
		WebAccess wa=new WebAccess();
		System.out.println(wa.login("robig","08150815"));
		StPreset p1=new StPreset();
		p1.setName("Test p1");
		wa.publish(p1);
	}
}
