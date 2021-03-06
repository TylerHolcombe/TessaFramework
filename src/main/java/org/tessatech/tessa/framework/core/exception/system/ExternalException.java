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

public class ExternalException extends RuntimeException
{
	private String externalExceptionCode;
	private String externalExceptionMessage;

	public ExternalException(String externalExceptionCode, String externalExceptionMessage, String debugMessage, Throwable cause)
	{
		super(debugMessage, cause);
		this.externalExceptionCode = externalExceptionCode;
		this.externalExceptionMessage = externalExceptionMessage;
	}

	public ExternalException(String externalExceptionCode, String externalExceptionMessage, String debugMessage)
	{
		this(externalExceptionCode, externalExceptionMessage, debugMessage, null);
	}

	public ExternalException(String externalExceptionCode, String debugMessage, Throwable cause)
	{
		this(externalExceptionCode, null, debugMessage, cause);
	}

	public ExternalException(String externalExceptionCode, String externalExceptionMessage)
	{
		this(externalExceptionCode, externalExceptionMessage, externalExceptionMessage, null);
	}

	public ExternalException(String debugMessage, Throwable cause)
	{
		this(null, null, debugMessage, cause);
	}

	public ExternalException(String debugMessage)
	{
		this(null, null, debugMessage, null);
	}

	public String getExternalExceptionCode()
	{
		return externalExceptionCode;
	}

	public String getExternalExceptionMessage()
	{
		return externalExceptionMessage;
	}
}
