package net.robig.stlab.net;

import org.testng.annotations.Test;

import net.robig.net.UpdateChecker;
import static org.testng.Assert.*;

public class UpdateCheckerTest {
	@Test
	public void testRegex(){
		UpdateChecker uc = new UpdateChecker();
		String v=uc.matchUrl("http://sourceforge.net/project/stlab/files/stlab-0.3.zip/download");
		assertTrue( v!=null );
		
		v=uc.matchUrl("http://sourceforge.net/project/stlab/files/Support/stlab-0.3.zip/download");
		assertTrue( v==null );
	}
}
