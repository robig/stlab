package net.robig.net;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nanoxml.XMLElement;
import net.robig.logging.Logger;
import net.robig.stlab.StLab;

public class UpdateChecker {
	private Logger log=new Logger(UpdateChecker.class);

	private int compareVersions(String v1, String v2){
		int r=v1.compareTo(v2);
		log.debug("Comparing version "+v1+" and "+v2+": "+r);
		return r;
	}
	
	public String getCurrentVersion() {
		return StLab.applicationVersion;
	}
	
	public void checkForUpdates() {
		for(String version:getVersions().keySet()){
			log.debug("processing Version: "+version+" "+compareVersions(version, getCurrentVersion()));
		}
		String version="0.2";
		compareVersions(version, getCurrentVersion());
		compareVersions(getCurrentVersion(),getCurrentVersion());
		compareVersions("0.1-SNAPSHOT",version);
	}
	
	
	
	public Map<String,String> getVersions() {
		Hashtable<String,String> ret= new Hashtable<String,String>();
		HttpRequest request = new HttpRequest("http://sourceforge.net/api/file/index/project-id/340544/mtime/desc/rss");
		request.requestXml();
		List<XMLElement> xml = request.findXmlTags("media:content");
		Pattern p = Pattern.compile("\\/(\\w*-(.*).\\w*)\\/download");
		for(XMLElement e: xml){
			String url=(String) e.getAttribute("url");
			Matcher m = p.matcher(url);
			if(m.matches()){
				String version=m.group(2);
				String file=m.group(1);
				log.debug("found File: "+file+" v"+version);
				ret.put(version,url);
			}
//			String[] pathList=url.split("/");
//			if(pathList.length>1 && pathList[pathList.length-1].equals("download")){
//				String file=pathList[pathList.length-2];
//				String fileNameList[]=file.replace(target, replacement)
//				if(fileNameList.length > 1){
//					String[] fileList=fileNameList[0].split("-",2);
//					if(fileList.length>1){
//						String version=fileList[1];
//						log.debug("found File: "+file+" v"+version);
//						ret.put(version, url);
//					}
//				}
//			}
		}
		return ret;
	}
	
	public static void main(String[] args) {
		
		System.getProperties().put( "proxySet", "true" );
		System.getProperties().put( "proxyHost", "192.168.100.2" );
		System.getProperties().put( "proxyPort", "8080" );
		
		new UpdateChecker().checkForUpdates();
	}
	
}
