package net.robig.stlab.midi;

import static org.testng.Assert.*;

import java.util.ArrayList;

import net.robig.logging.Logger;
import net.robig.stlab.midi.commands.GetPresetCommand;
import net.robig.stlab.midi.commands.IMidiCommand;
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
		log.info("#1 received present data: "+cmd.received);
		String data1=cmd.received;
		
		controller.executeCommand(cmd);
		assertNotNull(cmd.received);
		log.info("#2 received present data: "+cmd.received);
		assertEquals(data1, cmd.received,"received Data is not equal!");
		
		log.info("CHANGE A PARAMETER NOW AND SAVE AS PRESENT "+present+"!");
		for(int i=10;i>=0;i--){Thread.sleep(1000);System.out.println(i+" ");}
		
		controller.executeCommand(cmd);
		assertNotNull(cmd.received);
		log.info("#3 received present data: "+cmd.received);
		assertTrue(!data1.equals(cmd.received),"received Data IS equal but shouldnt!");
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
		//Thread.sleep(5000);
		assertNotNull(data);
		assertTrue(data.length()==4);
		assertTrue(cmd.ranSuccessfully());
		assertTrue(cmd.getCurrentPresetNumber()>=0);
	}
}
