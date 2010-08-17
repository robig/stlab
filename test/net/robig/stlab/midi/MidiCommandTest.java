package net.robig.stlab.midi;

import static org.testng.Assert.*;

import java.util.ArrayList;

import net.robig.logging.Logger;
import net.robig.stlab.midi.commands.GetParametersCommand;
import net.robig.stlab.midi.commands.GetPresetCommand;
import net.robig.stlab.midi.commands.IMidiCommand;
import net.robig.stlab.midi.commands.ParameterChangeCommand;
import net.robig.stlab.midi.commands.PresetRequestCommand;
import net.robig.stlab.midi.commands.SwitchPresetCommand;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test; 

@Test
public class MidiCommandTest {
	Logger log = new Logger(this.getClass());
	
	@BeforeSuite
	public void setUp() throws Exception{
		MidiControllerFactory.create();
		AbstractMidiController.getInstance().findAndConnectToVOX();
	}
	
	/**
	 * Wrapper class for getting received data of commands
	 * @author robig
	 *
	 * @param <E>
	 */
	private class TestCommand<E extends IMidiCommand> implements IMidiCommand{
		
		E original=null;
		public String received=null;
		
		public TestCommand(E o) {
			original=o;
		}
		
		public void receive(String data) throws MidiCommunicationException {
			received=data;
			original.receive(data);
		}

		@Override
		public void run() {
			original.run();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				log.debug(e.toString());
			}
		}

		@Override
		public void finished() {
		}

		@Override
		public void prepare() {
		}
	}
	
	/**
	 * test getting data of a preset of the device
	 * @throws Exception
	 */
	public void testGetPresent() throws Exception {
		AbstractMidiController controller = AbstractMidiController.getInstance();
		assertNotNull(controller);
		
		int present=0;
		TestCommand<GetPresetCommand> cmd = new TestCommand<GetPresetCommand>(new GetPresetCommand(present));
		controller.executeCommand(cmd);
		assertNotNull(cmd.received);
	}
	
	@Test
	public void testSwitchPresent() throws Exception {
		AbstractMidiController controller = AbstractMidiController.getInstance();
		assertNotNull(controller);
		
		int present=5;
		TestCommand<SwitchPresetCommand> cmd = new TestCommand<SwitchPresetCommand>(new SwitchPresetCommand(present));
		controller.executeCommand(cmd);
		assertNotNull(cmd.received);
		log.info("#1 received data: "+cmd.received);
		String data1=cmd.received;
	}
	
	public void testReqestCurrentPresetNumber() throws Exception {
		AbstractMidiController controller = AbstractMidiController.getInstance();
		assertNotNull(controller);
		
		PresetRequestCommand cmd = new PresetRequestCommand();
		String data=controller.runCommandBlocking(cmd);
		assertNotNull(data);
		assertTrue(data.length()==4);
		assertTrue(cmd.ranSuccessfully());
		assertTrue(cmd.getCurrentPresetNumber()>=0);
	}
	
	public void testGetParameters() {
		AbstractMidiController controller = AbstractMidiController.getInstance();
		assertNotNull(controller);
		
		GetParametersCommand cmd = new GetParametersCommand();
		String data=controller.runCommandBlocking(cmd);
		assertNotNull(data);
		assertTrue(data.length()==56);
		assertTrue(cmd.ranSuccessfully());
	}
	
	//TonelabST does not support this :(
	@Test(enabled=false)
	public void testParameterChange() {
		AbstractMidiController controller = AbstractMidiController.getInstance();
		assertNotNull(controller);
		
		ParameterChangeCommand cmd = new ParameterChangeCommand();
		String data=controller.runCommandBlocking(cmd);
		assertNotNull(data);
		assertTrue(data.length()==4);
		assertTrue(cmd.ranSuccessfully());
	}
}
