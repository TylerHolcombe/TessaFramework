package org.tessatech.tessa.framework.exception.system;

import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.TessaExceptionCodes;

public class ExternalTimeoutException extends TessaException
{

	public ExternalTimeoutException(String externalExceptionCode, String externalExceptionMessage, String debugMessage, Throwable cause)
	{
		super(TessaExceptionCodes.EXTERNAL_TIMEOUT_HTTP_STATUS, TessaExceptionCodes.EXTERNAL_TIMEOUT_CODE, TessaExceptionCodes.EXTERNAL_TIMEOUT_MESSAGE,
				externalExceptionCode, externalExceptionMessage, debugMessage, cause);
	}

	public ExternalTimeoutException(String externalExceptionCode, String externalExceptionMessage, String debugMessage)
	{
		this(externalExceptionCode, externalExceptionMessage, debugMessage, null);
	}

	public ExternalTimeoutException(String externalExceptionCode, String debugMessage, Throwable cause)
	{
		this(externalExceptionCode, null, debugMessage, cause);
	}

	public ExternalTimeoutException(String externalExceptionCode, String debugMessage)
	{
		this(externalExceptionCode, null, debugMessage, null);
	}

	public ExternalTimeoutException(String debugMessage, Throwable cause)
	{
		this(null, null, debugMessage, cause);
	}

	public ExternalTimeoutException(String debugMessage)
	{
		this(null, null, debugMessage, null);
	}
}
