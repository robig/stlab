package net.robig.stlab.model;

import static net.robig.stlab.midi.AbstractMidiController.toHexString;
import static net.robig.stlab.midi.AbstractMidiController.hex2int;

/**
 * Data transfer object and logic of a saveable preset of the VOX Tonelab ST multieffect device
 * @author robig
 * @version 0.1
 */

public class StPreset {
	private int number=0;
	private String name="unnamed";
	
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
	
	//ivate String data = "00  42 06 32 00 00 00 00 00  00 00 00 00 00 01 0A 08  00 62 00 50 07 0C 00 00  00 64 00";
	/*                         XX PP PE AM GG VV TR     MI BB PR NR          RE  MD    
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
	 * RE=Reverb effect (spring=00-0A,10-)
	 * XX=bin: ? ? ? 16 8 ? ? 1
	 *                       1=Pedal effect on/off
	 *                  8=Mod/Delay on/off
	 *               16=Reverb on/off
	 */
	
	public StPreset() {
	}
	
	public StPreset(String data){
		
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

	public int getReverbEffect() {
		return reverbEffect;
	}

	public void setReverbEffect(int reverbEffect) {
		if(reverbEffect>99 || reverbEffect < 0) return;
		this.reverbEffect = reverbEffect;
	}

	/**
	 * encode the preset data into hex data for the device
	 * @return
	 */
	public String getEncodedData(){
		String AM=toHexString(amp*ampType);
		String VV=toHexString(volume);
		String BB=toHexString(bass);
		String MI=toHexString(middle);
		String TR=toHexString(treble);
		String GG=toHexString(gain);
		String PR=toHexString(presence);
		String NR=toHexString(getNoiseReduction());
		String RE=toHexString(getReverbEffect());
		String MD=toHexString(getDelayEffect());
		return "00  42 06 32 "+AM+GG+VV+TR+"00  "+MI+BB+PR+NR+"00 01 0A"+RE+MD+"62 00 50 07 0C 00 00  00 64 00";
	}
	
	/**
	 * decode a hex string of midi data and fill member variables
	 * @param data
	 */
	public void parseData(String data){
		String cdata=data.replace(" ", "").toUpperCase();
		int pos=8;
		setAmp(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setGain(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setVolume(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setTreble(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		pos+=2;
		setMiddle(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setBass(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setPresence(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setNoiseReduction(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		pos+=2;
		pos+=2;
		setReverbEffect(hex2int(cdata.substring(pos, pos+2))); pos+=2;
		setDelayEffect(hex2int(cdata.substring(pos, pos+2))); pos+=2;
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
			" pedal   "+bool2Str(pedalEnabled)+": effect="+pedalEffect+" pedal edit="+pedalEdit+"\n"+
			" delay   "+bool2Str(delayEnabled)+": effect="+delayEffect+" delay depth="+delayDepth+" delay feedback="+delayFeedback+" delay speed="+delaySpeed+"\n"+
			" reverb  "+bool2Str(reverbEnabled)+" effect="+reverbEffect;
	}
	
}
