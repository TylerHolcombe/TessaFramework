package org.tessatech.tessa.framework.exception.logic;

import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.TessaExceptionCodes;
import org.springframework.http.HttpStatus;

public class InvalidAuthenticationException extends TessaException
{
	public InvalidAuthenticationException(String debugMessage, Throwable cause)
	{
		super(TessaExceptionCodes.INVALID_AUTHENTICATION_HTTP_STATUS, TessaExceptionCodes.INVALID_AUTHENTICATION_CODE,
				TessaExceptionCodes.INVALID_AUTHENTICATION_MESSAGE, debugMessage, cause);
	}

	public InvalidAuthenticationException(String debugMessage)
	{
		this(debugMessage, null);
	}

}
