package com.codepex.socketjava;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;

public class Client extends Socket {
	
	public Client(String hostname, int port) throws UnknownHostException, IOException {
		super(hostname, port);
		this.csocket = new java.net.Socket(hostname, port);
		this.out = new ObjectOutputStream(csocket.getOutputStream());
		this.in = new ObjectInputStream(csocket.getInputStream());
		this.t.start();
	}
	
	public Client(java.net.Socket socket) throws UnknownHostException, IOException {
		super(socket.getInetAddress().getHostName(), socket.getPort());
		this.csocket = socket;
		this.out = new ObjectOutputStream(csocket.getOutputStream());
		this.in = new ObjectInputStream(csocket.getInputStream());
		this.t.start();
	}
	
	@Override
	public void send(String name, String message) {
		try {
			out.writeObject("[%]"+name+"[%][%]"+message+"[%]");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
