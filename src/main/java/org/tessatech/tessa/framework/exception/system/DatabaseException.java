package org.tessatech.tessa.framework.exception.system;

import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.TessaExceptionCodes;
import org.springframework.http.HttpStatus;

public class DatabaseException extends TessaException
{
	public DatabaseException(String externalExceptionCode, String externalExceptionMessage, String debugMessage, Throwable cause)
	{
		super(TessaExceptionCodes.DATABASE_ERROR_HTTP_STATUS, TessaExceptionCodes.DATABASE_ERROR_CODE,
				TessaExceptionCodes.DATABASE_ERROR_MESSAGE,	externalExceptionCode, externalExceptionMessage,
				debugMessage, cause);
	}

	public DatabaseException(String debugMessage, Throwable cause)
	{
		this(null, null, debugMessage, cause);
	}

	public DatabaseException(String debugMessage)
	{
		this(debugMessage, null);
	}
}
