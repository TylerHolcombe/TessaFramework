package org.tessatech.tessa.framework.exception.logic;

import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.TessaExceptionCodes;

public class ValidationException extends TessaException
{
	public ValidationException(String invalidRequestMessage, String debugMessage, Throwable cause)
{
	super(TessaExceptionCodes.INVALID_REQUEST_HTTP_STATUS, TessaExceptionCodes.INVALID_REQUEST_ERROR_CODE,
			invalidRequestMessage, debugMessage, cause);
}

	public ValidationException(String invalidRequestMessage, String debugMessage)
	{
		this(invalidRequestMessage, debugMessage, null);
	}

	public ValidationException(String invalidRequestMessage)
	{
		this(invalidRequestMessage, invalidRequestMessage, null);
	}

	public ValidationException(String invalidRequestMessage, Throwable cause)
	{
		this(invalidRequestMessage, invalidRequestMessage, cause);
	}
}
