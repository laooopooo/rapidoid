package org.rapidoid.util;

/*
 * #%L
 * rapidoid-utils
 * %%
 * Copyright (C) 2014 - 2015 Nikolche Mihajlovski
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.log.Log;

@Authors("Nikolche Mihajlovski")
@Since("2.2.0")
public class Usage implements Constants {

	private static long lastAppUsedOn = U.time();

	private Usage() {}

	public static synchronized long getLastAppUsedOn() {
		return lastAppUsedOn;
	}

	public static synchronized void touchLastAppUsedOn() {
		lastAppUsedOn = U.time();
	}

	public static void terminateIfIdleFor(final int idleSeconds) {
		Log.warn("Will terminate if idle for " + idleSeconds + " seconds...");

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!Thread.interrupted()) {
					UTILS.sleep(500);
					long lastUsed = getLastAppUsedOn();
					long idleSec = (U.time() - lastUsed) / 1000;
					if (idleSec >= idleSeconds) {
						touchLastAppUsedOn();
						Log.warn("Terminating application", "idle", idleSec, "threshold", idleSeconds);
						System.exit(0);
					}
				}
			}
		}).start();
	}
}
