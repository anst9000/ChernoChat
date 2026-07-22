package com.coltla.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
	private List<ServerClient> clients = new ArrayList<ServerClient>();
	
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
    serverThread.start();
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
					clients.add(new ServerClient("Acke", packet.getAddress(), packet.getPort(), 50));
					System.out.println(clients.get(0).inetAddress.toString() + ":" + clients.get(0).port);
				}
			}

			private void process(DatagramPacket packet) {
				String received = new String(packet.getData());

				System.out.println("--> received: " + received);

				// Split with a limit of 4 to preserve slashes in the message
				String[] parts = received.split("/", 5);

				// parts[0] is empty because the string starts with "/"
				String type = parts[1];		// "c"
				String to = parts[2];		// "Name"
				String from = parts[3];		// "Name"
				String content = parts[4];	// "Message"

				// Optional: Clean up trailing slash from the message
				if (content.endsWith("/")) {
					content = content.substring(0, content.length() - 1);
				}

				switch(type) {
					case "c": {
						int clientId = UniqueIdentifier.getIdentifier();
						clients.add(new ServerClient(content, packet.getAddress(), packet.getPort(), clientId));
						System.out.println("Name: " + from + "\nID: " + clientId);
					}
					case "b": {
						broadcast(received);
					}
					case "p": {
//						private(received);
					}
				}
			}

			private void broadcast(String messageContent) {
				for (ServerClient client : clients) {
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
