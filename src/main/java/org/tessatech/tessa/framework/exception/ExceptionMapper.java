package org.tessatech.tessa.framework.exception;

import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.exception.system.UnknownException;

@Component
public class ExceptionMapper
{
	public static TessaException mapUnhandledException(Throwable throwable)
	{
		throw new UnknownException("Throwable mapped into UnknownException via the ExceptionMapper.", throwable);
	}

}
