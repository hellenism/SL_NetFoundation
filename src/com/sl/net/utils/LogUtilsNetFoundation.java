/*
 *  Copyright 2014 Stephen Lee
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.sl.net.utils;

import com.sl.net.core.NetAction;

import android.util.Log;

/**
 * NetFoundation Log Helper
 * 
 * @author Stephen
 */
public class LogUtilsNetFoundation {
	public static final boolean IS_SHOW_LOG = NetAction.getDebugModel();
	private static final String TAG = "SL_NetFoundation";

	public static void Log(String tag, String content) {
		if (IS_SHOW_LOG) {
			Log.d(tag, content);
		}
	}

	public static void Log(String content) {
		if (IS_SHOW_LOG) {
			Log.d(TAG, content);
		}
	}
}
