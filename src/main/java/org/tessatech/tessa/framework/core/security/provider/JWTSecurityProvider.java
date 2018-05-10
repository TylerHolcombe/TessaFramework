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

package org.tessatech.tessa.framework.core.security.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.core.exception.logic.InvalidAuthenticationException;
import org.tessatech.tessa.framework.core.exception.system.InternalException;
import org.tessatech.tessa.framework.core.security.client.container.Secret;
import org.tessatech.tessa.framework.core.security.context.AuthenticationType;
import org.tessatech.tessa.framework.core.security.context.SecurityContext;
import org.tessatech.tessa.framework.core.security.context.SecurityContextHolder;
import org.tessatech.tessa.framework.core.security.utils.SecurityUtils;
import org.tessatech.tessa.framework.core.util.validation.InternalValidationUtils;
import org.tessatech.tessa.framework.rest.request.TessaHttpHeaders;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Component
public class JWTSecurityProvider implements SecurityProvider
{
	private static final Logger logger = LogManager.getLogger(JWTSecurityProvider.class);

	private static final String authenticationScheme = "Bearer";

	@Value("${security.jwt.issuer}")
	private String jwtIssuer = null;

	@Value("${security.jwt.leeway:0}")
	private long jwtLeeway = 0;

	private Algorithm currentAlgorithm;
	private JWTVerifier currentVerifier;

	private Algorithm previousAlgorithm;
	private JWTVerifier previousVerifier;

	@Override
	public void loadAndVerifySecurityDetails(String[] validRoles, RequestEntity requestEntity)
			throws InvalidAuthenticationException
	{
		Optional<String> tokenOptional = retrieveAndValidateToken(requestEntity);

		if (tokenOptional.isPresent())
		{
			verifyToken(tokenOptional.get());

			DecodedJWT decodedToken = JWT.decode(tokenOptional.get());
			String jwtId = decodedToken.getId();


			String userId = decodedToken.getClaim("userId").asString();
			String appName = decodedToken.getClaim("appName").asString();
			String userName = decodedToken.getClaim("userName").asString();
			String[] userRoles = decodedToken.getClaim("userRoles").asArray(String.class);


			SecurityContext context =
					new SecurityContext(AuthenticationType.JWT, jwtId, authenticationScheme, tokenOptional.get(),
							appName, userId, userName, userRoles);
			SecurityContextHolder.setContext(context);
		}

		if (SecurityUtils.containsNonDefaultRoles(validRoles))
		{
			SecurityUtils.validateUserHasAnyRole(validRoles);
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

	private void verifyToken(String token)
	{
		try
		{
			currentVerifier.verify(token);
		}
		catch (JWTVerificationException currentVerifierException)
		{
			//Perhaps the token is old, try again with previous verifier.

			try
			{
				previousVerifier.verify(token);
			}
			catch (JWTVerificationException previousVerifierException)
			{
				throw new InvalidAuthenticationException("Could not verify JWT token", currentVerifierException);
			}
		}
	}

	private boolean isTheOnlyRoleTheDefault(String[] validRoles)
	{
		return validRoles.length == 1 && validRoles[0] == SecurityUtils.DEFAULT_NO_AUTHORIZATION_REQUIRED;
	}

	private Algorithm getAlgorithm(String secret)
	{
		InternalValidationUtils.getInstance().isNotTrimmedEmpty("secret", secret);

		try
		{
			return Algorithm.HMAC256(secret);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new InternalException("Could not load JWT HMAC-SHA256 Algorithm", e);
		}

	}

	private JWTVerifier getVerifier(Algorithm algorithm)
	{
		InternalValidationUtils.getInstance().isNotTrimmedEmpty("jwtIssuer", jwtIssuer);

		return JWT.require(algorithm).withIssuer(jwtIssuer).acceptLeeway(jwtLeeway).build();
	}

	void updateSecret(Secret secret)
	{
		if(secret.previousSecret != null)
		{
			previousAlgorithm = getAlgorithm(secret.previousSecret);
			previousVerifier = getVerifier(previousAlgorithm);
		}

		currentAlgorithm = getAlgorithm(secret.currentSecret);
		currentVerifier = getVerifier(currentAlgorithm);
	}
}
