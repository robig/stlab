package net.robig.stlab.gui.web;

import java.util.Hashtable;
import net.robig.net.WebAccess.SearchCondition;

public class TextSearchCondition implements SearchCondition {
	public TextSearchCondition(String t,String o,boolean searchDirection) {
		this.text=t;
		this.searchOrder=o;
		this.searchDesc=searchDirection;
	}
	public Hashtable<String, String> getParameters(){
		Hashtable<String,String> p=new Hashtable<String,String>();
		p.put("searchText",text);
		p.put("searchOrder",searchOrder);
		p.put("searchDesc", ""+(searchDesc?1:0));
		return p;
	}
	public String text="";
	public String searchOrder="";
	public boolean searchDesc=false;
	
	public String toString() {
		return getClass().getName()+": search for text='"+text+"' ordered by "+searchOrder+" "+
			(searchDesc?"DESC":"ASC"); 
	}
	
}