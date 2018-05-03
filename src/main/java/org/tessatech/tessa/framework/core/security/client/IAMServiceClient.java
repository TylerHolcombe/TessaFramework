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

package org.tessatech.tessa.framework.core.security.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.tessatech.tessa.framework.core.event.TessaEvent;
import org.tessatech.tessa.framework.core.event.context.EventContextHolder;
import org.tessatech.tessa.framework.core.logging.context.ExternalCallAttributesBuilder;
import org.tessatech.tessa.framework.core.security.client.container.Secret;
import org.tessatech.tessa.framework.core.security.client.container.SecretRequest;
import org.tessatech.tessa.framework.core.security.client.utils.SecretRequestUtils;
import org.tessatech.tessa.framework.core.util.RestClientUtils;
import org.tessatech.tessa.framework.rest.request.TessaHttpHeaders;

import java.net.URI;
import java.util.Optional;

@Component
public class IAMServiceClient
{
	private static final Logger logger = LogManager.getLogger(IAMServiceClient.class);

	@Autowired
	private SecretRequestUtils secretRequestUtils;

	@Value("${security.tessa.iam.app.name}")
	private String appName;

	@Value("${security.tessa.iam.endpoint.url}")
	private String endpointUrl;

	private  RestTemplate restTemplate = buildRestTemplate();

	@TessaEvent(eventName = "retrieveLatestSecret", eventGroup = "iam")
	public Optional<Secret> retrieveLatestSecret()
	{
		ExternalCallAttributesBuilder attributes = new ExternalCallAttributesBuilder("TessaTech LLC","IAM", "/secret", "POST" );
		try
		{
			TessaHttpHeaders headers = new TessaHttpHeaders(EventContextHolder.getContextOptional());
			SecretRequest request = secretRequestUtils.generateSecretRequest(appName);

			RequestEntity<SecretRequest> requestEntity = new RequestEntity<>(request, headers.getHeaders(), HttpMethod.POST, URI.create(endpointUrl));

			ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

			return  RestClientUtils.parseAndProcessTessaRestResponse(response, attributes, Secret.class);
		}
		catch (Exception e)
		{
			logger.error("Error occurred retrieving secret from IAM Service.", e);
			attributes.setThrowable(e).buildAndCommit(false);
		}

		return Optional.empty();
	}

	private RestTemplate buildRestTemplate()
	{
		return new RestTemplateBuilder()
				.setConnectTimeout(15000)
				.setReadTimeout(15000)
				.build();
	}
}
