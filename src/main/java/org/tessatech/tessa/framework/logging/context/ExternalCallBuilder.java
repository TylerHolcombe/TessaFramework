package org.tessatech.tessa.framework.logging.context;

public class ExternalCallBuilder
{
	public String serviceName;
	public String serviceVersion;
	public String serviceMethod;

	public Boolean success;
	public String externalResponseCode;
	public String externalResponseMessage;
	public String externalTraceId;

	public long startTime;
	public long endTime;
	public long runtime;


	public ExternalCallBuilder(String serviceName, String serviceVersion, String serviceMethod)
	{
		this.serviceName = serviceName;
		this.serviceVersion = serviceVersion;
		this.serviceMethod = serviceMethod;
		startTime = System.currentTimeMillis();
	}

	public ExternalCallBuilder addSuccessfulResponse(String externalResponseCode, String externalResponseMessage, String externalTraceId)
	{
		this.success = true;
		this.externalResponseCode = externalResponseCode;
		this.externalResponseMessage = externalResponseMessage;
		this.externalTraceId = externalTraceId;
		markRuntime();

		return this;
	}


	public ExternalCallBuilder addErrorResponse(String externalResponseCode, String externalResponseMessage, String externalTraceId)
	{
		this.success = false;
		this.externalResponseCode = externalResponseCode;
		this.externalResponseMessage = externalResponseMessage;
		this.externalTraceId = externalTraceId;
		markRuntime();

		return this;
	}

	public ExternalCallBuilder addUnknownErrorResponse()
	{
		this.success = false;
		this.externalResponseCode = "UNKNOWN_ERROR";
		markRuntime();

		return this;
	}

	public ExternalCallBuilder addTimeoutResponse()
	{
		this.success = false;
		this.externalResponseCode = "TIMEOUT";
		markRuntime();

		return this;
	}

	private void markRuntime()
	{
		endTime = System.currentTimeMillis();
		runtime = endTime - startTime;
	}
}
