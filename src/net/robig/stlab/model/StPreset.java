package net.robig.stlab.model;

import static net.robig.stlab.midi.MidiController.toHexString;

public class StPreset {
	public int volume=50;
	public int bass=50;
	public int middle=50;
	int treble=50; 
	int presence=50;
	public int gain=0;
	int noiseReduction=0; //0-49
	int amp=0; //0-10
	int amp_type=0; //0-3
	
	
	//ivate String data = "00  42 06 32 00 00 00 00 00  00 00 00 00 00 01 0A 08  00 62 00 50 07 0C 00 00  00 64 00";
	/*                                  AM GG VV TR     MI BB PR NR
	 * AM=AMP (GREEN: 0=Clean,1=CALI CLEAN,  ... 0A=BTO METAL) (ORANGE: 0B-..) (RED: 16-)
	 * VV=Volume 32=50
	 * BB=Bass   32=50
	 * MI=Middle 32=50
	 * TR=Treble 32=50
	 * GG=Gain   32=50
	 * PR=Presence 32=50
	 * NR=NR     32=100!
	 */
	
	public String getEncodedData(){
		String AM=toHexString(amp*amp_type);
		String VV=toHexString(volume);
		String BB=toHexString(bass);
		String MI=toHexString(middle);
		String TR=toHexString(treble);
		String GG=toHexString(gain);
		String PR=toHexString(presence);
		String NR=toHexString(noiseReduction);
		return "00  42 06 32 "+AM+GG+VV+TR+"00  "+MI+BB+PR+NR+"00 01 0A 08  00 62 00 50 07 0C 00 00  00 64 00";
	}
	
}
