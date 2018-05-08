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

package org.tessatech.tessa.framework.core.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.core.event.context.EventContext;
import org.tessatech.tessa.framework.core.event.context.EventContextHolder;
import org.tessatech.tessa.framework.core.exception.util.ThrowableAdapterFinderWrapper;
import org.tessatech.tessa.framework.core.logging.TessaLogManager;
import org.tessatech.tessa.framework.core.logging.context.LoggingContext;
import org.tessatech.tessa.framework.core.logging.context.LoggingContextHolder;
import org.tessatech.tessa.framework.core.util.UniqueIdentifierUtils;

@Aspect
@Component
public class TessaEventAspect
{
	Logger logger = LogManager.getLogger(TessaEventAspect.class);

	@Autowired
	private ThrowableAdapterFinderWrapper throwableAdapterFinderWrapper;

	@Autowired
	private TessaLogManager logManager;

	@Pointcut("execution(public * *(..))")
	void anyPublicMethod()
	{
	}

	@Around("anyPublicMethod() && @annotation(tessaEvent)")
	public Object tessaTransaction(ProceedingJoinPoint proceedingJoinPoint, TessaEvent tessaEvent) throws Throwable
	{
		boolean setLoggingContext = false;
		boolean setEventContext = false;

		Object response = null;
		Throwable throwable = null;

		try
		{
			try
			{
				setLoggingContext = initializeLoggingContext(setLoggingContext);
				setEventContext = initializeEventContext(tessaEvent, setEventContext);

				response = proceedingJoinPoint.proceed();
			}
			catch (Throwable t)
			{
				handleThrowable(tessaEvent, t);
				throwable = t;
			}
			finally
			{
				endEvent(setLoggingContext, setEventContext, response);
			}
		}
		catch (Throwable t)
		{
			logger.error("Exception occurred while attempting to end an Event: " + tessaEvent.eventName(), t);
		}

		if (throwable != null)
		{
			throw throwable;
		}

		return response;
	}

	private boolean initializeLoggingContext(boolean setLoggingContext)
	{
		if (!LoggingContextHolder.getContextOptional().isPresent())
		{
			LoggingContextHolder.setContext(new LoggingContext());
			setLoggingContext = true;
		}
		return setLoggingContext;
	}

	private boolean initializeEventContext(TessaEvent tessaEvent, boolean setEventContext)
	{
		if (!EventContextHolder.getContextOptional().isPresent())
		{
			long eventId = UniqueIdentifierUtils.getUniqueId();
			EventContext context = new EventContext(tessaEvent.eventName(), tessaEvent.eventVersion(), tessaEvent.eventGroup(), eventId);
			EventContextHolder.setContext(context);
			setEventContext = true;
		}
		return setEventContext;
	}

	private void handleThrowable(TessaEvent tessaEvent, Throwable throwable)
	{
		logger.error("An exception was thrown while processing an event: " + tessaEvent.eventName(), throwable);
		LoggingContextHolder.getContextOptional().ifPresent(loggingContext -> loggingContext.addThrowable(throwable));
	}

	private void endEvent(boolean setLoggingContext, boolean setEventContext, Object response)
	{
		logManager.logEvent(response);

		if (setLoggingContext)
		{
			LoggingContextHolder.clearContext();
		}
		if (setEventContext)
		{
			EventContextHolder.clearContext();
		}
	}
}
