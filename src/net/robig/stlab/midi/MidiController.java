package net.robig.stlab.midi;

import java.util.Stack;

import net.robig.logging.Logger;
import net.robig.stlab.util.StringUtil;
import de.humatic.mmj.MidiListener;
import de.humatic.mmj.MidiSystem;

/**
 * abstracts all midi operations, offering a independent interface
 * @author robig
 *
 */
public class MidiController {
	
	/**
	 * wrapper class to get midiInput() private
	 * @author robig
	 *
	 */
	private class MyMidiListener implements MidiListener {

		@Override
		public void midiInput(byte[] arg0) {
			instance.midiInput(arg0);
			
		}
		
	}
	
	private static Logger log=new Logger(MidiController.class);
	//private Logger log=new Logger(this.getClass());
	
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
	
	/**
	 * get a string array of available output devices
	 * @return
	 */
	public static String[] getOutputDevices() {
		return de.humatic.mmj.MidiSystem.getOutputs();
	}
	
	/**
	 * get a string array of available input devices
	 * @return
	 */
	public static String[] getInputDevices(){
		return de.humatic.mmj.MidiSystem.getInputs();
	}
	
	public static void findAndConnectToVOX() throws DeviceNotFoundException{
		log.debug("Searching for VOX device...");
		int inidx=-1;
		int outidx=-1;
		String[] devices=MidiController.getOutputDevices();
		log.debug("Available output devices: "+StringUtil.array2String(devices));
		for(int i=0; i<devices.length;i++ ){
			if(devices[i].equals("ToneLabST - Ctrl Out")){
				outidx=i;
			}
		}
		if(outidx==-1) throw new DeviceNotFoundException("VOX Output device not found!");
		
		devices=MidiController.getInputDevices();
		log.debug("Available  input devices: "+StringUtil.array2String(devices));
		for(int i=0; i<devices.length;i++ ){
			if(devices[i].equals("ToneLabST - Ctrl In")){
				inidx=i;
			}
		}
		if(inidx==-1) throw new DeviceNotFoundException("VOX Input device not found!");
		
		new MidiController(outidx, inidx);
		log.debug("VOX Device found.");
	}
	
	private static MidiController instance = null;
	
	/**
	 * MidiController is singleton
	 * @return
	 */
	public static MidiController getInstance() {
		if(instance==null) return new MidiController(0,0);
		return instance;
	}
	/**************************************************/
	
	de.humatic.mmj.MidiOutput output=null;
	de.humatic.mmj.MidiInput input=null;
	
	
	public MidiController(int outputDeviceIndex,int inputDeviceIndex) {
		instance=this;
		initialize(outputDeviceIndex,inputDeviceIndex);
	}
	
	private void initialize(int outputDeviceIndex, int inputDeviceIndex) {
		output = de.humatic.mmj.MidiSystem.openMidiOutput(outputDeviceIndex);
		input=MidiSystem.openMidiInput(inputDeviceIndex);
		input.addMidiListener(new MyMidiListener());
	}
	
	public void sendMessage(String hex){
		sendMessage(hex2byte(hex));
	}
	
	public void sendMessage(byte[] data){
		log.debug("sending message: "+toHexString(data));
		output.sendMidi(data);
	}
	
	Stack<IMidiCommand> commandStack = new Stack<IMidiCommand>();
	
	/**
	 * sends a command and <b>waits</b> for the answer
	 * @param cmd the Command of type IMidiComand
	 */
	public synchronized void runCommand(IMidiCommand cmd){
		log.debug("Executing command of type "+cmd.getClass().getName());
		synchronized (commandStack) {
			commandStack.push(cmd);
		}
		cmd.run();
	}

	private void midiInput(byte[] data) {
		String sdata=toHexString(data);
		log.debug("Incoming data: {1}",sdata);
		synchronized (commandStack) {
			try {
				commandStack.pop().receive(sdata);
			} catch (MidiCommunicationException e) {
				e.printStackTrace(log.getErrorPrintWriter());
			}
		}
	}
}
