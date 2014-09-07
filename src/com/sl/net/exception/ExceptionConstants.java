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

package com.sl.net.exception;

/**
 * @description ExceptionConstants
 * 
 * @author Stephen
 *
 */
public interface ExceptionConstants {
	
	/** network timeout */
	public static final int TIMEOUT = 0x0001;
	
	/** io exception */
	public static final int IOEXCEPTION = 0x0002;
	
	/** network disable */
	public static final int NETWORKDISABLE = 0x0003;
	
	/** permission don't registe on AndroidManifest.xml */
	public static final int PERMISSION = 0x0004;
}
