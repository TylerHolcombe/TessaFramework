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

package org.tessatech.tessa.framework.core.security.provider.tessa.jwt;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.core.exception.logic.InvalidAuthenticationException;
import org.tessatech.tessa.framework.core.security.context.SecurityContext;
import org.tessatech.tessa.framework.core.security.context.SecurityContextHolder;
import org.tessatech.tessa.framework.core.security.provider.SecurityProvider;
import org.tessatech.tessa.framework.core.security.provider.tessa.jwt.service.TokenService;
import org.tessatech.tessa.framework.core.security.provider.tessa.jwt.token.JWTToken;
import org.tessatech.tessa.framework.core.security.utils.SecurityUtils;
import org.tessatech.tessa.framework.rest.request.TessaHttpHeaders;

import java.util.Optional;

@Component
public class TessaJWTSecurityProvider implements SecurityProvider
{
	private static final Logger logger = LogManager.getLogger(TessaJWTSecurityProvider.class);

	private static final String authenticationScheme = "Bearer";

	@Autowired
	private TokenService tokenService;

	@Override
	public void loadAndVerifySecurityDetails(RequestEntity requestEntity)
			throws InvalidAuthenticationException
	{
		Optional<String> rawTokenOptional = retrieveAndValidateToken(requestEntity);

		if (rawTokenOptional.isPresent())
		{
			JWTToken token = tokenService.decodeToken(rawTokenOptional.get());

			SecurityContext context = new SecurityContext(token);
			SecurityContextHolder.setContext(context);
		}
	}

	private Optional<String> retrieveAndValidateToken(RequestEntity requestEntity)
	{
		if (requestEntity == null || requestEntity.getHeaders() == null)
		{
			return Optional.empty();
		}

		TessaHttpHeaders headers = new TessaHttpHeaders(requestEntity.getHeaders());

		String auth = headers.getAuthorization();
		if (auth == null)
		{
			return Optional.empty();
		}

		String[] authSplit = auth.split(" ");

		if (authSplit.length != 2)
		{
			return Optional.empty();
		}

		String scheme = authSplit[0];
		String token = authSplit[1];

		if (authenticationScheme.equalsIgnoreCase(scheme))
		{
			if (StringUtils.isNotBlank(token))
			{
				return Optional.of(token);
			}
		}

		return Optional.empty();
	}
}
