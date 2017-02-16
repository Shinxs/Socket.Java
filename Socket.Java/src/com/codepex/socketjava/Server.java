package com.codepex.socketjava;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Base64;

public class Server extends Socket {
	
	public Server(String hostname, int port) throws UnknownHostException, IOException {
		super(hostname, port);
		this.ssocket = new java.net.ServerSocket(port);
		this.ssocket.setSoTimeout(100);
		this.thread.start();
	}
	
	@Override
	public void send(String name, String body, boolean nobase64) {
		String addon = "[p]";
		if(!nobase64) {
			name = Base64.getEncoder().encodeToString(name.getBytes());
			body = Base64.getEncoder().encodeToString(body.getBytes());
			addon = "[b64]";
		}
		
		for(Client c : clients) {
			try {
				c.out.writeObject("[%]"+name+"[%][%]"+body+"[%]"+addon);
				c.out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public synchronized void close() throws IOException {
		ssocket.close();
	}
	
	@Override
	public void run() {
		while(!ssocket.isClosed()) {
			java.net.Socket socket;
			try {
				socket = ssocket.accept();
				clients.add(new Client(socket));
			} catch (IOException e) {
			}
			
			for(Client c : clients) {
				checkClient(c);
			}
		}
		
		try {
			thread.join();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
