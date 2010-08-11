package net.robig.stlab.midi;

import net.robig.stlab.util.Config;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

@Test
public class ConfigTest {
	/**
	 * Tests saving and loading of config files
	 */
	public void testLoadFiles() {
		Config conf = Config.getInstance();
		assertNotNull(conf);
		String setVal="saved string";
		conf.setValue("testLoadFiles", setVal);
		conf.saveConfig();
		
		Config conf2=new Config();
		assertNotNull(conf2);
		String value=conf2.getValue("testLoadFiles", null);
		assertNotNull(value);
		assertTrue(value.equals(setVal));
	}
}
