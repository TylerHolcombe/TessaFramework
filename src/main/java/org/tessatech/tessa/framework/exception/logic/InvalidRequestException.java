package org.tessatech.tessa.framework.exception.logic;

import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.TessaExceptionCodes;
import org.springframework.http.HttpStatus;

public class InvalidRequestException extends TessaException
{
	public InvalidRequestException(String debugMessage, Throwable cause)
	{
		super(TessaExceptionCodes.INVALID_REQUEST_HTTP_STATUS, TessaExceptionCodes.INVALID_REQUEST_ERROR_CODE,
				TessaExceptionCodes.INVALID_REQUEST_MESSAGE, debugMessage, cause);
	}

	public InvalidRequestException(String debugMessage)
	{
		this(debugMessage, (Throwable) null);
	}

	public InvalidRequestException(String invalidRequestMessage, String debugMessage, Throwable cause)
{
	super(TessaExceptionCodes.INVALID_REQUEST_HTTP_STATUS, TessaExceptionCodes.INVALID_REQUEST_ERROR_CODE,
			invalidRequestMessage, debugMessage, cause);
}

	public InvalidRequestException(String invalidRequestMessage, String debugMessage)
	{
		this(invalidRequestMessage, debugMessage, null);
	}
}
