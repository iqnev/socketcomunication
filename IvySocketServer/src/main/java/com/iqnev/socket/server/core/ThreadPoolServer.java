/*
 * Copyright (C) 2021 Ivelin Yanev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iqnev.socket.server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iqnev.socket.server.core.imp.ClientHandler;
import com.iqnev.socket.server.messaging.DataGenerator;

/**
 * Multi-Client Server using Sockets.
 * 
 * <ol>
 * <li>Listens for connections on a specified port</li>
 * <li>Handles the new client in separate thread</li>
 * <li>Starts data generator</li>
 * 
 * @author Ivelin Yanev <bgfortran@gmail.com>
 * @since 2021-02-07
 *
 */
public class ThreadPoolServer {

	private static final Logger log = LoggerFactory.getLogger(ThreadPoolServer.class);
	private static final String DATA_GENERATOR_THREAD = "DATA_GENERATOR_THREAD";

	private final int port;
	private volatile boolean keepRunning = true;
	private ExecutorService threadPool;
	private ServerSocket listenSocket;
	private DataGenerator dGenerator;
	private List<ClientHandler> listClients;

	/**
	 * Constructor that receive the port to listen to for connection as parameter.
	 * 
	 * @param port the given port number by type <code>int</code>.
	 */
	public ThreadPoolServer(final int port) {
		this.port = port;
		this.threadPool = Executors.newFixedThreadPool(10, new ClientHandlerThreadFactory());
		this.listClients = new ArrayList<>();
		try {
			this.listenSocket = new ServerSocket(port);
		} catch (IOException ioException) {
			log.error("An exception occurred while creating the listen socket: {}", ioException);
		}

		this.dGenerator = new DataGenerator();
		Thread thread = new Thread(this.dGenerator);
		thread.setDaemon(true);
		thread.setName(DATA_GENERATOR_THREAD);
		thread.start();
	}

	public void start() {
		log.info("------------- Starting Server Up -------------");

		while (this.keepRunning) {
			try {
				final Socket clientSocket = this.listenSocket.accept();
				log.info("------------- Client is connected -------------");
				final ClientHandler handler = new ClientHandler(clientSocket);
				dGenerator.addClient(handler);
				listClients.add(handler);
				threadPool.execute(handler);
			} catch (IOException e) {
				log.error("Exception occurred while handling client request:", e);
			}
		}

		shutdown();

		log.info("Stopped accepting incoming connections.");
	}

	/** Shuts down this server */
	protected void shutdown() {
		log.info("Shutting down the server.");
		for (ClientHandler clientHandler : listClients) {
			clientHandler.shutdown();
		}
		try {
			this.listenSocket.close();
		} catch (IOException e) {
			log.error("Exception occurred when closing the socket:", e);
		}

		threadPool.shutdownNow();
	}

	// A class that creates new threads on demand
	private static final class ClientHandlerThreadFactory implements ThreadFactory {
		private int counter = 1;

		@Override
		public Thread newThread(final Runnable r) {
			final Thread thread = new Thread(r);
			thread.setDaemon(true);
			thread.setName("CLIENTHANDLER-" + counter++);
			return thread;
		}
	}
}
