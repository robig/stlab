package net.robig.stlab.midi;

import net.robig.logging.Logger;
import net.robig.stlab.midi.commands.AbstractMidiCommand;
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
		String data=preset.encodeData();
		StPreset preset2 = new StPreset();
		preset2.parseParameters(data);
		String data2=preset2.encodeData();
		log.debug("preset1:"+preset);
		log.debug("data1: "+AbstractMidiCommand.formatIncomingData(data));
		log.debug("data2: "+AbstractMidiCommand.formatIncomingData(data2));
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

	/** Tests illegal values! */
	public void testIllegalAssignments() {
		StPreset preset = new StPreset();
		preset.setAmp(11);
		assertEquals(preset.getAmp(), 0);
		
		preset.setAmp(0-1);
		assertEquals(preset.getAmp(), 0);
		
		preset.setAmpType(9);
		assertEquals(preset.getAmpType(), 0);
		
		preset.setAmpType(-1);
		assertEquals(preset.getAmpType(), 0);
		
		preset.setBass(101);
		assertEquals(preset.getBass(), 50);
		
		preset.setBass(-5);
		assertEquals(preset.getBass(), 50);
		
		preset.setCabinet(11);
		assertEquals(preset.getCabinet(), 0);
		
		preset.setCabinet(-1);
		assertEquals(preset.getCabinet(), 0);
		
		preset.setDelayEffect(11);
		assertEquals(preset.getDelayEffect(), 0);
		
		preset.setDelayEffect(-1);
		assertEquals(preset.getDelayEffect(), 0);
		
		preset.setGain(101);
		assertEquals(preset.getGain(), 0);
		
		preset.setGain(-1);
		assertEquals(preset.getGain(), 0);
		
		preset.setMiddle(101);
		assertEquals(preset.getMiddle(), 50);
		
		preset.setMiddle(-1);
		assertEquals(preset.getMiddle(), 50);
		
		preset.setNoiseReduction(101);
		assertEquals(preset.getNoiseReduction(), 0);
		
		preset.setNoiseReduction(-1);
		assertEquals(preset.getNoiseReduction(), 0);
		
		preset.setNumber(100);
		assertEquals(preset.getNumber(), 0);
		
		preset.setNumber(-1);
		assertEquals(preset.getNumber(), 0);
		
		preset.setPedalEffect(11);
		assertEquals(preset.getPedalEffect(), 0);
		
		preset.setPedalEffect(-1);
		assertEquals(preset.getPedalEffect(), 0);
		
		preset.setPedalEdit(101);
		assertEquals(preset.getPedalEdit(), 0);
		
		preset.setPedalEdit(-1);
		assertEquals(preset.getPedalEdit(), 0);
		
		preset.setPresence(101);
		assertEquals(preset.getPresence(), 50);
		
		preset.setPresence(-1);
		assertEquals(preset.getPresence(), 50);
		
		preset.setReverbEffect(41);
		assertEquals(preset.getReverbEffect(), 0);
		
		preset.setReverbEffect(-1);
		assertEquals(preset.getReverbEffect(), 0);
		
		preset.setReverbType(3);
		assertEquals(preset.getReverbType(), 0);
		
		preset.setReverbType(-1);
		assertEquals(preset.getReverbType(), 0);
		
		preset.setTreble(101);
		assertEquals(preset.getTreble(), 50);
		
		preset.setTreble(-1);
		assertEquals(preset.getTreble(), 50);
		
		preset.setVolume(101);
		assertEquals(preset.getVolume(), 50);
		
		preset.setVolume(-1);
		assertEquals(preset.getVolume(), 50);
	}


	public void testByteConverting() throws FileFormatException {
		StPreset preset = new StPreset();
		byte[] data1=preset.toBytes();
		log.debug("data1:"+formatIncomingData(toHexString(data1))); // new String(data1).substring(23);
		StPreset preset2 = new StPreset();
		preset2.fromBytes(data1);
		byte[] data2=preset2.toBytes();
		log.debug("data2:"+formatIncomingData(toHexString(data2))); // new String(data2).substring(23)
		assertEquals(new String(data2).substring(23), new String(data1).substring(23));
		assertEquals(data1,data2);
	}
	
	
	class TestData {
		StPreset preset = new StPreset();
		public String byte0;
		public String byte1;
		public String byte2;
		public String expected;
		public TestData(String exp,String b0,String b1, String b2) {
			byte0=b0; byte1=b1; byte2=b2;expected=exp;
		}
		public void test() {
			String data = "004f0635 1f594e44 002b423e 32090000 "+byte0+"030000 "+byte1+byte2+"0e7f 001f3501";
			preset.parseParameters(data);
			log.debug("original Data: "+data);
			String encoded=AbstractMidiCommand.formatIncomingData(preset.encodeData());
			log.debug("encoded  Data: "+encoded);
			assertEquals(encoded,data);
			double value=Math.floor(100*1000/(double)preset.getDelaySpeed()+0.5d)/100;
			double expValue=Double.parseDouble(expected);
			double expR=1000/expValue;
			System.out.println(expected+": expected="+expValue+" int="+preset.getDelaySpeed()+" value="+value);
			assertTrue(Math.abs(expValue-value)<=0.1d);
			encodeDecode(preset);
		}
	}
	
	public void testDelaySpeeds() {
		StPreset preset = new StPreset();
		preset.parseParameters("00770064 0d1c5a3a 002e2035 240a0219 00083232 53020000 00000000");
		assertEquals(preset.getDelaySpeed(), 595);
		
		preset.parseParameters("00770064 0d1c5a3a 002e2035 240a0219 08083232 22030000 00000000");
		assertEquals(preset.getDelaySpeed(), 930);
		
		preset.parseParameters("00770064 0d1c5a3a 002e2035 240a0219 00083232 19010000 00000000");
		assertEquals(preset.getDelaySpeed(), 281);
		
		preset.parseParameters("00770064 0d1c5a3a 002e2035 240a0219 08083232 0b000000 00000000");
		assertEquals(preset.getDelaySpeed(), 139);
		
		
//		new TestData("0.14","48","4f","1c").test();
		new TestData("0.40","08","4e","09").test();
		new TestData("0.50","08","50","07").test();
		new TestData("0.54","00","3d","07").test();
		new TestData("0.67","08","52","05").test();
		new TestData("0.81","08","50","04").test();
		new TestData("1.10","08","04","03").test();
		new TestData("1.46","08","2d","02").test();
		new TestData("2.27","08","38","01").test();
		new TestData("2.35","08","29","01").test();
		new TestData("2.35","08","29","01").test();
		new TestData("3.14","00","3e","01").test();
		new TestData("5.65","08","31","00").test();
		new TestData("6.25","08","20","00").test();
	}
}
