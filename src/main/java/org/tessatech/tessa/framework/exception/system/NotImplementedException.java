package org.tessatech.tessa.framework.exception.system;

import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.TessaExceptionCodes;
import org.springframework.http.HttpStatus;

public class NotImplementedException extends TessaException
{

	public NotImplementedException(String debugMessage, Throwable cause)
	{
		super(TessaExceptionCodes.NOT_IMPLEMENTED_HTTP_STATUS, TessaExceptionCodes.NOT_IMPLEMENTED_CODE, TessaExceptionCodes.NOT_IMPLEMENTED_MESSAGE,
				debugMessage, cause);
	}

	public NotImplementedException(String message)
	{
		this(message, null);
	}
}
