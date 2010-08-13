package net.robig.stlab.midi;

import net.robig.logging.Logger;
import net.robig.stlab.model.StPreset;
import net.robig.stlab.util.FileFormatException;
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
	
	private void encodeDecode(StPreset preset){
		String data=preset.getEncodedData();
		StPreset preset2 = new StPreset();
		preset2.parseData(data);
		String data2=preset2.getEncodedData();
		log.debug("preset1:"+preset);
		log.debug("data1: "+data);
		log.debug("data2: "+data2);
		log.debug("preset2:"+preset2);
		assertTrue(data.equals(data2));
	}
	
	public void testEncodeDecode1(){
		StPreset preset = new StPreset();
		encodeDecode(preset);
	}
	
	public void testEncodeDecode2Amp(){
		StPreset preset = new StPreset();
		preset.setAmp(5);
		preset.setAmpType(2);
		encodeDecode(preset);
	}
	
	public void testEncodeDecode3BigKnobs(){
		StPreset preset = new StPreset();
		preset.setBass(16);
		preset.setTreble(17);
		preset.setMiddle(18);
		preset.setPresence(19);
		preset.setNoiseReduction(20);
		preset.setCabinet(9);
		encodeDecode(preset);
	}
	
	public void testEncodeDecode4OnOff(){
		StPreset preset = new StPreset();
		preset.setPedalEnabled(true);
		preset.setDelayEnabled(true);
		preset.setReverbEnabled(true);
		encodeDecode(preset);
		
		preset = new StPreset();
		preset.setPedalEnabled(false);
		preset.setDelayEnabled(true);
		preset.setReverbEnabled(true);
		encodeDecode(preset);
		
		preset = new StPreset();
		preset.setPedalEnabled(true);
		preset.setDelayEnabled(false);
		preset.setReverbEnabled(true);
		encodeDecode(preset);
		
		preset = new StPreset();
		preset.setPedalEnabled(true);
		preset.setDelayEnabled(true);
		preset.setReverbEnabled(false);
		encodeDecode(preset);
		
		preset = new StPreset();
		preset.setPedalEnabled(true);
		preset.setDelayEnabled(false);
		preset.setReverbEnabled(false);
		encodeDecode(preset);
		
		preset = new StPreset();
		preset.setPedalEnabled(false);
		preset.setDelayEnabled(true);
		preset.setReverbEnabled(false);
		encodeDecode(preset);
		
		preset = new StPreset();
		preset.setPedalEnabled(false);
		preset.setDelayEnabled(false);
		preset.setReverbEnabled(true);
		encodeDecode(preset);
	}
	
	public void testEncodeDecode5Effects(){
		StPreset preset = new StPreset();
		preset.setPedalEffect(22);
		preset.setPedalEdit(25);
		preset.setDelayEffect(23);
		preset.setDelayDepth(26);
		//TODO preset.setDepth, speed
		preset.setReverbEffect(27);
		encodeDecode(preset);
	}
	
	//TODO: Test illegal values!

	public void testByteConverting() throws FileFormatException {
		StPreset preset = new StPreset();
		String data1=new String(preset.toBytes());
		StPreset preset2 = new StPreset();
		preset2.fromBytes(preset.toBytes());
		String data2=new String(preset2.toBytes());
		assertTrue(data1.equals(data2));
	}
}
