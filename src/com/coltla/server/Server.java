package com.coltla.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
	private List<ClientInformation> clients = new ArrayList<ClientInformation>();
	
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
					
					process(packet);
					clients.add(new ClientInformation("Acke", packet.getAddress(), packet.getPort(), 50));
					System.out.println(clients.get(0).inetAddress.toString() + ":" + clients.get(0).port);
				}
			}

			private void process(DatagramPacket packet) {
				String received = new String(packet.getData());
				
				switch(received.charAt(0)) {
					case 'c': {
						clients.add(new ClientInformation(received.substring(1), packet.getAddress(), packet.getPort(), 50));
						System.out.println(received.substring(1));
					}
				}
			}
		};
		
		receiveThread.start();
	}	
}
