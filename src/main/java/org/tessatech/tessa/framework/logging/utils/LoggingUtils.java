package org.tessatech.tessa.framework.logging.utils;

import org.tessatech.tessa.framework.logging.context.LoggingContextHolder;

public class LoggingUtils
{

	private static final String AUTHENTICATED = "authenticated";
	private static final String AUTHORIZATION = "authenticated";

	private static final String PASSED = "passed";
	private static final String SKIPPED = "skipped";
	private static final String FAILED = "failed";

	public static void logAuthenticationPassed()
	{
		LoggingContextHolder.getContextOptional().ifPresent(loggingContext ->  loggingContext.addField(AUTHENTICATED, PASSED));
	}

	public static void logAuthenticationSkipped()
	{
		LoggingContextHolder.getContextOptional().ifPresent(loggingContext ->  loggingContext.addField(AUTHENTICATED, SKIPPED));
	}

	public static void logAuthenticationFailed()
	{
		LoggingContextHolder.getContextOptional().ifPresent(loggingContext -> loggingContext.addField(AUTHENTICATED, FAILED));
	}

	public static void logAuthorizationPassed()
	{
		LoggingContextHolder.getContextOptional().ifPresent(loggingContext ->  loggingContext.addField(AUTHORIZATION, PASSED));
	}

	public static void logAuthorizationSkipped()
	{
		LoggingContextHolder.getContextOptional().ifPresent(loggingContext ->  loggingContext.addField(AUTHORIZATION, SKIPPED));
	}

	public static void logAuthorizationFailed()
	{
		LoggingContextHolder.getContextOptional().ifPresent(loggingContext -> loggingContext.addField(AUTHORIZATION, FAILED));
	}
}
