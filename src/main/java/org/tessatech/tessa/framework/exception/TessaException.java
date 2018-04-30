package org.tessatech.tessa.framework.exception;

import org.springframework.http.HttpStatus;

public abstract class TessaException extends RuntimeException
{
	public HttpStatus httpCode;

	public long exceptionCode;
	public String exceptionMessage;

	public String externalExceptionCode;
	public String externalExceptionMessage;

	public TessaException(HttpStatus httpCode, long exceptionCode, String exceptionMessage, String externalExceptionCode, String externalExceptionMessage, String debugMessage, Throwable cause)
	{
		super(debugMessage, cause);
		this.httpCode = httpCode;
		this.exceptionCode = exceptionCode;
		this.exceptionMessage = exceptionMessage;
		this.externalExceptionCode = externalExceptionCode;
		this.externalExceptionMessage = externalExceptionMessage;
	}

	public TessaException(HttpStatus httpCode, long exceptionCode, String exceptionMessage, String debugMessage, Throwable cause)
	{
		super(debugMessage, cause);
		this.httpCode = httpCode;
		this.exceptionCode = exceptionCode;
		this.exceptionMessage = exceptionMessage;
	}
}
