package net.robig.stlab.gui.preferences;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import net.robig.logging.Logger;
import net.robig.stlab.util.config.AbstractValue;
import net.robig.stlab.util.config.MapValue;
import net.robig.stlab.util.config.ObjectConfig;
import net.robig.stlab.util.config.StringValue;

public class TablePreferenceCrontrol extends AbstractPreferenceControl {

	public static TableModel toTableModel(Map<?,?> map) {
	    DefaultTableModel model = new DefaultTableModel(
	        new Object[] { "Key", "Value" }, 0
	    );
	    for (Map.Entry<?,?> entry : map.entrySet()) {
	        model.addRow(new Object[] { entry.getKey(), entry.getValue() });
	    }
	    return model;
	}

	private String prefix=null;
	private JPanel rootPane=null;
	private Logger log=new Logger(this);
	private Model model=null;
	private JTable table;
	private String[] defaultKeys={};
	
	public TablePreferenceCrontrol(String name, String prefix) {
		super(name, null);
		this.prefix=prefix;
		initialize();
	}
	
	public TablePreferenceCrontrol(String name, String prefix, String[] defaultKeys) {
		super(name, null);
		this.prefix=prefix;
		this.defaultKeys=defaultKeys;
		initialize();
	}
	
	class Model implements TableModelListener, TableModel{
		private static final long serialVersionUID = 1L;
		protected EventListenerList listenerList = new EventListenerList();
		private MapValue valueMap=null;  
		public Model(String prefix) {
			init(prefix);
		}
		public void init(String prefix){
			log.debug("requesting config keys for "+prefix);
			valueMap=(MapValue) ObjectConfig.getAbstractValue(prefix, new MapValue(prefix));
			addTableModelListener(this);
		}
		@Override
		public boolean isCellEditable(int row, int column) {
			return column>0;
		}
		
		public void add(String key, String val){
			valueMap.add(key, val);
			notifyListeners(valueMap.keySet().size()-1);
		}
		
		public void remove(String key){
			boolean found=false;
			for(int i=0; i<getRowCount();i++){
				if(getKey(i).equals(key)){
					valueMap.remove(key);
					found=true;
				}
				if(found)
					notifyListeners(i);
			}
		}
		
		public void update(String key, String value){
			StringValue v=valueMap.get(key);
			if(v==null) add(key,value);
			else{
				v.setValue(value);
			}
		}
		
		public String getKey(int index){
			if(index>=getRowCount()) return "";
			return (String) getValueAt(index,0);
		}
		
		public String getValue(int index){
			if(index>=getRowCount()) return "";
			return (String) getValueAt(index,1);
		}
		
		@Override
		public void tableChanged(TableModelEvent e) {
			String key=getKey(e.getFirstRow());
			update(key,getValue(e.getFirstRow()));
			
		}
		
		@Override
		public void addTableModelListener(TableModelListener l) {
			listenerList.add(TableModelListener.class, l);
		}
		@Override
		public void removeTableModelListener(TableModelListener l) {
			listenerList.remove(TableModelListener.class, l);
		}
		
		public TableModelListener[] getTableModelListeners() {
	        return (TableModelListener[])listenerList.getListeners(
	                TableModelListener.class);
	    }
		private synchronized void notifyListeners(int row) {
			for(TableModelListener l:this.getTableModelListeners()){
				l.tableChanged(new TableModelEvent(this, row));
			}
		}
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}
		@Override
		public int getColumnCount() {
			return 2;
		}
		@Override
		public String getColumnName(int columnIndex) {
			switch(columnIndex){
			case 0: return "Key";
			case 1: return "Value";
			}
			return null;
		}
		@Override
		public int getRowCount() {
			return valueMap.keySet().size();
		}
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(columnIndex==0){
				return valueMap.keySet().toArray()[rowIndex];
			}
			return valueMap.get((String) getValueAt(rowIndex, 0)).toString();
		}
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			String key=(String) valueMap.keySet().toArray()[rowIndex];
			StringValue v=valueMap.get(key);
			if(v!=null){
				v.setValue(aValue.toString());
			}
		}
	}

	private void initialize() {
		rootPane=new JPanel();
		rootPane.setLayout(new BorderLayout());
		table=new JTable();
		rootPane.add(table,BorderLayout.CENTER);
		
		model=new Model(prefix);
		//Add default keys:
		boolean found=false;
		for(String k:defaultKeys){
			found=false;
			for(int i=0;i<model.getRowCount();i++){
				if(model.getKey(i).equals(k)) found=true;
			}
			if(!found)
				model.add(k, "");
		}
		
		table.setModel(model);
		table.setBorder(BorderFactory.createLoweredBevelBorder());
		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent arg0) {
				if(arg0.getType()==TableModelEvent.UPDATE){
					log.debug("Model changed: "+arg0);
					arg0.getType();
					//arg0.getFirstRow()
					onChange();
				}
			}
		});
		
		JPanel buttonPanel=new JPanel(new FlowLayout());
		rootPane.add(buttonPanel, BorderLayout.AFTER_LAST_LINE);
		
		JButton addButton = new JButton("Add");
		buttonPanel.add(addButton);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addButtonPressed();
			}
		});
		
		JButton delButton = new JButton("Remove");
		buttonPanel.add(delButton);
		delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				delButtonPressed();
			}
		});
	}

	@Override
	public Component getComponent() {
		return rootPane;
	}

	@Override
	public void onChange() {
//		log.debug("onChange");
	}

	@Override
	public void configUpdated() {
//		log.debug("configUpdated");
	}

	private void delButtonPressed() {
		log.debug("DEL button pressed");
		model.remove(model.getKey(table.getSelectedRow()));
	}
	
	private void addButtonPressed() {
		log.debug("Add button pressed");
		KeyValueDialog dialog=new KeyValueDialog(parent);
		if(dialog.getKey() != null && dialog.getValue()!=null)
			model.add(dialog.getKey(),dialog.getValue());
	}

}
