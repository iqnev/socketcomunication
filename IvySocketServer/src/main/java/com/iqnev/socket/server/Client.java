package com.iqnev.socket.server;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;

//The Client that can be run as a console
public class Client {

	// notification
	private String notif = " *** ";

	// for I/O
	private ObjectInputStream sInput; // to read from the socket
	private ObjectOutputStream sOutput; // to write on the socket
	private Socket socket; // socket object

	private String server; // server and username
	private int port; // port

	/*
	 * Constructor to set below things server: the server address port: the port
	 * number username: the username
	 */

	Client(String server, int port) {
		this.server = server;
		this.port = port;
	}

	/*
	 * To start the chat
	 */
	public boolean start() {
		// try to connect to the server
		try {
			socket = new Socket(server, port);
		}
		// exception handler if it failed
		catch (Exception ec) {
			display("Error connectiong to server:" + ec);
			return false;
		}

		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		display(msg);

		InputStream input;
		try {
			input = socket.getInputStream();
			DataInputStream reader = new DataInputStream(input);
			while (true) {
				String message = reader.readUTF();
		        System.out.println("The message sent from the socket was: " + message);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	/*
	 * To send a message to the console
	 */
	private void display(String msg) {

		System.out.println(msg);

	}

	/*
	 * When something goes wrong Close the Input/Output streams and disconnect
	 */
	private void disconnect() {
		try {
			if (sInput != null)
				sInput.close();
		} catch (Exception e) {
		}
		try {
			if (sOutput != null)
				sOutput.close();
		} catch (Exception e) {
		}
		try {
			if (socket != null)
				socket.close();
		} catch (Exception e) {
		}

	}

	/*
	 * To start the Client in console mode use one of the following command > java
	 * Client > java Client username > java Client username portNumber > java Client
	 * username portNumber serverAddress at the console prompt If the portNumber is
	 * not specified 1500 is used If the serverAddress is not specified "localHost"
	 * is used If the username is not specified "Anonymous" is used
	 */
	public static void main(String[] args) {
		// default values if not entered
		int portNumber = 1113;
		String serverAddress = "localhost";

		// create the Client object
		Client client = new Client(serverAddress, portNumber);
		// try to connect to the server and return if not connected
		if (!client.start()) {
			return;

		}

		// close resource

		// client completed its job. disconnect client.
		// client.disconnect();
	}

}
