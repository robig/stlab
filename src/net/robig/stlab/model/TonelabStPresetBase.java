package net.robig.stlab.model;

import net.robig.logging.Logger;

public class TonelabStPresetBase {
	final static String[] ampNames = {"CLEAN","CALI CLEAN","US BLUES","US 2x12","VOX AC15","VOX AC30","UK ROCK","UK METAL","US HIGAIN","US METAL","BTQ METAL"};
	final static String[] ampTypes = {"STD","CST","SPC"};
	final static String[] cabNames = {"TWEED 1x8","TWEED 1x12","TWEED 4x10", "BLACK 2x10","BLACK 2x12","VOX AC15", "VOX AC30","VOX AD120VTX","UK H30 4x12", "UK T75 4x12","US V30 4x12" };
	final static String[] pedalNames={"COMP","ACOUSTIC","VOX WAH","U-VIBE","OCTAVE","TREBLE BOOST","TUBE OD","BOUTIQUE","ORANGE DIST","METAL DIST","FUZZ"};
	final static String[] delayNames={"CLASSIC CHORUS","MULTITAP CHORUS","CLASSIC FLANGER","PHASER","TEXTREM","ROTARY","PITCH SHIFTER","FILTRON","ECHO PLUS","DELAY","CHORUS+DELAY"};
	final static String[] reverbNames = {"SPRING","ROOM","HALL"};
	final static String[] filtronNames = {"UP","DOWN"};
	final static String[] pitchNames = {"-12","-7","-5","dt","5","7","12"};
	
	Logger log = new Logger(this.getClass());
	
	/** preset number */
	private int number=0;
	
	protected int featureBase=0;
	private int volume=50;
	private int bass=50;
	private int middle=50;
	private int treble=50; 
	private int presence=50;
	private int gain=0;
	private int noiseReduction=0; //0-49
	private int amp=0; //0-10
	private int ampType=0; //0-2
	
	private boolean cabinetEnabled=false;
	private int cabinet=0; // 0-10
	
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
	private int reverbType = 0;

	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		if(number>99 || number < 0) return;
		this.number = number;
	}


	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		if(volume>100 || volume < 0) return;
		this.volume = volume;
	}

	public int getBass() {
		return bass;
	}

	public void setBass(int bass) {
		if(bass>100 || bass < 0) return;
		this.bass = bass;
	}

	public int getMiddle() {
		return middle;
	}

	public void setMiddle(int middle) {
		if(middle>100 || middle < 0) return;
		this.middle = middle;
	}

	public int getTreble() {
		return treble;
	}

	public void setTreble(int treble) {
		if(treble>100 || treble < 0) return;
		this.treble = treble;
	}

	public int getPresence() {
		return presence;
	}

	public void setPresence(int presence) {
		if(presence>100 || presence < 0) return;
		this.presence = presence;
	}

	public int getGain() {
		return gain;
	}

	public void setGain(int gain) {
		if(gain>100 || gain < 0) return;
		this.gain = gain;
	}

	public int getNoiseReduction() {
		return noiseReduction;
	}

	public void setNoiseReduction(int nr) {
		if(nr>100 || nr < 0) return;
		this.noiseReduction = nr;
	}

	public int getAmp() {
		return amp;
	}
	
	public String getAmpName() {
		return ampNames[getAmp()];
	}

	public void setAmp(int amp) {
		if(amp>10 || amp < 0) return;
		this.amp = amp;
	}

	public int getAmpType() {
		return ampType;
	}
	
	public String getAmpTypeName() {
		return ampTypes[getAmpType()];
	}

	public void setAmpType(int ampType) {
		if(ampType>2 || ampType < 0) return;
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
	
	public String getCabinetName() {
		return cabNames[getCabinet()];
	}

	public void setCabinet(int cabinet) {
		if(cabinet>10 || cabinet < 0) return;
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
	
	public String getPedalEffectName() {
		return pedalNames[getPedalEffect()];
	}

	public void setPedalEffect(int pedalEffect) {
		if(pedalEffect>10 || pedalEffect< 0) return;
		this.pedalEffect = pedalEffect;
	}

	public int getPedalEdit() {
		return pedalEdit;
	}

	public void setPedalEdit(int pedalEdit) {
		if(pedalEdit>100 || pedalEdit < 0) return;
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

	public String getDelayEffectName() {
		return delayNames[getDelayEffect()];
	}
	
	public void setDelayEffect(int delayEffect) {
		if(delayEffect>10 || delayEffect < 0) return;
		this.delayEffect = delayEffect;
		if(delayIsFiltron()||delayIsPitch())setDelaySpeed(0);
	}

	public int getDelayDepth() {
		return delayDepth;
	}

	public void setDelayDepth(int delayDepth) {
		if(delayDepth>100 || delayDepth<0) return;
		this.delayDepth = delayDepth;
	}
	
	public int getDelayFeedback() {
		return delayFeedback;
	}

	public void setDelayFeedback(int delayFeedback) {
		if(delayFeedback>100 || delayFeedback<0) return;
		this.delayFeedback = delayFeedback;
	}

	public int getDelaySpeed() {
		return delaySpeed;
	}
	
	public String getDelaySpeedString() {
		if(delayIsFrequency()) return getDelayFrequency()+" Hz";
		if(delayIsFiltron()) return filtronNames[getDelaySpeed()>filtronNames.length?0:getDelaySpeed()];
		if(delayIsPitch()) return getPitchName();
		return getDelaySpeed()+" ms";
	}
	
	private String getPitchName() {
		int val=getDelaySpeed();
		if(val==5) return pitchNames[1];
		if(val==7) return pitchNames[2];
		if(val==0x0d) return pitchNames[3];
		if(val==0x12) return pitchNames[4];
		if(val==0x14) return pitchNames[5];
		if(val==0x19) return pitchNames[6];
		return pitchNames[0];
	}

	public boolean delayHasDepth(){
		//TODO:
		return true;
	}
	
	public boolean delayHasFeedback() {
		//TODO:
		return true;
	}
	
	public boolean delayIsFrequency() {
		//int[] freqSet={0,1,2,3,4,5};
		if(getDelayEffect()<6) return true;
		return false;
	}
	
	public boolean delayIsPitch() {
		return getDelayEffect()==6;
	}
	
	public boolean delayIsFiltron() {
		return getDelayEffect()==7;
	}
	
	public boolean isTapLedUsed() {
		return !delayIsFiltron() && !delayIsPitch();
	}
	
	public double getDelayFrequency() {
		// returns Hz rounded to two digits
		return Math.floor(100*1000/(double)getDelaySpeed()+0.5d)/100;
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
	
	public int getReverbEdit() {
		return reverbEffect-reverbType*40;
	}

	public String getReverbEffectName() {
		return reverbNames[getReverbType()];
	}
	
	public void setReverbEffect(int reverbEffect) {
		if(reverbEffect>40 || reverbEffect < 0) return;
		this.reverbEffect = reverbEffect;
	}
	
}
