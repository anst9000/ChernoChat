package com.coltla.server;

import java.net.InetAddress;

public class ClientInformation {
	public String name;
	public InetAddress inetAddress;
	public int port;
	private final int ID;
	public int attempt = 0;
	
	public ClientInformation(String name, InetAddress inetAddress, int port, final int ID) {
		this.name = name;
		this.inetAddress = inetAddress;
		this.port = port;
		this.ID = ID;
	}
	
	public int getID() {
		return ID;
	}
}
