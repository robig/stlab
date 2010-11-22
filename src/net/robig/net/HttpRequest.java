package net.robig.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.List;
import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.robig.logging.Logger;
import net.robig.net.XmlParser.XmlElement;

public class HttpRequest {
	
	Source source = null;
	XmlParser xml = null;
	String requestUrl = null;
	boolean sent=false;
	Logger log = new Logger(this.getClass());
	
	public HttpRequest(String url) {
		requestUrl=url;
	}
	
	public boolean isSent() {
		return sent;
	}
	
	public void requestXml() throws Exception {
        try {
        	log.debug("Get request to "+requestUrl);
            URL url = new URL(requestUrl.toString());
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            xml=new XmlParser(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace(log.getWarnPrintWriter());
            throw e;
        }
	}
	
	public void postXmlRequest(Hashtable<String, String> data) throws Exception{
		try {
			log.debug("Post request to "+requestUrl);
		    // Construct data
			String encData="";
			for(String key: data.keySet()){
				encData+=URLEncoder.encode(key, "UTF-8") + "=" + 
					URLEncoder.encode(data.get(key), "UTF-8")+"&";
			}
			
		    // Send data
		    URL url = new URL(requestUrl.toString());
		    URLConnection conn = url.openConnection();
		    conn.setDoOutput(true);
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(encData);
		    wr.flush();
		    wr.close();
		    
		    // Get the response
		    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    xml=new XmlParser(in);
//		    String line=null;
//		    while ((line = rd.readLine()) != null) {
//		        // Process line...
//		    }
		    in.close();
		} catch (Exception e) {
			e.printStackTrace(log.getWarnPrintWriter());
            throw e;
		}

	}
	
	public List<XmlElement> findXmlTags(String name){
		if(xml==null) return null;
		return xml.findTags(name);
	}
	
	public void requestHtml() {
        try {
            URL url = new URL(requestUrl.toString());
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
              source=new Source(in);

              sent=true;
              source.fullSequentialParse();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public String getTitle() {
		if(!isSent()) return null;
		Element titleElement=source.getFirstElement(HTMLElementName.TITLE);
		if (titleElement==null) return null;
		// TITLE element never contains other tags so just decode it collapsing whitespace:
		return CharacterReference.decodeCollapseWhiteSpace(titleElement.getContent());
	}
	
	private String[] getLinks() {
		if(!isSent()) return null;
		List<Element> elements=source.getAllElements("link");
		if (elements==null) return null;
		String[] ret=new String[elements.size()];
		for(int i=0;i<elements.size();i++){
			// element never contains other tags so just decode it collapsing whitespace:
			ret[i]=CharacterReference.decodeCollapseWhiteSpace(elements.get(i).getContent());
		}
		return ret;
	}
	
	public String getMetaValue(String key) {
		if(!isSent()) return null;
		for (int pos=0; pos<source.length();) {
			StartTag startTag=source.getNextStartTag(pos,"name",key,false);
			if (startTag==null) return null;
			if (startTag.getName()==HTMLElementName.META)
				return startTag.getAttributeValue("content"); // Attribute values are automatically decoded
			pos=startTag.getEnd();
		}
		return null;
	}
	
	public List<Element> getAllElements() {
		if(!isSent()) return null;
		return source.getAllElements();
	}
	
	public List<Element> getAllElements(String pattern) {
		if(!isSent()) return null;
		return source.getAllElements(pattern);
	}

	public static void main(String[] args) {
		System.getProperties().put( "proxySet", "true" );
		System.getProperties().put( "proxyHost", "192.168.100.2" );
		System.getProperties().put( "proxyPort", "8080" );

		
		HttpRequest request = new HttpRequest("http://sourceforge.net/api/file/index/project-id/340544/mtime/desc/rss");
		request.requestHtml();
		//System.out.println(request.getTitle());
		//request.getAllElements()
		//request.getAllElements("link").get(0).getContent().toString()
		request.getLinks();
	}
}
