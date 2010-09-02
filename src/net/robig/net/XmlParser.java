package net.robig.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import nanoxml.XMLElement;
import nanoxml.XMLParseException;

public class XmlParser  {
	
	XMLElement root = new XMLElement();
	
	
	public List<XMLElement> findTags(XMLElement element, String name) {
		List<XMLElement> ret=new ArrayList<XMLElement>();
		Vector<XMLElement> childs=element.getChildren();
		for(XMLElement next: childs){
			if(next.getName().equals(name)) ret.add(next);
			else {
				ret.addAll(findTags(next,name));
			}
		}
		return ret;
	}


	public void parseFromReader(BufferedReader in) throws XMLParseException, IOException {
		root.parseFromReader(in);
	}


	public List<XMLElement> findTags(String name) {
		return findTags(root, name);
	}
}
