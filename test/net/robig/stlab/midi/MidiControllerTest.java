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
		
		assertTrue(AbstractMidiController.toHexString(10).equalsIgnoreCase("0A"));
		assertTrue(AbstractMidiController.toHexString(5).equalsIgnoreCase("05"));
		assertTrue(AbstractMidiController.toHexString(50).equalsIgnoreCase("32"));
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
