package net.robig.stlab.midi;

import java.util.Stack;
import net.robig.logging.Logger;
import net.robig.stlab.gui.IDeviceListener;
import net.robig.stlab.midi.commands.AbstractMidiCommand;
import net.robig.stlab.midi.commands.IMidiCommand;
import net.robig.stlab.midi.incoming.IncomingCommandProcessor;
import net.robig.stlab.util.HexConvertionUtil;
import net.robig.stlab.util.StringUtil;

/**
 * abstracts all midi operations, offering a independent interface
 * @author robig
 *
 */
public abstract class AbstractMidiController {
	
	private static final String TONELAB_DEVICE_NAME="ToneLabST";
	
	// the logger
	private static Logger log=new Logger(AbstractMidiController.class);
	protected String outDeviceName="";
	boolean inputConnected=false;
	boolean outputConnected=false;
	int channel=0;
	
	static AbstractMidiController instance = null;
	
	public boolean isOutputConnected(){
		return outputConnected;
	}
	
	public boolean isInputConnected(){
		return inputConnected;
	}
	
	/**
	 * MidiController is singleton
	 * @return
	 */
	public static AbstractMidiController getInstance() {
		return instance;
	}
	/**************************************************/

	
	public AbstractMidiController() {
		instance=this;
		// Thread for waiting for incoming Commands
		incomingCommandProcessor.start();
	}
	
//	public AbstractMidiController(int outputDeviceIndex,int inputDeviceIndex) throws DeviceNotFoundException {
//		instance=this;
//		connect(outputDeviceIndex,inputDeviceIndex);
//		
//	}
	
	
	public void findAndConnectToVOX() throws DeviceNotFoundException{
		findAndConnect(TONELAB_DEVICE_NAME,TONELAB_DEVICE_NAME);
	}
	
	public void findAndConnect(String inputName,String outputName) throws DeviceNotFoundException{
		
		if(outputName!=null){
			log.debug("Searching for "+outputName+" device...");
			String[] devices=getOutputDevices();
			log.debug("Available output devices: "+StringUtil.array2String(devices));
			for(int i=0; i<devices.length;i++ ){
				if(devices[i].startsWith(outputName)){
					connectOutput(outputName);
					outputConnected=true;
					break;
				}
			}
			if(!outputConnected)
				throw new DeviceNotFoundException(outputName+" Output device not found!");
		}
		
		if(inputName!=null){
			String[] devices=getInputDevices();
			log.debug("Available  input devices: "+StringUtil.array2String(devices));
			for(int i=0; i<devices.length;i++ ){
				if(devices[i].startsWith(inputName)){
					log.debug("Connected to "+inputName+" device.");
					connectInput(inputName);
					inputConnected=true;
					break;
				}
			}
			if(!inputConnected)
				throw new DeviceNotFoundException(inputName+" Input device not found!");
		}
	}

	abstract void connectOutput(String name) throws DeviceNotFoundException; 
	
	abstract void connectInput(String name) throws DeviceNotFoundException;

	abstract void sendMessage(byte[] data);
	
	public abstract void sendNote(int key, int velocity, boolean on);
	
	abstract void closeConnection();
	
	/**
	 * get a string array of available output devices
	 * @return
	 */
	abstract String[] getOutputDevices();
	
	/**
	 * get a string array of available input devices
	 * @return
	 */
	abstract String[] getInputDevices();
	
	public void sendMessage(String hex){
		sendMessage(HexConvertionUtil.hex2byte(hex));
	}
	
	Stack<IMidiCommand> commandStack = new Stack<IMidiCommand>();
	IncomingCommandProcessor incomingCommandProcessor = new IncomingCommandProcessor();
	
	/**
	 * sends a command and queue to get te answer
	 * @param cmd the Command of type IMidiComand
	 */
	synchronized void executeCommand(IMidiCommand cmd){
		log.debug("Executing command of type "+cmd.getClass().getName());
		synchronized (commandStack) {
			commandStack.push(cmd);
		}
		cmd.prepare();
		cmd.run();
		cmd.finished();
	}
	
	/**
	 * run a Command and wait for the answer of the device
	 * @param cmd
	 * @return answer data of device if successful otherwise null
	 */
	public String runCommandBlocking(AbstractMidiCommand cmd){
		executeCommand(cmd);
		//log.debug("Waiting for answer...");
		cmd.waitForResult();
		log.debug("Command finished "+(cmd.ranSuccessfully()?"successfully.":"with error!"));
		if(cmd.ranSuccessfully()){
			return cmd.getResultData();
		}
		return null;
	}
	
	public void runCommand(AbstractMidiCommand cmd){
		executeCommand(cmd);
		log.debug("Not waiting for result of "+cmd);
	}

	/**
	 * got some bytes from the device, decodes and processes them
	 * @param data
	 */
	synchronized void midiInput(byte[] data) {
		//String sdata=toHexString(data);
		log.debug("Incoming midi data: {1}", HexConvertionUtil.formatHexData(data));
		synchronized (commandStack) {
			try {
				if(commandStack.size()>0){
					commandStack.pop().receive(HexConvertionUtil.toHexString(data));
					return;
				}

			} catch (MidiCommunicationException e) {
				e.printStackTrace(log.getDebugPrintWriter());
			}
		}
		processIncomingCommand(data);
	}
	
	/**
	 * process incoming commands from device
	 * @param data
	 * @return true if command was identified as incoming and was already processed
	 */
	private void processIncomingCommand(byte[] data) {
		incomingCommandProcessor.processIncomingCommand(data);
	}
	
	public void addDeviceListener(IDeviceListener l) {
		incomingCommandProcessor.addDeviceListener(l);
	}
	
}
