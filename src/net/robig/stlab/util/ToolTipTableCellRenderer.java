package net.robig.stlab.util;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public abstract class ToolTipTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected,
				hasFocus, row, column);
		setToolTipText(getCellInfo(row, column));
		return this;
	}
	
	public abstract String getCellInfo(int r, int c);
}
