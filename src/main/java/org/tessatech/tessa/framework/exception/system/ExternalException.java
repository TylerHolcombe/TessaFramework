package org.tessatech.tessa.framework.exception.system;

import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.TessaExceptionCodes;
import org.springframework.http.HttpStatus;

public class ExternalException extends TessaException
{

	public ExternalException(String externalExceptionCode, String externalExceptionMessage, String debugMessage, Throwable cause)
	{
		super(TessaExceptionCodes.EXTERNAL_ERROR_HTTP_STATUS, TessaExceptionCodes.EXTERNAL_ERROR_CODE, TessaExceptionCodes.EXTERNAL_ERROR_MESSAGE,
				externalExceptionCode, externalExceptionMessage, debugMessage, cause);
	}

	public ExternalException(String externalExceptionCode, String externalExceptionMessage, String debugMessage)
	{
		this(externalExceptionCode, externalExceptionMessage, debugMessage, null);
	}

	public ExternalException(String externalExceptionCode, String debugMessage, Throwable cause)
	{
		this(externalExceptionCode, null, debugMessage, cause);
	}

	public ExternalException(String externalExceptionCode, String debugMessage)
	{
		this(externalExceptionCode, null, debugMessage, null);
	}

	public ExternalException(String debugMessage, Throwable cause)
	{
		this(null, null, debugMessage, cause);
	}

	public ExternalException(String debugMessage)
	{
		this(null, null, debugMessage, null);
	}
}
