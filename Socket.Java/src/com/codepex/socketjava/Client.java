package com.codepex.socketjava;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.util.Base64;

public class Client extends Socket {
	
	public Client(String hostname, int port) throws UnknownHostException, IOException {
		this(new java.net.Socket(hostname, port));
	}
	
	public Client(java.net.Socket socket) throws UnknownHostException, IOException {
		super(socket.getInetAddress().getHostName(), socket.getPort());
		this.csocket = socket;
		this.out = new ObjectOutputStream(csocket.getOutputStream());
		this.in = new ObjectInputStream(csocket.getInputStream());
		
	}
	
	public synchronized void open() {
		thread.start();
	}
	
	@Override
	public void send(String name, String body, boolean nobase64) {
		if(csocket.isConnected() && thread.isAlive()) {
			String addon = "[p]";
			if(!nobase64) {
				name = Base64.getEncoder().encodeToString(name.getBytes());
				body = Base64.getEncoder().encodeToString(body.getBytes());
				addon = "[b64]";
			}
			
			try {
				out.writeObject("[%]"+name+"[%][%]"+body+"[%]"+addon);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public synchronized void close() throws InterruptedException, IOException {
		csocket.close();
		thread.join();
	}
}
