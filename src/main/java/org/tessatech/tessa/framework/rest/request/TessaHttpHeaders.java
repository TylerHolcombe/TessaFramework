package org.tessatech.tessa.framework.rest.request;

import org.springframework.http.HttpHeaders;

public class TessaHttpHeaders
{
	private static final String REQUEST_ID = "X-Request-ID";
	private static final String CORRELATION_ID = "X-Correlation-ID";
	private static final String INTERNAL_TRACE_ID = "INTERNAL-TRACE-ID";

	private static final String DEVICE_ID = "Device-Id";
	private static final String DEVICE_TYPE = "Device-Type";


	private HttpHeaders headers;

	public TessaHttpHeaders(HttpHeaders headers)
	{
		this.headers = headers;
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

	public String getInternalTraceId()
	{
		return headers.getFirst(INTERNAL_TRACE_ID);
	}

	public void setInternalTraceId(String value)
	{
		headers.set(INTERNAL_TRACE_ID, value);
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
