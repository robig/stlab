package net.robig.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import net.robig.logging.Logger;
import net.robig.net.XmlParser.XmlElement;
import net.robig.stlab.StLabConfig;
import net.robig.stlab.model.InvalidXmlException;
import net.robig.stlab.model.StPreset;
import net.robig.stlab.model.WebPreset;
import net.robig.stlab.model.WebVote;

/**
 * Does all xml based communication with the StLab Web Server
 * @author robig
 *
 */
public class WebAccess {
	
	public interface SearchCondition{
		public Hashtable<String, String> getParameters();
	}
	
	private int userId=0;
	private String user="";
	private String pass="";
	private String session="";
	private boolean loggedIn=false;
	private String message="";
	
	private Logger log= new Logger(this);
	
	public WebAccess() {
	}
	
	/**
	 * gets last error message found by <b>findError</b>
	 * @return
	 */
	public String getMessage(){
		return message;
	}
	
	/**
	 * @return true if you're successfully logged in to server
	 */
	public boolean isLoggedIn(){
		return loggedIn;
	}
	
	/**
	 * helper method to find error tag in XML
	 * @param xml
	 */
	public void findError(XmlElement xml){
		List<XmlElement> errs=xml.find("error");
		if(errs.size()==1){
			message=errs.get(0).getText();
			log.error("Web error: "+message);
		}
	}
	
	/**
	 * Loads a Preset from the server by its ID with creator info and first 10 votes
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public StPreset load(int id) throws IOException{
		HttpRequest http=new HttpRequest(StLabConfig.getWebUrl()+"view.php?id="+id+"&session="+session);
		try {
			http.requestXml();
		} catch(Exception ex){
			log.error("Cannot parse page "+ex.getLocalizedMessage());
			return null;
		}
		try {
			XmlElement presetElement = http.findXmlTags("vote").get(0);
			if(presetElement!=null){
				String title=presetElement.getAttribute("title");
				String data=presetElement.find("data").get(0).getText();
				StPreset p=new StPreset();
				p.parseParameters(data);
				p.setTitle(title);
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
	
	/**
	 * @deprecated use find
	 * @return
	 */
	public List<WebPreset> list(){
		return find(null);
	}
	
	/**
	 * Find presets defined by a SearchCondition
	 * @param c
	 * @return
	 */
	public List<WebPreset> find(SearchCondition c){
		log.info("starting search "+c);
		List<WebPreset> result=new ArrayList<WebPreset>();
		HttpRequest http=new HttpRequest(StLabConfig.getWebUrl()+"find.php");
		try {
			Hashtable<String,String> params=c.getParameters();
			params.put("session", session);
			http.postXmlRequest(params);
		} catch(Exception ex){
			log.error("Cannot parse page "+ex.getMessage());
			return null;
		}
		try {
			List<XmlElement> errs=http.findXmlTags("error");
			if(errs.size()==1){
				message=errs.get(0).getText();
				log.error("find failed: "+message);
			}
			List<XmlElement> presets = http.findXmlTags("preset");
			log.debug("found "+presets.size()+" presets");
			int i=0;
			for(XmlElement presetElement: presets){
				if(presetElement!=null){
					WebPreset wp=WebPreset.fromXml(presetElement);
					result.add(wp);
					log.debug("result #"+(++i)+" title="+wp.getTitle()+" desc="+wp.getDescription()+" created="+wp.getCreatedFormated()+" rating="+wp.getRating());
				}
			}
			return result;
		}catch(InvalidXmlException ex){
			log.error("Document parse error "+ex+" "+ex.getMessage());
		}
		return null;
	}
	
	public List<WebPreset> getMyShares(SearchCondition c){
		if(!isLoggedIn() || c==null) return null;
		c.getParameters().put("searchUserId",userId+"");
		return find(c);
	}
	
	/** 
	 * Register a new User
	 * @param username
	 * @param pass
	 * @param mail
	 * @return
	 */
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
	
	/**
	 * Publish a preset: send its data to the server
	 * @param preset
	 * @return
	 */
	public boolean publish(WebPreset preset){
		HttpRequest http=new HttpRequest(StLabConfig.getWebUrl()+"publish.php");
		Hashtable<String,String> table=new Hashtable<String, String>();
//		table.put("username", user);
		table.put("session", session);
		table.put("title", preset.getTitle());
		table.put("preset.data", preset.getData().encodeData());
		table.put("data_version", preset.getData().getDataVersion()+"");
		table.put("time_created", preset.getCreated().toString());
		Properties author=preset.getData().getAuthorInfo();
		for(Object k: author.keySet()){
			table.put("preset.author."+k.toString(), author.getProperty((String) k));
		}
		table.put("description",preset.getDescription());
		table.put("tags", preset.getTags());
		
		try {
			http.postXmlRequest(table);
			if(http.findXmlTags("success").size()==1){
				return true;
			}else{
				List<XmlElement> errs=http.findXmlTags("error");
				if(errs.size()==1){
					message=errs.get(0).getText();
					log.error("Publish failed: "+message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace(log.getWarnPrintWriter());
		}
		return false;
	}
	
	/**
	 * Login with a user and a password
	 * @param user
	 * @param pass
	 * @return
	 */
	public boolean login(String user, String pass){
		this.user=user;
		this.pass=pass;
		return relogin();
	}
	
	/**
	 * Relogin with last user, password
	 * @return
	 */
	public boolean relogin() {
		HttpRequest http=new HttpRequest(StLabConfig.getWebUrl()+"login.php");
		Hashtable<String,String> table=new Hashtable<String, String>();
		table.put("username", user);
		table.put("password", pass);
		try {
			http.postXmlRequest(table);
			if(http.findXmlTags("success").size()==1){
				String sess=http.findXmlTags("session").get(0).getText();
				int id=Integer.parseInt(http.findXmlTags("userid").get(0).getText());
				log.info("Logged in as "+user);
				this.session=sess;
				this.userId=id;
				log.debug("Using session "+session+" logged in as user_id #"+userId);
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
	
	/**
	 * Vote for a preset
	 * @param preset
	 * @param vote
	 * @param comment
	 * @return
	 */
	public boolean vote(WebPreset preset, int vote, String comment) {
		HttpRequest http=new HttpRequest(StLabConfig.getWebUrl()+"vote.php");
		Hashtable<String,String> table=new Hashtable<String, String>();
		table.put("preset_id", preset.getId()+"");
		table.put("session", session);
		table.put("vote", ""+vote);
		table.put("comment", comment);
		table.put("time_created", new Date().toString());
		try {
			http.postXmlRequest(table);
			if(http.findXmlTags("success").size()==1){
				return true;
			}else{
				List<XmlElement> errs=http.findXmlTags("error");
				if(errs.size()==1){
					message=errs.get(0).getText();
					log.error("Vote failed: "+message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace(log.getWarnPrintWriter());
		}
		return false;
	}
	
	public List<WebVote> loadVotes(WebPreset p, int page){
		if(p==null) return null;
		HttpRequest http=new HttpRequest(StLabConfig.getWebUrl()+"votes.php?preset="+p.getId()+"&page="+page);
		try {
			http.requestXml();
		} catch(Exception ex){
			log.error("Cannot parse page "+ex.getMessage());
			return null;
		}
		List<WebVote> ret=null;
		try {
			ret=new ArrayList<WebVote>();
			for(XmlElement e: http.findXmlTags("vote")){
				ret.add(WebVote.fromXml(e));
			}
			findError(http.findXmlTags("response").get(0));
		}catch(IndexOutOfBoundsException ex){
			log.error("Document parse error "+ex.getMessage());
		}catch(NullPointerException ex){
			log.error("Document parse error "+ex.getMessage());
		} catch (InvalidXmlException ex) {
			log.error("Document parse error "+ex.getMessage());
		}
		
		return ret;
	}
	
	/**
	 * gets the userid from the current login or 0 if not logged in
	 * @return
	 */
	public int getUserId(){
		return userId;
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
