/*
* Copyright (C) 2016 Sebastian Hjelm
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*/

package delay.gui.panels.running;

import delay.gui.panels.AbstractButton;

public class AbortButton extends AbstractButton {
	
	private RunningPanel runningPanel;

	public AbortButton(RunningPanel runningPanel) {
		super("Abort");
		this.runningPanel = runningPanel;
	}

	@Override
	protected void doAction() {
		runningPanel.abort();
	}
}
