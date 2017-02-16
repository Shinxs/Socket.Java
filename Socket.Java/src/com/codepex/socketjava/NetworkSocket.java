package com.codepex.socketjava;

import java.io.IOException;

public interface NetworkSocket extends Runnable {
	
	public void on(String name, NetworkCallback callback);
	public void send(String name, String body, boolean nobase64) throws Exception;
	public void close() throws InterruptedException, IOException;
	public void run();
}
