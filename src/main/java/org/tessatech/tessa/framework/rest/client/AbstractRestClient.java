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

package org.tessatech.tessa.framework.rest.client;

import com.google.gson.Gson;
import io.atlassian.fugue.Either;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.tessatech.tessa.framework.core.exception.system.ExternalException;
import org.tessatech.tessa.framework.core.exception.system.InternalException;
import org.tessatech.tessa.framework.core.logging.external.ExternalCallAttributesBuilder;
import org.tessatech.tessa.framework.core.security.client.IAMServiceClient;

import java.net.URI;

public abstract class AbstractRestClient<Request, SuccessfulResponse, ErrorResponse>
{
	private static final Logger logger = LogManager.getLogger(IAMServiceClient.class);
	private static final Gson gson = new Gson();

	private String systemName;
	private String serviceName;
	private String serviceVersion;

	private Class<SuccessfulResponse> successClass;
	private Class<ErrorResponse> errorClass;

	private RestTemplate restTemplate = buildRestTemplate();

	public AbstractRestClient(String systemName, String serviceName, String serviceVersion,
			Class<SuccessfulResponse> successClass, Class<ErrorResponse> errorClass)
	{
		this.systemName = systemName;
		this.serviceName = serviceName;
		this.serviceVersion = serviceVersion;
		this.successClass = successClass;
		this.errorClass = errorClass;
	}


	protected SuccessfulResponse execute(String methodName, Request request, String uri, HttpMethod method)
	{
		Either<ErrorResponse, SuccessfulResponse> response =
				execute(methodName, request, buildHttpHeaders(), uri, method, true);


		if (response.isLeft())
		{
			throw new InternalException("AbstractRestClient is retuning null when throwErrors is turned on.");
		}

		return response.right().get();
	}

	protected Either<ErrorResponse, SuccessfulResponse> execute(String methodName, Request request, String uri,
			HttpMethod method, boolean throwErrors)
	{
		return execute(methodName, request, buildHttpHeaders(), uri, method, throwErrors);
	}


	protected abstract HttpHeaders buildHttpHeaders();

	protected ResponseErrorHandler getResponseErrorHandler()
	{
		return new TessaClientErrorHandler();
	}

	private RestTemplate buildRestTemplate()
	{
		RestTemplate template = new RestTemplateBuilder().setConnectTimeout(15000).setReadTimeout(15000).build();
		template.setErrorHandler(getResponseErrorHandler());
		return template;
	}

	protected abstract ExternalException convertErrorIntoException(ErrorResponse errorResponse);

	protected abstract void addSuccessAttributes(ExternalCallAttributesBuilder builder, SuccessfulResponse response);

	protected abstract void addErrorAttributes(ExternalCallAttributesBuilder builder, ErrorResponse response);

	private Either<ErrorResponse, SuccessfulResponse> execute(String serviceMethodName, Request request,
			HttpHeaders headers, String uri, HttpMethod method, boolean throwErrors)
	{
		ExternalCallAttributesBuilder builder =
				new ExternalCallAttributesBuilder(systemName, serviceName, serviceMethodName, method.name(),
						serviceVersion);

		Either<ErrorResponse, SuccessfulResponse> responseEither = null;
		try
		{
			responseEither = performCall(request, headers, uri, method, builder, responseEither);

			if (isResponseAnError(responseEither))
			{
				addErrorAttributes(builder, responseEither.left().get());
				builder.setSuccess(false);

				handleError(throwErrors, builder, responseEither);
			}
			else
			{
				addSuccessAttributes(builder, responseEither.right().get());
				builder.setSuccess(true);
			}
		}
		catch (ExternalException externalException)
		{
			throw externalException;
		}
		catch (HttpStatusCodeException httpStatusCodeException)
		{
			handleStatusCodeException(builder, httpStatusCodeException);

		}
		catch (RuntimeException exception)
		{
			handleUnknownException(builder, exception);
		}
		finally
		{
			builder.buildAndCommit();
		}

		return responseEither;
	}

	private Either<ErrorResponse, SuccessfulResponse> performCall(Request request, HttpHeaders headers, String uri,
			HttpMethod method, ExternalCallAttributesBuilder builder,
			Either<ErrorResponse, SuccessfulResponse> responseEither)
	{
		RequestEntity<Request> requestEntity = new RequestEntity<>(request, headers, method, URI.create(uri));

		ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);


		builder.setHttpStatusCode(responseEntity.getStatusCode().value());

		responseEither = buildEitherFromResponse(responseEntity);
		return responseEither;
	}

	private boolean isResponseAnError(Either<ErrorResponse, SuccessfulResponse> responseEither)
	{
		return responseEither.isLeft();
	}

	private void handleError(boolean throwErrors, ExternalCallAttributesBuilder builder,
			Either<ErrorResponse, SuccessfulResponse> responseEither)
	{
		if (throwErrors)
		{
			ExternalException externalException = convertErrorIntoException(responseEither.left().get());
			builder.setThrowable(externalException);
			throw externalException;
		}
	}

	private void handleUnknownException(ExternalCallAttributesBuilder builder, RuntimeException exception)
	{
		logger.error("Exception occurred executing Rest request", exception);
		builder.setThrowable(exception).setSuccess(false);

		throw exception;

	}

	private void handleStatusCodeException(ExternalCallAttributesBuilder builder,
			HttpStatusCodeException httpStatusCodeException)
	{
		logger.error("HttpStatusCodeException occurred executing Rest request", httpStatusCodeException);
		builder.setHttpStatusCodeException(httpStatusCodeException).setSuccess(false);

		throw httpStatusCodeException;
	}

	private Either<ErrorResponse, SuccessfulResponse> buildEitherFromResponse(ResponseEntity<String> responseEntity)
	{
		Either<ErrorResponse, SuccessfulResponse> eitherErrorOrSecret;

		if (!responseEntity.getStatusCode().is1xxInformational() && !responseEntity.getStatusCode().is2xxSuccessful())
		{
			return Either.left(gson.fromJson(responseEntity.getBody(), errorClass));
		}

		return Either.right(gson.fromJson(responseEntity.getBody(), successClass));
	}


}
