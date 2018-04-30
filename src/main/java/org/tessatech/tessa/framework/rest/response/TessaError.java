package org.tessatech.tessa.framework.rest.response;

public class TessaError
{
	public TessaError(String httpStatus, long errorCode, String errorMessage, long internalErrorId)
	{
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.internalErrorId = internalErrorId;
	}

	public String httpStatus;
	public long errorCode;
	public String errorMessage;
	public long internalErrorId;

}
