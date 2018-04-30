package org.tessatech.tessa.framework.logging.context;

import org.tessatech.tessa.framework.exception.system.InternalException;

import java.util.Optional;

public class LoggingContextHolder
{

	private static final ThreadLocal<LoggingContext> LoggingContextLocal = new ThreadLocal<>();

	public static void setContext(LoggingContext context)
	{
		LoggingContextLocal.set(context);
	}

	public static void clearContext()
	{
		LoggingContextLocal.set(null);
		LoggingContextLocal.remove();
	}

	public static LoggingContext getContext()
	{
		LoggingContext context = LoggingContextLocal.get();

		if (context == null)
		{
			throw new InternalException("LoggingContext was requested but is Null");
		}

		return context;
	}

	public static Optional<LoggingContext> getContextOptional()
	{
		return Optional.ofNullable(LoggingContextLocal.get());
	}

}
