package net.robig.stlab.model;

import java.util.ArrayList;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;


public class PresetList extends ArrayList<StPreset> implements TableModel {
	private static final long serialVersionUID = 1L;
	public static final String NA="--";

	public StPreset getPreset(int num){
		if(size()>num)
			return get(num);
		return null;
	}
	
	int columnCount=14;
	public String requestData(int presetNum, int index){
		StPreset preset = getPreset(presetNum);
		if(preset==null) return "NULL";
		switch (index) {
		case 1:  return preset.getAmpName();
		case 2:	 return preset.getAmpTypeName();
		case 3:	 return preset.isCabinetEnabled()?preset.getCabinetName():NA;
		case 4:	 return preset.getGain()+"";
		case 5:  return preset.getTreble()+"";
		case 6:  return preset.getMiddle()+"";
		case 7:  return preset.getBass()+"";
		case 8:  return preset.getVolume()+"";
		case 9:  return preset.getPresence()+"";
		case 10: return preset.getNoiseReduction()+"";
		case 11: return preset.isPedalEnabled()?preset.getPedalEffectName():NA;
		case 12: return preset.isDelayEnabled()?preset.getDelayEffectName():NA;
		case 13: return preset.isReverbEnabled()?preset.getReverbEffectName():NA;
			
		}
		return preset.getNumber()+"";
	}
	
	public String getCellInfo(int row, int col) {
		String data="";
		StPreset preset=getPreset(row);
		switch (col){
		case 11: data=preset.isPedalEnabled()?"Edit: "+preset.getPedalEdit():"disabled";break;
		case 12: data=preset.isDelayEnabled()?"" +
				(preset.delayHasDepth()?"Depth: "+preset.getDelayDepth()+"\n":"")+
				(preset.delayHasFeedback()?" Feedback: "+preset.getDelayFeedback()+"\n":"")+
				"Speed: "+preset.getDelaySpeedString():"disabled";break;
		case 13: data=preset.isReverbEnabled()?"Value: "+preset.getReverbEffect():"disabled";break;
		}
		return getColumnName(col)+" "+requestData(row, col)+"\n"+
			data;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		switch(columnIndex){
		case 1: return "AMP";
		case 2: return "TYP";
		case 3: return "Cabinet";
		case 4: return "Gain";
		case 5: return "Treble";
		case 6: return "Middle";
		case 7: return "Bass";
		case 8: return "Volume";
		case 9: return "Presence";
		case 10: return "NR";
		case 11: return "Pedal";
		case 12: return "Mod/Delay";
		case 13: return "Reverb";
		}
		
		return "Nr";
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {
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
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}


}
