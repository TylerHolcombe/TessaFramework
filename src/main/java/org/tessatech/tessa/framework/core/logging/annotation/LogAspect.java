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

package org.tessatech.tessa.framework.core.logging.annotation;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.core.logging.context.LoggingContextHolder;

@Aspect
@Component
public class LogAspect
{
	private static final Logger logger = LogManager.getLogger(LogAspect.class);

	@Pointcut("execution(public * *(..))")
	void anyPublicMethod()
	{
	}

	@Around("anyPublicMethod() && @annotation(logRuntime)")
	public Object logRuntime(ProceedingJoinPoint proceedingJoinPoint, LogRuntime logRuntime) throws Throwable
	{
		if(logRuntime.debugOnly() && logger.getLevel().isLessSpecificThan(Level.DEBUG))
		{
			return proceedingJoinPoint.proceed();
		}

		long startTime = System.currentTimeMillis();

		try
		{
			return proceedingJoinPoint.proceed();
		}
		finally
		{
			long runtime = System.currentTimeMillis() - startTime;

			String methodName = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName()
					+ "." + proceedingJoinPoint.getSignature().getName() + "()";

			LoggingContextHolder.getContextOptional().ifPresent(
					loggingContext -> loggingContext.addRuntime(methodName, runtime));

			logger.debug("Executed: " + methodName + " in " + runtime + " ms." );
		}
	}
}
