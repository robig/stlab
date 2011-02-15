package net.robig.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;
import net.robig.logging.Logger;
import qdxml.DocHandler;
import qdxml.QDParser;

/**
 * Xml parser for processig xml documents in an object structure. 
 * provides methods for finding tags
 * @author robig
 *
 */
public class XmlParser implements DocHandler {
	
	Logger log=new Logger(this);
	
	public class XmlElement {
		String tag=null;
		List<XmlElement> elements=new ArrayList<XmlElement>();
		Hashtable<String, String> attributes=new Hashtable<String, String>();
		String text="";
		
		public String toString() {
			String attribs="";
			for(String k:attributes.keySet())attribs+=k+"="+(attributes.get(k))+";";
			return tag+"("+attribs+")="+text;
		}
		
		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public XmlElement(String name) {
			tag=name;
		}
		
		public String getName(){
			return tag;
		}
		
		public boolean hasChilds(){
			return elements.size()>0;
		}
		
		public List<XmlElement> find(String name){
			log.debug(tag+": Find "+name);
			List<XmlElement> result=new ArrayList<XmlElement>();
			if(getName().equals(name)){
				result.add(this);
				return result;
			}
			for(XmlElement e: elements){
				if(e.getName().equals(name)){
					result.add(e);
				}else if(e.hasChilds()){
					result.addAll(e.find(name));
				}
			}
			return result;
		}
		
		public void addElement(XmlElement e){
			elements.add(e);
		}
		
		public void setAttribute(String key, String value){
			attributes.put(key, value);
		}
		
		public String getAttribute(String key){
			return attributes.get(key);
		}
	}
	
	Stack<XmlElement> stack;
	XmlElement current=null;
	XmlElement root=null;
	
	public XmlParser(BufferedReader in) throws Exception {
		parseFromReader(in);
	}
	
	
	public XmlParser(String xml) {
		// TODO Auto-generated constructor stub
	}

	private void parse(Reader reader) throws Exception{
		log.debug("Starting XML parsing...");
		QDParser.parse(this,reader);
	}
	
	private void parseFromString(String all) throws Exception {
		log.debug("Parsing XML: "+all.replace("\n", ""));
		StringBufferInputStream sbin=new StringBufferInputStream(all);
		BufferedReader sbreader = new BufferedReader(new InputStreamReader(sbin));
		parse(sbreader);
	}

	private void parseFromReader(BufferedReader in) throws Exception {
		StringBuffer buffer=new StringBuffer();
        String ln;
		while((ln=in.readLine())!=null){
        	buffer.append(ln+"\n");
        }
		parseFromString(buffer.toString());
	}


	public List<XmlElement> findTags(String name) {
		if(root==null)return null;
		return root.find(name);
	}


	@Override
	public void endDocument() throws Exception {
		stack=null;
	}


	@Override
	public void endElement(String tag) throws Exception {
		XmlElement last = stack.pop();
		if(stack.size()>0){
			current=stack.lastElement();
			current.addElement(last);
		}else
			current=null;
		log.debug("end of tag "+tag+" current:"+current);
		
	}


	@Override
	public void startDocument() throws Exception {
		 
	}


	@Override
	public void startElement(String tag, Hashtable h) throws Exception {
		log.debug(" parsing tag "+tag);
		current=new XmlElement(tag);
		if(stack==null){
			stack = new Stack<XmlElement>();
			root=current;
		}
		stack.push(current);
		Enumeration e = h.keys();
	    while(e.hasMoreElements()) {
	    	String key=(String)e.nextElement();
	    	String val = (String)h.get(key);
	    	current.setAttribute(key, val);
	    }
	}


	@Override
	public void text(String str) throws Exception {
		current.setText(str);
	}
}
