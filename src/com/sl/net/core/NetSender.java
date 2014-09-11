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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.sl.net.constants.Constants;
import com.sl.net.exception.ExceptionConstants;
import com.sl.net.exception.NetException;
import com.sl.net.exception.SSLVerifyException;
import com.sl.net.interfaces.IHttpListener;
import com.sl.net.utils.LogUtilsNetFoundation;
import com.sl.net.utils.Utils;

/**
 * @ClassName {@link NetSender}
 * @Description 
 * @author Stephen Lee
 * @date 2014-06-17
 */
public class NetSender extends AbsSender {

	private static final String	METHODS_POST	= "POST";
	private static final String	METHODS_GET		= "GET";
	private static final String	CHARSETNAME		= "UTF-8";
	private static final String	EMPTY_STR		= "";

	private NetParams			mNetParams;
	private Context				mContext;
	private Handler				mHandler;
	private HttpURLConnection	mConnection;
	private IHttpListener		mHttpListener;
	private Thread				mWorkThread;

	public NetSender(Context context, NetParams netParams, IHttpListener httpListener) {
		mContext = context;
		mHandler = new Handler(mContext.getMainLooper());
		mNetParams = netParams;
		mHttpListener = httpListener;

		mWorkThread = new Thread(new Runnable() {
			@Override
			public void run() {
				send(mNetParams);
			}
		});
	}

	public void send() {
		mWorkThread.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void cancel() {
		try {
			mWorkThread.stop();

			if (null != mConnection) {
				mConnection.disconnect();
			}

			mHttpListener = null;
		} catch (Exception e) {
			// ignore
		}
	}

	/**
	 * 
	 * @param params
	 */
	private void send(NetParams params) {
		NetException netException = null;
		String responseString = EMPTY_STR;

		try {
			InputStream responseInputStream = getInputStreamFactory(params);
			responseString = Utils.InputStream2String_ByteArray(responseInputStream, CHARSETNAME);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			netException = new NetException();
		} catch (SSLVerifyException e) {
			e.printStackTrace();
			netException = new NetException();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			netException = new NetException(e);
			netException.setmExceptionCode(ExceptionConstants.TIMEOUT);
		} catch (SecurityException e) {
			e.printStackTrace();
			netException = new NetException(e);
			netException.setmExceptionCode(ExceptionConstants.PERMISSION);
		} catch (IOException e) {
			e.printStackTrace();

			if (!Utils.checkNetWorkAvailable(mContext)) {
				netException = new NetException();
				netException.setmExceptionCode(ExceptionConstants.NETWORKDISABLE);
			} else {
				netException = new NetException(e);
				netException.setmExceptionCode(ExceptionConstants.IOEXCEPTION);
			}
		} finally {
			if (null != netException) {
				onHttpFail(params.getConnection(), netException);
				showLog(params, "onHttpFail:" + netException.getMessage());
			} else {
				onHttpSuccess(responseString, params.getConnection());
				showLog(params, "onHttpSuccess:" + responseString);
			}
		}
	}

	/**
	 * 
	 * @param params
	 * @param other
	 */
	private void showLog(NetParams params, String other) {
		if (NetAction.getDebugModel()) {
			try {
				LogUtilsNetFoundation.Log("Http Method:" + params.getHttpMethod());
				LogUtilsNetFoundation.Log("connection.getResponseCode():" + params.getConnection().getResponseCode());
				LogUtilsNetFoundation.Log("connection.getResponseMessage()"
						+ params.getConnection().getResponseMessage());
				LogUtilsNetFoundation.Log(other);
			} catch (Exception e) {
				// ignore
			}
		}
	}

	/**
	 * Get response data from connection , this is a factory thought NetParams
	 * object to decided HTTP Method
	 * 
	 * @param urlString
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SSLVerifyException
	 */
	public InputStream getInputStreamFactory(NetParams params) throws MalformedURLException, IOException,
			SSLVerifyException {
		InputStream inputStream = null;

		if (METHODS_POST.equalsIgnoreCase(params.getHttpMethod())) {
			inputStream = getInputSreamByPost(params);
		} else if (METHODS_GET.equalsIgnoreCase(params.getHttpMethod())) {
			inputStream = getInputSreamByGet(params);
		}

		return inputStream;
	}

	/**
	 * Get inputstream by [GET] method
	 * 
	 * @param params
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream getInputSreamByGet(NetParams params) throws MalformedURLException, IOException,
			SecurityException {
		String urlString = params.getUrlString();
		HashMap<String, String> queryParams = params.getRequestParams();
		URL url = createGetMethodURL(urlString, queryParams);
		mConnection = (HttpURLConnection) url.openConnection();
		mConnection.setConnectTimeout(params.getConnectTimeout());
		mConnection.setReadTimeout(params.getReadTimeout());
		mConnection.setRequestMethod(params.getHttpMethod());
		mConnection.setUseCaches(false);
		mConnection.setDoInput(true);
		mConnection.setDoOutput(true);
		mConnection.connect();
		params.setConnection(mConnection);
		return mConnection.getInputStream();
	}

	/**
	 * Get inputstream by [POST] method
	 * 
	 * @param params
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SSLVerifyException
	 */
	public InputStream getInputSreamByPost(NetParams params) throws MalformedURLException, IOException,
			SSLVerifyException, SecurityException {
		String urlString = params.getUrlString();

		HashMap<String, String> queryParams = params.getRequestParams();
		HashMap<String, String> headers = params.getHeaders();

		URL url = new URL(urlString);
		String queryString = createQueryString(queryParams);
		mConnection = (HttpURLConnection) url.openConnection();

		if (null != headers && headers.size() != 0) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				String key = entry.getKey() != null ? entry.getKey() : Constants.EMPTY_STR;
				String value = entry.getValue() != null ? entry.getValue() : Constants.EMPTY_STR;
				mConnection.setRequestProperty(key, value);
			}
		}

		mConnection.setConnectTimeout(params.getConnectTimeout());
		mConnection.setReadTimeout(params.getReadTimeout());
		mConnection.setRequestMethod(params.getHttpMethod());
		mConnection.setUseCaches(false);
		mConnection.setDoInput(true);
		mConnection.setDoOutput(true);
		mConnection.connect();
		OutputStream outputStream = null;

		try {
			outputStream = mConnection.getOutputStream();
			outputStream.write(queryString.getBytes());
		} catch (IOException e) {
			String s = e.getMessage();
			Pattern p = Pattern.compile("^Hostname '.*' was not verified$");
			Matcher m = p.matcher(s);
			if (m.find()) {
				throw new SSLVerifyException(s);
			}
			throw new IOException();
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
		params.setConnection(mConnection);
		return mConnection.getInputStream();
	}

	// ------ private methods ------
	/**
	 * 
	 * @param urlString
	 * @param queryParams
	 * @return
	 * @throws MalformedURLException
	 */
	private URL createGetMethodURL(String urlString, HashMap<String, String> queryParams) throws MalformedURLException {
		URL url = null;
		String queryString = createQueryString(queryParams);

		if (urlString.contains("?")) {
			if (urlString.startsWith("?")) {
				throw new MalformedURLException("urlString is illegal");
			}

			String[] tempStrings = urlString.split("?");

			if (tempStrings.length != 2) {
				throw new MalformedURLException("urlString is illegal");
			}

			urlString += queryString;
		} else {
			urlString = urlString + "?" + queryString;
		}

		url = new URL(urlString);
		return url;
	}

	/**
	 * 
	 * @param queryParams
	 * @return
	 */
	public String createQueryString(HashMap<String, String> queryParams) {
		String resultString = Constants.EMPTY_STR;

		if (null == queryParams || queryParams.size() == 0) {
			return resultString;
		}

		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			String value = (String) entry.getValue();
			if (TextUtils.isEmpty(value)) {
				value = Constants.EMPTY_STR;
			}
			sb.append((String) entry.getKey() + "=" + value + "&");
		}

		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}

		return resultString;
	}

	@Override
	public InputStream getResponseInputStream(String urlString, HashMap<String, String> queryParams) {
		return null;
	}

	@Override
	public String getResponseString(String urlString, HashMap<String, String> queryParams) {
		return null;
	}

	/**
	 * 
	 * @param responseString
	 * @param connection
	 */
	private void onHttpSuccess(final String responseString, final HttpURLConnection connection) {
		if (null != mHttpListener) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					mHttpListener.httpSuccess(responseString, connection);
				}
			});
		}
	}

	/**
	 * 
	 * @param connection
	 * @param error
	 */
	private void onHttpFail(final HttpURLConnection connection, final Throwable error) {
		if (null != mHttpListener) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					mHttpListener.httpFail(connection, error);
				}
			});
		}
	}
}
