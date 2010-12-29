package net.robig.stlab.gui.web;

import java.util.Hashtable;
import net.robig.net.WebAccess.SearchCondition;

public class TextSearchCondition implements SearchCondition {
	public TextSearchCondition(String t) {
		this.text=t;
	}
	public Hashtable<String, String> getParameters(){
		Hashtable<String,String> p=new Hashtable<String,String>();
		p.put("searchText",text);
		return p;
	}
	public String text="";
}