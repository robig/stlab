package net.robig.stlab.gui.preferences;

import java.awt.BorderLayout;
import java.awt.Component;
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
	private TableModel model=null;
	
	public TablePreferenceCrontrol(String name, String prefix) {
		super(name, null);
		this.prefix=prefix;
		initialize();
	}
	
	class Model extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
		private MapValue value=null;  
		public Model(String prefix) {
			super(new Object[]{"Key","Value"},0);
			init(prefix);
		}
		public void init(String prefix){
			log.debug("requesting config keys for "+prefix);
			value=new MapValue(prefix);
			for(String name: value.keySet()){
				addRow(new Object[]{name,value.get(name)});
			}
		}
		@Override
		public boolean isCellEditable(int row, int column) {
			return column>0;
		}
	}

	private void initialize() {
		rootPane=new JPanel();
		rootPane.setLayout(new BorderLayout());
		JTable table=new JTable();
		rootPane.add(table,BorderLayout.CENTER);
		
		model=new Model(prefix);
		table.setModel(model);
		table.setBorder(BorderFactory.createLoweredBevelBorder());
		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent arg0) {
				log.debug("Model changed: "+arg0);
				onChange();
			}
		});
		//TODO: table.getTableHeader().set
		
		JButton addButton = new JButton("Add");
		rootPane.add(addButton,BorderLayout.AFTER_LAST_LINE);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addButtonPressed();
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

	private void addButtonPressed() {
		log.debug("Add button pressed");
		KeyValueDialog dialog=new KeyValueDialog(parent);
	}

}
