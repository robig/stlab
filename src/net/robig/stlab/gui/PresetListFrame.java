package net.robig.stlab.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import net.robig.logging.Logger;
import net.robig.stlab.model.PresetList;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PresetListFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JScrollPane listScrollPane = null;
	private JTable presetList = null;
	private IDeviceController device=null;
	PresetList list=null;
	Logger log = new Logger(this);

	/**
	 * This is the default constructor
	 */
	public PresetListFrame(IDeviceController controller) {
		super();
		initializeGui();
		device=controller;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initializeGui() {
		this.setSize(637, 450);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}
	
	/**
	 * Fills the table initially with data from the device
	 */
	public void initializeData() {
		PresetList l=new PresetList();
		try {
			for(int i=0;i<100;i++){
				l.add(device.getPresetParameters(i));
			}
		}catch(Exception e){
			e.printStackTrace(log.getWarnPrintWriter());
		}
		setList(l);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getListScrollPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes listScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getListScrollPane() {
		if (listScrollPane == null) {
			listScrollPane = new JScrollPane();
			listScrollPane.setViewportView(getPresetList());
		}
		return listScrollPane;
	}

	/**
	 * This method initializes presetList	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getPresetList() {
		if (presetList == null) {
			presetList = new JTable();
			presetList.setShowGrid(true);
			TableColumnModel colModel = presetList.getColumnModel();
	        for(int j = 0; j < colModel.getColumnCount(); j++)
	            colModel.getColumn(j).setCellRenderer(new RowRenderer());
		}
		return presetList;
	}

	public void setList(PresetList list){
		this.list=list;
		updateTable();
	}

	private void updateTable() {
		presetList.setModel(list);
		TableColumnModel colModel = presetList.getColumnModel();
        for(int j = 0; j < colModel.getColumnCount(); j++)
            colModel.getColumn(j).setCellRenderer(new RowRenderer());
	}
	
	public static void main(String[] args) {
		DummyDeviceController controller=new DummyDeviceController();
		controller.initialize();
		PresetListFrame frame=new PresetListFrame(controller);
		PresetList list=controller.getPresetList();
		frame.setList(list);
		// Close Button of window:
		frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
		frame.show();
	}
	
	class RowRenderer extends DefaultTableCellRenderer {
	    public Component getTableCellRendererComponent(JTable table,
	                                                   Object value,
	                                                   boolean isSelected,
	                                                   boolean hasFocus,
	                                                   int row, int column) {
	        super.getTableCellRendererComponent(table, value, isSelected,
	                                            hasFocus, row, column);
	        setToolTipText(list.getCellInfo(row, column));
	        return this;
	    }
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
