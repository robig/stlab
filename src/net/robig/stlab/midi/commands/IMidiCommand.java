package net.robig.stlab.midi.commands;

import net.robig.stlab.midi.MidiCommunicationException;

public interface IMidiCommand {
	
	public final static String SET_MODE="F0 7E 7F 06 01 F7"; // ???
	
	
	//  KORG SYSTEM EXCLUSIVE MESSAGE FORMAT: F0 42 30 00 01 08 FunctionalCode Data F7
	public final static String COMMAND_START="F0 42 30 00 01 08 ";
	public final static String COMMAND_END  ="F7";
	
	public void run();
	public void receive(String data) throws MidiCommunicationException;
	public void prepare();
	public void finished();
}
