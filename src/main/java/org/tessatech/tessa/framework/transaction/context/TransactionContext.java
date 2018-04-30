package org.tessatech.tessa.framework.transaction.context;

public class TransactionContext
{
	private final String serviceName;
	private final String serviceVersion;
	private final String serviceMethodName;

	private final String requestId;
	private final String correlationId;
	private final long internalTraceId;

	private final String deviceId;
	private final String deviceType;

	public TransactionContext(String serviceName, String serviceVersion, String serviceMethodName, String requestId,
							  String correlationId, long internalTraceId, String deviceId, String deviceType)
	{
		this.serviceName = serviceName;
		this.serviceVersion = serviceVersion;
		this.serviceMethodName = serviceMethodName;
		this.requestId = requestId;
		this.correlationId = correlationId;
		this.internalTraceId = internalTraceId;
		this.deviceId = deviceId;
		this.deviceType = deviceType;
	}

	public String getServiceName()
	{
		return serviceName;
	}

	public String getServiceVersion()
	{
		return serviceVersion;
	}

	public String getServiceMethodName()
	{
		return serviceMethodName;
	}

	public String getRequestId()
	{
		return requestId;
	}

	public String getCorrelationId()
	{
		return correlationId;
	}

	public long getInternalTraceId()
	{
		return internalTraceId;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public String getDeviceType()
	{
		return deviceType;
	}
}
