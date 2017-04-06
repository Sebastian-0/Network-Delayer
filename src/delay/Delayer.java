/*
* Copyright (C) 2016 Sebastian Hjelm
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*/

package delay;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Delayer {
	private int hostPort;
	
	private String targetIp;
	private int targetPort;
	
	private volatile int transmissionDelay;
	private volatile boolean suspendMessages;
	
	private Timer timer;
	private StartupThread startupThread;
	
	private ConnectionListener connectionListener;

	
	public void setHostPort(int hostPort) {
		this.hostPort = hostPort;
	}
	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
	}
	public void setTargetPort(int targetPort) {
		this.targetPort = targetPort;
	}
	public void setTransmissionDelay(int delayInMillis) {
		transmissionDelay = delayInMillis;
	}
	public void setSuspendMessages(boolean suspendMessages) {
		this.suspendMessages = suspendMessages;
	}
	public void setConnectionListener(ConnectionListener connectionListener) { 
		this.connectionListener = connectionListener;
	}
	
	
	public void start() { 
		timer = new Timer();
		startupThread = new StartupThread();
		startupThread.start();
	}

	public void stop() {
		if (timer != null) {
			timer.cancel();
			try {
				if (startupThread.serverSocket != null)
					startupThread.serverSocket.close();
			} catch (IOException e) {
			}
			startupThread.interrupt();
		}
	}


	private class StartupThread extends Thread {
		
		private ServerSocket serverSocket;

		@Override
		public void run() {
			serverSocket = createServerSocket();
			
			Socket sourceSocket = null;
			InputStream sourceInputStream = null;
			OutputStream sourceOutputStream = null;
			while (sourceSocket == null) {
				try {
					sourceSocket = serverSocket.accept();
					sourceInputStream = sourceSocket.getInputStream();
					sourceOutputStream = sourceSocket.getOutputStream();
				} catch (IOException e) {
					if (Thread.currentThread().isInterrupted()) {
						closeSilently(serverSocket);
						if (connectionListener != null)
							connectionListener.connectionLost("Manual abortion");
						return;
					}
				}
			}
			
			if (connectionListener != null)
				connectionListener.connectedToSource();

			Socket targetSocket = null;
			InputStream targetInputStream = null;
			OutputStream targetOutputStream = null;
			try {
				targetSocket = new Socket(targetIp, targetPort);
				targetInputStream = targetSocket.getInputStream();
				targetOutputStream = targetSocket.getOutputStream();
			} catch (IOException e) {
				closeSilently(serverSocket);
				System.out.println("Couldn't connect to target");
				if (connectionListener != null)
					connectionListener.connectionLost("Couldn't connect to target");
				return;
			}
			
			if (connectionListener != null)
				connectionListener.connectedToTarget();

			Thread sourceToTarget = new Thread(new ConnectionRunnable(sourceInputStream, targetOutputStream));
			Thread targetToSource = new Thread(new ConnectionRunnable(targetInputStream, sourceOutputStream));
			sourceToTarget.start();
			targetToSource.start();
			
			while (!Thread.currentThread().isInterrupted() && !sourceSocket.isClosed() && !targetSocket.isClosed() &&
					sourceToTarget.isAlive() && targetToSource.isAlive()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
			
			sourceToTarget.interrupt();
			targetToSource.interrupt();

			timer.cancel();
			closeSilently(sourceSocket);
			closeSilently(targetSocket);

			if (connectionListener != null)
				connectionListener.connectionLost("Connection closed");
		}
		
		private ServerSocket createServerSocket() { 
			try {
				return new ServerSocket(hostPort);
			} catch (IOException e1) {
				System.out.println("Failed to create server socket");
			}
			return null;
		}
		
		private void closeSilently(Closeable closable) { 
			try {
				closable.close();
			} catch (IOException e) {
			}
		}
	};
	
	
	private class ConnectionRunnable implements Runnable {
		
		private InputStream sourceInputStream;
		private OutputStream targetOutputStream;
		
		private long latestMessageArrivalTime;
		
		private List<Task> suspendedTasks;
		
		public ConnectionRunnable(InputStream sourceInputStream, OutputStream targetOutputStream) {
			this.sourceInputStream = sourceInputStream;
			this.targetOutputStream = targetOutputStream;
			suspendedTasks = new ArrayList<Task>();
		}

		@Override
		public void run() {
			try {
				byte[] dataPool = new byte[1024];
				while (true) {
					int amount = sourceInputStream.read(dataPool);
					if (amount < 0) {
						break;
					}
					byte[] data = new byte[amount];
					System.arraycopy(dataPool, 0, data, 0, amount);
					final Task timerTask = new Task(targetOutputStream, data);
					if (suspendMessages) {
						suspendedTasks.add(timerTask);
					} else {
						if (!suspendedTasks.isEmpty()) {
							for (Task task : suspendedTasks) {
								addToTimer(task);
							}
							suspendedTasks.clear();
						}
						addToTimer(timerTask);
					}
				}
			} catch (IOException e) {
				System.out.println("Failed to read data");
				connectionListener.connectionLost("The connection closed");
			}
		}

		private void addToTimer(final Task timerTask) {
			int delay = Math.max(transmissionDelay, (int)(latestMessageArrivalTime - System.currentTimeMillis()) + 1);
			timer.schedule(timerTask, delay);
			latestMessageArrivalTime = System.currentTimeMillis() + delay;
		}
	}
	
	
	private static class Task extends TimerTask {
		
		private OutputStream targetStream;
		private byte[] data;
		
		public Task(OutputStream targetStream, byte[] data) {
			this.targetStream = targetStream;
			this.data = data;
		}

		@Override
		public void run() {
			try {
				targetStream.write(data);
				targetStream.flush();
			} catch (IOException e) {
				System.out.println("Failed to write data");
			}
		}
	}
}
