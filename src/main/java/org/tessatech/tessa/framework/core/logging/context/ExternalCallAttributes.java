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

public class ExternalCallAttributes
{
	public final String systemName;
	public final String serviceName;
	public final String serviceOperation;
	public final String serviceVersion;
	public final String serviceMethod;

	public final Boolean success;
	public final Integer httpStatusCode;
	public final String externalResponseCode;
	public final String externalResponseMessage;
	public final String externalTraceId;

	public final Throwable throwable;

	public final long startTime;
	public final long endTime;
	public final long runtime;

	ExternalCallAttributes(String systemName, String serviceName, String serviceOperation, String serviceVersion, String serviceMethod, Boolean success, Integer httpStatusCode, String externalResponseCode, String externalResponseMessage, String externalTraceId, long startTime, long endTime, long runtime, Throwable throwable)
	{
		this.systemName = systemName;
		this.serviceName = serviceName;
		this.serviceOperation = serviceOperation;
		this.serviceVersion = serviceVersion;
		this.serviceMethod = serviceMethod;
		this.success = success;
		this.httpStatusCode = httpStatusCode;
		this.externalResponseCode = externalResponseCode;
		this.externalResponseMessage = externalResponseMessage;
		this.externalTraceId = externalTraceId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.runtime = runtime;
		this.throwable = throwable;
	}
}
