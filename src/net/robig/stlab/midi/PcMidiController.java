package net.robig.stlab.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Transmitter;

import org.jsresources.midi.MidiCommon;

import net.robig.logging.Logger;
import net.robig.stlab.util.HexConvertionUtil;

public class PcMidiController extends AbstractMidiController implements Receiver {

	Logger log = new Logger(this.getClass());
	MidiDevice	device = null;
	MidiDevice	input = null;
	Receiver	receiver = null;
	Receiver	inputReceiver = null;
	
	@Override
	void closeConnection() {
		if(device!=null)
			device.close();
		if(receiver!=null)
			receiver.close();
	}

	@Override
	void initialize(int outputDeviceIndex, int inputDeviceIndex) throws DeviceNotFoundException {
		log.debug("initializing...");
		
		MidiDevice.Info[] infos = MidiCommon.getOutputDevices();
		if(infos.length<=outputDeviceIndex){
			throw new DeviceNotFoundException("No such midi output device! (index: "+outputDeviceIndex+")");
		}
		try {
			device = MidiSystem.getMidiDevice(infos[outputDeviceIndex]);
			log.info("Using output device: "+infos[outputDeviceIndex].getName());
			device.open();
			receiver = device.getReceiver();
		} catch (MidiUnavailableException e) {
			e.printStackTrace(log.getDebugPrintWriter());
			throw new DeviceNotFoundException("Cannot open midi output device!");
		}
		
		MidiDevice.Info[] iInfos = MidiCommon.getInputDevices();
		if(iInfos.length<=inputDeviceIndex){
			throw new DeviceNotFoundException("No such midi input device! (index: "+inputDeviceIndex+")");
		}
		try {
			input = MidiSystem.getMidiDevice(iInfos[inputDeviceIndex]);
			log.info("Using input device: "+iInfos[inputDeviceIndex].getName());
			input.open();
			//inputReceiver = input.getReceiver();
			Transmitter	t = input.getTransmitter();
			t.setReceiver(this);
		} catch (MidiUnavailableException e) {
			e.printStackTrace(log.getDebugPrintWriter());
			throw new DeviceNotFoundException("Cannot open midi input device!");
		}

	}

	@Override
	void sendMessage(byte[] data) {
		log.debug("Creating new SysexMessage...");
		SysexMessage onMessage = new SysexMessage();
		try {
			onMessage.setMessage(data,data.length);
			receiver.send(onMessage, -1);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace(log.getDebugPrintWriter());
		}
	}

	@Override
	public String[] getOutputDevices() {
		return MidiCommon.listOutputDevices();
	}
	
	@Override
	public String[] getInputDevices(){
		return  MidiCommon.listInputDevices();
	}

	@Override
	public void close() {
		log.debug("closing connections...");
		input.close();
		receiver.close();
		
	}

	/**
	 * this is the receiving method!!!
	 */
	@Override
	public synchronized void send(MidiMessage message, long timeStamp) {
		log.debug("received: "+HexConvertionUtil.toHexString(message.getMessage()));
		midiInput(message.getMessage());
	}

	@Override
	public void findAndConnectToVOX() throws DeviceNotFoundException {
		super.findAndConnectToVOX();
	}
}
