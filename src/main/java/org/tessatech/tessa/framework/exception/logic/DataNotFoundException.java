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

package org.tessatech.tessa.framework.exception.logic;

import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.TessaExceptionCodes;

public class DataNotFoundException extends TessaException
{
	public DataNotFoundException(String debugMessage, Throwable cause)
	{
		super(TessaExceptionCodes.NOT_FOUND_HTTP_STATUS, TessaExceptionCodes.NOT_FOUND_CODE,
				TessaExceptionCodes.NOT_FOUND_MESSAGE, debugMessage, cause);
	}

	public DataNotFoundException(String debugMessage)
	{
		this(debugMessage, null);
	}

}
