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
 * NetFoundation�����ϲ㣬����������ʲ�������ֱ�ӽ�����������ͨ������Ķ�����
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
	 * @descrption �����Ƿ�Ϊ����ģʽ
	 * 
	 * @note ��Ӱ����־���
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
	 * ��ʼ��
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
	 * ִ����������
	 * 
	 * @param urlString
	 * @param queryParams
	 */
	public void execute(final String urlString, final HashMap<String, String> queryParams) {
		NetParams params = new NetParams(urlString, queryParams);
		params.setConnectTimeout(mConnectTimeout);
		params.setReadTimeout(mReadTimeout);
		params.setHttpMethod(mHttpMethod);

		// �Ժ�����չ����ֱ����չSender����
		mSender = new NetSender(mContext, params, mHttpListener);
		mSender.send();
	}

	/**
	 * ִ����������
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

		// �Ժ�����չ����ֱ����չSender����
		mSender = new NetSender(mContext, params, mHttpListener);
		mSender.send();
	}
}
