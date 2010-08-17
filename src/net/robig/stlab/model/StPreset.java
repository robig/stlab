package net.robig.stlab.model;

import static net.robig.stlab.midi.AbstractMidiController.toHexString;
import static net.robig.stlab.midi.AbstractMidiController.hex2int;
import static net.robig.stlab.midi.AbstractMidiController.toHexString4;
import static net.robig.stlab.midi.AbstractMidiController.hex2byte;
import java.util.Properties;

import net.robig.stlab.util.FileFormatException;

/**
 * Data transfer object and logic of a saveable preset of the VOX Tonelab ST multieffect device
 * @author robig
 * @version 0.1
 */

public class StPreset {
	
	private static final int presetDataVersion=1;
	
	private static final int BIN_PEDAL_EFFECT=1;
	private static final int BIN_MOD_DELAY_EFFECT=8;
	private static final int BIN_REVERB_EFFECT=16;
	private static final int BIN_CABINET=64;
	
	/** preset number */
	private int number=0;
	
	/* member variables for saving to file: */
	private String name="unnamed";
	private Properties author = new Properties(){
		private static final long serialVersionUID = 1L;
		{
			setProperty("author", System.getProperty("user.name"));
		}
	};
	
	
	private int volume=50;
	private int bass=50;
	private int middle=50;
	private int treble=50; 
	private int presence=50;
	private int gain=0;
	private int noiseReduction=0; //0-49
	private int amp=0; //0-10
	private int ampType=0; //0-3
	
	private boolean cabinetEnabled=false;
	private int cabinet=0;
	
	private boolean pedalEnabled=false;
	private int pedalEffect=0;
	private int pedalEdit=0;
	
	private boolean delayEnabled=false;
	private int delayEffect=0;
	private int delayDepth=0; // Edit 1
	private int delayFeedback=0; // Edit 2
	private int delaySpeed=0; // Edit 3
	
	private boolean reverbEnabled=false;
	private int reverbEffect = 0;

	private int reverbType;
	
	//ivate String data = "00  42 06 32 00 00 00 00 00  00 00 00 00 00 01 0A 08  00 62 00 5007 0C 00 00  00 64 00";
	/*                         XX PP PE AM GG VV TR     MI BB PR NR    RE RV     MD DD DF SSSS 
	 * AM=AMP (GREEN: 0=Clean,1=CALI CLEAN,  ... 0A=BTO METAL) (ORANGE: 0B-..) (RED: 16-)
	 * VV=Volume 32=50
	 * BB=Bass   32=50
	 * MI=Middle 32=50
	 * TR=Treble 32=50
	 * GG=Gain   32=50
	 * PR=Presence 32=50
	 * NR=NR     32=100!
	 * PE=Pedal edit 32=50
	 * PP=Pedal effect (00=COMP,... 05=TREB BOOST, 0A=FUZZ) (effects PE!)
	 * MD=Mod/Delay effect (00=CLASSIC CHORUS, ... 0A=CHORUS+DELAY)
	 * DD=Delay depth (00..32..64=100d)
	 * DF=Delay feedback (00-64=100)
	 * SSSS=Delay speed
	 * RE=Reverb effect (spring=00 ROOM=01 HALL=02)
	 * RV=Reverb value (00-28)
	 * XX=bin: ? 64 ?? 16 8 ? ? 1
	 *                          1=Pedal effect on/off
	 *                    8=Mod/Delay on/off
	 *                 16=Reverb on/off
	 *           64=Cabinet on/off
	 */
	
	
	/*IS 00 7b0a460100000000 00003232 00010008 0a32321001000000000000 ???
	//   00 7b0a460100000000 00003232 00001408 0a32321001000000000000 Reverb 20 SPRING
	 *   00 7b0a460100000000 00003232 00011408 0a32321001000000000000 Reverb 20 ROOM
	 *   00 7b0a460100000000 00003232 00021408 0a32321001000000000000 Reverb 20 HALL
	 *   007f0a46 01000000 00000032 32000214 080a3232 10010000 00000000 Cabinet on
	 *   007b0a46 01000000 00000032 32000214 080a3232 10010000 00000000 Cabinet off
	 *   007f0a46 01000000 00000032 32000214 080a0032 10010000 00000000 delay edit 0
	 *   007f0a46 01000000 00000032 32000214 080a6409 10010000 00000000 delay edit 100 + edit 2 09
	 *   007f0a46 01000000 00000032 32000214 080a6464 00020000 00000000 low speed
	 *   007f0a46 01000000 00000032 32000214 080a6464 28000000 00000000 high speed
	 *   007f0a46 01000000 00000032 32000214 080a6464 28000c00 00006400 assigned pedal to DD=64
	 *   007f0a46 01000000 00000032 32000214 080a6464 28000c00 00006400 moved pedal
	 *   007f0a32 01000000 00000032 32000214 080a6464 28000400 00006400 assigned pedal to PE=32
	 */
	
	public StPreset() {
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		if(volume>99 || volume < 0) return;
		this.volume = volume;
	}

	public int getBass() {
		return bass;
	}

	public void setBass(int bass) {
		if(bass>99 || bass < 0) return;
		this.bass = bass;
	}

	public int getMiddle() {
		return middle;
	}

	public void setMiddle(int middle) {
		if(middle>99 || middle < 0) return;
		this.middle = middle;
	}

	public int getTreble() {
		return treble;
	}

	public void setTreble(int treble) {
		if(treble>99 || treble < 0) return;
		this.treble = treble;
	}

	public int getPresence() {
		return presence;
	}

	public void setPresence(int presence) {
		if(presence>99 || presence < 0) return;
		this.presence = presence;
	}

	public int getGain() {
		return gain;
	}

	public void setGain(int gain) {
		if(gain>99 || gain < 0) return;
		this.gain = gain;
	}

	public int getNoiseReduction() {
		return noiseReduction;
	}

	public void setNoiseReduction(int nr) {
		if(nr>99 || nr < 0) return;
		this.noiseReduction = nr;
	}

	public int getAmp() {
		return amp;
	}

	public void setAmp(int amp) {
		if(amp>10 || amp < 0) return;
		this.amp = amp;
	}

	public int getAmpType() {
		return ampType;
	}

	public void setAmpType(int ampType) {
		if(ampType>2 || volume < 0) return;
		this.ampType = ampType;
	}

	public boolean isCabinetEnabled() {
		return cabinetEnabled;
	}

	public void setCabinetEnabled(boolean cabinetEnabled) {
		this.cabinetEnabled = cabinetEnabled;
	}

	public int getCabinet() {
		return cabinet;
	}

	public void setCabinet(int cabinet) {
		//TODO if(volume>99 || volume < 0) return;
		this.cabinet = cabinet;
	}

	public boolean isPedalEnabled() {
		return pedalEnabled;
	}

	public void setPedalEnabled(boolean pedalEnabled) {
		this.pedalEnabled = pedalEnabled;
	}

	public int getPedalEffect() {
		return pedalEffect;
	}

	public void setPedalEffect(int pedalEffect) {
		if(pedalEffect>10 || pedalEffect< 0) return;
		this.pedalEffect = pedalEffect;
	}

	public int getPedalEdit() {
		return pedalEdit;
	}

	public void setPedalEdit(int pedalEdit) {
		if(pedalEdit>99 || pedalEdit < 0) return;
		this.pedalEdit = pedalEdit;
	}

	public boolean isDelayEnabled() {
		return delayEnabled;
	}

	public void setDelayEnabled(boolean delayEnabled) {
		this.delayEnabled = delayEnabled;
	}

	public int getDelayEffect() {
		return delayEffect;
	}

	public void setDelayEffect(int delayEffect) {
		if(delayEffect>10 || delayEffect < 0) return;
		this.delayEffect = delayEffect;
	}

	public int getDelayDepth() {
		return delayDepth;
	}

	public void setDelayDepth(int delayDepth) {
		this.delayDepth = delayDepth;
	}

	public int getDelayFeedback() {
		return delayFeedback;
	}

	public void setDelayFeedback(int delayFeedback) {
		this.delayFeedback = delayFeedback;
	}

	public int getDelaySpeed() {
		return delaySpeed;
	}

	public void setDelaySpeed(int delaySpeed) {
		this.delaySpeed = delaySpeed;
	}

	public boolean isReverbEnabled() {
		return reverbEnabled;
	}

	public void setReverbEnabled(boolean reverbEnabled) {
		this.reverbEnabled = reverbEnabled;
	}
	
	public int getReverbType() {
		return reverbType;
	}

	public void setReverbType(int reverbType) {
		if(reverbType>2 || reverbType < 0) return;
		this.reverbType = reverbType;
	}

	public int getReverbEffect() {
		return reverbEffect;
	}

	public void setReverbEffect(int reverbEffect) {
		if(reverbEffect>40 || reverbEffect < 0) return;
		this.reverbEffect = reverbEffect;
	}

	/**
	 * encode the preset data into hex data for the device
	 * @return
	 */
	public String getEncodedData(){
		String PP=toHexString(getPedalEffect());
		String PE=toHexString(getPedalEdit());
		String AM=toHexString(getAmp()+11*getAmpType());
		String VV=toHexString(getVolume());
		String BB=toHexString(getBass());
		String MI=toHexString(getMiddle());
		String TR=toHexString(getTreble());
		String GG=toHexString(getGain());
		String PR=toHexString(getPresence());
		String NR=toHexString(getNoiseReduction());
		String RT=toHexString(getReverbType());
		String RE=toHexString(getReverbEffect());
		String MD=toHexString(getDelayEffect());
		String DD=toHexString(getDelayDepth());
		String DF=toHexString(getDelayFeedback());
		String SSSS=toHexString4(getDelaySpeed());
		String XX=toHexString(getFeatureValue(
				isCabinetEnabled(),
				isPedalEnabled(),
				isDelayEnabled(),
				isReverbEnabled()
			));

		return "00 "+XX+PP+PE+AM+GG+VV+TR+"00  "+MI+BB+PR+NR+"00"+RT+RE+"08 "+MD+DD+DF+SSSS+"0C 00 00  00 64 00";
	}
	
	/**
	 * decode a hex string of midi data and fill member variables
	 * @param data
	 */
	public void parseData(String data){
		String cdata=data.replace(" ", "").toUpperCase();
		int pos=2;
		int XX = hex2int(cdata.substring(pos, pos+2)); pos+=2;
		setCabinetEnabled(isEnabled(XX, BIN_CABINET));
		setPedalEnabled(isEnabled(XX, BIN_PEDAL_EFFECT));
		setDelayEnabled(isEnabled(XX, BIN_MOD_DELAY_EFFECT));
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
		setNoiseReduction(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		pos+=2;
		setReverbType(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setReverbEffect(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		pos+=2;
		
		setDelayEffect(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setDelayDepth(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setDelayFeedback(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setDelaySpeed(hex2int(cdata.substring(pos, pos+4))); pos+=4;
	}
	
	private int getFeatureValue(boolean cab, boolean ped, boolean del, boolean rev){
		int val=0;
		if(cab) val+=BIN_CABINET;
		if(ped) val+=BIN_PEDAL_EFFECT;
		if(del) val+=BIN_MOD_DELAY_EFFECT;
		if(rev)	val+=BIN_REVERB_EFFECT;
		return val;
	}
	
	private boolean isEnabled(int value, int binFeature){
		return (value&binFeature) > 0;
	}
	
	public static String bool2Str(boolean b){
		return b?"on ":"off";
	}
	
	@Override
	public String toString() {
		return getClass().getName()+" ("+name+") #"+number+":\n"+
			" AMP="+amp+" type="+ampType+" VOLUME="+volume+"\n"+
			" GAIN="+gain+" treble="+treble+" middle="+middle+" bass="+bass+" presence="+presence+" NR="+noiseReduction+"\n"+
			" cabinet "+bool2Str(cabinetEnabled)+": type="+cabinet+"\n"+
			" pedal   "+bool2Str(pedalEnabled)+": effect="+pedalEffect+" edit="+pedalEdit+"\n"+
			" delay   "+bool2Str(delayEnabled)+": effect="+delayEffect+" depth="+delayDepth+" feedback="+delayFeedback+" speed="+delaySpeed+"\n"+
			" reverb  "+bool2Str(reverbEnabled)+": effect="+reverbEffect+" type="+reverbType;
	}
	
	/**
	 * Converts all preset data to bytes for writing to a file. 
	 * @return
	 */
	public byte[] toBytes() {
		String hexAuthorInfo=null;
		String authorInfo="";
		for(String name: author.stringPropertyNames()){
			String value=author.getProperty(name).replace("=", "\\=").replace("|", "\\|");
			authorInfo+="|"+name+"="+value;
		}
		hexAuthorInfo=toHexString(authorInfo.getBytes());
		return hex2byte(
			//data version(byte):
			toHexString(presetDataVersion)+
			//data (fixed length):
			getEncodedData()+
			//Name
			toHexString(getName().getBytes())+
			//|author information:
			toHexString(hexAuthorInfo.getBytes())
			);
	}
	
	/**
	 * Fills Preset object from byte data.
	 * @param data
	 * @throws FileFormatException
	 */
	public void fromBytes(byte[] data) throws FileFormatException {
		int minlen=1+getEncodedData().replace(" ", "").length()/2; //TODO: optimize
		if(data.length<minlen) throw new FileFormatException("Minimal length not reached!");
		int version=data[0];
		if(version != presetDataVersion) throw new FileFormatException("Unsupported file data version: "+version);
		String sdata=toHexString(data);
		parseData(sdata.substring(2));
		// Additional Data:
		String[] add=new String(data,minlen, data.length-minlen).split("|");
		if(add.length>0) setName(add[0].replaceAll("\\=", "=").replaceAll("\\|", "|"));
		if(add.length>1) {
			for(int i=1;i<add.length;i++){
				String[] keyValue=add[i].split("=", 1);
				if(keyValue.length==2)
					author.setProperty(keyValue[0],
							keyValue[1].replaceAll("\\=", "=").replaceAll("\\|", "|"));
			}
		}
	}

}

