package com.coltla.server;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Server implements Runnable {
	private DatagramSocket socket;
	private int port;
	
	private boolean isRunning = false;
	
	private Thread serverThread;
	private Thread manageThread;
	private Thread sendThread;
	private Thread receiveThread;

  public Server(int port) {
    this.port = port;
    
    try {
		socket = new DatagramSocket(port);
	} catch (SocketException soex) {
		soex.printStackTrace();
	}
    
    serverThread = new Thread(this, "Server Thread");
  }

	public void run() {
		isRunning = true;
		manageClients();
		receive();
	}
	
	private void manageClients() {
		manageThread = new Thread("Manage Thread") {
			public void run() {
				while (isRunning) {
					// Managing Clients
				}
			}
		};
		
		manageThread.start();
	}

	private void receive() {
		receiveThread = new Thread("Receive Thread") {
			public void run() {
				while (isRunning) {
					// Receive Data
				}
			}
		};
		
		receiveThread.start();
	}
}
