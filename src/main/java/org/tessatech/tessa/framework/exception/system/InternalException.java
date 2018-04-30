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

public class InternalException extends TessaException
{

	public InternalException(long internalErrorCode, String internalErrorMessage, String debugMessage, Throwable cause)
	{
		super(TessaExceptionCodes.INTERNAL_ERROR_HTTP_STATUS, internalErrorCode, internalErrorMessage, debugMessage, cause);
	}

	public InternalException(long internalErrorCode, String internalErrorMessage, String debugMessage)
	{
		super(TessaExceptionCodes.INTERNAL_ERROR_HTTP_STATUS, internalErrorCode, internalErrorMessage, debugMessage, null);
	}

	public InternalException(String debugMessage, Throwable cause)
	{
		super(TessaExceptionCodes.INTERNAL_ERROR_HTTP_STATUS, TessaExceptionCodes.INTERNAL_ERROR_CODE, TessaExceptionCodes.INTERNAL_ERROR_MESSAGE, debugMessage, cause);
	}

	public InternalException(String debugMessage)
	{
		this(debugMessage, null);
	}
}
