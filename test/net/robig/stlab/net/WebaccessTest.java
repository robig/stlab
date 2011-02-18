package net.robig.stlab.net;

import net.robig.logging.Logger;
import net.robig.net.WebAccess;
import net.robig.stlab.StLabConfig;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class WebaccessTest {
	
	public static boolean withProxy=true;
	public static Logger log = new Logger(WebaccessTest.class);

	@BeforeSuite
	public void prepare(){
		StLabConfig.getInstance().setValue("environment", "DIT");
		System.out.println("Using Environment: "+StLabConfig.getEnvironment());
		assertFalse(StLabConfig.getEnvironmentString().equals(""),"Not testing on production environment!");
		
		StLabConfig.getWebProxyHost().setValue("192.168.100.2");
		StLabConfig.getWebProxyPort().setValue(8080);
		StLabConfig.isWebProxyEnabled().setValue(withProxy);
	}
	
	@BeforeMethod
	public void prepareTest() {
		log.debug("prepareTest: settin Proxy: "+withProxy);
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
	
	@Test
	public void testRegistration(){
		WebAccess web=new WebAccess();
		boolean ok;
		// Username too short
		ok = web.register("1234", "1234", "1234");
		assertFalse(ok);
		// invalid email:
		ok = web.register("unittest1234", "1234", "1234");
		assertFalse(ok);
	}
}
