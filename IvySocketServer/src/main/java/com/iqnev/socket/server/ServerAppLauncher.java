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
package com.iqnev.socket.server;

import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iqnev.socket.server.app.App;

/**
 * The launcher class of the Socket server Application. Parsers the input
 * arguments and runs the application.
 * 
 * @author Ivelin Yanev <bgfortran@gmail.com>
 * @since 2021-02-07
 *
 */
public class ServerAppLauncher {

	private static final Logger log = LoggerFactory.getLogger(ServerAppLauncher.class);

	public static void main(String[] args) {

		if (args.length < 1) {
			log.error("Invalid Arguments");
			System.exit(1);
		}

		int port = -1;
		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException nException) {
			log.error("\"Invalid listen port value: {}", args[1]);
			System.exit(1);
		}

		// Make sure the port number is valid .
		if (port <= 0 || port > 65536) {
			log.error("Port value must be in (0, 65535].");
			System.exit(1);
		}

		start(port);

	}

	private static void start(final int port) {
		final App app = App.getInstance();
		/** change the name of the thread */
		Thread.currentThread().setName("Socket Server");

		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				log.error("Unrecoverable error, exiting: {}", e);
				return;
				//TODO: restart ThreadPollSerevr
			}

		});

		app.launch(port);
	}

}
