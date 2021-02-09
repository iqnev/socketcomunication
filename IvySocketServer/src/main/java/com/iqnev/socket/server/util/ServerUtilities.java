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
package com.iqnev.socket.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ivelin Yanev <bgfortran@gmail.com>
 * @since 2021-02-09 
 *
 */
public final class ServerUtilities {
	private static final Logger log = LoggerFactory.getLogger(ServerUtilities.class);
	
	private ServerUtilities() {
		throw new UnsupportedOperationException();
	}
	
	public static void threadSleep(final int sleep) {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			log.error("An InterruptedException exception occurred: {}", e);
		}
	}
}
