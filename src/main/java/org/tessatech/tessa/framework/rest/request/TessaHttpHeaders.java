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

package org.tessatech.tessa.framework.rest.request;

import org.springframework.http.HttpHeaders;
import org.tessatech.tessa.framework.core.event.context.EventContext;
import org.tessatech.tessa.framework.core.event.context.EventContextHolder;

import java.util.Optional;

public class TessaHttpHeaders
{
	private static final String REQUEST_ID = "X-Request-ID";
	private static final String CORRELATION_ID = "X-Correlation-ID";
	private static final String SESSION_ID = "X-Session-ID";
	private static final String INTERNAL_TRACE_ID = "INTERNAL-TRACE-ID";

	private static final String CLIENT_IP = "X-Forwarded-For";

	private static final String DEVICE_ID = "Device-ID";
	private static final String DEVICE_TYPE = "Device-Type";


	private HttpHeaders headers;

	public TessaHttpHeaders()
	{
		this.headers = new HttpHeaders();
	}

	public TessaHttpHeaders(HttpHeaders headers)
	{
		this.headers = headers;
	}

	public TessaHttpHeaders(Optional<EventContext> contextOptional)
	{
		this();

		if (contextOptional.isPresent())
		{
			EventContext context = EventContextHolder.getContext();
			setRequestId(String.valueOf(context.getInternalTraceId()));
			setCorrelationId(String.valueOf(context.getInternalTraceId()));
		}
	}

	public String getRequestId()
	{
		return headers.getFirst(REQUEST_ID);
	}

	public void setRequestId(String value)
	{
		headers.add(REQUEST_ID, value);
	}

	public String getCorrelationId()
	{
		return headers.getFirst(CORRELATION_ID);
	}

	public void setCorrelationId(String value)
	{
		headers.add(CORRELATION_ID, value);
	}

	public String getSessionId()
	{
		return headers.getFirst(SESSION_ID);
	}

	public void setSessionId(String value)
	{
		headers.add(SESSION_ID, value);
	}

	public String getInternalTraceId()
	{
		return headers.getFirst(INTERNAL_TRACE_ID);
	}

	public void setInternalTraceId(String value)
	{
		headers.set(INTERNAL_TRACE_ID, value);
	}

	public String getClientIp()
	{
		return headers.getFirst(CLIENT_IP);
	}

	public void setClientIp(String value)
	{
		headers.set(CLIENT_IP, value);
	}

	public String getDeviceId()
	{
		return headers.getFirst(DEVICE_ID);
	}

	public void setDeviceId(String value)
	{
		headers.set(DEVICE_ID, value);
	}

	public String getDeviceType()
	{
		return headers.getFirst(DEVICE_TYPE);
	}

	public void setDeviceType(String value)
	{
		headers.set(DEVICE_TYPE, value);
	}

	public HttpHeaders getHeaders()
	{
		return headers;
	}
}
