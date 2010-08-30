package net.robig.stlab.midi.commands;

import net.robig.logging.Logger;
import net.robig.stlab.midi.AbstractMidiController;
import net.robig.stlab.midi.MidiCommunicationException;

public abstract class AbstractMidiCommand implements IMidiCommand {

	public static final String command_start_data=COMMAND_START.replaceAll(" ", "").toLowerCase();
	public static final String command_end_data=COMMAND_END.replaceAll(" ", "").toLowerCase();
	
	public enum State { NEW, INIT, EXECUTED, FINISHED, WAIT, ARRIVED };
	protected Logger log = new Logger(this.getClass());
	protected String functionCode = "40";
	protected String expectedReturnCode = "23"; // Data Load completed
	protected boolean expectData=false;
	protected String resultData=null;
	protected boolean ranSuccessfully=false;
	protected State state = State.NEW;
	protected AbstractMidiController controller = AbstractMidiController.getInstance();
	
	public static String formatIncomingData(String fulldata){
		String ret="";
		int off=0;
		int len=fulldata.length();
		for(off=0;off<len;off+=8){
			int remain=len-off;
			if(remain<=8)
				ret+=fulldata.substring(off);
			else
				ret+=fulldata.substring(off,off+8)+" ";
		}
		return ret;
	}
	
	@Override
	abstract public void run();
	
	@Override
	public void prepare() {
		state=State.INIT;	
	}
	
	@Override
	public void finished() {
		state=State.FINISHED;
	}
	
	public synchronized void waitForResult() {
		while(isRunning()){
			log.debug("Waiting for answer of command: "+this);
			try {
				//log.debug("waiting...");
				state=State.WAIT;
				wait(1000);
				//log.debug("wait ende");
			} catch (InterruptedException e) {
				e.printStackTrace(log.getDebugPrintWriter());
			}
		}
	}
	
	private synchronized void arrived() {
		state=State.ARRIVED;
		log.debug("notify waiting thread");
		this.notify();
	}
	
	
	public synchronized boolean isRunning() {
//		synchronized (state) {
			//log.debug("isRunning: "+state);
			if(state.equals(State.ARRIVED))
				return false;
//		}
		return true;
	}
	
	@Override
	public void receive(String data) throws MidiCommunicationException {
		ranSuccessfully=analyzeResult(data);
		arrived();
		if(!ranSuccessfully()) throw new MidiCommunicationException("unexpected midi return code!",data);
		log.info("Midi command successful");
	}
	
	/**
	 * analyze incoming data, check against expectations
	 * @param data
	 * @return true if data is like expected, false if not
	 */
	protected boolean analyzeResult(String data){
		if(!data.startsWith(command_start_data)){
			log.error("received message does not start with usual start data!");
			if(expectData) return false;
		}
		String resultCode=data.substring(command_start_data.length(),command_start_data.length()+2);
		String resultData=data.substring(command_start_data.length()+expectedReturnCode.length(),data.length()-command_end_data.length());
		if(!expectedReturnCode.equals(resultCode)){
			log.error("expected return code: "+expectedReturnCode+" but got: "+resultCode);
			return false;
		}
		if(expectData) {
			log.debug("received data: "+formatIncomingData(resultData));
			receiveData(resultData);
			return true;
		}
		return true;
	}

	/**
	 * Overwrite this method to process received data
	 * @param resultData
	 */
	protected void receiveData(String resultData){
		this.resultData=resultData;
	}
	
	/**
	 * get the command result data
	 * @return
	 */
	public String getResultData() {
		return resultData;
	}
	
	public boolean ranSuccessfully() {
		return this.ranSuccessfully;
	}
	
	protected void sendData(String data){
		controller.sendMessage(COMMAND_START+functionCode+data+COMMAND_END);
	}

	static public String toHexString(int i){
		return AbstractMidiController.toHexString(i);
	}
}
