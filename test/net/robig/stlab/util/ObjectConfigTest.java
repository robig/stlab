package net.robig.stlab.util;

import net.robig.stlab.StLabConfig;
import net.robig.stlab.util.config.DoubleValue;
import net.robig.stlab.util.config.IntValue;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class ObjectConfigTest {

	private void checkConfig(String key, String value){
		Config config = new Config();
		String is = config.getValue(key, "NULL");
		assertEquals(is, value);
	}
	
	@BeforeClass
	void setUp() {
		Config.setConfigFile("testconfig.properties");
	}
	
	public void testIntValueConfig() {
		IntValue value = StLabConfig.getMouseSensitivity();
		assertNotNull(value);
		if(value.getValue()>100){
			StLabConfig.setMouseSensitivity(99);
			assertEquals(value.getValue(),new Integer(99));
			checkConfig("knobs.mouse.sensitivity","99");
		}
		StLabConfig.setMouseSensitivity(133);
		assertEquals(value.getValue(),new Integer(133));
		checkConfig("knobs.mouse.sensitivity","133");
	}
	
	public void testDoubleValueConfig() {
		DoubleValue value = StLabConfig.getMouseWheelSensitivity();
		assertNotNull(value);
		if(value.getValue()>1){
			StLabConfig.setMouseWheelSensitivity(0.99);
			assertEquals(value.getValue(),0.99);
			checkConfig("knobs.mousewheel.sensitivity","0.99");
		}
		StLabConfig.setMouseWheelSensitivity(1.1);
		assertEquals(value.getValue(),1.1);
		checkConfig("knobs.mousewheel.sensitivity","1.1");
	}
	
}
