/*
* Copyright (C) 2016 Sebastian Hjelm
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*/

package delay.gui;

import javax.swing.JFrame;

import delay.Delayer;
import delay.gui.panels.PanelSwitcher;
import delay.gui.panels.running.RunningPanel;
import delay.gui.panels.start.StartPanel;

public class Window extends JFrame implements PanelSwitcher {
	private StartPanel startPanel;
	private RunningPanel runningPanel;
	private Delayer delayer;
	
	public Window() {
		setTitle("Network delayer");
		
		delayer = new Delayer();
		
		startPanel = new StartPanel(this, delayer);
		runningPanel = new RunningPanel(this, delayer);
		
		add(startPanel);
		pack();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void displayStartPanel() {
		remove(runningPanel);
		add(startPanel);
		pack();
		repaint();
	}

	@Override
	public void displayRunningPanel() {
		runningPanel.reset();
		
		remove(startPanel);
		add(runningPanel);
		pack();
		repaint();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		delayer.setConnectionListener(null);
		delayer.stop();
	}
}
