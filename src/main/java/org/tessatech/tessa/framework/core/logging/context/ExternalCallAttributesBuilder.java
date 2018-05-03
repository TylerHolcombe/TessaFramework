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

package org.tessatech.tessa.framework.core.logging.context;

import org.springframework.http.HttpStatus;

public class ExternalCallAttributesBuilder
{
	private String systemName;
	private String serviceName;
	private String serviceVersion;
	private String serviceMethod;
	private String serviceOperation;
	private Integer httpStatusCode;
	private String externalResponseCode;
	private String externalResponseMessage;
	private String externalTraceId;
	private long startTime;
	private Throwable throwable;

	public ExternalCallAttributesBuilder(String systemName, String serviceName, String serviceMethod, String serviceOperation, String serviceVersion)
	{
		startTime = System.currentTimeMillis();
		this.systemName = systemName;
		this.serviceMethod = serviceMethod;
		this.serviceName = serviceName;
		this.serviceVersion = serviceVersion;
		this.serviceOperation = serviceOperation;
	}

	public ExternalCallAttributesBuilder(String systemName, String serviceName, String serviceMethod, String serviceOperation)
	{
		this(systemName, serviceName, serviceMethod, serviceOperation, null);
	}

	public ExternalCallAttributesBuilder(String systemName, String serviceMethod, String serviceOperation)
	{
		this(systemName, null, serviceMethod, serviceOperation, null);
	}

	public ExternalCallAttributesBuilder(String systemName, String serviceMethod)
	{
		this(systemName, null, serviceMethod,null, null);
	}

	public ExternalCallAttributesBuilder setHttpStatusCode(HttpStatus httpStatusCode)
	{
		if (httpStatusCode != null)
		{
			this.httpStatusCode = httpStatusCode.value();
		}

		return this;
	}

	public ExternalCallAttributesBuilder setHttpStatusCode(int httpStatusCode)
	{
		if(httpStatusCode != 0)
			this.httpStatusCode = httpStatusCode;

		return this;
	}

	public ExternalCallAttributesBuilder setExternalResponseCode(Object externalResponseCode)
	{
		this.externalResponseCode = String.valueOf(externalResponseCode);
		return this;
	}

	public ExternalCallAttributesBuilder setExternalResponseCode(String externalResponseCode)
	{
		this.externalResponseCode = externalResponseCode;
		return this;
	}

	public ExternalCallAttributesBuilder setExternalResponseMessage(String externalResponseMessage)
	{
		this.externalResponseMessage = externalResponseMessage;
		return this;
	}

	public ExternalCallAttributesBuilder setExternalTraceId(String externalTraceId)
	{
		this.externalTraceId = externalTraceId;
		return this;
	}

	public ExternalCallAttributesBuilder setExternalTraceId(Object externalTraceId)
	{
		this.externalTraceId = String.valueOf(externalTraceId);
		return this;
	}

	public ExternalCallAttributesBuilder setThrowable(Throwable throwable)
	{
		this.throwable = throwable;
		return this;
	}

	private ExternalCallAttributes build(boolean wasSuccessful)
	{
		long endTime = System.currentTimeMillis();
		return new ExternalCallAttributes(systemName, serviceName, serviceOperation, serviceVersion, serviceMethod, wasSuccessful, httpStatusCode, externalResponseCode, externalResponseMessage, externalTraceId, startTime, endTime, (endTime - startTime), throwable);
	}

	public void buildAndCommit(boolean success)
	{
		LoggingContextHolder.getContextOptional().ifPresent(loggingContext -> loggingContext.addExternalCall(build(success)));
	}
}