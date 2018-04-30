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

package org.tessatech.tessa.framework.exception;

import org.springframework.http.HttpStatus;

public abstract class TessaException extends RuntimeException
{
	public HttpStatus httpCode;

	public long exceptionCode;
	public String exceptionMessage;

	public String externalExceptionCode;
	public String externalExceptionMessage;

	public TessaException(HttpStatus httpCode, long exceptionCode, String exceptionMessage, String externalExceptionCode, String externalExceptionMessage, String debugMessage, Throwable cause)
	{
		super(debugMessage, cause);
		this.httpCode = httpCode;
		this.exceptionCode = exceptionCode;
		this.exceptionMessage = exceptionMessage;
		this.externalExceptionCode = externalExceptionCode;
		this.externalExceptionMessage = externalExceptionMessage;
	}

	public TessaException(HttpStatus httpCode, long exceptionCode, String exceptionMessage, String debugMessage, Throwable cause)
	{
		super(debugMessage, cause);
		this.httpCode = httpCode;
		this.exceptionCode = exceptionCode;
		this.exceptionMessage = exceptionMessage;
	}
}
