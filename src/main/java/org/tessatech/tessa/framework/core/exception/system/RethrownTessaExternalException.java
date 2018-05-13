/*
 * Copyright (c) 2015-2018 TessaTech LLC.
 *
 * Licensed under the MIT License (the "License"); you may not use this file
 *  except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 *
 */

package org.tessatech.tessa.framework.core.exception.system;

public class RethrownTessaExternalException extends ExternalException
{
	private int httpStatus;
	private long exceptionCode;
	private String exceptionMessage;

	public RethrownTessaExternalException(int httpStatus, long exceptionCode, String exceptionMessage,
			String debugMessage, Throwable cause)
	{
		super(String.valueOf(exceptionCode), exceptionMessage, debugMessage, cause);
		this.httpStatus = httpStatus;
		this.exceptionCode = exceptionCode;
		this.exceptionMessage = exceptionMessage;
	}

	public RethrownTessaExternalException(int httpStatus, long exceptionCode, String exceptionMessage,
			String debugMessage)
	{
		this(httpStatus, exceptionCode, exceptionMessage, debugMessage, null);
	}


	public RethrownTessaExternalException(int httpStatus, long exceptionCode, String debugMessage, Throwable cause)
	{
		this(httpStatus, exceptionCode, null, debugMessage, cause);
	}

	public RethrownTessaExternalException(int httpStatus, long exceptionCode, String debugMessage)
	{
		this(httpStatus, exceptionCode, null, debugMessage, null);
	}

	public int getHttpStatus()
	{
		return httpStatus;
	}

	public String getExceptionMessage()
	{
		return exceptionMessage;
	}

	public long getExceptionCode()
	{
		return exceptionCode;
	}
}
