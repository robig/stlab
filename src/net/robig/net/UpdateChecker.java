package net.robig.net;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import net.robig.logging.Logger;
import net.robig.net.XmlParser.XmlElement;
import net.robig.stlab.StLab;
import net.robig.stlab.util.Browser;

public class UpdateChecker implements Runnable {
	
	protected static final String UPDATE_URL="http://sourceforge.net/api/file/index/project-id/340544/mtime/desc/rss";
	private Logger log=new Logger(UpdateChecker.class);
	private String latestVersion = getCurrentVersion();
	private String latestVersionUrl = "";
	private boolean updateAvailable=false;
	// Pattern by regex tester: http://www.cis.upenn.edu/~matuszek/General/RegexTester/regex-tester.html
	private Pattern urlPattern=Pattern.compile("(?<![Ss]upport)/(\\w+-([\\w-.]+)\\.\\w+)");

	private int compareVersions(String v1, String v2){
		int r=v1.compareTo(v2);
		log.debug("Comparing version "+v1+" and "+v2+": "+r);
		return r;
	}
	
	public String getCurrentVersion() {
		//return "0.0";
		return StLab.applicationVersion;
	}
	
	public String getApplicationName() {
		return StLab.applicationName;
	}
	
	public String getDownloadPage(){
		return "http://robig.net/wiki/?wiki=EnStLab";
	}
	
	public void checkForUpdates() {
		int updateCompValue=0;
		log.info("Checking for updates...");
		Map<String,String> versions=getVersions();
		if(versions==null) return;
		for(String version:versions.keySet()){
			int c=compareVersions(version, getCurrentVersion());
			if(c>0){
				log.info("found newer Version: "+version+" ("+c+")");
				if(c>updateCompValue){
					updateCompValue=c;
					latestVersion=version;
					latestVersionUrl=versions.get(version);
					updateAvailable=true;
				}
			}
		}
		if(!isUpdateAvailable()){
			log.info("No updated version available.");
		}
//		compareVersions("0.2", getCurrentVersion());
//		compareVersions("0.3", getCurrentVersion());
//		compareVersions("0.1.2", getCurrentVersion());
//		compareVersions(getCurrentVersion(),getCurrentVersion());
//		compareVersions("0.1-SNAPSHOT",version);
	}
	
	public boolean isUpdateAvailable() {
		return updateAvailable;
	}
	
	public String getLatestVersion() {
		return latestVersion;
	}
	
	public String getLatestVersionUrl() {
		return latestVersionUrl;
	}
	
	public Map<String,String> getVersions() {
		Hashtable<String,String> ret= new Hashtable<String,String>();
		HttpRequest request = new HttpRequest(UPDATE_URL);
		try {
			request.requestXml();
		} catch (Exception e1) {
			log.warn("Error connecting to update site: "+e1.getMessage());
			return null;
		}
		List<XmlElement> xml = request.findXmlTags("media:content");

		for(XmlElement e: xml){
			String url=(String) e.getAttribute("url");
			String version=matchUrl(url);
			if(version!=null){
				ret.put(version,url);
			}
		}
		return ret;
	}
	
	public String matchUrl(String url){
		Matcher m = urlPattern.matcher(url);
		boolean matched=m.find();
//		log.debug("trying url "+url+" to match "+urlPattern.toString()+": "+matched);
		if(matched){
			String version=m.group(2);
			String file=m.group(1);
			log.debug("found File: "+file+" v"+version);
			return version;
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		UpdateChecker uc=new UpdateChecker();
		
		String v=uc.matchUrl("http://sourceforge.net/project/stlab/files/stlab-0.3.zip/download");
		assert v!=null;
		
		v=uc.matchUrl("http://sourceforge.net/project/stlab/files/Support/stlab-0.3.zip/download");
		assert v==null;
		
//		System.getProperties().put( "proxySet", "true" );
//		System.getProperties().put( "proxyHost", "192.168.100.2" );
//		System.getProperties().put( "proxyPort", "8080" );
		
		new UpdateChecker().run();
	}

	@Override
	public void run() {
		checkForUpdates();
		if(isUpdateAvailable()){
			//Custom button text
			Object[] options = {"Yes, please",
			                    "No, thanks"
			                    };
			int n = JOptionPane.showOptionDialog(null,
			    "Update "+getApplicationName()+" Version "+getLatestVersion()+" is available!\n"
			    + "Do you want to open your Browser with the download page?",
			    getApplicationName()+" Update available",
			    JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    options,
			    options[0]);
			if(n==JOptionPane.YES_OPTION){
				Browser.getInstance().browse(getDownloadPage());
			}

		}
	}
	
}
