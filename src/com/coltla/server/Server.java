package com.coltla.server;

import java.io.IOException;
import java.net.DatagramPacket;
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
    serverThread.run();
  }

	public void run() {
		isRunning = true;
		System.out.println("Server started on port " + port);
		
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
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (IOException ioex) {
						ioex.printStackTrace();
					}
					
					String received = new String(packet.getData());
					System.out.println(received);
				}
			}
		};
		
		receiveThread.start();
	}
}
