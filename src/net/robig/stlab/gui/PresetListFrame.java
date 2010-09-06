package net.robig.stlab.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import net.robig.logging.Logger;
import net.robig.stlab.StLab;
import net.robig.stlab.StLabConfig;
import net.robig.stlab.model.PresetList;
import net.robig.stlab.util.config.IntValue;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class PresetListFrame extends JFrame implements MouseListener,WindowListener,ComponentListener,KeyListener {

	private static final Color FOREGROUND=new Color(187,154,77);
	
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JScrollPane listScrollPane = null;
	private JTable presetList = null;
	private IDeviceController device=null;
	private DeviceFrame parent=null;
	PresetList list=null;
	Logger log = new Logger(this);
	IntValue width=null;
	IntValue height=null;
	IntValue x=null;
	IntValue y=null;
	boolean initialized=false;

	/**
	 * This is the default constructor
	 */
	public PresetListFrame(DeviceFrame mainWindow) {
		super();
		initializeGui();
		parent=mainWindow;
		device=mainWindow.getDeviceController();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initializeGui() {
		width=StLabConfig.getPresetListWindowWidth();
		height=StLabConfig.getPresetListWindowHeight();
		x=StLabConfig.getPresetListWindowX();
		y=StLabConfig.getPresetListWindowY();
//		this.setSize(width.getValue(),
//				height.getValue());
		this.setBounds(x.getValue(), y.getValue(), width.getValue(), height.getValue());
		this.setContentPane(getJContentPane());
		this.setTitle(StLab.applicationName+" Preset List");
		this.setName("Preset List");
		this.addWindowListener(this);
		this.addComponentListener(this);
		this.addKeyListener(this);
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
		initialized=true;
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
			presetList.setBackground(new Color(44,45,48));
			presetList.setForeground(FOREGROUND);
			presetList.setSelectionBackground(Color.BLACK);
			presetList.setSelectionForeground(new Color(204,75,73));
			presetList.setGridColor(new Color(92,77,38));
			presetList.getTableHeader().setForeground(FOREGROUND);
			presetList.getTableHeader().setBackground(Color.BLACK);
			TableColumnModel colModel = presetList.getColumnModel();
	        for(int j = 0; j < colModel.getColumnCount(); j++)
	            colModel.getColumn(j).setCellRenderer(new RowRenderer());
	        presetList.addMouseListener(this);
	        presetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        presetList.setFocusable(true);
	        presetList.addKeyListener(this);
		}
		return presetList;
	}

	public void setList(PresetList list){
		this.list=list;
		updateTable();
	}

	public void setSelectionIndex(int offset){
		if(!initialized) return;
		presetList.setRowSelectionInterval(offset, offset);
	}
	
	public int getSelectionIndex() {
		return presetList.getSelectedRow();
	}
	
	private void updateTable() {
		presetList.setModel(list);
		TableColumnModel colModel = presetList.getColumnModel();
        for(int j = 0; j < colModel.getColumnCount(); j++)
            colModel.getColumn(j).setCellRenderer(new RowRenderer());
	}
	
	class RowRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
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

	
	private void onChange() {
		int p=getSelectionIndex();
		log.info("Selected "+p);
		try {
			device.selectPreset(p);
		} catch (Exception e1) {
			e1.printStackTrace(log.getWarnPrintWriter());
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		onChange();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		parent.setPresetListVisible(false);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		parent.setPresetListVisible(false);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		x.setValue(getX());
		y.setValue(getY());
	}

	@Override
	public void componentResized(ComponentEvent e) {
		width.setValue(getWidth());
		height.setValue(getHeight());
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ENTER){
			onChange();
		}else if(e.getKeyCode()==KeyEvent.VK_SPACE){
			parent.setPresetListVisible(false);
		}else if(e.getKeyCode()==KeyEvent.VK_TAB){
			parent.requestFocus();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
