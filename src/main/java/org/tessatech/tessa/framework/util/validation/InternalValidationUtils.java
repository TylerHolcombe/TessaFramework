package org.tessatech.tessa.framework.util.validation;

import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.system.InternalException;

public class InternalValidationUtils extends AbstractValidationUtils
{

	private static InternalValidationUtils instance = new InternalValidationUtils();

	public static InternalValidationUtils getInstance()
	{
		return instance;
	}

	@Override
	TessaException generateExceptionForMessage(String message)
	{
		return new InternalException(message);
	}

	@Override
	TessaException generateExceptionForMessage(String message, Throwable cause)
	{
		return new InternalException(message, cause);
	}
}
