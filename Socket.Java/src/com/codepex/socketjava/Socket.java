package com.codepex.socketjava;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Socket implements NetworkSocket {
	
	protected java.net.Socket csocket;
	protected java.net.ServerSocket ssocket;
	protected List<Client> clients;
	protected ObjectOutputStream out;
	protected ObjectInputStream in;
	protected String hostname;
	protected int port;
	protected Thread thread;
	
	private HashMap<String, NetworkCallback> events = new HashMap<String, NetworkCallback>();
	
	public Socket(String hostname, int port) throws UnknownHostException, IOException {
		this.hostname = hostname;
		this.port = port;
		this.clients = new ArrayList<Client>();
		this.thread = new Thread(this);
	}
	
	public void on(String name, NetworkCallback callback) {
		events.put(name, callback);
	}
	
	public void send(String name, String body, boolean nobase64) throws Exception {
		throw new Exception("Not yet implemented");
	}

	public synchronized void close() throws InterruptedException, IOException {
		thread.join();
	}
	
	@Override
	public void run() {
		if(this instanceof Client) {
			while(csocket.isConnected()) {
				checkClient((Client) this);
			}
		}
	}
	
	protected void checkClient(Client c) {
		try {
			// Get the message from stream
			String message = (String) c.in.readObject();
			if(message != null && message.length() > 0) {
				// Get the event name and body from message which looks like: [%]eventName[%][%]body[%]
				Matcher m = Pattern.compile("(?<=\\[%\\])([^\\[%\\]]+)(?=\\[%\\])").matcher(message);
				ArrayList<String> values = new ArrayList<String>();
				while(m.find()) {
					values.add(m.group(1));
				}
				
				String name = values.get(0);
				String body = values.get(1);
				
				if(message.endsWith("[b64]")) {
					name = new String(Base64.getDecoder().decode(name.getBytes()));
					body = new String(Base64.getDecoder().decode(body.getBytes()));
				}
				
				if(events.containsKey(name)) {
					events.get(name).receive(body);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
