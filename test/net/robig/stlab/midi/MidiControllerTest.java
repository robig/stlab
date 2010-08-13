package net.robig.stlab.midi;

import static org.testng.Assert.*;
import net.robig.logging.Logger;
import net.robig.stlab.midi.commands.IMidiCommand;
import net.robig.stlab.util.StringUtil;

import org.testng.annotations.Test; 

@Test
public class MidiControllerTest {
	Logger log = new Logger(this.getClass());
	
	public void testConvert() {
		assertEquals(AbstractMidiController.toHexString(new byte[]{
				16, 8
		}), "1008");
		
		assertEquals(AbstractMidiController.toHexString(10).toUpperCase(), "0A");
		assertEquals(AbstractMidiController.toHexString(5).toUpperCase(), "05");
		assertEquals(AbstractMidiController.toHexString(50).toUpperCase(), "32");
		
		int big=AbstractMidiController.hex2int("2000");
		assertTrue(big==8192);
		
		String hex=AbstractMidiController.toHexString(8192);
		assertEquals(hex, "00");
		//assertTrue(hex.equals("2000"));
		
		assertEquals(
				AbstractMidiController.toHexString4(2304),
				"0900");
		assertEquals(
				AbstractMidiController.toHexString4(50),
				"0032");
		assertEquals(
				AbstractMidiController.toHexString4(8192),
				"2000");
	}
	
	public void testListDevices() throws DeviceNotFoundException {
		MidiControllerFactory.create();
		
		String[] inputDevices=AbstractMidiController.getInstance().getInputDevices();
		assertNotNull(inputDevices);
		assertTrue(inputDevices.length > 0, "You must have at least one midi input device connected!");
		
		String[] outputDevices=AbstractMidiController.getInstance().getOutputDevices();
		assertNotNull(outputDevices);
		assertTrue(outputDevices.length > 0, "You must have at least one midi output device connected!");
		
		AbstractMidiController controller = AbstractMidiController.getInstance();
		controller.connect(0, 0);
//		assertEquals(controller.input.getName(),inputDevices[0]);
//		assertEquals(controller.output.getName(),outputDevices[0]);
		
		log.info("Input  Devices: "+ StringUtil.array2String(inputDevices));
		log.info("Output Devices: "+ StringUtil.array2String(outputDevices));
	}
	
	@Test(expectedExceptions={java.lang.ArrayIndexOutOfBoundsException.class})
	public void testInvalidIndex() throws DeviceNotFoundException{
		MidiControllerFactory.create();
		@SuppressWarnings("unused")
		AbstractMidiController controller = AbstractMidiController.getInstance();
		controller.connect(-1, -1);
	}
	
	class TestCommand implements IMidiCommand {

		public String received=null;
		public String tosend="0F112233F7";
		
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
		controller.connect(0, 0);
		
		log.info("using input  device: "+controller.getInputDevices()[0]);
		log.info("using output device: "+controller.getOutputDevices()[0]);
		
		TestCommand cmd = new TestCommand();
		assertNotNull(cmd);
		controller.executeCommand(cmd);	
	}
}
