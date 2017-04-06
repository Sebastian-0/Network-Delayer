/*
* Copyright (C) 2016 Sebastian Hjelm
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*/

package delay.gui.panels.running;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.net.InetAddress;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import delay.ConnectionListener;
import delay.Delayer;
import delay.gui.panels.PanelSwitcher;
import util.SimpleGridBagLayout;

public class RunningPanel extends JPanel {
	
	private PanelSwitcher panelSwitcher;
	private Delayer delayer;
	private JLabel statusLabel;
	
	public RunningPanel(PanelSwitcher panelSwitcher, Delayer delayer) {
		this.panelSwitcher = panelSwitcher;
		this.delayer = delayer;
		
		statusLabel = new JLabel();
		statusLabel.setOpaque(true);
		statusLabel.setBorder(new EmptyBorder(5, 5, 5, 5));

		SimpleGridBagLayout layout = new SimpleGridBagLayout(this);
		final DelaySlider slider = new DelaySlider(delayer);
		layout.addToGrid(slider, 0, 0, 1, 1, GridBagConstraints.BOTH, 1, 0);
		layout.addToGrid(statusLabel, 0, 1, 1, 1);
		layout.addToGrid(new InfiniteDelayCheckbox(delayer, slider), 0, 2, 1, 1);
		layout.addToGrid(new AbortButton(this), 0, 3, 1, 1);
	}
	
	public void abort() {
		delayer.setConnectionListener(null);
		delayer.stop();
		panelSwitcher.displayStartPanel();
	}
	
	private ConnectionListener listener = new ConnectionListener() {
		@Override
		public void connectionLost(String reason) {
			JOptionPane.showMessageDialog(RunningPanel.this, "Connection lost: " + reason, "Error", JOptionPane.ERROR_MESSAGE);
			abort();
		}

		@Override
		public void connectedToSource() {
			statusLabel.setText("Connecting to target...");
		}

		@Override
		public void connectedToTarget() {
			statusLabel.setText("Connection open!");
			statusLabel.setBackground(new Color(60, 200, 0));
		}
	};

	public void reset() {
		statusLabel.setText("Waiting for connection...");
		statusLabel.setBackground(Color.ORANGE);
		delayer.setConnectionListener(listener);
	}
}
