/*
* Copyright (C) 2016 Sebastian Hjelm
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*/

package delay.gui.panels;

import java.util.prefs.Preferences;

import javax.swing.JTextField;

import delay.Delayer;

public abstract class AbstractTextField extends JTextField {
	public AbstractTextField(String defaultText, int columns) {
		super(defaultText, columns);
	}
	
	public abstract void saveSettings(Delayer delayer, Preferences preferences);
}
