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
package com.iqnev.socket.server.core.imp;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iqnev.socket.server.core.Operation;
import com.iqnev.socket.server.util.ServerUtilities;

/**
 * A runnable class that performs the basic work of this server. It will send
 * messages to the clients.
 * 
 * @author Ivelin Yanev <bgfortran@gmail.com>
 * @since 2021-02-07
 *
 */
public class ClientHandler implements Runnable, Operation {

	private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

	private static final int THREAD_SLEEP = 4000;

	/** The socket connected to the client. */
	private final Socket clientSock;
	/** An queue to keep the list of the messages */
	private final BlockingQueue<String> queue;
	/** to check if server is running */
	private boolean keepRunning = true;

	/**
	 * Creates a new ClientHandler thread for the socket provided.
	 * 
	 * @param clientSocket the given socket by type {@link Socket}.
	 */
	public ClientHandler(final Socket clientSocket) {
		this.clientSock = clientSocket;
		this.queue = new LinkedBlockingQueue<>();
	}

	/** The run method is invoked by the ExecutorService */
	@Override
	public void run() {
		try (OutputStreamWriter userOutput = new OutputStreamWriter(this.clientSock.getOutputStream(),
				StandardCharsets.UTF_8)) {
			while (keepRunning) {
				userOutput.write(queue.take());
				userOutput.flush();
				ServerUtilities.threadSleep(THREAD_SLEEP);
			}
		} catch (IOException | InterruptedException e) {
			log.error("An exception occurred while operating with the socket", e);
		}
	}

	/** stops the thread */
	public void shutdown() {
		keepRunning = false;
	}

	@Override
	public void addPackage(final String msg) {
		log.info("Add a new message: {}", msg);
		queue.add(msg);

	}
}
