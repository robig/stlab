package net.robig.gui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public final class CursorController {

  public final static Cursor busyCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
  public final static Cursor defaultCursor = Cursor.getDefaultCursor();

private CursorController() {}

	public static ActionListener createListener(final Component component, final ActionListener mainActionListener) {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					component.setCursor(busyCursor);
					mainActionListener.actionPerformed(ae);
				} finally {
					component.setCursor(defaultCursor);
				}
			}
		};
		return actionListener;
	}
	
	public static ChangeListener createListener(final Component component, final ChangeListener mainActionListener) {
		ChangeListener actionListener = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				try {
					component.setCursor(busyCursor);
					mainActionListener.stateChanged(arg0);
				} finally {
					component.setCursor(defaultCursor);
				}
			}
		};
		return actionListener;
	}
}

