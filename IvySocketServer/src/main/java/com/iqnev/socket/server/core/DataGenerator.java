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

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ivelin Yanev <bgfortran@gmail.com>
 * @since 2021-02-09
 *
 */
public class DataGenerator implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(DataGenerator.class);

	private volatile List<Operation> clients;
	private static int COUNTER = 0;

	public DataGenerator() {
		this.clients = new ArrayList<>();
	}

	public synchronized void addClient(final Operation client) {
		log.info("Added a new client");
		clients.add(client);
	}

	@Override
	public void run() {

		while (true) {
			if (!clients.isEmpty()) {
				final String currentMsg = sendMsg().toJSONString();
				for (Operation operation : clients) {
					operation.addPackage(currentMsg);
				}
			} else {
				log.info("An queue with clients is emppty!");
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@SuppressWarnings("unchecked")
	private JSONObject sendMsg() {
		JSONObject json = new JSONObject();
		json.put("type", "CONNECT__" + COUNTER++);
		return json;
	}

}
