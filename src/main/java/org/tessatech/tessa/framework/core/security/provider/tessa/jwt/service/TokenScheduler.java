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

package org.tessatech.tessa.framework.core.security.provider.tessa.jwt.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.core.security.provider.tessa.jwt.client.TessaIAMServiceClient;
import org.tessatech.tessa.framework.core.security.provider.tessa.jwt.client.container.Secret;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@Component
public class TokenScheduler implements Runnable
{
	private static final Logger logger = LogManager.getLogger(TokenScheduler.class);

	@Autowired
	private TaskScheduler scheduler;

	@Autowired
	private TessaIAMServiceClient tessaIamServiceClient;

	@Autowired
	private TokenService securityProvider;

	@Value("${security.jwt.enabled:true}")
	private boolean isJwtEnabled = true;

	@Override
	@PostConstruct
	public void run()
	{
		if(!isJwtEnabled)
		{
			logger.warn("JWT is disabled - not rescheduling.");
			return;
		}

		Optional<Secret> optionalSecret = tessaIamServiceClient.retrieveLatestSecret();

		optionalSecret = retryIfFirstCallFailed(optionalSecret);

		if (optionalSecret.isPresent())
		{
			Secret secret = optionalSecret.get();
			securityProvider.updateSecret(secret);

			Date nextRuntime = new Date(System.currentTimeMillis() + secret.expires);
			scheduler.schedule(this, nextRuntime);
		}
		else
		{
			Date nextRuntime = new Date(System.currentTimeMillis() + (30 * 1000));
			scheduler.schedule(this, nextRuntime);
		}
	}

	private Optional<Secret> retryIfFirstCallFailed(Optional<Secret> optionalSecret)
	{
		if(!optionalSecret.isPresent())
		{
			optionalSecret = tessaIamServiceClient.retrieveLatestSecret();
		}
		return optionalSecret;
	}
}
