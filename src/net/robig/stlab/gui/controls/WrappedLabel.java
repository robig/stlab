package net.robig.stlab.gui.controls;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public class WrappedLabel extends JTextArea {
	private static final long serialVersionUID = 6212590661353410646L;
	JLabel label = new JLabel();
    
    public WrappedLabel(String text) {
        super(text);
        init();
    }
    
    public WrappedLabel() {
    	init();
	}
    
    private void init() {
        this.setEditable(false);
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setFont(label.getFont());
        this.setOpaque(false);
    }
}

