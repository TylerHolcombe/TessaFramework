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
import org.tessatech.tessa.framework.core.util.RestClientUtils;
import org.tessatech.tessa.framework.rest.response.TessaError;
import org.tessatech.tessa.framework.rest.response.TessaWebserviceResponse;

public class TessaRestClient<Request, SuccessfulResponse>
		extends AbstractRestClient<Request, SuccessfulResponse, TessaWebserviceResponse>
{
	public TessaRestClient(String serviceName, Class<SuccessfulResponse> successClass)
	{
		this("Tessa", serviceName, successClass);
	}

	public TessaRestClient(String systemName, String serviceName, Class<SuccessfulResponse> successClass)
	{
		this(systemName, serviceName, null, successClass);
	}

	public TessaRestClient(String systemName, String serviceName, String serviceVersion, Class<SuccessfulResponse> successClass)
	{
		super(systemName, serviceName, serviceVersion, successClass, TessaWebserviceResponse.class);
	}

	public SuccessfulResponse post(String methodName, String uri, Request request)
	{
		return super.execute(methodName, request, uri, HttpMethod.POST);
	}

	public SuccessfulResponse get(String methodName, String uri, Request request)
	{
		return super.execute(methodName, null, uri, HttpMethod.GET);
	}

	public SuccessfulResponse put(String methodName, String uri, Request request)
	{
		return super.execute(methodName, request, uri, HttpMethod.PUT);
	}

	public SuccessfulResponse patch(String methodName, String uri, Request request)
	{
		return super.execute(methodName, request, uri, HttpMethod.POST);
	}

	public SuccessfulResponse delete(String methodName, String uri, Request request)
	{
		return super.execute(methodName, request, uri, HttpMethod.DELETE);
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
	protected ExternalException convertErrorIntoException(TessaWebserviceResponse tessaWebserviceResponse)
	{
		TessaError error = tessaWebserviceResponse.getError();

		return new ExternalException(String.valueOf(error.errorCode), error.errorMessage);
	}

	@Override
	protected void addSuccessAttributes(ExternalCallAttributesBuilder builder, SuccessfulResponse response)
	{
		return;
	}

	@Override
	protected void addErrorAttributes(ExternalCallAttributesBuilder builder,
			TessaWebserviceResponse tessaWebserviceResponse)
	{
		TessaError error = tessaWebserviceResponse.getError();
		builder.setExternalResponseCode(error.errorCode);
		builder.setExternalResponseMessage(error.errorMessage);
		builder.setExternalTraceId(error.internalErrorId);
	}
}
