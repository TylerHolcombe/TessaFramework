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

package org.tessatech.tessa.framework.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.core.exception.adapter.ThrowableAdapter;
import org.tessatech.tessa.framework.core.exception.logic.InvalidRequestException;
import org.tessatech.tessa.framework.core.exception.system.InternalException;
import org.tessatech.tessa.framework.core.transaction.TessaTransaction;
import org.tessatech.tessa.framework.core.transaction.context.TransactionContext;
import org.tessatech.tessa.framework.core.transaction.context.TransactionContextHolder;
import org.tessatech.tessa.framework.core.util.UniqueIdentifierUtils;
import org.tessatech.tessa.framework.rest.exception.adapter.RestThrowableAdapter;
import org.tessatech.tessa.framework.rest.request.TessaHttpHeaders;
import org.tessatech.tessa.framework.rest.response.DefaultTessaWebserviceResponse;
import org.tessatech.tessa.framework.rest.response.TessaError;
import org.tessatech.tessa.framework.rest.response.TessaWebserviceResponse;

@Component
public class RestServiceMapper
{

	public TransactionContext mapInboundRequestToContext(TessaTransaction transaction, RequestEntity requestEntity)
	{
		if (transaction == null)
		{
			throw new InternalException("Cannot map a null TessaTransaction annotation into a context.");
		}

		if (requestEntity == null)
		{
			throw new InternalException("Cannot map a null request into a context.");
		}

		if (requestEntity.getHeaders() == null)
		{
			throw new InvalidRequestException("Cannot map null headers into a transaction");
		}

		TessaHttpHeaders headers = new TessaHttpHeaders(requestEntity.getHeaders());

		long internalTraceId = UniqueIdentifierUtils.getUniqueId();

		return new TransactionContext(transaction.serviceName(), transaction.serviceOperation(), transaction.serviceVersion(),
				transaction.serviceMethodName(), headers.getRequestId(), headers.getCorrelationId(), headers.getSessionId(),
				internalTraceId, headers.getDeviceId(),
				headers.getDeviceType());

	}

	public ResponseEntity<TessaWebserviceResponse> mapTessaExceptionResponse(ThrowableAdapter details, Throwable throwable)
	{
		long internalTraceId = 0;

		if (TransactionContextHolder.getContextOptional().isPresent())
		{
			internalTraceId = TransactionContextHolder.getContextOptional().get().getInternalTraceId();
		}

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		if (details instanceof RestThrowableAdapter)
		{
			status = ((RestThrowableAdapter) details).getHttpStatus();
		}

		TessaError tessaError = new TessaError(String.valueOf(status.value()), details.getExceptionCode(throwable),
				details.getExceptionMessage(throwable), internalTraceId);

		TessaWebserviceResponse response = new DefaultTessaWebserviceResponse(tessaError);


		return new ResponseEntity<TessaWebserviceResponse>(response, status);
	}
}
