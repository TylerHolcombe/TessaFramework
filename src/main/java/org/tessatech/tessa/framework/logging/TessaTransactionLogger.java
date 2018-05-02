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

package org.tessatech.tessa.framework.logging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.tessatech.tessa.framework.exception.TessaException;
import org.tessatech.tessa.framework.logging.context.LoggingContext;
import org.tessatech.tessa.framework.logging.context.LoggingContextHolder;
import org.tessatech.tessa.framework.logging.export.LogDataExporter;
import org.tessatech.tessa.framework.security.context.SecurityContext;
import org.tessatech.tessa.framework.security.context.SecurityContextHolder;
import org.tessatech.tessa.framework.transaction.context.TransactionContext;
import org.tessatech.tessa.framework.rest.response.TessaError;
import org.tessatech.tessa.framework.rest.response.TessaWebserviceResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.transaction.context.TransactionContextHolder;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class TessaTransactionLogger
{
	private static final Logger logger = LogManager.getLogger("org.tessatech.transaction.logger");

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Autowired
	private LogDataExporter logDataExporter;

	@Value("${log.export.enabled:false}")
	private boolean exportEnabled = false;

	public void logTransaction(ResponseEntity<TessaWebserviceResponse> responseEntity)
	{
		JsonObject object = new JsonObject();

		boolean loggedException = false;

		if(TransactionContextHolder.getContextOptional().isPresent())
		{
			addTransactionContextFields(TransactionContextHolder.getContextOptional(), object);
		}

		if(SecurityContextHolder.getContextOptional().isPresent())
		{
			addSecurityContextFields(SecurityContextHolder.getContextOptional(), object);
		}

		if(LoggingContextHolder.getContextOptional().isPresent())
		{
			loggedException = addLoggingContextFields(LoggingContextHolder.getContextOptional(), object, loggedException);
		}

		if (doesResponseContainAnError(responseEntity) && !loggedException)
		{
			addResponseFields(responseEntity.getBody(), object);
		}
		else if (responseEntity != null)
		{
			addIfNotNull(object, "httpStatusCode", responseEntity.getStatusCodeValue());
		}


		String logMessage = gson.toJson(object);

		logger.info(logMessage);

		if(exportEnabled)
		{
			logDataExporter.exportLogMessage(logMessage);
		}
	}

	private void addSecurityContextFields(Optional<SecurityContext> securityContextOptional, JsonObject object)
	{
		SecurityContext securityContext = securityContextOptional.get();

		addIfNotNull(object, "authenticationType", securityContext.getAuthenticationType());
		addIfNotNull(object, "authenticationId", securityContext.getAuthenticationId());

		addIfNotNull(object, "userId", securityContext.getUserId());
		addIfNotNull(object, "userName", securityContext.getUserName());
		addSecurityRoles(object, securityContext);
	}

	private void addSecurityRoles(JsonObject object, SecurityContext securityContext)
	{
		if(securityContext.getUserRoles() != null)
		{
			JsonArray rolesArray = new JsonArray();
			for (int i = 0; i < 3 && i < securityContext.getUserRoles().length; i++)
			{
				rolesArray.add(securityContext.getUserRoles()[i].toString());
			}
			object.add("roles", rolesArray);
		}
	}

	private void addTransactionContextFields(Optional<TransactionContext> transactionContextOptional, JsonObject object)
	{
		TransactionContext transactionContext = transactionContextOptional.get();

		addIfNotNull(object, "serviceName", transactionContext.getServiceName());
		addIfNotNull(object, "serviceOperation", transactionContext.getServiceOperation());
		addIfNotNull(object, "serviceVersion", transactionContext.getServiceVersion());
		addIfNotNull(object, "serviceMethodName", transactionContext.getServiceMethodName());


		addIfNotNull(object, "requestId", transactionContext.getRequestId());
		addIfNotNull(object, "correlationId", transactionContext.getCorrelationId());
		addIfNotNull(object, "internalTraceId", transactionContext.getInternalTraceId());

		addIfNotNull(object, "deviceId", transactionContext.getDeviceId());
		addIfNotNull(object, "deviceType", transactionContext.getDeviceType());
	}

	private boolean addLoggingContextFields(Optional<LoggingContext> loggingContextOptional, JsonObject object, boolean loggedException)
	{
		LoggingContext loggingContext = loggingContextOptional.get();

		long endTime = System.currentTimeMillis();
		addIfNotNull(object, "startTime",loggingContext.getStartTime());
		addIfNotNull(object, "endTime", endTime);
		addIfNotNull(object, "runtime", (endTime - loggingContext.getStartTime()));

		object.add("keyValue", getKeyValueJson(loggingContext.getKeyValueFields()));
		object.add("runtimes", getRuntimes(loggingContext.getRuntimes()));
		object.add("externalCalls", gson.toJsonTree(loggingContext.getExternalLogAttributes()));

		Throwable throwable = loggingContext.getThrowable();
		if(throwable != null)
		{
			if(throwable instanceof TessaException)
			{
				loggedException = true;
				addTessaException(object, (TessaException) throwable);
			}

			object.add("stackTrace", getStackTraceJson(loggingContext.getThrowable()));
		}
		return loggedException;
	}

	private boolean doesResponseContainAnError(ResponseEntity<TessaWebserviceResponse> response)
	{
		return response != null && response.getBody() != null && response.getBody().getTessaError() != null;
	}

	private void addTessaException(JsonObject object, TessaException throwable)
	{
		TessaException tessaException = throwable;
		addIfNotNull(object, "httpStatusCode", tessaException.httpCode.toString());
		addIfNotNull(object, "exceptionCode", tessaException.exceptionCode);
		addIfNotNull(object, "exceptionMessage", tessaException.exceptionMessage);
		addIfNotNull(object, "externalExceptionCode", tessaException.externalExceptionCode);
		addIfNotNull(object, "externalExceptionMessage", tessaException.externalExceptionMessage);
	}

	private void addIfNotNull(JsonObject object, String key, String value)
	{
		if(key != null && value != null)
		{
			object.addProperty(key, value);
		}
	}

	private void addIfNotNull(JsonObject object, String key, Object value)
	{
		if(key != null && value != null)
		{
			object.addProperty(key, value.toString());
		}
	}

	private void addIfNotNull(JsonObject object, String key, Number value)
	{
		if(key != null && value != null)
		{
			object.addProperty(key, value);
		}
	}

	private void addIfNotNull(JsonObject object, String key, Boolean value)
	{
		if(key != null && value != null)
		{
			object.addProperty(key, value);
		}
	}

	private JsonObject getKeyValueJson(Set<Map.Entry<String, Object>> kvPairs)
	{
		if (kvPairs != null && kvPairs.size() > 0)
		{
			JsonObject kvJson = new JsonObject();
			for(Map.Entry<String, Object> pair : kvPairs)
			{
				addIfNotNull(kvJson, pair.getKey(), pair.getValue());
			}

			return kvJson;
		}

		return null;
	}

	private JsonObject getRuntimes(Set<Map.Entry<String, Long>> runtimePairs)
	{
		if (runtimePairs != null && runtimePairs.size() > 0)
		{
			JsonObject runtimesJson = new JsonObject();
			for(Map.Entry<String, Long> pair : runtimePairs)
			{
				addIfNotNull(runtimesJson, pair.getKey(), pair.getValue().longValue());
			}

			return runtimesJson;
		}

		return null;
	}

	private JsonObject getStackTraceJson(Throwable throwable)
	{
		JsonObject stackTrace = new JsonObject();
		stackTrace.addProperty("class", throwable.getClass().getCanonicalName());
		stackTrace.addProperty("message", throwable.getMessage());

		JsonArray stackTraceArray = new JsonArray();
		for(int i = 0; i < 3 && i < throwable.getStackTrace().length; i++)
		{
			stackTraceArray.add(throwable.getStackTrace()[i].toString());
		}
		stackTrace.add("trace", stackTraceArray);

		if(throwable.getCause() != null)
		{
			stackTrace.add("causedBy", getStackTraceJson(throwable.getCause()));
		}

		return stackTrace;
	}

	private void addResponseFields(TessaWebserviceResponse response, JsonObject object)
	{
		TessaError tessaTessaError = response.getTessaError();
		addIfNotNull(object, "httpStatusCode", tessaTessaError.httpStatus);
		addIfNotNull(object, "exceptionCode", tessaTessaError.errorCode);
		addIfNotNull(object, "exceptionMessage", tessaTessaError.errorMessage);
	}

}
