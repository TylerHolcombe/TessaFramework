package org.tessatech.tessa.framework.exception.system;

import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.TessaExceptionCodes;

public class RemovedException extends TessaException
{

	public RemovedException(String debugMessage, Throwable cause)
	{
		super(TessaExceptionCodes.REMOVED_HTTP_STATUS, TessaExceptionCodes.REMOVED_CODE, TessaExceptionCodes.REMOVED_MESSAGE,
				debugMessage, cause);
	}

	public RemovedException(String debugMessage)
	{
		this(debugMessage, null);
	}
}
