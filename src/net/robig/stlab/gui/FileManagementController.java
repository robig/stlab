package net.robig.stlab.gui;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import net.robig.logging.Logger;
import net.robig.stlab.StLab;
import net.robig.stlab.StLabConfig;
import net.robig.stlab.midi.commands.AbstractMidiCommand;
import net.robig.stlab.model.StPreset;

public class FileManagementController {
	final Logger log = new Logger(this.getClass());
	final JFileChooser fileChooser = new JFileChooser();
	DeviceFrame parent=null;
	static final int MAX_FILE_SIZE=5*1025;
	static final String fileExtention="stp";
	
	final FileFilter filter = new FileFilter(){
		@Override
		public boolean accept(File f) {
			if(f.canRead() && f.isFile() && f.getName().toLowerCase().endsWith("."+fileExtention))
				return true;
			return false;
		}
		@Override
		public String getDescription() {
			return "*.stp StLab Preset file";
		}
	};
	
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
	}
	
	/**
	 * Opens the FileDialog to save the Preset from the parameter 
	 * @param preset
	 */
	public void openSavePresetDialog(StPreset preset) {
		fileChooser.setDialogTitle("Save current Preset to a file");
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		//TODO save in config: fileChooser.setCurrentDirectory(dir)
		int returnVal = fileChooser.showDialog(parent,"Save Preset");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if(!file.getName().toLowerCase().endsWith("."+fileExtention))
            	file=new File(file.getAbsolutePath()+"."+fileExtention);
            if(file.exists()){
            	//Custom button text
    			Object[] options = {"Yes, please",
    			                    "No, thanks"
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
		Properties authInfo=StLabConfig.getAuthorInfo();
		for(Object key: authInfo.keySet()){
			String k=(String) key;
			preset.addAuthorInfo(k, authInfo.getProperty(k));
		}
		preset.addAuthorInfo("created", new Date(System.currentTimeMillis()).toString());
	}

	/**
	 * Opens a FileDialog to choose preset file then loads it
	 * @return the loaded Preset if successful, null otherwise
	 */
	public StPreset openLoadPresetDialog(){
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.setDialogTitle("Load Preset from file");
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
