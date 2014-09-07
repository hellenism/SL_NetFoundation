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

import com.sl.net.interfaces.IHttpListener;

/**
 * NetFoundation�Ļص�������,������ʲ�����лص���ͨ�������������
 * 
 * @author Stephen
 *
 */
public abstract class HttpResponseHandler implements IHttpListener {
	// HTTP ����ɹ�
	@Override
	public void httpSuccess(String responseString, HttpURLConnection connection) {
	}

	// HTTP ����ʧ��
	@Override
	public void httpFail(HttpURLConnection connection, Throwable error) {
	}
}
