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

package org.tessatech.tessa.framework.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.exception.ExceptionMapper;
import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.exception.system.InternalException;
import org.tessatech.tessa.framework.logging.TessaTransactionLogger;
import org.tessatech.tessa.framework.logging.context.LoggingContext;
import org.tessatech.tessa.framework.logging.context.LoggingContextHolder;
import org.tessatech.tessa.framework.rest.RestServiceMapper;
import org.tessatech.tessa.framework.rest.response.TessaWebserviceResponse;
import org.tessatech.tessa.framework.security.SecurityManager;
import org.tessatech.tessa.framework.security.context.SecurityContextHolder;
import org.tessatech.tessa.framework.transaction.context.TransactionContextHolder;

@Aspect
@Component
public class TessaTransactionAspect
{
	Logger logger = LogManager.getLogger(TessaTransactionAspect.class);

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private RestServiceMapper restServiceMapper;

	@Autowired
	private TessaTransactionLogger transactionLogger;

	@Autowired
	private ExceptionMapper exceptionMapper;

	@Pointcut("execution(public * *(..))")
	void anyPublicMethod()
	{
	}

	@Around("anyPublicMethod() && @annotation(tessaTransaction)")
	public Object tessaTransaction(ProceedingJoinPoint proceedingJoinPoint, TessaTransaction tessaTransaction)
	{
		ResponseEntity<TessaWebserviceResponse> response = null;

		try
		{
			try
			{
				beginTransaction(tessaTransaction, getHttpHeadersFromRequest(proceedingJoinPoint));

				response = getTessaResponseEntityFromResponse(proceedingJoinPoint, proceedingJoinPoint.proceed());

			}
			catch (Throwable throwable)
			{
				logger.error("Caught in expected block", throwable);
				response = handleError(throwable);
			}
			finally
			{
				endTransaction(response);
			}
		}
		catch (Throwable throwable)
		{
			logger.error("Exception occurred while ending previous transaction", throwable);
			response = handleError(throwable);
		}

		return response;
	}

	private void beginTransaction(TessaTransaction transaction, RequestEntity requestEntity)
	{
		LoggingContextHolder.setContext(new LoggingContext());
		TransactionContextHolder.setContext(restServiceMapper.mapInboundRequestToContext(transaction, requestEntity));

		securityManager.secureRequest(transaction, requestEntity);
	}

	private void endTransaction(ResponseEntity<TessaWebserviceResponse> response)
	{
		transactionLogger.logTransaction(response.getBody());

		LoggingContextHolder.clearContext();
		TransactionContextHolder.clearContext();
		SecurityContextHolder.clearContext();
	}

	private ResponseEntity<TessaWebserviceResponse> handleError(Throwable throwable)
	{
		TessaException mappedException = exceptionMapper.mapUnhandledException(throwable);
		LoggingContextHolder.getContextOptional().ifPresent(context -> context.addThrowable(mappedException));
		return restServiceMapper.mapTessaExceptionResponse(mappedException);
	}


	private RequestEntity getHttpHeadersFromRequest(ProceedingJoinPoint joinPoint)
	{
		if (joinPoint != null && joinPoint.getArgs() != null && joinPoint.getArgs().length > 0)
		{
			for (Object object : joinPoint.getArgs())
			{
				if (object instanceof RequestEntity<?>)
				{
					return (RequestEntity) object;
				}
			}
		}

		throw new InternalException("Could not apply @TessaTransaction to a serviceMethodName without a RequestEntity parameter");
	}

	private ResponseEntity<TessaWebserviceResponse> getTessaResponseEntityFromResponse(ProceedingJoinPoint joinPoint, Object methodCallReponse)
	{
		if (methodCallReponse instanceof ResponseEntity)
		{
			try
			{
				return (ResponseEntity<TessaWebserviceResponse>) methodCallReponse;
			}
			catch (ClassCastException classCastException)
			{
				throw new InternalException("ClassCastException attempting to convert a ResponseEntity into a ResponseEntity<TessaWebserviceResponse. Your body class must implement TessaWebserviceResponse.", classCastException);
			}
		}

		throw new InternalException("Could not apply @TessaTransaction to a serviceMethodName without a ResponseEntity<TessaWebserviceResponse> response.");
	}

}
