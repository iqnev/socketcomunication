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
package com.iqnev.socket.server.app;

import com.iqnev.socket.server.core.ThreadPoolServer;

/**
 * @author Ivelin Yanev <bgfortran@gmail.com>
 * @since 2021-02-07
 *
 */
public class App {
	private App() {
	}

	private static class LazyHolder {
		static final App INSTANCE = new App();
	}

	public static App getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	
	public void launch(int port) {
				 
		 new ThreadPoolServer(port).start();
	}
}
