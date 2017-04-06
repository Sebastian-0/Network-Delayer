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
import delay.gui.panels.AbstractValueSlider;

public class DelaySlider extends AbstractValueSlider {
	
	private Delayer delayer;

	public DelaySlider(Delayer delayer) {
		super(0, 5000, 0, "Transmission Delay");
		this.delayer = delayer;
	}

	@Override
	protected void valueChangedByUser(int newValue) {
		delayer.setTransmissionDelay(newValue);
	}
}
