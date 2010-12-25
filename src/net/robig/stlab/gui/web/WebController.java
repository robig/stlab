package net.robig.stlab.gui.web;

import net.robig.net.WebAccess;

public class WebController {
	WebAccess web=new WebAccess();
	
	
	
	public WebController() {
		
	}
	
	public boolean isLoggedIn(){
		return web.isLoggedIn();
	}
	
	public boolean login(String user, String pass){
		return web.login(user, pass);
	}
	
	public void search(){
		web.find(null);
	}
	
	public String getMessage(){
		return web.getMessage();
	}
	
}
