/*
* Copyright (C) 2016 Sebastian Hjelm
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*/

package delay.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.SimpleGridBagLayout;

public abstract class AbstractValueSlider extends JPanel
{
	private JSlider slider;
	private JTextField textField;

	public AbstractValueSlider(int min, int max, int initial, String borderTitle)
	{
		slider = new JSlider(min, max, initial);
		textField = new JTextField(Integer.toString(initial), 5);

		slider.addChangeListener(changeListener);
		textField.addActionListener(actionListener);

		slider.setMajorTickSpacing(max - min);
		slider.setPaintLabels(true);

		SimpleGridBagLayout layout = new SimpleGridBagLayout(this);
		layout.addToGrid(slider, 0, 0, 1, 1, GridBagConstraints.HORIZONTAL, 1, 0);
		layout.addToGrid(textField, 1, 0, 1, 1, GridBagConstraints.HORIZONTAL, 0.3, 0);

		setBorder(new TitledBorder(borderTitle));
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		slider.setEnabled(enabled);
		textField.setEnabled(enabled);
	}


	private ChangeListener changeListener = new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent e)
		{
			textField.setText(Integer.toString(slider.getValue()));
			valueChangedByUser(slider.getValue());
		}
	};

	protected abstract void valueChangedByUser(int newValue);



	private ActionListener actionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				int newValue = Integer.parseInt(textField.getText());
				slider.setValue(newValue);
				valueChangedByUser(slider.getValue());
			}
			catch (IllegalArgumentException e) { }
		}
	};
	
	public void setValue(int value) {
		slider.setValue(value);
		textField.setText(String.valueOf(value));
	}
}
