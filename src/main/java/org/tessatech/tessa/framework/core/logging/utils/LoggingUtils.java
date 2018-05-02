/*
 * Copyright (c) 2015-2018 TessaTech LLC.
 *
 * Licensed under the MIT License (the "License"); you may not use this file
 *  except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 *
 */

package org.tessatech.tessa.framework.core.logging.utils;

import org.tessatech.tessa.framework.core.logging.context.LoggingContextHolder;

public class LoggingUtils
{

	private static final String AUTHENTICATED = "authenticated";
	private static final String AUTHORIZATION = "authenticated";

	private static final String PASSED = "passed";
	private static final String SKIPPED = "skipped";
	private static final String FAILED = "failed";

	public static void logAuthenticationPassed()
	{
		LoggingContextHolder.getContextOptional().ifPresent(loggingContext -> loggingContext.addField(AUTHENTICATED, PASSED));
	}

	public static void logAuthenticationSkipped()
	{
		LoggingContextHolder.getContextOptional().ifPresent(loggingContext -> loggingContext.addField(AUTHENTICATED, SKIPPED));
	}

	public static void logAuthenticationFailed()
	{
		LoggingContextHolder.getContextOptional().ifPresent(loggingContext -> loggingContext.addField(AUTHENTICATED, FAILED));
	}

	public static void logAuthorizationPassed()
	{
		LoggingContextHolder.getContextOptional().ifPresent(loggingContext -> loggingContext.addField(AUTHORIZATION, PASSED));
	}

	public static void logAuthorizationSkipped()
	{
		LoggingContextHolder.getContextOptional().ifPresent(loggingContext -> loggingContext.addField(AUTHORIZATION, SKIPPED));
	}

	public static void logAuthorizationFailed()
	{
		LoggingContextHolder.getContextOptional().ifPresent(loggingContext -> loggingContext.addField(AUTHORIZATION, FAILED));
	}
}
