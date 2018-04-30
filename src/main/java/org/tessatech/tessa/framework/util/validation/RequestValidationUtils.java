package org.tessatech.tessa.framework.util.validation;

import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.logic.ValidationException;

public class RequestValidationUtils extends AbstractValidationUtils
{

	private static RequestValidationUtils instance = new RequestValidationUtils();

	public static RequestValidationUtils getInstance()
	{
		return instance;
	}


	@Override
	TessaException generateExceptionForMessage(String message)
	{
		return new ValidationException(message);
	}

	@Override
	TessaException generateExceptionForMessage(String message, Throwable cause)
	{
		return new ValidationException(message, cause);
	}
}
