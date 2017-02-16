package com.codepex.socketjava;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Client extends Socket {
	
	public Client(String hostname, int port) throws UnknownHostException, IOException {
		super(hostname, port);
		this.csocket = new java.net.Socket(hostname, port);
		this.out = new ObjectOutputStream(csocket.getOutputStream());
		this.in = new ObjectInputStream(csocket.getInputStream());
		this.thread.start();
	}
	
	public Client(java.net.Socket socket) throws UnknownHostException, IOException {
		super(socket.getInetAddress().getHostName(), socket.getPort());
		this.csocket = socket;
		this.out = new ObjectOutputStream(csocket.getOutputStream());
		this.in = new ObjectInputStream(csocket.getInputStream());
		this.thread.start();
	}
	
	@Override
	public void send(String name, String body, boolean nobase64) {
		String addon = "[p]";
		if(!nobase64) {
			name = Base64.encode(name.getBytes());
			body = Base64.encode(body.getBytes());
			addon = "[b64]";
		}
		
		try {
			out.writeObject("[%]"+name+"[%][%]"+body+"[%]"+addon);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void close() throws InterruptedException, IOException {
		csocket.close();
		thread.join();
	}
}
