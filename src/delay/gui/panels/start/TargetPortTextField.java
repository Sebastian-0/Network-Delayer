/*
* Copyright (C) 2016 Sebastian Hjelm
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*/

package delay.gui.panels.start;

import java.util.prefs.Preferences;

import delay.Delayer;
import delay.gui.panels.AbstractTextField;

public class TargetPortTextField extends AbstractTextField {

	private static final String TARGET_PORT_KEY = "target_port";

	public TargetPortTextField(int columns, Preferences preferences) {
		super(preferences.get(TARGET_PORT_KEY, "1337"), columns);
	}
	
	@Override
	public void saveSettings(Delayer delayer, Preferences preferences) {
		delayer.setTargetPort(Integer.parseInt(getText()));
		preferences.put(TARGET_PORT_KEY, getText());
	}
}
