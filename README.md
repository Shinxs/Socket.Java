# Socket.Java
A socket.io created within Java

We are trying to sort of recreate Socket.IO within Java for game development or anything that needs to use sockets.
This provides an easy way to start working with networking without the need of knowing anything about sockets.

## Table of Contents
* [Tutorial](#tutorial)
* [Setting up the server](#setting-up-the-server) 
* [Setting up the client](#setting-up-the-client)
* [Communicating](#communicating)

## Tutorial

Socket.Java is quite easy to use. For our example we will be using a simple scenario with one server and one client.

#### Setting up the server

First of all we're going to set up the server:
```java
Server server = new Server("0.0.0.0", 5555);
```
This will create a new server object.

#### Setting up the client

Now let's create the client we are going to use:
```java
Client client = new Client("127.0.0.1", 5555);
```

To open our client socket we will call the .open(); function on the client:
```java
client.open();
```
This will start the client and will start listening for messages.

So now that we have the server and the client we can communicate between those two.

#### Communicating

The communicating is pretty straight forward, for the client and the server you each setup events that get ran whenever that events is being called from the other sender.
For example, we are going to create an event called 'hello':
```java
server.on("hello", new NetworkCallback() {
	@Override
	public void receive(String body) {
		System.out.println(body);
	}
});
```
This will listen for any incoming events called hello and everything you put within the receive method will get called.

Let's try calling this event from our client for that we will use the .send(String eventName, String message, boolean nobase64) method:
```java
client.send("hello", "world!", true);
```

We have a boolean called nobase64 which will toggle a function that will encrypt the messages and eventnames in base64. This will make sure the code doesn't crash even if you provide some wierd characters but it adds a little bit of a slowdown. That why you can disable it for some speed. 

If we now run this we will see "world!" displayed in the console.
We can also call .on(String eventName, NetworkCallback networkCallback) on the client and the .send(String eventName, String message, boolean nobase64) on the server in order to communicate back and forwards.
