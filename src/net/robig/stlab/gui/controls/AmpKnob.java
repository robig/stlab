package net.robig.stlab.gui.controls;

import net.robig.gui.IntegerValueKnob;

public class AmpKnob extends IntegerValueKnob {
	private static final long serialVersionUID = 1L;
	String[] ampNames = {"CLEAN","CALI CLEAN","US BLUES","US 2x12","VOX AC15","VOX AC30","UK ROCK","UK METAL","US HIGAIN","US METAL","BTQ METAL"};
	public String getDisplayedTextValue() {
		String ret="("+getDisplayedValue()+") ";
		ret+=ampNames[getValue()];
		return ret;
	};
}
