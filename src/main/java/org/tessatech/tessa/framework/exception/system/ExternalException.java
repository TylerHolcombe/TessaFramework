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

package org.tessatech.tessa.framework.exception.system;

import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.TessaExceptionCodes;
import org.springframework.http.HttpStatus;

public class ExternalException extends TessaException
{

	public ExternalException(String externalExceptionCode, String externalExceptionMessage, String debugMessage, Throwable cause)
	{
		super(TessaExceptionCodes.EXTERNAL_ERROR_HTTP_STATUS, TessaExceptionCodes.EXTERNAL_ERROR_CODE, TessaExceptionCodes.EXTERNAL_ERROR_MESSAGE,
				externalExceptionCode, externalExceptionMessage, debugMessage, cause);
	}

	public ExternalException(String externalExceptionCode, String externalExceptionMessage, String debugMessage)
	{
		this(externalExceptionCode, externalExceptionMessage, debugMessage, null);
	}

	public ExternalException(String externalExceptionCode, String debugMessage, Throwable cause)
	{
		this(externalExceptionCode, null, debugMessage, cause);
	}

	public ExternalException(String externalExceptionCode, String debugMessage)
	{
		this(externalExceptionCode, null, debugMessage, null);
	}

	public ExternalException(String debugMessage, Throwable cause)
	{
		this(null, null, debugMessage, cause);
	}

	public ExternalException(String debugMessage)
	{
		this(null, null, debugMessage, null);
	}
}
