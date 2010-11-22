package net.robig.stlab.midi;

import static org.testng.Assert.*;

import java.awt.List;
import java.util.ArrayList;

import net.robig.logging.Logger;
import net.robig.stlab.midi.commands.AbstractMidiCommand;
import net.robig.stlab.midi.commands.IMidiCommand;
import net.robig.stlab.util.HexConvertionUtil;
import net.robig.stlab.util.StringUtil;

import org.testng.annotations.Test; 

@Test
public class MidiControllerTest {
	Logger log = new Logger(this.getClass());
	
	public void testConvert() {
		assertEquals(HexConvertionUtil.toHexString(new byte[]{
				16, 8
		}), "1008");
		
		assertEquals(HexConvertionUtil.toHexString(10).toUpperCase(), "0A");
		assertEquals(HexConvertionUtil.toHexString(5).toUpperCase(), "05");
		assertEquals(HexConvertionUtil.toHexString(50).toUpperCase(), "32");
		
		int big=HexConvertionUtil.hex2int("2000");
		assertTrue(big==8192);
		
		String hex=HexConvertionUtil.toHexString(8192);
		assertEquals(hex, "00");
		//assertTrue(hex.equals("2000"));
		
		assertEquals(
				HexConvertionUtil.toHexString2(2304),
				"0900");
		assertEquals(
				HexConvertionUtil.toHexString2(50),
				"0032");
		assertEquals(
				HexConvertionUtil.toHexString2(8192),
				"2000");
	}
	
	public void testListDevices() throws DeviceNotFoundException {
		MidiControllerFactory.create();
		
		String[] inputDevices=AbstractMidiController.getInstance().getInputDevices();
		assertNotNull(inputDevices);
		assertTrue(inputDevices.length > 0, "You must have at least one midi input device connected!");
		log.info("Input  Devices: "+ StringUtil.array2String(inputDevices));
		int foundInput=-1;
		for(int i=0;i<inputDevices.length;i++){
			if(inputDevices[i].startsWith("ToneLabST")) foundInput=i;
		}
		assertTrue(foundInput>=0,"input device not found");
		
		
		String[] outputDevices=AbstractMidiController.getInstance().getOutputDevices();
		assertNotNull(outputDevices);
		assertTrue(outputDevices.length > 0, "You must have at least one midi output device connected!");
		log.info("Output Devices: "+ StringUtil.array2String(outputDevices));
		int foundOutput=-1;
		for(int i=0;i<outputDevices.length;i++){
			if(outputDevices[i].startsWith("ToneLabST")) foundOutput=i;
		}
		assertTrue(foundOutput>=0,"output device not found");
		
		
		AbstractMidiController controller = AbstractMidiController.getInstance();
//		controller.connect(foundOutput, foundInput);
//		assertEquals(controller.input.getName(),inputDevices[0]);
//		assertEquals(controller.output.getName(),outputDevices[0]);
		
	}
	
	@Test(enabled=false, expectedExceptions={java.lang.ArrayIndexOutOfBoundsException.class})
	public void testInvalidIndex() throws DeviceNotFoundException{
		MidiControllerFactory.create();
		@SuppressWarnings("unused")
		AbstractMidiController controller = AbstractMidiController.getInstance();
//		controller.connect(-1, -1);
	}
	
	class TestCommand implements IMidiCommand {

		public String received=null;
		public String tosend="0F423000010842F7";
		
		@Override
		public void receive(String data) {
			received=data;
		}

		@Override
		public void run() {
			AbstractMidiController.getInstance().sendMessage(tosend);
		}

		@Override
		public void finished() {
		}

		@Override
		public void prepare() {
		}
		
	}
	
	public void testMidiCommand() throws DeviceNotFoundException {
		MidiControllerFactory.create();
		
		AbstractMidiController controller =	AbstractMidiController.getInstance();
		assertNotNull(controller);
		controller.findAndConnectToVOX();
		
		TestCommand cmd = new TestCommand();
		assertNotNull(cmd);
		controller.executeCommand(cmd);	
	}
	
	public void testByte08() throws DeviceNotFoundException{
		final String data="007f0a 32010000 00000000 32320a02 14000a64 64280004 00000064 00";
		//                                                     08 
		//-> stops tapping blinking!
		MidiControllerFactory.create();
		
		AbstractMidiController controller =	AbstractMidiController.getInstance();
		assertNotNull(controller);
		controller.findAndConnectToVOX();
		
		TestCommand cmd = new TestCommand(){
			{
				tosend=AbstractMidiCommand.command_start_data+"40"+data+AbstractMidiCommand.command_end_data;
			}
		};
		assertNotNull(cmd);
		controller.executeCommand(cmd);
	}
	
	public void testByteDelay() throws DeviceNotFoundException{
		final String data="006f002a 0f305f33 001b1323 1a050118 08000400 50040100 00006400";
		/*                                                     08       5004 ->  0.81 Hz
		 *                                                     08       5205 ->  0.67 Hz
		 * 													   08		5006 ->  0.57 Hz
		 *                                                     08       5007 ->  0.50 Hz
		 *                                                     08       4007 ->  0.50 Hz
		 *                                                     08		5008 ->  0.44 Hz
		 *													   00       0000 -> 15.15 Hz
		 *													   08       5000 ->  4.81 Hz
		 */
		MidiControllerFactory.create();
		
		AbstractMidiController controller =	AbstractMidiController.getInstance();
		assertNotNull(controller);
		controller.findAndConnectToVOX();
		
		TestCommand cmd = new TestCommand(){
			{
				tosend=AbstractMidiCommand.command_start_data+"40"+data+AbstractMidiCommand.command_end_data;
			}
		};
		assertNotNull(cmd);
		controller.executeCommand(cmd);
	}
}
