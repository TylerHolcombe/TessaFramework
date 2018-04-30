package org.tessatech.tessa.framework.exception.logic;

import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.TessaExceptionCodes;

public class DataNotFoundException extends TessaException
{
	public DataNotFoundException(String debugMessage, Throwable cause)
	{
		super(TessaExceptionCodes.NOT_FOUND_HTTP_STATUS, TessaExceptionCodes.NOT_FOUND_CODE,
				TessaExceptionCodes.NOT_FOUND_MESSAGE, debugMessage, cause);
	}

	public DataNotFoundException(String debugMessage)
	{
		this(debugMessage, null);
	}

}
