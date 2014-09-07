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
 * @Description NetAction �����࣬ ���ڴ���Connection���ҷ���Http����
 * @author Stephen Lee
 * @date 2014-06-17
 */
public class NetSender extends AbsSender{

	private static final String METHODS_POST = "POST";
	private static final String METHODS_GET = "GET";
	private static final String CHARSETNAME = "UTF-8";
	private static final String EMPTY_STR = "";

	private NetParams mNetParams;
	private Context mContext;
	private Handler mHandler;
	private IHttpListener mHttpListener;

	public NetSender(Context context,NetParams netParams,IHttpListener httpListener)
	{
		mContext = context;
		mHandler = new Handler(mContext.getMainLooper());
		mNetParams = netParams;
		mHttpListener = httpListener;
	}

	@Override
	public void send() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				send(mNetParams);
			}
		}).start();
	}
	
	/**
	 * ��������
	 * 
	 * ������ص���Ϣ����װ����NetParams������
	 * 
	 * @param params
	 */
	private void send(NetParams params) {
		NetException netException = null;
		String responseString = EMPTY_STR;

		try {
			// ͨ��Sender���Ի�ȡ���������Ϣ
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

			// �ж��Ƿ�Ϊ���粻ͨ
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
			} else {
				onHttpSuccess(responseString, params.getConnection());
			}
		}
	}
	
	/**
	 * Get response data from connection , this is a factory thought NetParams
	 * object to decided HTTP Method
	 * 
	 * ����NetParams���󣬷������󣬻�ȡInputStream , ��һ���򵥵Ĺ�����ͨ��Params��Я����Methods����ѡ����õķ���
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
		} else if(METHODS_GET.equalsIgnoreCase(params.getHttpMethod())){
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
		String httpMethod = "GET";
		String urlString = params.getUrlString();
		HashMap<String, String> queryParams = params.getRequestParams();
		URL url = createGetMethodURL(urlString, queryParams);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(params.getConnectTimeout());
		connection.setReadTimeout(params.getReadTimeout());
		connection.setRequestMethod(params.getHttpMethod());
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.connect();
		LogUtilsNetFoundation.Log("Http Method:" + httpMethod);
		LogUtilsNetFoundation.Log("connection.getResponseCode():" + connection.getResponseCode());
		LogUtilsNetFoundation.Log("connection.getResponseMessage()" + connection.getResponseMessage());
		params.setConnection(connection);
		return connection.getInputStream();
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
		String httpMethod = "POST";
		String urlString = params.getUrlString();

		HashMap<String, String> queryParams = params.getRequestParams();
		HashMap<String, String> headers = params.getHeaders();

		URL url = new URL(urlString);
		String queryString = createQueryString(queryParams);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// ����HTTP Headers
		if (null != headers && headers.size() != 0) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				String key = entry.getKey() != null ? entry.getKey() : Constants.EMPTY_STR;
				String value = entry.getValue() != null ? entry.getValue() : Constants.EMPTY_STR;
				connection.setRequestProperty(key, value);
			}
		}

		connection.setConnectTimeout(params.getConnectTimeout());
		connection.setReadTimeout(params.getReadTimeout());
		connection.setRequestMethod(params.getHttpMethod());
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.connect();
		OutputStream outputStream = null;

		try {
			outputStream = connection.getOutputStream();
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
		LogUtilsNetFoundation.Log("Http Method:" + httpMethod);
		LogUtilsNetFoundation.Log("connection.getResponseCode():" + connection.getResponseCode());
		LogUtilsNetFoundation.Log("connection.getResponseMessage()" + connection.getResponseMessage());
		params.setConnection(connection);
		return connection.getInputStream();
	}

	// ------ private methods ------
	/**
	 * ����URL����
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
	 * ������ѯ�ַ���
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
	public InputStream getInputStream(String urlString, HashMap<String, String> queryParams) {
		return null;
	}

	@Override
	public String getResponseString(String urlString, HashMap<String, String> queryParams) {
		return null;
	}
	
	/**
	 * HTTP����ɹ�
	 * 
	 * @param responseString
	 * @param connection
	 */
	private void onHttpSuccess(final String responseString, final HttpURLConnection connection) {
		if (null != mHttpListener) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// �ص�������UI�߳�
					mHttpListener.httpSuccess(responseString, connection);
				}
			});
		}
	}

	/**
	 * HTTP����ʧ��
	 * 
	 * @param connection
	 * @param error
	 */
	private void onHttpFail(final HttpURLConnection connection, final Throwable error) {
		if (null != mHttpListener) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					// �ص�������UI�߳�
					mHttpListener.httpFail(connection, error);
				}
			});
		}
	}
}