package net.robig.stlab.midi;

import java.util.BitSet;

import net.robig.logging.Logger;
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
	public void test() {
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
}
