package net.robig.stlab.midi;

import net.robig.logging.Logger;
import net.robig.stlab.model.StPreset;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

@Test
public class ModelTest {
	Logger log = new Logger(this.getClass());
	
	private static int feat1=1;
	private static int feat2=4;
	private static int feat3=16;
	
	private boolean compare(int value, int feature){
		return (value&feature) > 0;
	}
	
	//TODO!
	public void testBin() {
		int value=5;
		//BitSet bits = new BitSet(value);
		//log.debug(5&3?"5&3: ja":"nein");
		//5&8
		assertTrue(compare(value,feat1));
		assertTrue(compare(value,feat2));
		assertTrue(compare(value,feat3)==false);
		
		value=32;
		assertTrue(compare(value,feat1)==false);
		assertTrue(compare(value,feat2)==false);
		assertTrue(compare(value,feat3)==false);
		
		value=17;
		assertTrue(compare(value,feat1)==true);
		assertTrue(compare(value,feat2)==false);
		assertTrue(compare(value,feat3)==true);
	}
	
	public void testEncodeDecode1(){
		StPreset preset = new StPreset();
		String data=preset.getEncodedData();
		StPreset preset2 = new StPreset();
		preset2.parseData(data);
		String data2=preset2.getEncodedData();
		log.debug("preset1:"+preset);
		log.debug("preset2:"+preset2);
		assertTrue(data.equals(data2));
	}
}
