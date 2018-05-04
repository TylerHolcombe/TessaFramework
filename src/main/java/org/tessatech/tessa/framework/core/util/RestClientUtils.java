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

package org.tessatech.tessa.framework.core.util;

import com.google.gson.Gson;
import io.atlassian.fugue.Either;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.tessatech.tessa.framework.core.event.context.EventContext;
import org.tessatech.tessa.framework.core.event.context.EventContextHolder;
import org.tessatech.tessa.framework.core.logging.external.ExternalCallAttributesBuilder;
import org.tessatech.tessa.framework.core.security.context.SecurityContext;
import org.tessatech.tessa.framework.core.security.context.SecurityContextHolder;
import org.tessatech.tessa.framework.core.transaction.context.TransactionContext;
import org.tessatech.tessa.framework.core.transaction.context.TransactionContextHolder;
import org.tessatech.tessa.framework.rest.request.TessaHttpHeaders;
import org.tessatech.tessa.framework.rest.response.TessaError;
import org.tessatech.tessa.framework.rest.response.TessaWebserviceResponse;

import java.util.Optional;

public class RestClientUtils
{
	private static final Gson gson = new Gson();

	public static TessaHttpHeaders buildTessaHttpHeaders()
	{
		TessaHttpHeaders headers = new TessaHttpHeaders();
		EventContextHolder.getContextOptional().ifPresent(context -> populateTessaHeaders(headers, context));
		TransactionContextHolder.getContextOptional().ifPresent(context -> populateTessaHeaders(headers, context));
		return headers;
	}

	public static HttpHeaders buildHttpHeaders()
	{
		return buildTessaHttpHeaders().getHeaders();
	}

	public static TessaHttpHeaders buildTessaHttpHeadersWithAuth()
	{
		TessaHttpHeaders headers = buildTessaHttpHeaders();
		addClientAuthToHeaders(headers);
		return headers;
	}

	public static HttpHeaders buildHttpHeadersWithAuth()
	{
		return buildTessaHttpHeadersWithAuth().getHeaders();
	}

	public static TessaHttpHeaders addClientAuthToHeaders(TessaHttpHeaders headers)
	{
		SecurityContext context = SecurityContextHolder.getContext();
		String token = context.getAuthenticationScheme() + " " + context.getAuthenticationToken();
		headers.setAuthorization(token);
		return  headers;
	}

	public static TessaHttpHeaders populateTessaHeaders(TessaHttpHeaders headers, TransactionContext context)
	{
		headers.setRequestId(context.getRequestId());
		headers.setCorrelationId(context.getCorrelationId());
		headers.setSessionId(context.getSessionId());
		headers.setClientIp(context.getClientIp());
		headers.setDeviceId(context.getDeviceId());
		headers.setDeviceType(context.getDeviceType());
		return headers;
	}

	public static TessaHttpHeaders populateTessaHeaders(TessaHttpHeaders headers, EventContext context)
	{
		headers.setRequestId(String.valueOf(context.getInternalTraceId()));
		headers.setCorrelationId(String.valueOf(context.getInternalTraceId()));
		return headers;
	}


	private static <T> Either<TessaWebserviceResponse, T> parseTessaRestResponse(ResponseEntity<String> response, Class<T> successClass)
	{
		if (!response.getStatusCode().is1xxInformational() && !response.getStatusCode().is2xxSuccessful())
		{
			return Either.left(gson.fromJson(response.getBody(), TessaWebserviceResponse.class));
		}

		return Either.right(gson.fromJson(response.getBody(), successClass));

	}

	public static <T> Optional<T> parseAndProcessTessaRestResponse(ResponseEntity<String> response, ExternalCallAttributesBuilder attributes, Class<T> successClass)
	{
		attributes.setHttpStatusCode(response.getStatusCode().value());

		Either<TessaWebserviceResponse, T> eitherErrorOrSecret = parseTessaRestResponse(response, successClass);

		if (eitherErrorOrSecret.isLeft())
		{
			TessaError error = eitherErrorOrSecret.left().get().getError();
			attributes.setExternalResponseCode(error.errorCode);
			attributes.setExternalResponseMessage(error.errorMessage);
			attributes.setExternalTraceId(error.internalErrorId);
			attributes.buildAndCommit(false);
			return Optional.empty();
		}

		attributes.buildAndCommit(true);
		return Optional.ofNullable(eitherErrorOrSecret.right().get());

	}

}
