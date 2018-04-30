package org.tessatech.tessa.framework.rest.response;

public class DefaultTessaWebserviceResponse implements TessaWebserviceResponse
{
	TessaError tessaError;

	public DefaultTessaWebserviceResponse(TessaError tessaError)
	{
		this.tessaError = tessaError;
	}

	@Override
	public TessaError getTessaError()
	{
		return tessaError;
	}
}
