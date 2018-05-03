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
import org.springframework.http.ResponseEntity;
import org.tessatech.tessa.framework.core.logging.context.ExternalCallAttributesBuilder;
import org.tessatech.tessa.framework.rest.response.TessaError;
import org.tessatech.tessa.framework.rest.response.TessaWebserviceResponse;

import java.util.Optional;

public class RestClientUtils
{
	private static final Gson gson = new Gson();

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
