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

package org.tessatech.tessa.framework.core.exception.logic;

public class InvalidRequestException extends RuntimeException
{
	private String invalidRequestMessage;

	public InvalidRequestException(String invalidRequestMessage, String debugMessage, Throwable cause)
	{
		super(debugMessage, cause);
		this.invalidRequestMessage = invalidRequestMessage;
	}

	public InvalidRequestException(String debugMessage)
	{
		super(debugMessage);
	}

	public InvalidRequestException(String invalidRequestMessage, String debugMessage)
	{
		this(invalidRequestMessage, debugMessage, null);
	}

	public InvalidRequestException(String debugMessage, Throwable cause)
	{
		super(debugMessage, cause);
	}


	public String getInvalidRequestMessage()
	{
		return invalidRequestMessage;
	}
}
