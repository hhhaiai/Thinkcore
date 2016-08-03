/*
 *  Copyright 2010 Kevin Gaudin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.thinkcore.crash.collector;

import java.lang.reflect.Field;

import com.thinkcore.crash.TCrash;
import com.thinkcore.utils.log.TLog;

import android.content.Context;
import android.provider.Settings;

/**
 * 设置信息收集
 */
final class SettingsCollector {

	/**
	 * Collect data from {@link Settings.System}. This
	 * collector uses reflection to be sure to always get the most accurate data
	 * whatever Android API level it runs on.
	 * 
	 * @param ctx
	 *            Application context.
	 * @return A human readable String containing one key=value pair per line.
	 */
	public static String collectSystemSettings(Context ctx) {
		final StringBuilder result = new StringBuilder();
		final Field[] keys = Settings.System.class.getFields();
		for (final Field key : keys) {
			// Avoid retrieving deprecated fields... it is useless, has an
			// impact on perfs, and the system writes many warnings in the
			// logcat.
			if (!key.isAnnotationPresent(Deprecated.class)
					&& key.getType() == String.class) {
				try {
					final Object value = Settings.System.getString(
							ctx.getContentResolver(), (String) key.get(null));
					if (value != null) {
						result.append(key.getName()).append("=").append(value)
								.append("\n");
					}
				} catch (IllegalArgumentException e) {
					TLog.w(TCrash.TAG, "Error : " + e.getMessage());
				} catch (IllegalAccessException e) {
					TLog.w(TCrash.TAG, "Error : " + e.getMessage());
				}
			}
		}

		return result.toString();
	}

	/**
	 * Collect data from {@link Settings.Secure}. This
	 * collector uses reflection to be sure to always get the most accurate data
	 * whatever Android API level it runs on.
	 * 
	 * @param ctx
	 *            Application context.
	 * @return A human readable String containing one key=value pair per line.
	 */
	public static String collectSecureSettings(Context ctx) {
		final StringBuilder result = new StringBuilder();
		final Field[] keys = Settings.Secure.class.getFields();
		for (final Field key : keys) {
			if (!key.isAnnotationPresent(Deprecated.class)
					&& key.getType() == String.class && isAuthorized(key)) {
				try {
					final Object value = Settings.Secure.getString(
							ctx.getContentResolver(), (String) key.get(null));
					if (value != null) {
						result.append(key.getName()).append("=").append(value)
								.append("\n");
					}
				} catch (IllegalArgumentException e) {
					TLog.w(TCrash.TAG, "Error : " + e.getMessage());
				} catch (IllegalAccessException e) {
					TLog.w(TCrash.TAG, "Error : " + e.getMessage());
				}
			}
		}

		return result.toString();
	}

	private static boolean isAuthorized(Field key) {
		if (key == null || key.getName().startsWith("WIFI_AP")) {
			return false;
		}
		return true;
	}

}
