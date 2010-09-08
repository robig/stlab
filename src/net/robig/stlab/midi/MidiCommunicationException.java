package net.robig.stlab.midi;

import net.robig.logging.Logger;
import net.robig.stlab.midi.commands.AbstractMidiCommand;

public class MidiCommunicationException extends Exception {
	private static final long serialVersionUID = 1L;
	Logger log = new Logger(this.getClass());
	
	public MidiCommunicationException(String errortext,String data) {
		super(errortext);
		String errorCode=getErrorCode(data);
		log.error(errortext+" (ErrorCode: "+errorCode+")");
	}
	
	private String getErrorCode(String data){
		if(data.startsWith(AbstractMidiCommand.command_start_data)&&data.length()==AbstractMidiCommand.command_start_data.length()+2+AbstractMidiCommand.command_end_data.length())
			return data.substring(AbstractMidiCommand.command_start_data.length(),2); 
		return data;
	}
	
}
