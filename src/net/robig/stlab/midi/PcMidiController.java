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
		log.error("Not tested yet");
		
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
			e.printStackTrace(log.getErrorPrintWriter());
			throw new DeviceNotFoundException("Cannot open midi output device!");
		}
		
		MidiDevice.Info[] iInfos = MidiCommon.getInputDevices();
		if(iInfos.length<=inputDeviceIndex){
			throw new DeviceNotFoundException("No such midi input device! (index: "+inputDeviceIndex+")");
		}
		try {
			input = MidiSystem.getMidiDevice(infos[inputDeviceIndex]);
			log.info("Using input device: "+infos[inputDeviceIndex].getName());
			input.open();
			inputReceiver = input.getReceiver();
			Transmitter	t = input.getTransmitter();
			t.setReceiver(this);
		} catch (MidiUnavailableException e) {
			e.printStackTrace(log.getErrorPrintWriter());
			throw new DeviceNotFoundException("Cannot open midi input device!");
		}

	}

	@Override
	void sendMessage(byte[] data) {
		log.error("Not tested yet");
		SysexMessage onMessage = new SysexMessage();
		try {
			onMessage.setMessage(data,data.length);
			receiver.send(onMessage, -1);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace(log.getErrorPrintWriter());
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		log.debug("Receiver send()"+message.toString());
	}

}
