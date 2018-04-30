package org.tessatech.tessa.framework.exception.logic;

import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.TessaExceptionCodes;
import org.springframework.http.HttpStatus;

public class InsufficientAuthorizationException extends TessaException
{
	public InsufficientAuthorizationException(String debugMessage, Throwable cause)
	{
		super(TessaExceptionCodes.INSUFFICIENT_AUTHENTICATION_HTTP_STATUS, TessaExceptionCodes.INSUFFICIENT_AUTHENTICATION_CODE,
				TessaExceptionCodes.INSUFFICIENT_AUTHENTICATION_MESSAGE, debugMessage, cause);
	}

	public InsufficientAuthorizationException(String debugMessage)
	{
		this(debugMessage, null);
	}

}
