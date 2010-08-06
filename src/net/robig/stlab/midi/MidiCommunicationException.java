package net.robig.stlab.midi;

import net.robig.logging.Logger;

public class MidiCommunicationException extends Exception {
	Logger log = new Logger(this.getClass());
	
	public MidiCommunicationException(String errortext,String data) {
		super(errortext);
		String errorCode=getErrorCode(data);
		log.error(errortext+" (ErrorCode: "+errorCode+")");
	}
	
	private String getErrorCode(String data){
		if(data.startsWith(AbstratMidiCommand.command_start_data)&&data.length()==AbstratMidiCommand.command_start_data.length()+2+AbstratMidiCommand.command_end_data.length())
			return data.substring(AbstratMidiCommand.command_start_data.length(),2); 
		return data;
	}
	
}
