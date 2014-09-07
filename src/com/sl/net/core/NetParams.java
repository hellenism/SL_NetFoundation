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

import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * 请求相关信息的封装
 * 
 * 包括:
 * 1.HTTP Methods
 * 2.Header信息
 * 3.请求参数
 * 
 * @author Stephen
 *
 */
public class NetParams {
	private HashMap<String, String> mRequestParams;
	private HashMap<String, String> mHeaders;
	private HttpURLConnection mConnection;
	private String mHttpMethod;
	private String mUrlString;
	
	private int mConnectTimeout = 15000;
	private int mReadTimeout = 15000;
	
	public NetParams(String urlString, HashMap<String, String> requestParams) {
		mUrlString = urlString;
		mRequestParams = requestParams;
	}

	public NetParams(String urlString, HashMap<String, String> requestParams, HashMap<String, String> headers) {
		this(urlString, requestParams);
		mHeaders = headers;
	}
	
	public int getConnectTimeout() {
		return mConnectTimeout;
	}

	public void setConnectTimeout(int timeout) {
		this.mConnectTimeout = timeout;
	}

	public int getReadTimeout() {
		return mReadTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.mReadTimeout = readTimeout;
	}
	
	public HttpURLConnection getConnection() {
		return mConnection;
	}
	
	public void setConnection(HttpURLConnection mConnection) {
		this.mConnection = mConnection;
	}

	public String getHttpMethod() {
		return mHttpMethod;
	}

	public void setHttpMethod(String mHttpMethod) {
		this.mHttpMethod = mHttpMethod;
	}
	
	public String getUrlString() {
		return mUrlString;
	}

	public void setUrlString(String mUrlString) {
		this.mUrlString = mUrlString;
	}

	public HashMap<String, String> getRequestParams() {
		return mRequestParams;
	}

	public void setRequestParams(HashMap<String, String> mRequestParams) {
		this.mRequestParams = mRequestParams;
	}

	public HashMap<String, String> getHeaders() {
		return mHeaders;
	}

	public void setHeaders(HashMap<String, String> mReaders) {
		this.mHeaders = mReaders;
	}
}
