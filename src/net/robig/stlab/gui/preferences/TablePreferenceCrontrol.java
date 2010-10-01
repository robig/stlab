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
	
	public TablePreferenceCrontrol(String name, String prefix) {
		super(name, null);
		this.prefix=prefix;
		initialize();
	}
	
	class Model extends DefaultTableModel implements TableModelListener{
		private static final long serialVersionUID = 1L;
		private MapValue valueMap=null;  
		public Model(String prefix) {
			super(new Object[]{"Key","Value"},0);
			init(prefix);
		}
		public void init(String prefix){
			log.debug("requesting config keys for "+prefix);
			valueMap=new MapValue(prefix);
			for(String name: valueMap.keySet()){
				addRow(new Object[]{name,valueMap.get(name)});
			}
			addTableModelListener(this);
		}
		@Override
		public boolean isCellEditable(int row, int column) {
			return column>0;
		}
		
		public void add(String key, String val){
			valueMap.add(key, val);
			addRow(new Object[]{key,val});
		}
		
		public void remove(String key){
			for(int i=0; i<getRowCount();i++){
				if(getKey(i).equals(key)){
					removeRow(i);
					valueMap.remove(key);
					return;
				}
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
	}

	private void initialize() {
		rootPane=new JPanel();
		rootPane.setLayout(new BorderLayout());
		table=new JTable();
		rootPane.add(table,BorderLayout.CENTER);
		
		model=new Model(prefix);
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
		
	}

	@Override
	public void configUpdated() {

	}

	private void delButtonPressed() {
		log.debug("DEL button pressed");
		model.remove(model.getKey(table.getSelectedRow()));
	}
	
	private void addButtonPressed() {
		log.debug("Add button pressed");
		KeyValueDialog dialog=new KeyValueDialog(parent);
		model.add(dialog.getKey(),dialog.getValue());
	}

}
