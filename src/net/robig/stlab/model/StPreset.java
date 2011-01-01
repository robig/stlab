package net.robig.stlab.model;

import static net.robig.stlab.util.HexConvertionUtil.toHexString;
import static net.robig.stlab.util.HexConvertionUtil.hex2int;
import static net.robig.stlab.util.HexConvertionUtil.toHexString3;
import static net.robig.stlab.util.HexConvertionUtil.hex2byte;
import java.util.Properties;
import net.robig.stlab.util.FileFormatException;

/**
 * Data transfer object and logic of a saveable preset of the VOX Tonelab ST multi effect device
 * @author robig
 * @version 0.2
 */

public class StPreset extends TonelabStPresetBase implements Cloneable {
	
	/*byte0            4            8            12           16           20           24       27
	 * NU 00 42 06 32  00 00 00 00  00 00 00 00  00 00 01 0A  08 00 62 00  50 07 0C 00  00 00 64 00;
	 *       XX PP PE  AM GG VV TR     MI BB PR  NR CA RE RV  S0 MD DD DF  S1 S2 ET ??  ?? ?? EX ??
	 * 
	 * NU=(optional) Preset number when requesting preset data
	 * AM=AMP (GREEN: 0=Clean,1=CALI CLEAN,  ... 0A=BTO METAL) (ORANGE: 0B-..) (RED: 16-)
	 * VV=Volume 32=50
	 * BB=Bass   32=50
	 * MI=Middle 32=50
	 * TR=Treble 32=50
	 * GG=Gain   32=50 (0-100d)
	 * PR=Presence 32=50
	 * NR=NR     32=100!
	 * CA=Cabinet 00-0A
	 * PE=Pedal edit 32=50
	 * PP=Pedal effect (00=COMP,... 05=TREB BOOST, 0A=FUZZ) (effects PE!)
	 * MD=Mod/Delay effect (00=CLASSIC CHORUS, ... 0A=CHORUS+DELAY)
	 * DD=Delay depth (00..32..64=100d)
	 * DF=Delay feedback (00-64=100)
	 * RE=Reverb effect (spring=00 ROOM=01 HALL=02)
	 * RV=Reverb value (00-28)
	 * XX=bin: ? 64 32 16 8 ? ? 1
	 *                          1=Pedal effect on/off
	 *                      4=Cabinet of/off
	 *                    8=Mod/Delay on: Speed is in Hz
	 *                 16=Mod/Delay on: Speed in ms
	 *              32=Reverb on/off
	 *           64=Cabinet on/off
	 *           
	 * S0 S1 S2=Delay speed: value=S0*16+S1+S2*256
	 *   if MD<6: measured in Hz, use 1000/value
	 *   else if MD==6: S0=00 S2=00: S1 = 00:-12, 05:-7, 07:-5, 0d:dt, 12:5, 14:7, 19:12
	 *   else if MD==7: S0=00 S2=00: S1 = 00: Up, 01: Down
	 *   else use value as it is. its measured in ms
	 *   
	 * ET=Expression target
	 *   01=Volume
	 *   04=Pedal
	 *   08=Gain
	 *   0c=Mod/Delay
	 *   0e=Mod/Delay
	 */
	
	/*
	 *   004f0635 1f594e44 002b423e 32090000 48030000 4f1c0e7f 001f3501 phaser 0.14 Hz
	 *   004f0400 08494450 00384451 2801011d 00033900 3d070400 00006400 phaser 0.54 Hz
	 *   00730646 0b1b5c27 00203253 26020118 08083c32 10010c00 00006400 ex.pedal assigned to mod/delay edit, kicked
	 *   00730646 0b1b5c27 00203253 26020118 08083c32 10010000 00000000 ex.pedal unassigned
	 *   00730646 0b1b5c27 00203253 26020118 08086032 10010c00 00006400 ex.pedal reassigned (quick) to mod/delay
	 */

	
	private static final int presetDataVersion=1;
	private static final int DATA_LENGTH=28;
	
	private static final int BIN_PEDAL_EFFECT=1;
	private static final int BIN_MOD_DELAY_EFFECT_HZ=8;
	private static final int BIN_MOD_DELAY_EFFECT_MS=16;
	private static final int BIN_REVERB_EFFECT=32;
	private static final int BIN_CABINET=4;
	

	public int getDataVersion() {
		return presetDataVersion;
	}
	
	/* member variables for saving to file: */
	private String name="";
	
	private Properties author = new Properties(){
		private static final long serialVersionUID = 1L;
		{
			setProperty("author", System.getProperty("user.name"));
		}
	};
	
	
	//TODO: implement pedal support and decode remaining data:
	protected String theEnd = "000000000000";
	

	public StPreset() {
	}
	
	public StPreset(byte[] data) throws FileFormatException{
		fromBytes(data);
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * encode the preset data into hex data for the device
	 * @return
	 */
	@Deprecated
	public String encodeData(){
		String PP=toHexString(getPedalEffect());
		String PE=toHexString(getPedalEdit());
		String AM=toHexString(getAmp()+11*getAmpType());
		String VV=toHexString(getVolume());
		String BB=toHexString(getBass());
		String MI=toHexString(getMiddle());
		String TR=toHexString(getTreble());
		String GG=toHexString(getGain());
		String PR=toHexString(getPresence());
		String NR=toHexString(getNoiseReduction()/2);
		String CA=toHexString(getCabinet());
		String RT=toHexString(getReverbType());
		String RE=toHexString(getReverbEffect());
		String MD=toHexString(getDelayEffect());
		String DD=toHexString(getDelayDepth());
		String DF=toHexString(getDelayFeedback());
		String XX=toHexString(getFeatureValue(
				isCabinetEnabled(),
				isPedalEnabled(),
				isDelayEnabled(),
				isReverbEnabled()
			));
		String[] S= getDelaySpeedBytes();
		return "00"+XX+PP+PE +AM+GG+VV+TR+ "00"+MI+BB+PR+ NR+CA+RT+RE+ S[0]+MD+DD+DF+ S[1]+S[2]+theEnd;
	}
	
	@Deprecated
	public void parsePreset(String data){
		String cdata=data.replace(" ", "").toLowerCase();
		if(data.length()<DATA_LENGTH*2+2){
			log.error("Wrong data length: "+data.length());
			return;
		}
		int num=hex2int(cdata.substring(0,2));
		setNumber(num);
		parseParameters(cdata.substring(2));
	}
	
	/**
	 * decode a hex string of midi data and fill member variables
	 * @param data
	 */
	@Deprecated
	public void parseParameters(String data){
		String cdata=data.replace(" ", "").toLowerCase();
		if(data.length()<DATA_LENGTH*2){
			log.error("Wrong data length: "+data.length());
			return;
		}
		
		int pos=2;
		int XX = hex2int(cdata.substring(pos, pos+2)); pos+=2;
		setFeatureBase(XX);
		setCabinetEnabled(isEnabled(XX, BIN_CABINET));
		setPedalEnabled(isEnabled(XX, BIN_PEDAL_EFFECT));
		setDelayEnabled( isEnabled(XX, BIN_MOD_DELAY_EFFECT_HZ) || isEnabled(XX, BIN_MOD_DELAY_EFFECT_MS) );
		setReverbEnabled(isEnabled(XX, BIN_REVERB_EFFECT));
		setPedalEffect(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setPedalEdit(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		
		int AM=hex2int(cdata.substring(pos, pos+2)); pos+=2;
		int type=AM/11; setAmpType(type);
		int amp=AM-type*11; setAmp(amp);
		setGain(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setVolume(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setTreble(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		
		pos+=2;
		setMiddle(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setBass(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setPresence(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		
		setNoiseReduction(2*hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setCabinet(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setReverbType(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setReverbEffect(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		
		pos+=2; // Delay speed byte0
		setDelayEffect(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setDelayDepth(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setDelayFeedback(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		
		//Delay speed:
		setDelaySpeed(calculateDelaySpeed(cdata)); pos+=4;
		
		theEnd=cdata.substring(pos,DATA_LENGTH*2); // max data length
	}
	
	/**
	 * encode the preset data into hex data for the device
	 * @return
	 */
	public byte[] encodeDataBytes(){
		byte PP=(byte) getPedalEffect();
		byte PE=(byte) getPedalEdit();
		byte AM=(byte) (getAmp()+11*getAmpType());
		byte VV=(byte) getVolume();
		byte BB=(byte) getBass();
		byte MI=(byte) getMiddle();
		byte TR=(byte) getTreble();
		byte GG=(byte) getGain();
		byte PR=(byte) getPresence();
		byte NR=(byte) (getNoiseReduction()/2);
		byte CA=(byte) getCabinet();
		byte RT=(byte) getReverbType();
		byte RE=(byte) getReverbEffect();
		byte MD=(byte) getDelayEffect();
		byte DD=(byte) getDelayDepth();
		byte DF=(byte) getDelayFeedback();
		byte XX=(byte)getFeatureValue(
				isCabinetEnabled(),
				isPedalEnabled(),
				isDelayEnabled(),
				isReverbEnabled()
			);
		byte[] S=getDelaySpeedByteArray();
		//return "00"+XX+PP+PE +AM+GG+VV+TR+ "00"+MI+BB+PR+ NR+CA+RT+RE+ S[0]+MD+DD+DF+ S[1]+S[2]+theEnd;
		return new byte[] {
			0x00,XX,PP,PE, AM,GG,VV,TR, 0x00,MI,BB,PR, NR,CA,RT,RE, S[0],MD,DD,DF, S[1], S[2],
			0x00, 0x00, 0x00,  0x00, 0x00, 0x00 // TODO end data
		};
	}
	
	/** 
	 * decode midi parameter data and fill member variables
	 * @param data
	 */
	public void parsePreset(byte[] data){
		if(data.length<DATA_LENGTH+1){
			log.error("parsePreset: Wrong data length: "+data.length+" expected: "+(DATA_LENGTH+1));
			return;
		}
		int num=data[0];
		setNumber(num);
		parseParameters(data,1);
	}
	
	/** 
	 * decode midi parameter data and fill member variables
	 * @param data
	 */
	public void parseParameters(byte[] data){
		parseParameters(data,0);
	}
	
	/**
	 * internal method for parsing parametes starting at an offset
	 * @param data
	 * @param offset
	 */
	private void parseParameters(byte[] data,int offset){
		if(data.length<DATA_LENGTH+offset){
			log.error("parseParameters: Wrong data length: "+data.length+" expected: "+(DATA_LENGTH+offset));
			return;
		}
		
		int pos=1+offset; //skip 1st byte
		
		int XX = data[pos++];
		setFeatureBase(XX);
		setCabinetEnabled(isEnabled(XX, BIN_CABINET));
		setPedalEnabled(isEnabled(XX, BIN_PEDAL_EFFECT));
		setDelayEnabled( isEnabled(XX, BIN_MOD_DELAY_EFFECT_HZ) || isEnabled(XX, BIN_MOD_DELAY_EFFECT_MS) );
		setReverbEnabled(isEnabled(XX, BIN_REVERB_EFFECT));
		setPedalEffect(data[pos++]);
		setPedalEdit(data[pos++]);
		
		int AM=data[pos++];
		int type=AM/11; setAmpType(type);
		int amp=AM-type*11; setAmp(amp);
		setGain(data[pos++]);
		setVolume(data[pos++]);
		setTreble(data[pos++]);
		
		pos++; // skip a byte
		setMiddle(data[pos++]);
		setBass(data[pos++]);
		setPresence(data[pos++]);
		
		setNoiseReduction(2*data[pos++]);
		setCabinet(data[pos++]);
		setReverbType(data[pos++]);
		setReverbEffect(data[pos++]);
		
		pos++; // skip Delay speed byte0, analyze later
		setDelayEffect(data[pos++]);
		setDelayDepth(data[pos++]);
		setDelayFeedback(data[pos++]);
		
		//Delay speed:
		setDelaySpeed(calculateDelaySpeed(data)); pos+=2;
		
		//FIXME:
		theEnd=new String(data).substring(pos,DATA_LENGTH); // max data length
	}
	
	private void setFeatureBase(int featureBase) {
		this.featureBase = featureBase;
		int diff=featureBase & 0xFF - (BIN_CABINET + BIN_PEDAL_EFFECT + BIN_MOD_DELAY_EFFECT_MS + BIN_MOD_DELAY_EFFECT_HZ + BIN_REVERB_EFFECT);
		if(diff==0x42)
			log.warn("unknown feature byte: "+toHexString(featureBase)+" remaining: "+toHexString(diff));
		else if(diff>0)
			log.error("unknown feature byte: "+toHexString(featureBase)+" remaining: "+toHexString(diff)+" please report to stlab@robig.net");
	}

	private int calculateDelaySpeed(byte[] data){
		if(data.length<DATA_LENGTH) return 0;
		int byte0=data[16];
		int byte1=data[20];
		int byte2=data[21];
		return byte2*256+byte1+byte0*16;
	}
	
	private byte[] getDelaySpeedByteArray() {
		int value=getDelaySpeed();
		
		byte byte1=(byte) (value & 0x00ff00);
		byte byte0=(byte) ((value & 0xff0000)+((byte1 & 0x80) >> 4));
		byte byte2=(byte) (value & 0x0000ff);
		
		
		return new byte[] {
			byte0,
			(byte) (byte1  & 0x7F),
			byte2
		};
	}
	
	@Deprecated
	private int calculateDelaySpeed(String data){
		if(data.length()<43) return 0;
		int byte0=hex2int(data.substring(32,34));
		int byte1=hex2int(data.substring(40,42));
		int byte2=hex2int(data.substring(42,44));
		return byte2*256+byte1+byte0*16;
	}
	
	@Deprecated
	private String[] getDelaySpeedBytes() {
		int value=getDelaySpeed();
		String hex=toHexString3(value);
		int byte1=hex2int(hex.substring(4,6));
		int byte0=hex2int(hex.substring(0,2))+((byte1 & 0x80) >> 4);
		
		return new String[] {
			toHexString(byte0),
			toHexString(byte1  & 0x7F),
			hex.substring(2,4)
		};
	}
	
	private int getFeatureValue(boolean cab, boolean ped, boolean del, boolean rev){
		int val=featureBase & 0xFF - (BIN_CABINET + BIN_PEDAL_EFFECT + BIN_MOD_DELAY_EFFECT_MS + BIN_MOD_DELAY_EFFECT_HZ + BIN_REVERB_EFFECT);
		if(cab) val+=BIN_CABINET;
		if(ped) val+=BIN_PEDAL_EFFECT;
		if(del) {
			if(!delayIsFrequency()) val+=BIN_MOD_DELAY_EFFECT_MS;
			else val+=BIN_MOD_DELAY_EFFECT_HZ;
		}
		if(rev)	val+=BIN_REVERB_EFFECT;
		return val;
	}
	
	private boolean isEnabled(int value, int binFeature){
		return (value&binFeature) > 0;
	}
	
	public static String bool2Str(boolean b){
		return b?"on ":"off";
	}
	
	/**
	 * dumps preset in a human readable table format
	 */
	@Override
	public String toString() {
		return getClass().getName()+" ("+name+") #"+getNumber()+":\n"+formatReadable();
			
	}
	
	public String formatReadable() {
		return " AMP="+getAmp()+" "+getAmpName()+" type="+getAmpTypeName()+" ("+getAmpType()+") VOLUME="+getVolume()+"\n"+
		" GAIN="+getGain()+" treble="+getTreble()+" middle="+getMiddle()+" bass="+getBass()+" presence="+getPresence()+" NR="+getNoiseReduction()+"\n"+
		" cabinet "+bool2Str(isCabinetEnabled())+": "+getCabinetName()+" value="+getCabinet()+"\n"+
		" pedal   "+bool2Str(isPedalEnabled())+": "+getPedalEffectName()+" value="+getPedalEffect()+" edit="+getPedalEdit()+"\n"+
		" delay   "+bool2Str(isDelayEnabled())+": "+getDelayEffectName()+" value="+getDelayEffect()+" depth="+getDelayDepth()+" feedback="+getDelayFeedback()+" speed="+
			getDelaySpeedString()+"\n"+
		" reverb  "+bool2Str(isReverbEnabled())+": "+getReverbEffectName()+" value="+getReverbEffect()+" type="+getReverbType();
	}
	
	/**
	 * Converts all preset data to bytes for writing to a file. 
	 * @return
	 */
	public byte[] toBytes() {
		String authorInfo="";
		for(String name: author.stringPropertyNames()){
			String value=author.getProperty(name).replace("=", "\\=").replace("|", "\\|");
			authorInfo+="|"+name+"="+value;
		}
		return encode(hex2byte(
			//data version(byte):
			toHexString(presetDataVersion)+
			//data (fixed length):
			encodeData()+
			//Name
			toHexString(getName().getBytes())+
			//|author information:
			toHexString(authorInfo.getBytes())
			));
	}
	
	/**
	 * Simple encoding of data to save to files
	 * @param in
	 * @return
	 */
	private byte[] encode(byte[] in) {
		//return in;
		return toHexString(in).getBytes();
	}
	
	/**
	 * Simple decoding of data to save to files
	 * @param in
	 * @return
	 */
	private byte[] decode(byte[] in){
		//return in;
		return hex2byte(new String(in));
	}
	
	/**
	 * Fills Preset object from byte data.
	 * @param data
	 * @throws FileFormatException
	 */
	public void fromBytes(byte[] encodedData) throws FileFormatException {
		byte[] data=decode(encodedData);
		int minlen=1+DATA_LENGTH;
		if(data.length<minlen) throw new FileFormatException("Minimal length not reached!");
		int version=data[0];
		if(version != presetDataVersion) throw new FileFormatException("Unsupported file data version: "+version);
		String sdata=toHexString(data);
		parseParameters(sdata.substring(2));
		// Additional Data:
		author.clear();
		String alladd=new String(data,minlen, data.length-minlen);
		String[] add=new String(alladd).split("\\|");
		if(add.length>0) setName(add[0].replaceAll("\\=", "=").replaceAll("\\|", "|"));
		if(add.length>1) {
			for(int i=1;i<add.length;i++){
				String[] keyValue=add[i].split("=", 2);
				if(keyValue.length==2)
					author.setProperty(keyValue[0],
							keyValue[1].replaceAll("\\=", "=").replaceAll("\\|", "|"));
			}
		}
	}
	
	public void addAuthorInfo(String key, String value) {
		author.setProperty(key, value);
	}
	
	public Properties getAuthorInfo(){
		return author;
	}
	
	public StPreset clone() {
		try {
			return (StPreset) super.clone();
		} catch (CloneNotSupportedException e) {
			log.debug("Cloning Preset failed! "+e.getMessage());
			e.printStackTrace(log.getDebugPrintWriter());
		}
		return this;
	}

}

