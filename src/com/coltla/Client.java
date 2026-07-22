package com.coltla;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {

	private static final long serialVersionUID = 1L;

	private ClientWindow clientWindow;

	private String name;
	private String address;
	private int port;

	private DatagramSocket socket;
	private InetAddress inetAddress;

	private Thread sendThread;

	private final static String SERVER = "server";

	public enum MessageType {
		CONNECT,
		DISCONNECT,
		BROADCAST,
		PRIVATE
	}

	public Client(String name, String address, int port) {
		this.name = name;
		this.address = address;
		this.port = port;

		clientWindow = new ClientWindow(this, this.name, this.address, this.port);

		boolean connect = openConnection(this.address);
		if (!connect) {
			System.err.println("Connection failed!");
			clientWindow.console("Connection failed!");
			return;
		}

		send(MessageType.CONNECT, SERVER, this.name, "");
	}

	private boolean openConnection(String address) {
		try {
			socket = new DatagramSocket();
			inetAddress = InetAddress.getByName(address);
		} catch (UnknownHostException uhex) {
			uhex.printStackTrace();
			return false;
		} catch (SocketException soex) {
			soex.printStackTrace();
			return false;
		}

		return true;
	}

	private void send(MessageType messageType, String to, String from, String message) {
		switch (messageType) {
			case CONNECT: {
				handleConnect(to, from, message);
				break;
			}
			case DISCONNECT: {
				handleDisconnect();
				break;
			}
//			case BROADCAST: handleBroadcast(to, from, message);
//			case PRIVATE: handlePrivate(to, from, message);
		}

		if (message.isEmpty())
			return;

		message = name + ": " + message;
		clientWindow.console(message);
		clientWindow.setTxtMessage("");
	}

	private void handleConnect(String to, String from, String message) {
		message = "/c/" + to + "/" + from + "/message/";
		sendToServer(message.getBytes());
	}

	private void handleDisconnect() {
		String message = "/d/" + SERVER + "/" + this.name + "//";
		sendToServer(message.getBytes());
	}

	public void handleSend(String message) {
		send(MessageType.BROADCAST, SERVER, this.name, message);
	}

	private String receiveFromServer() {
		byte[] data = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(data, data.length);

		try {
			socket.receive(receivePacket);
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}

		String message = new String(receivePacket.getData());

		return message;
	}

	private void sendToServer(final byte[] data) {
		sendThread = new Thread("Send Thread") {
			public void run() {
				DatagramPacket sendPacket = new DatagramPacket(data, data.length, inetAddress, port);
				try {
					socket.send(sendPacket);
				} catch (IOException ioex) {
					ioex.printStackTrace();
				}
			}
		};

		sendThread.start();
	}
}
