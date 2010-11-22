package net.robig.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import net.robig.logging.Logger;
import net.robig.net.XmlParser.XmlElement;
import net.robig.stlab.StLabConfig;
import net.robig.stlab.model.StPreset;

public class WebAccess {
	
	public class SearchCondition{
		
	}
	
	private String user="";
	private String pass="";
	Logger log= new Logger(this);
	
	public WebAccess() {
	}
	
	
	public StPreset load(int id) throws IOException{
		HttpRequest http=new HttpRequest(StLabConfig.getWebUrl()+"view.php?id="+id);
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
	
	public void login() {
		
	}
	
	public void vote() {
		
	}
	
	public static void main(String[] args) throws IOException {
//		System.getProperties().put( "proxySet", "true" );
//		System.getProperties().put( "proxyHost", "192.168.100.2" );
//		System.getProperties().put( "proxyPort", "8080" );
		
//		System.out.println(new WebAccess().load(1));
		new WebAccess().register("robig","08150815","bla@blub");
	}
}
