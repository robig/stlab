package net.robig.stlab.gui.preferences;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.beans.*; //property change stuff
import java.awt.*;
import java.awt.event.*;

class KeyValueDialog extends JDialog
                   implements ActionListener,
                              PropertyChangeListener {
    private String keyValue = null;
    private String valueValue = null;
    private JTextField keyField;
    private JTextField valueField;

    private JOptionPane optionPane;
    
    protected static final int MAX_KEY_LENGTH=50;
    protected static final int MAX_VALUE_LENGTH=256;

    private String btnString1 = "Enter";
    private String btnString2 = "Cancel";

    /**
     * Returns null if the typed string was invalid;
     * otherwise, returns the string as the user entered it.
     */
    public String getValidatedText() {
        return keyValue;
    }

    /** Creates the reusable dialog. */
    public KeyValueDialog(Frame aFrame) {
        super(aFrame, true);

        setTitle("Key,Value");

        keyField = new JTextField(10);
        valueField = new JTextField(20);

        //Create an array of the text and components to be displayed.
        String msgString1 = "Enter Key/Value-Pair:";
        Object[] array = {msgString1, "Key:", keyField, "Value:", valueField};

        //Create an array specifying the number of dialog buttons
        //and their text.
        Object[] options = {btnString1, btnString2};

        //Create the JOptionPane.
        optionPane = new JOptionPane(array,
                                    JOptionPane.QUESTION_MESSAGE,
                                    JOptionPane.YES_NO_OPTION,
                                    null,
                                    options,
                                    options[0]);

        //Make this dialog display it.
        setContentPane(optionPane);
        pack();

        //Handle window closing correctly.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                /*
                 * Instead of directly closing the window,
                 * we're going to change the JOptionPane's
                 * value property.
                 */
                    optionPane.setValue(new Integer(
                                        JOptionPane.CLOSED_OPTION));
            }
        });

        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
                keyField.requestFocusInWindow();
            }
        });

        //Register an event handler that puts the text into the option pane.
        keyField.addActionListener(this);
        valueField.addActionListener(this);

        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
        setVisible(true);
    }

    /** This method handles events for the text field. */
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(btnString1);
    }

    /** This method reacts to state changes in the option pane. */
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (isVisible()
         && (e.getSource() == optionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
             JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            //Reset the JOptionPane's value.
            //If you don't do this, then if the user
            //presses the same button next time, no
            //property change event will be fired.
            optionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);

            if (btnString1.equals(value)) {
                keyValue = keyField.getText();
                valueValue = valueField.getText();
                if (keyValue.length()==0 || keyValue.length()>MAX_KEY_LENGTH) {
                    //text was invalid
                    keyField.selectAll();
                    JOptionPane.showMessageDialog(
                                    KeyValueDialog.this,
                                    "Sorry, \"" + keyValue + "\" is invalid! Must be between 1 and "+MAX_KEY_LENGTH+" chars long.",
                                    "Try again",
                                    JOptionPane.ERROR_MESSAGE);
                    keyValue = null;
                    keyField.requestFocusInWindow();
                    return;
                }
                if (valueValue.length()>MAX_VALUE_LENGTH) {
                    //text was invalid
                    valueField.selectAll();
                    JOptionPane.showMessageDialog(
                                    KeyValueDialog.this,
                                    "Sorry, the Value is invalid! Must not  have more than "+MAX_KEY_LENGTH+" chars.",
                                    "Try again",
                                    JOptionPane.ERROR_MESSAGE);
                    keyValue = null;
                    keyField.requestFocusInWindow();
                    return;
                }
                clearAndHide();
            } else { //user closed dialog or clicked cancel
                keyValue = null;
                clearAndHide();
            }
        }
    }

    /** This method clears the dialog and hides it. */
    public void clearAndHide() {
        keyField.setText(null);
        setVisible(false);
    }
    
    public String getKey() {
		return keyValue;
	}
    
    public String getValue() {
		return valueValue;
	}
    
}

