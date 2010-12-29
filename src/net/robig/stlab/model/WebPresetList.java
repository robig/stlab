package net.robig.stlab.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import net.robig.logging.Logger;
import net.robig.stlab.gui.IDeviceListener;
import net.robig.stlab.util.Config;


public class WebPresetList extends ArrayList<WebPreset> implements TableModel {
	private static final long serialVersionUID = 1L;
	public static final String NA="--";
	/** List of listeners */
    protected EventListenerList listenerList = new EventListenerList();
    Logger log = new Logger(this); 

    public WebPresetList(List<WebPreset> l) {
		for(WebPreset p: l)
			add(p);
	}
    
    
	public WebPreset getPreset(int num){
		if(size()>num)
			return get(num);
		return null;
	}
	
	int columnCount=5;
	public String requestData(int presetNum, int index){
		WebPreset preset = getPreset(presetNum);
		if(preset==null) return "NULL";
		switch (index) {
			case 0:	 return preset.getTitle(); //TODO: trim
			case 1:  return preset.getDescription();
			case 2:  return preset.getRating()+"";
			case 3:  return preset.getData().getAmpName(); 
		}
		return preset.getId()+"";
	}
	
	public String getCellInfo(int row, int col) {
		String data="";
		WebPreset preset=getPreset(row);
		switch (col){
		case 1: 
			return preset.getDescription();
		}
		return getColumnName(col)+": "+requestData(row, col)+"\n"+
			data;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		switch(columnIndex){
		case 0: return "Title";
		case 1: return "Description";
		case 2: return "Rating";
		case 3: return "AMP";
		case 4: return "ID";
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
	}
	
	public int[] getIds() {
		int[] ret=new int[getRowCount()];
		for(int i=0;i<getRowCount();i++){
			WebPreset p=get(i);
			if(p!=null) ret[i]=p.getId(); 
		}
		return ret;
	}

	public synchronized void notifyListeners(int row) {
		for(TableModelListener l:this.getTableModelListeners()){
			l.tableChanged(new TableModelEvent(this, row));
		}
	}
}
