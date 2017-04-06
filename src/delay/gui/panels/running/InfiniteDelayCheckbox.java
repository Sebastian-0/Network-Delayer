/*
* Copyright (C) 2016 Sebastian Hjelm
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*/

package delay.gui.panels.running;

import delay.Delayer;
import delay.gui.panels.AbstractCheckbox;

public class InfiniteDelayCheckbox extends AbstractCheckbox {
	private Delayer delayer;
	private DelaySlider delaySlider;

	public InfiniteDelayCheckbox(Delayer delayer, DelaySlider delaySlider) {
		super("Simulate loss of connection");
		this.delayer = delayer;
		this.delaySlider = delaySlider;
	}

	@Override
	protected void doAction() {
		delayer.setSuspendMessages(isSelected());
		delaySlider.setEnabled(!isSelected());
	}

}
