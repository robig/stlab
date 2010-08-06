package net.robig.stlab.midi;

import net.robig.logging.Logger;

public abstract class AbstratMidiCommand implements IMidiCommand {

	public static final String command_start_data=COMMAND_START.replaceAll(" ", "").toLowerCase();
	public static final String command_end_data=COMMAND_END.replaceAll(" ", "").toLowerCase();
	
	protected Logger log = new Logger(this.getClass());
	protected String functionCode = "40";
	protected String expectedReturnCode = "23"; // Data Load completed
	protected MidiController controller = MidiController.getInstance();
	
	@Override
	public void receive(String data) throws MidiCommunicationException {
		if(!analyzeResult(data)) throw new MidiCommunicationException("unexpected midi return code!",data);
		log.info("Midi command successful");
	}
	
	protected boolean analyzeResult(String data){
		return data.equals(command_start_data+expectedReturnCode+command_end_data);
	}

	
	protected void sendData(String data){
		controller.sendMessage(COMMAND_START+functionCode+data+COMMAND_END);
	}

	static public String toHexString(int i){
		return MidiController.toHexString(i);
	}
}
