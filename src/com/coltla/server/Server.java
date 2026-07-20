package com.coltla.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
				char messageType = received.charAt(0);
				String messageContent = received.substring(1);
				
				switch(messageType) {
					case 'c': {
						int clientId = UniqueIdentifier.getIdentifier();
						clients.add(new ClientInformation(messageContent, packet.getAddress(), packet.getPort(), clientId));
						System.out.println("Name: " + messageContent + "\nID: " + clientId);
					}
					case 'a': {
						sendToAll(received);
					}
				}
			}

			private void sendToAll(String messageContent) {
				for (ClientInformation client : clients) {
					 send(messageContent.getBytes(), client.inetAddress, client.port);
				}
			}
			
			private void send(final byte[] data, final InetAddress inetAddress, final int port) {
				sendThread = new Thread("Send Thread") {
					public void run() {
						DatagramPacket packet = new DatagramPacket(data, data.length, inetAddress, port);
						try {
							socket.send(packet);
						} catch (IOException ioex) {
							ioex.printStackTrace();
						}						
					}
				};
				
				sendThread.start();
			}
 		};
		
		receiveThread.start();
	}	
}
