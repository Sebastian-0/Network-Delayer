/*
* Copyright (C) 2016 Sebastian Hjelm
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*/

package delay.gui.panels.start;

import java.awt.GridBagConstraints;
import java.util.prefs.Preferences;

import javax.swing.JLabel;
import javax.swing.JPanel;

import util.SimpleGridBagLayout;
import delay.Delayer;
import delay.gui.panels.PanelSwitcher;

public class StartPanel extends JPanel {
	
	private Preferences preferences;
	
	private PanelSwitcher panelSwitcher;
	private Delayer delayer;

	private HostPortTextField hostPortField;
	
	private TargetIpTextField targetIpField;
	private TargetPortTextField targetPortField;
	
	public StartPanel(PanelSwitcher panelSwitcher, Delayer delayer) {
		this.panelSwitcher = panelSwitcher;
		this.delayer = delayer;
		
		preferences = Preferences.userNodeForPackage(Delayer.class);
		
		final int columns = 12;
		hostPortField = new HostPortTextField(columns, preferences);
		targetIpField = new TargetIpTextField(columns, preferences);
		targetPortField = new TargetPortTextField(columns, preferences);
		
		SimpleGridBagLayout layout = new SimpleGridBagLayout(this);
		layout.addToGrid(new JLabel("Port"), 0, 0, 1, 1);
		layout.addToGrid(hostPortField, 1, 0, 1, 1, GridBagConstraints.BOTH, 1, 0);
		layout.addToGrid(new JLabel("Target IP"), 0, 1, 1, 1);
		layout.addToGrid(targetIpField, 1, 1, 1, 1, GridBagConstraints.BOTH, 1, 0);
		layout.addToGrid(new JLabel("Target Port"), 0, 2, 1, 1);
		layout.addToGrid(targetPortField, 1, 2, 1, 1, GridBagConstraints.BOTH, 1, 0);
		layout.addToGrid(new StartButton(this), 0, 3, 2, 1);
	}
	
	
	public void start() { 
		hostPortField.saveSettings(delayer, preferences);
		targetIpField.saveSettings(delayer, preferences);
		targetPortField.saveSettings(delayer, preferences);
		panelSwitcher.displayRunningPanel();
		delayer.start();
	}
}
