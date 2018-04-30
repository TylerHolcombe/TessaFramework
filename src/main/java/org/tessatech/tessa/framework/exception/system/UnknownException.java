package org.tessatech.tessa.framework.exception.system;

import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.TessaExceptionCodes;

public class UnknownException extends TessaException
{
	public UnknownException(String debugMessage, Throwable cause)
	{
		super(TessaExceptionCodes.UNKNOWN_ERROR_HTTP_STATUS, TessaExceptionCodes.UNKNOWN_ERROR_CODE, TessaExceptionCodes.UNKNOWN_ERROR_MESSAGE, debugMessage, cause);
	}

	public UnknownException(String debugMessage)
	{
		this(debugMessage, null);
	}
}
