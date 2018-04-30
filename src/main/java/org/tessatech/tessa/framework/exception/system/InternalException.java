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
