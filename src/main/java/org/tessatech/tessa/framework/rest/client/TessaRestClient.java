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


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.tessatech.tessa.framework.core.exception.system.ExternalException;
import org.tessatech.tessa.framework.core.logging.external.ExternalCallAttributesBuilder;
import org.tessatech.tessa.framework.core.security.context.SecurityContextHolder;
import org.tessatech.tessa.framework.rest.util.RestClientUtils;
import org.tessatech.tessa.framework.rest.response.TessaError;
import org.tessatech.tessa.framework.rest.response.TessaErrorResponse;

public class TessaRestClient extends AbstractRestClient<TessaErrorResponse>
{
	public TessaRestClient(String serviceName)
	{
		this(null, serviceName);
	}

	public TessaRestClient(String systemName, String serviceName)
	{
		this(systemName, serviceName, null);
	}

	public TessaRestClient(String systemName, String serviceName, String serviceVersion)
	{
		super(systemName, serviceName, serviceVersion, TessaErrorResponse.class);
	}

	public <Request, SuccessfulResponse> SuccessfulResponse post(String methodName, String url, Request request,
			Class<SuccessfulResponse> responseClass)
	{
		return super.execute(methodName, request, url, HttpMethod.POST, responseClass);
	}

	public <Request, SuccessfulResponse> SuccessfulResponse get(String methodName, String url, Request request,
			Class<SuccessfulResponse> responseClass)
	{
		return super.execute(methodName, null, url, HttpMethod.GET, responseClass);
	}

	public <Request, SuccessfulResponse> SuccessfulResponse put(String methodName, String url, Request request,
			Class<SuccessfulResponse> responseClass)
	{
		return super.execute(methodName, request, url, HttpMethod.PUT, responseClass);
	}

	public <Request, SuccessfulResponse> SuccessfulResponse patch(String methodName, String url, Request request,
			Class<SuccessfulResponse> responseClass)
	{
		return super.execute(methodName, request, url, HttpMethod.POST, responseClass);
	}

	public <Request, SuccessfulResponse> SuccessfulResponse delete(String methodName, String url, Request request,
			Class<SuccessfulResponse> responseClass)
	{
		return super.execute(methodName, request, url, HttpMethod.DELETE, responseClass);
	}

	@Override
	protected HttpHeaders buildHttpHeaders()
	{
		if (SecurityContextHolder.isPresent())
		{
			return RestClientUtils.buildHttpHeadersWithAuth();
		}

		return RestClientUtils.buildHttpHeaders();
	}

	@Override
	protected ExternalException convertErrorIntoException(TessaErrorResponse tessaErrorResponse)
	{
		TessaError error = tessaErrorResponse.getError();

		return new ExternalException(String.valueOf(error.errorCode), error.errorMessage);
	}

	@Override
	protected void addErrorAttributes(ExternalCallAttributesBuilder builder,
			TessaErrorResponse tessaErrorResponse)
	{
		TessaError error = tessaErrorResponse.getError();
		builder.setExternalResponseCode(error.errorCode);
		builder.setExternalResponseMessage(error.errorMessage);
		builder.setExternalTraceId(error.internalErrorId);
	}
}
