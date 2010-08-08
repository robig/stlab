package net.robig.stlab.midi;

import static org.testng.Assert.*;
import net.robig.logging.Logger;
import net.robig.stlab.util.StringUtil;

import org.testng.annotations.Test; 

@Test
public class MidiControllerTest {
	Logger log = new Logger(this.getClass());
	
	public void testConvert() {
		assertEquals(MidiController.toHexString(new byte[]{
				16, 8
		}), "1008");
		
		assertTrue(MidiController.toHexString(10).equalsIgnoreCase("0A"));
		assertTrue(MidiController.toHexString(5).equalsIgnoreCase("05"));
		assertTrue(MidiController.toHexString(50).equalsIgnoreCase("32"));
	}
	
	public void testListDevices() {
		String[] inputDevices=MidiController.getInputDevices();
		assertNotNull(inputDevices);
		assertTrue(inputDevices.length > 0, "You must have at least one midi input device connected!");
		
		String[] outputDevices=MidiController.getOutputDevices();
		assertNotNull(outputDevices);
		assertTrue(outputDevices.length > 0, "You must have at least one midi output device connected!");
		
		MidiController controller = new MidiController(0, 0);
		assertEquals(controller.input.getName(),inputDevices[0]);
		assertEquals(controller.output.getName(),outputDevices[0]);
		
		log.info("Input  Devices: "+ StringUtil.array2String(inputDevices));
		log.info("Output Devices: "+ StringUtil.array2String(outputDevices));
	}
	
	@Test(expectedExceptions={java.lang.ArrayIndexOutOfBoundsException.class})
	public void testInvalidIndex(){
		@SuppressWarnings("unused")
		MidiController controller = new MidiController(-1, -1);
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
			MidiController.getInstance().sendMessage(tosend);
		}

		@Override
		public void finished() {
		}

		@Override
		public void prepare() {
		}
		
	}
	
	public void testMidiCommand() {
		new MidiController(0, 0);
		
		MidiController controller =	MidiController.getInstance();
		assertNotNull(controller);
		
		log.info("using input  device: "+MidiController.getInputDevices()[0]);
		log.info("using output device: "+MidiController.getOutputDevices()[0]);
		
		TestCommand cmd = new TestCommand();
		assertNotNull(cmd);
		controller.executeCommand(cmd);	
	}
}
