package net.robig.stlab.net;

import net.robig.net.WebAccess;
import net.robig.stlab.StLabConfig;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class WebaccessTest {
	
	public static boolean withProxy=true;

	@BeforeSuite
	public void prepare(){
		StLabConfig.getWebProxyHost().setValue("192.168.100.2");
		StLabConfig.getWebProxyPort().setValue(8080);
		StLabConfig.isWebProxyEnabled().setValue(withProxy);
	}
	
	@Test
	public void testLogin(){
		WebAccess web=new WebAccess();
		web.login("robig", "08150815");
		assertTrue(	web.isLoggedIn());
		assertTrue(web.getUserId()>0);
	}
	
	@Test
	public void testLoginFails(){
		WebAccess web=new WebAccess();
		web.login("robig", "123");
		assertFalse(web.isLoggedIn());
		assertFalse(web.getUserId()>0);
	}
	
	@Test(enabled=true)
	public void testLoginFailProxy(){
		StLabConfig.isWebProxyEnabled().setValue(false);
		try {
			WebAccess web=new WebAccess();
			web.login("robig", "08150815");
			assertFalse(web.isLoggedIn());
			assertFalse(web.getUserId()>0);
		} finally{
			StLabConfig.isWebProxyEnabled().setValue(withProxy);
		}
	}
}
