package net.robig.stlab.midi;

import net.robig.logging.Logger;
import net.robig.stlab.model.StPreset;
import net.robig.stlab.util.FileFormatException;
import static org.testng.Assert.*;
import static net.robig.stlab.midi.AbstractMidiController.*;
import static net.robig.stlab.midi.commands.AbstractMidiCommand.formatIncomingData;
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
		preset.setDelayDepth(2000);
		preset.setReverbEffect(27);
		encodeDecode(preset);
	}
	
	//TODO: Test illegal values!

	public void testByteConverting() throws FileFormatException {
		StPreset preset = new StPreset();
		byte[] data1=preset.toBytes();
		log.debug("data1:"+formatIncomingData(toHexString(data1)));
		StPreset preset2 = new StPreset();
		preset2.fromBytes(data1);
		byte[] data2=preset2.toBytes();
		log.debug("data2:"+formatIncomingData(toHexString(data2)));
		assertEquals(data1,data2);
	}
	
	private float half2Float(int bits){
		int f=bits & 0x03ff;
		int e=(bits >> 10) & 0x001f;
		int s=(bits >> 15) & 0x0001;
		
		int k=5;
		int bias=2^(k-1)-1;

	    // need to handle 7c00 INF and fc00 -INF?
	    if (e == 0) {
	        // need to handle +-0 case f==0 or f=0x8000?
	        if (f == 0)                                            // Plus or minus zero
	            return s << 31;
	        else {                                                 // Denormalized number -- renormalize it
	            while ((f & 0x0400)<=0) {
	                f <<= 1;
	                e -=  1;
	            }
	            e += 1;
	            f &= ~0x0400;
	        }
	    } else if (e == 31) {
	        if (f == 0)                                             // Inf
	            return (s << 31) | 0x7f800000;
	        else                                                    // NaN
	            return (s << 31) | 0x7f800000 | (f << 13);
	    }

	    float m=Float.parseFloat("0."+f);
	    //m*2^(e-bias)
	    e = e + (127 - bias);
	    f = f << 13;

	    return Float.intBitsToFloat((s << 31) | (e << 23) | f);
	}
	
	public void floatTest() {
		int r=Integer.reverse(0x3d07);
		Float.intBitsToFloat(Integer.reverse(0x3d07));
		Float.intBitsToFloat(0x08200001);
		Integer.toHexString(Float.floatToIntBits(0x060401));
		float v1=1/half2Float(0x5007);
		float v2=1/half2Float(0x0604);
		
		v1=1/half2Float(0x0750);
		v2=1/half2Float(0x0406);
//			
//		(0x0604 >> 10) & 0x001f 
//		
//		0x5007 & 0x03ff
//		(0x5007 >> 10) & 0x001f 
//		
//		0x3100 & 0x03ff
//		(0x3100 >> 10) & 0x001f 
	}
}
