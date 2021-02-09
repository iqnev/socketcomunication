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
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ivelin Yanev <bgfortran@gmail.com>
 * @since 2021-02-07
 *
 */
public class ThreadPoolServer {

	private static final Logger log = LoggerFactory.getLogger(ThreadPoolServer.class);
	private final int port;
	private volatile boolean keepRunning = true;
	private ExecutorService threadPool;
	private ServerSocket listenSocket;
	private DataGenerator dGenerator;

	public ThreadPoolServer(final int port) {
		this.port = port;
		this.threadPool = Executors.newFixedThreadPool(10, new ClientHandlerThreadFactory());

		try {
			this.listenSocket = new ServerSocket(port);
		} catch (IOException ioException) {
			log.error("An exception occurred while creating the listen socket: {}", ioException);
		}
		
		this.dGenerator = new DataGenerator();
		Thread thread = new Thread(this.dGenerator);
		thread.setDaemon(true);
		thread.setName("DATA_GENERATOR_THREAD");
		thread.start();
	}

	public void start() {
		System.out.println("------------- Starting Server Up -------------");
	/*	try {
			this.listenSocket.setSoTimeout(1000);
		} catch (SocketException sException) {
			log.error("Unable to set acceptor timeout value.  The server may not shutdown gracefully: {}", sException);
		}
*/

		
		while (this.keepRunning) {
			try {
				final Socket clientSocket = this.listenSocket.accept();
				System.out.println("------------- Client is connected -------------");
				final ClientHandler handler = new ClientHandler(clientSocket);
				dGenerator.addClient(handler);
				threadPool.execute(handler);
			} catch (IOException e) {
				log.error("Exception occurred while handling client request:", e);
			}
		}

		try {
			this.listenSocket.close();
		} catch (IOException e) {

		}

		log.info("Stopped accepting incoming connections.");
	}

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
