package net.robig.stlab.midi;

import java.util.ArrayList;
import java.util.Stack;
import static net.robig.stlab.midi.AbstractMidiCommand.command_start_data;
import net.robig.logging.Logger;
import net.robig.stlab.gui.IDeviceListener;
import net.robig.stlab.util.StringUtil;

/**
 * abstracts all midi operations, offering a independent interface
 * @author robig
 *
 */
public abstract class AbstractMidiController {
	
	// the logger
	private static Logger log=new Logger(AbstractMidiController.class);
	
	/** converts any hex string to an byte array
	 * @param hex
	 * @return byte array of the hex values
	 */
	public static byte[] hex2byte(String hex){
		String strMessage=hex.replaceAll(" ", "").toUpperCase();
		int	nLengthInBytes = strMessage.length() / 2;
		byte[]	abMessage = new byte[nLengthInBytes];
		for (int i = 0; i < nLengthInBytes; i++)
		{
			abMessage[i] = (byte) Integer.parseInt(strMessage.substring(i * 2, i * 2 + 2), 16);
		}
		return abMessage;//toHexString(abMessage)
	}
	
	public static int hex2int(String hex){
		if(hex.length()<2) return 0;
		return Integer.parseInt(hex, 16);	
	}
	
	/** convert bytes to its hex string 
	 * @param bytes
	 * @return string of hex values
	 */
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
	
	public static String toHexString(int i){
		return Integer.toHexString(0x0100 + (i & 0x00FF)).substring(1);
	}
	
	static AbstractMidiController instance = null;
	
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
	}
	
	public AbstractMidiController(int outputDeviceIndex,int inputDeviceIndex) throws DeviceNotFoundException {
		instance=this;
		connect(outputDeviceIndex,inputDeviceIndex);
		
	}
	
	public void findAndConnectToVOX() throws DeviceNotFoundException{
		log.debug("Searching for VOX device...");
		int inidx=-1;
		int outidx=-1;
		String[] devices=getOutputDevices();
		log.debug("Available output devices: "+StringUtil.array2String(devices));
		for(int i=0; i<devices.length;i++ ){
			if(devices[i].equals("ToneLabST - Ctrl Out")){
				outidx=i;
			}
		}
		if(outidx==-1) throw new DeviceNotFoundException("VOX Output device not found!");
		
		devices=getInputDevices();
		log.debug("Available  input devices: "+StringUtil.array2String(devices));
		for(int i=0; i<devices.length;i++ ){
			if(devices[i].equals("ToneLabST - Ctrl In")){
				inidx=i;
			}
		}
		if(inidx==-1) throw new DeviceNotFoundException("VOX Input device not found!");
		log.debug("VOX Device found. connecting...");
		
		connect(outidx, inidx);
		log.debug("Connected to VOX device.");
	}

	public void connect(int outidx, int inidx) throws DeviceNotFoundException {
		initialize(outidx, inidx);
	}
	
	abstract void initialize(int outputDeviceIndex, int inputDeviceIndex) throws DeviceNotFoundException; 

	abstract void sendMessage(byte[] data);
	
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
		sendMessage(hex2byte(hex));
	}
	
	
	Stack<IMidiCommand> commandStack = new Stack<IMidiCommand>();
	ArrayList<IDeviceListener> deviceListeners = new ArrayList<IDeviceListener>();
	
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
		log.debug("Not waiting vor result of "+cmd);
	}

	/**
	 * got some bytes from the device, decodes and processes them
	 * @param data
	 */
	synchronized void midiInput(byte[] data) {
		String sdata=toHexString(data);
		log.debug("Incoming midi data: {1}",sdata);
		//TODO: processIncomingCommand(sdata);
		synchronized (commandStack) {
			try {
				if(commandStack.size()>0){
					commandStack.pop().receive(sdata);
				}
//				else
//					log.warn("no command in queue");
			} catch (MidiCommunicationException e) {
				e.printStackTrace(log.getErrorPrintWriter());
			}
		}
		processIncomingCommand(sdata);
	}
	
	/**
	 * process incoming commands from device
	 * @param data
	 * @return true if command was identified as incoming and was already processed
	 */
	private synchronized boolean processIncomingCommand(String data) {
		if(!data.startsWith(command_start_data)) return false;
		String functionCode=data.substring(command_start_data.length(),2);
		if(functionCode.equals("4E")){
			//TODO: Preset change
			log.info("Incoming command: change preset");
			for(IDeviceListener l: deviceListeners){
				//l.switchPreset(p)
			}
			return true;
		}else if(functionCode=="24"){
			log.error("Got Error Code 24!");
		}
		return false;
	}
	
	public synchronized void addDeviceListener(IDeviceListener l) {
		deviceListeners.add(l);
	}
	
}
