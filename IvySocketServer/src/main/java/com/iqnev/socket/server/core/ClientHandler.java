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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ivelin Yanev <bgfortran@gmail.com>
 * @since 2021-02-07
 *
 */
public class ClientHandler implements Runnable, Operation {

	private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);
	/**
	 * The socket connected to the client.
	 */
	private final Socket clientSock;
	private BlockingQueue<String> queue;

	public ClientHandler(Socket clientSocket) {
		this.clientSock = clientSocket;
		this.queue = new LinkedBlockingQueue<>();
	}

	@Override
	public void run() {
		try (OutputStreamWriter userOutput = new OutputStreamWriter(this.clientSock.getOutputStream(),
				StandardCharsets.UTF_8)) {
			while (true) {
				userOutput.write(queue.take());
				userOutput.flush();
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException | InterruptedException e) {

		}
	}

	@Override
	public void addPackage(String msg) {
		log.info("Add a new message: {}", msg);
		queue.add(msg);
		
	}
}
