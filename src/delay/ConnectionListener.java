/*
* Copyright (C) 2016 Sebastian Hjelm
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*/

package delay;

public interface ConnectionListener {
	public void connectionLost(String reason);
	public void connectedToSource();
	public void connectedToTarget();
}
