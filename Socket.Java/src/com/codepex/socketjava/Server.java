package com.codepex.socketjava;

import java.io.IOException;
import java.net.UnknownHostException;

public class Server extends Socket {
	
	public Server(String hostname, int port) throws UnknownHostException, IOException {
		super(hostname, port);
		this.ssocket = new java.net.ServerSocket(port);
		this.ssocket.setSoTimeout(100);
		this.t.start();
	}
	
	@Override
	public void send(String name, String message) {
		for(Client c : clients) {
			try {
				c.out.writeObject("[%]"+name+"[%][%]"+message+"[%]");
				c.out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
			t.join();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
