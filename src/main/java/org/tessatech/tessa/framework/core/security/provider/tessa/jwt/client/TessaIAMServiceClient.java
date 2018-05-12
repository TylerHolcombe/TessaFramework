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

package org.tessatech.tessa.framework.core.security.provider.tessa.jwt.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.core.event.TessaEvent;
import org.tessatech.tessa.framework.core.security.provider.tessa.jwt.client.container.Secret;
import org.tessatech.tessa.framework.core.security.provider.tessa.jwt.client.container.Token;
import org.tessatech.tessa.framework.core.security.provider.tessa.jwt.client.utils.IAMClientUtils;
import org.tessatech.tessa.framework.core.security.utils.SecurityUtils;
import org.tessatech.tessa.framework.rest.client.TessaRestClient;

import java.util.Optional;

@Component
public class TessaIAMServiceClient
{
	private static final Logger logger = LogManager.getLogger(TessaIAMServiceClient.class);

	@Autowired
	private IAMClientUtils IAMClientUtils;

	@Autowired
	private SecurityUtils securityUtils;

	@Value("${security.tessa.iam.app.name}")
	private String appName;

	@Value("${security.tessa.iam.secret.endpoint.url}")
	private String secretEndpointUrl;

	@Value("${security.tessa.iam.token.endpoint.url}")
	private String tokenEndpointUrl;

	private TessaRestClient client = new TessaRestClient("IAM Service");

	@TessaEvent(eventName = "retrieveLatestSecret", eventGroup = "Framework")
	public Optional<Secret> retrieveLatestSecret()
	{
		try
		{
			return Optional.of(client
					.post("/secret",
							secretEndpointUrl,
							IAMClientUtils.generateSecretAuthorization(appName),
							Secret.class));
		}
		catch (Exception e)
		{
			logger.error("Error occurred retrieving secret from IAM Service.", e);
		}

		return Optional.empty();
	}

	public String retrieveAuthenticationTokenForUserInContext()
	{
		securityUtils.validateUserIsSignedIntoApp();

		Token authToken = client.get("/token/authorization", tokenEndpointUrl, Token.class);

		return authToken.token;
	}

}
