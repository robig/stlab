package net.robig.stlab.net;

import net.robig.net.InvalidLinkException;
import net.robig.stlab.StLabConfig;

import org.testng.annotations.Test;
import static net.robig.net.LinkValidator.*;
import static org.testng.Assert.*;

public class LinkValidatorTest {

	public static void assertIsUrl(String url, boolean yesno){
		if(yesno){
			assertTrue(isUrl(url),"'"+url+"' is not a valid URL but should be!");
			System.out.println("OK "+url);
		}else
			assertFalse(isUrl(url),"'"+url+"' is a valid URL but should'nt!");
	}
	
	@Test
	public void testIsLink(){
		assertIsUrl("http://",false);
		assertIsUrl("http://web.de",true);
		assertIsUrl("ftp://bla.de",false);
		assertIsUrl("http://robig.net",true);
		assertIsUrl(StLabConfig.getAboutUrl(),true);
		assertIsUrl(StLabConfig.getTaCUrl(),true);
		assertIsUrl(StLabConfig.getFeedbackUrl(),true);
		assertIsUrl(StLabConfig.getWebUrl(),true);
	}
	
	@Test
	public void testIsYoutube(){
		assertTrue(isYoutube("http://www.youtube.com/watch?v=dZPnO5gjrSE"));
		assertFalse(isYoutube("http://www.you-tube.com/watch?v=dZPnO5gjrSE"));
		assertFalse(isYoutube("http://www.youtube.com/"));
		assertFalse(isYoutube("http://www.youtube.com/watch?v="));
		assertTrue(isYoutube("http://www.youtube.com/watch?v=b1Mfzt8zF_k&feature=related"));
	}
	
	@Test
	public void testAutocorrectUrl() throws InvalidLinkException{
		assertEquals(autocorrectUrl("web.de"),"http://web.de");
	}
}
