package net.robig.stlab.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import net.robig.logging.Logger;

public class WebPresetList extends ArrayList<WebPreset> implements TableModel {
	private static final long serialVersionUID = 1L;
	private static final int MAX_TABLE_CELL_LENGTH=25;
	public static final String NA="--";
	/** List of listeners */
    protected EventListenerList listenerList = new EventListenerList();
    Logger log = new Logger(this); 
    int orderIndex=0;
    boolean orderDesc=false;

    public WebPresetList(List<WebPreset> l) {
		for(WebPreset p: l)
			add(p);
	}
    
    /**
     * shorten a string ot its maximum length
     * @param in
     * @return
     */
    private static String shorten(String in){
    	if(in.length()>MAX_TABLE_CELL_LENGTH)
    		return in.substring(0,MAX_TABLE_CELL_LENGTH-2)+"..";
    	return in;
    }
    
    
	public WebPreset getPreset(int num){
		if(size()>num)
			return get(num);
		return null;
	}
	
	int columnCount=4;
	public String requestData(int presetNum, int index){
		WebPreset preset = getPreset(presetNum);
		if(preset==null) return "NULL";
		switch (index) {
			case 0:	 return preset.getTitle();
			case 1:  return preset.getDescription();
			case 2:  return preset.getCreatedFormated();
			case 3:  return preset.getRating()+"";
		}
		return preset.getId()+"";
	}
	
	public String getCellInfo(int row, int col) {
		String data="";
		WebPreset preset=getPreset(row);
		switch (col){
			case 0: return preset.getTitle();
			case 1: return preset.getDescription();
			case 2: return preset.getCreatedFormated();
			case 3: return preset.getVoteCount()+" votes. average: "+preset.getVoteAvg();
		}
		return getColumnName(col)+": "+requestData(row, col)+"\n"+
			data;
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		return (orderIndex==columnIndex?(orderDesc?"<":">"):" ")+
				getHeaderName(columnIndex)+
				(orderIndex==columnIndex?(orderDesc?"<":">"):" ");
	}
	
	public void setOrderIndex(int i){
		orderIndex=i;
	}
	
	public void setOrderDesc(boolean b){
		orderDesc=b;
	}
	
	public static String getHeaderName(int columnIndex) {
		switch(columnIndex){
			case 0: return "Title";
			case 1: return "Description";
			case 2: return "Created";
			case 3: return "Rating";
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
//		log.debug("satValue "+presetNum+","+index+": "+value);
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
