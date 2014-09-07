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

package com.sl.net.core;

import java.util.HashMap;

import android.content.Context;
import com.sl.net.interfaces.IHttpListener;

/**
 * @description be used to create http request
 * 
 * @author Stephen
 * 
 */
public class NetAction extends NetActionBase {
	private static String sHttpMethod = "POST";
	private static int sConnectTimeout = 15000;
	private static int sReadTimeout = 15000;
	private static boolean sIsDebug = false;

	private IHttpListener mHttpListener;
	private String mHttpMethod;
	private int mConnectTimeout;
	private int mReadTimeout;
	private Context mContext;
	private AbsSender mSender;

	public NetAction(Context context, IHttpListener httpListner) {
		mHttpListener = httpListner;
		mContext = context;
		mHttpMethod = sHttpMethod;
		mConnectTimeout = sConnectTimeout;
		mReadTimeout = sReadTimeout;
	}

	public String getHttpMethod() {
		return mHttpMethod;
	}

	public void setHttpMethod(String mHttpMethod) {
		this.mHttpMethod = mHttpMethod;
	}

	public int getConnectTimeout() {
		return mConnectTimeout;
	}

	public void setConnectTimeout(int mConnectTimeout) {
		this.mConnectTimeout = mConnectTimeout;
	}

	public int getReadTimeout() {
		return mReadTimeout;
	}

	public void setReadTimeout(int mReadTimeout) {
		this.mReadTimeout = mReadTimeout;
	}

	/**
	 * set debug model . if debug model is true , you can get log message on
	 * LogCat
	 * 
	 * @param isDebug
	 */
	public static void setDebugModel(boolean isDebug) {
		sIsDebug = isDebug;
	}

	public static boolean getDebugModel() {
		return sIsDebug;
	}

	/**
	 * init NetAction
	 * 
	 * @param httpMethod
	 * @param connectTimeout
	 * @param readTimeout
	 */
	public static void init(String httpMethod, int connectTimeout, int readTimeout) {
		sHttpMethod = httpMethod;
		sConnectTimeout = connectTimeout;
		sReadTimeout = readTimeout;
	}

	/**
	 * send http request
	 * 
	 * @param urlString
	 * @param queryParams
	 */
	public void execute(final String urlString, final HashMap<String, String> queryParams) {
		NetParams params = new NetParams(urlString, queryParams);
		params.setConnectTimeout(mConnectTimeout);
		params.setReadTimeout(mReadTimeout);
		params.setHttpMethod(mHttpMethod);

		// if want to change data access layer fundation , need create a new
		// Sender , it need to implements ISend
		mSender = new NetSender(mContext, params, mHttpListener);
		mSender.send();
	}

	/**
	 * send http request , you can add http headers
	 * 
	 * @param urlString
	 * @param queryParams
	 * @param headers
	 */
	public void execute(final String urlString, final HashMap<String, String> queryParams,
			final HashMap<String, String> headers) {
		NetParams params = new NetParams(urlString, queryParams, headers);
		params.setConnectTimeout(mConnectTimeout);
		params.setReadTimeout(mReadTimeout);
		params.setHttpMethod(mHttpMethod);

		// if want to change data access layer fundation , need create a new
		// Sender , it need to implements ISend
		mSender = new NetSender(mContext, params, mHttpListener);
		mSender.send();
	}
}
