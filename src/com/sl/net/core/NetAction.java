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
 * NetFoundation的最上层，它是网络访问层与外界的直接交互对象，请求将通过此类的对象发起
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
	 * @descrption 设置是否为调试模式
	 * 
	 * @note 仅影响日志输出
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
	 * 初始化
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
	 * 执行网络请求
	 * 
	 * @param urlString
	 * @param queryParams
	 */
	public void execute(final String urlString, final HashMap<String, String> queryParams) {
		NetParams params = new NetParams(urlString, queryParams);
		params.setConnectTimeout(mConnectTimeout);
		params.setReadTimeout(mReadTimeout);
		params.setHttpMethod(mHttpMethod);

		// 以后想扩展，就直接扩展Sender即可
		mSender = new NetSender(mContext, params, mHttpListener);
		mSender.send();
	}

	/**
	 * 执行网络请求
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

		// 以后想扩展，就直接扩展Sender即可
		mSender = new NetSender(mContext, params, mHttpListener);
		mSender.send();
	}
}
