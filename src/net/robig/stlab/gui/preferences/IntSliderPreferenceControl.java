package net.robig.stlab.gui.preferences;

import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.robig.stlab.util.config.IntValue;

public class IntSliderPreferenceControl extends AbstractPreferenceControl {

	JSlider slider=null;
	IntValue configValue=null;
	int min=0; int max=10;
	
	public IntSliderPreferenceControl(String name, IntValue config, int min, int max) {
		super(name, config);
		configValue=config;
		this.min=min; this.max=max;
		initialize();
	}

	private void initialize() {
		slider=new JSlider();
		//slider2.setBorder(BorderFactory.createTitledBorder("JSlider with Tick Marks"));
		slider.setMinimum(min);
		slider.setMaximum(max);
		int range=max-min;
		slider.setMajorTickSpacing(range/5);
		slider.setMinorTickSpacing(range/20);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				onChange();
			}
		});
		configUpdated();
	}

	@Override
	public JComponent getComponent() {
		return slider;
	}

	@Override
	public void onChange() {
		configValue.setValue(slider.getValue());
	}

	@Override
	public void configUpdated() {
		slider.setValue(configValue.getSimpleValue());
	}

}
