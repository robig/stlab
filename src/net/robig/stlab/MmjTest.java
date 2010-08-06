package net.robig.stlab;

import java.math.BigInteger;

public class MmjTest {
	public static byte[] hex2byte(String hex){
		String strMessage=hex.replaceAll(" ", "").toUpperCase();
		int	nLengthInBytes = strMessage.length() / 2;
		byte[]	abMessage = new byte[nLengthInBytes];
		for (int i = 0; i < nLengthInBytes; i++)
		{
			abMessage[i] = (byte) Integer.parseInt(strMessage.substring(i * 2, i * 2 + 2), 16);
		}
		return abMessage;
	}
	
	public static String toHexString(byte bytes[])
	{
		StringBuffer retString = new StringBuffer();
		for (int i = 0; i < bytes.length; ++i)
		{
			retString.append(
				Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1));
		}
		return retString.toString();
	}
	
	public static void main(String[] args) {
		String[] outputs = de.humatic.mmj.MidiSystem.getOutputs();
		int index = 1;
		 
		// insert code to set correct index here...
		 
		de.humatic.mmj.MidiOutput mo = de.humatic.mmj.MidiSystem.openMidiOutput(index);
		mo.sendMidi(hex2byte("C0 09")); //damn .. no program change on Tonelab ST :(
		//mo.sendMidi(hex2byte("F0 42 30 00 01 08 40 00  77 06 32 1A 5A 53 2B 00 10  2A 48 37 20 09 01 11 00  09 18 17 78 00 01 00 00 20  00 64 00 F7"));
		//mo.sendMidi(hex2byte("F0 42 30 00 01 08 1C 20 00 F7"));
	}
}
