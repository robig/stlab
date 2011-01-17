package net.robig.stlab.gui.web;

import java.util.Hashtable;
import net.robig.net.WebAccess.SearchCondition;

public class TextSearchCondition implements SearchCondition {
	public TextSearchCondition(String t,String o) {
		this.text=t;
		this.searchOrder=o;
	}
	public Hashtable<String, String> getParameters(){
		Hashtable<String,String> p=new Hashtable<String,String>();
		p.put("searchText",text);
		p.put("searchOrder",searchOrder);
		return p;
	}
	public String text="";
	public String searchOrder="";
}