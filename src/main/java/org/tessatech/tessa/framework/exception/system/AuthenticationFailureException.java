package org.tessatech.tessa.framework.exception.system;

import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.TessaExceptionCodes;
import org.springframework.http.HttpStatus;

public class AuthenticationFailureException extends TessaException
{
	public AuthenticationFailureException(String debugMessage, Throwable cause)
	{
		super(TessaExceptionCodes.AUTH_FAILURE_HTTP_STATUS, TessaExceptionCodes.AUTH_FAILURE_CODE,
				TessaExceptionCodes.AUTH_FAILURE_MESSAGE, debugMessage, cause);
	}

	public AuthenticationFailureException(String debugMessage)
	{
		this(debugMessage, null);
	}

}
