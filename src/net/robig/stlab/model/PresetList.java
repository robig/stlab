package net.robig.stlab.model;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import net.robig.logging.Logger;
import net.robig.stlab.gui.IDeviceListener;
import net.robig.stlab.util.Config;


public class PresetList extends ArrayList<StPreset> implements TableModel,IDeviceListener {
	private static final long serialVersionUID = 1L;
	public static final String NA="--";
	/** List of listeners */
    protected EventListenerList listenerList = new EventListenerList();
    Logger log = new Logger(this); 

	public StPreset getPreset(int num){
		if(size()>num)
			return get(num);
		return null;
	}
	
	int columnCount=15;
	public String requestData(int presetNum, int index){
		StPreset preset = getPreset(presetNum);
		if(preset==null) return "NULL";
		switch (index) {
		case 1:  return preset.getName(); 
		case 2:  return preset.getAmpName();
		case 3:	 return preset.getAmpTypeName();
		case 4:	 return preset.isCabinetEnabled()?preset.getCabinetName():NA;
		case 5:	 return preset.getGain()+"";
		case 6:  return preset.getTreble()+"";
		case 7:  return preset.getMiddle()+"";
		case 8:  return preset.getBass()+"";
		case 9:  return preset.getVolume()+"";
		case 10: return preset.getPresence()+"";
		case 11: return preset.getNoiseReduction()+"";
		case 12: return preset.isPedalEnabled()?preset.getPedalEffectName():NA;
		case 13: return preset.isDelayEnabled()?preset.getDelayEffectName():NA;
		case 14: return preset.isReverbEnabled()?preset.getReverbEffectName():NA;
			
		}
		return preset.getNumber()+"";
	}
	
	public String getCellInfo(int row, int col) {
		String data="";
		StPreset preset=getPreset(row);
		switch (col){
		case 1: 
			if(requestData(row, col).equals(""))
				return "Doubleclick to enter your preset title.";
			else
				return requestData(row, col);
		case 12: data=preset.isPedalEnabled()?"Edit: "+preset.getPedalEdit():"disabled";break;
		case 13: data=preset.isDelayEnabled()?"" +
				(preset.delayHasDepth()?"Depth: "+preset.getDelayDepth()+"\n":"")+
				(preset.delayHasFeedback()?" Feedback: "+preset.getDelayFeedback()+"\n":"")+
				"Speed: "+preset.getDelaySpeedString():"disabled";break;
		case 14: data=preset.isReverbEnabled()?"Value: "+preset.getReverbEffect():"disabled";break;
		}
		return getColumnName(col)+": "+requestData(row, col)+"\n"+
			data;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		switch(columnIndex){
		case 1: return "Title";
		case 2: return "AMP";
		case 3: return "TYP";
		case 4: return "Cabinet";
		case 5: return "Gain";
		case 6: return "Treble";
		case 7: return "Middle";
		case 8: return "Bass";
		case 9: return "Volume";
		case 10: return "Presence";
		case 11: return "NR";
		case 12: return "Pedal";
		case 13: return "Mod/Delay";
		case 14: return "Reverb";
		}
		
		return "Nr";
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {
		listenerList.add(TableModelListener.class, l);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return columnCount;
	}

	@Override
	public int getRowCount() {
		return size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return requestData(rowIndex, columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(columnIndex==1)return true;
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listenerList.remove(TableModelListener.class, l);
	}
	
	public TableModelListener[] getTableModelListeners() {
        return (TableModelListener[])listenerList.getListeners(
                TableModelListener.class);
    }

	@Override
	public void setValueAt(Object value, int presetNum, int index) {
		log.debug("satValue "+presetNum+","+index+": "+value);
		StPreset preset = getPreset(presetNum);
		if(preset==null) return;
		switch (index) {
		case 1:  
			preset.setName(value.toString());
			persistNames();
			return;
//		case 2:  preset.setAmpName(value.toString()); return;
//		case 3:	 preset.getAmpTypeName();
//		case 4:	 preset.isCabinetEnabled()?preset.getCabinetName():NA;
//		case 5:	 preset.getGain()+"";
//		case 6:  preset.getTreble()+"";
//		case 7:  return preset.getMiddle()+"";
//		case 8:  return preset.getBass()+"";
//		case 9:  return preset.getVolume()+"";
//		case 10: return preset.getPresence()+"";
//		case 11: return preset.getNoiseReduction()+"";
//		case 12: return preset.isPedalEnabled()?preset.getPedalEffectName():NA;
//		case 13: return preset.isDelayEnabled()?preset.getDelayEffectName():NA;
//		case 14: return preset.isReverbEnabled()?preset.getReverbEffectName():NA;
			
		}
	}
	
	public String[] getNames() {
		ArrayList<String> list=new ArrayList<String>();
		for(int i=0;i<getRowCount();i++){
			StPreset p=get(i);
			if(p!=null) list.add(p.getName()); 
		}
		return list.toArray(new String[]{});
	}
	
	public void persistNames() {
		log.debug("Writing preset list names...");
		for(int i=0;i<getRowCount();i++){
			StPreset p=get(i);
			if(p!=null) Config.getInstance().setValue("presetlist.names."+i, p.getName());
		}
		Config.getInstance().saveConfig();
	}
	
	public void loadNames(){
		log.debug("Loading preset list names...");
		for(int i=0;i<getRowCount();i++){
			StPreset p=get(i);
			if(p!=null) 
				p.setName(Config.getInstance().getValue("presetlist.names."+i, p.getName()));
		}
	}

	@Override
	public void presetSaved(StPreset preset, int presetNumber) {
		if(preset==null){
			log.error("Got NULL preset! aborted.");
			return;
		}
		if(presetNumber<0 || presetNumber>=size()) {
			log.error("Invalid Peset Number!");
			return;
		}
		set(presetNumber,preset);
	}

	@Override
	public void presetSwitched(int p) {
		//do nothing
	}
}
