package net.robig.stlab.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import net.robig.logging.Logger;
import net.robig.stlab.StLab;
import net.robig.stlab.StLabConfig;
import net.robig.stlab.gui.preferences.PreferencesModel;
import net.robig.stlab.midi.commands.AbstractMidiCommand;
import net.robig.stlab.model.StPreset;
import net.robig.stlab.util.FileFormatException;
import net.robig.stlab.util.config.MapValue;

public class FileManagementController {
	final Logger log = new Logger(this.getClass());
	final JFileChooser fileChooser = new JFileChooser();
	DeviceFrame parent=null;
	static final int MAX_FILE_SIZE=5*1025;
	static final String fileExtention="stp";
	JComponent saveAccessory=null;
	JComponent openAccessory=null;
	JLabel infoLabel=null;
	StPreset tmpPreset=new StPreset();
	JTextField nameTextField=null;
	enum Mode { OPEN,SAVE };
	Mode mode=null;
	
	final FileFilter filter = new FileFilter(){
		@Override
		public boolean accept(File f) {
			if(f.isDirectory()) return true;
			if(f.canRead() && f.isFile() && f.getName().toLowerCase().endsWith("."+fileExtention))
				return true;
			return false;
		}
		@Override
		public String getDescription() {
			return "*.stp StLab Preset file";
		}
	};
	
	private JComponent getOpenAccessory() {
		if(openAccessory==null){
			openAccessory=new JPanel(new FlowLayout());
			infoLabel=new JLabel();
			openAccessory.add(infoLabel);
		}
		return openAccessory;
	}
	
	private JComponent getSaveAccessory() {
		if(saveAccessory==null){
			saveAccessory=new JPanel(new BorderLayout());
			PreferencesModel preferences=new PreferencesModel(parent);
			String sec=preferences.getSections()[0];
			saveAccessory.add(preferences.getSectionPanel(sec),BorderLayout.CENTER);
			JPanel namePanel=new JPanel(new BorderLayout());
			namePanel.add(new JLabel("Preset Name:"),BorderLayout.WEST);
			nameTextField=new JTextField();
			nameTextField.setColumns(20);
			namePanel.add(nameTextField,BorderLayout.CENTER);
			saveAccessory.add(namePanel,BorderLayout.NORTH);
		}
		return saveAccessory;
	}
	
	private void showInfo(File presetFile){
		if(presetFile.isDirectory()&&mode.equals(Mode.SAVE)) fileChooser.setAccessory(getSaveAccessory());
		if(!presetFile.isFile()) return;
		fileChooser.setAccessory(getOpenAccessory());
		try {
			tmpPreset.fromBytes(getBytesFromFile(presetFile));
			String author=tmpPreset.getAuthorInfo().getProperty("author");
			String info="<html>";
			
			info+="<b>Preset settings:</b>";
			info+="<br>AMP: "+tmpPreset.getAmpName();
			info+="<br>Cabinet: "+tmpPreset.getCabinetName();
			info+="<br>Pedal: "+tmpPreset.getPedalEffectName();
			info+="<br>Delay: "+tmpPreset.getDelayEffectName();
			info+="<br>Reverb: "+tmpPreset.getReverbEffectName();
			info+="<br>EQ: T:"+tmpPreset.getTreble()+" M:"+tmpPreset.getMiddle()+" B:"+tmpPreset.getBass();
			
			info+="<br><b>General Preset Informations:</b>";
			info+="<br>Name: "+tmpPreset.getName();
			info+="<br>Author: "+author;
			for(String key: tmpPreset.getAuthorInfo().stringPropertyNames()){
				if(!key.equals("author"))
					info+="<br>"+key+": "+tmpPreset.getAuthorInfo().getProperty(key);
			}
			
			infoLabel.setText(info+"</html>");
			return;
		} catch (FileFormatException e) {
			log.debug(e.getMessage());
			//e.printStackTrace(log.getDebugPrintWriter());
		} catch (IOException e) {
			e.printStackTrace(log.getDebugPrintWriter());
		}
		infoLabel.setText("Invalid file format");
	}
	
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
        if (length > MAX_FILE_SIZE) {
            throw new IOException("File exceeds max file size!");
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
    
    public static void writeBytesToFile(File file, byte[] data) throws IOException{
    	FileOutputStream fos = new FileOutputStream(file);
    	fos.write(data);
    	fos.close();
    }
	
	public FileManagementController(DeviceFrame window) {
		parent=window;
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileFilter(filter);
		fileChooser.setPreferredSize(new Dimension(700, 500));
		fileChooser.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
//				log.debug("Property Change: "+evt.getPropertyName()+" value:"+evt.getNewValue());
				if(evt.getPropertyName().equals("SelectedFileChangedProperty") && evt.getNewValue()!=null){
					showInfo(new File(evt.getNewValue().toString()));
				}else if(evt.getPropertyName().equals("directoryChanged") && evt.getNewValue()!=null){
					StLabConfig.getPresetsDirectory().setValue(evt.getNewValue().toString());
				}
			}
		});
		getSaveAccessory();
		getOpenAccessory();
	}
	
	/**
	 * Opens the FileDialog to save the Preset from the parameter 
	 * @param preset
	 */
	public void openSavePresetDialog(StPreset preset) {
		fileChooser.setCurrentDirectory(new File(StLabConfig.getPresetsDirectory().getValue()));
		fileChooser.setDialogTitle("Save current Preset to a file");
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		mode=Mode.SAVE;
		//TODO save in config: fileChooser.setCurrentDirectory(dir)
		fileChooser.setAccessory(getSaveAccessory());
		nameTextField.setText(preset.getName());
		int returnVal = fileChooser.showDialog(parent,"Save Preset");
		if(!nameTextField.getText().equals("")) preset.setName(nameTextField.getText());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if(!file.getName().toLowerCase().endsWith("."+fileExtention))
            	file=new File(file.getAbsolutePath()+"."+fileExtention);
            if(file.exists()){
            	//Custom button text
    			Object[] options = {"Yes",
    			                    "No"
    			                    };
    			int n = JOptionPane.showOptionDialog(null,
    			    "Selected file "+file+" already exists, overwrite?",
    			    StLab.applicationName+": File exists",
    			    JOptionPane.YES_NO_CANCEL_OPTION,
    			    JOptionPane.QUESTION_MESSAGE,
    			    null,
    			    options,
    			    options[0]
    			    );
    			if(n!=JOptionPane.YES_OPTION){
    				openSavePresetDialog(preset);
    				return;
    			}
            }
            try {
            	setAuthotInfos(preset);
				writeBytesToFile(file, preset.toBytes());
				log.info("Preset written to file: "+file);
				parent.output("Preset written to file: "+file);
			} catch (IOException e) {
				log.error("Cannot write Preset to file: "+file+" "+e.getMessage());
				e.printStackTrace(log.getWarnPrintWriter());
			}
        }
	}
	
	private void setAuthotInfos(StPreset preset) {
		MapValue authInfo=StLabConfig.getAuthorInfo();
		for(Object key: authInfo.keySet()){
			String k=(String) key;
			preset.addAuthorInfo(k, authInfo.get(k).getValue());
		}
		preset.addAuthorInfo("created", new Date(System.currentTimeMillis()).toString());
	}

	/**
	 * Opens a FileDialog to choose preset file then loads it
	 * @return the loaded Preset if successful, null otherwise
	 */
	public StPreset openLoadPresetDialog(){
		fileChooser.setCurrentDirectory(new File(StLabConfig.getPresetsDirectory().getValue()));
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		mode=Mode.OPEN;
		fileChooser.setDialogTitle("Load Preset from file");
		fileChooser.setAccessory(null);
		int returnVal = fileChooser.showDialog(parent,"Load Preset");

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
				byte[] data=getBytesFromFile(file);
				log.info("Loaded preset file: "+file);
				parent.output("Preset read from file: "+file);
				StPreset preset = new StPreset(data);
				log.debug("read preset data: "+AbstractMidiCommand.formatIncomingData(new String(data))+preset);
				return preset;
			} catch (Exception e) {
				log.error("Cannot load preset: "+file+"! "+e.getMessage());
				e.printStackTrace(log.getWarnPrintWriter());
			}
        }
		return null;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(
	            UIManager.getCrossPlatformLookAndFeelClassName());

		FileManagementController c = new FileManagementController(null);
		
		StPreset p1=new StPreset();
		c.openSavePresetDialog(p1);
		
		StPreset p2=c.openLoadPresetDialog();
		assert p2!=null;
		System.out.println(p2.toString());
	}
}
