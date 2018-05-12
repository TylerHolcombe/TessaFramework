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

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.core.exception.logic.InvalidAuthenticationException;
import org.tessatech.tessa.framework.core.exception.system.InternalException;
import org.tessatech.tessa.framework.core.logging.annotation.LogRuntime;
import org.tessatech.tessa.framework.core.security.provider.tessa.jwt.client.TessaIAMServiceClient;
import org.tessatech.tessa.framework.core.security.provider.tessa.jwt.client.container.Secret;
import org.tessatech.tessa.framework.core.security.provider.tessa.jwt.client.container.Token;
import org.tessatech.tessa.framework.core.security.provider.tessa.jwt.token.JWTToken;
import org.tessatech.tessa.framework.core.security.provider.tessa.jwt.token.JWTTokenType;
import org.tessatech.tessa.framework.core.util.validation.InternalValidationUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class TokenService
{
	@Autowired TessaIAMServiceClient iamServiceClient;

	@Value("${security.jwt.issuer}")
	private String jwtIssuer = null;

	@Value("${security.jwt.leeway:0}")
	private long jwtLeeway = 0;

	private Algorithm currentAlgorithm;
	private JWTVerifier currentVerifier;

	private Algorithm previousAlgorithm;
	private JWTVerifier previousVerifier;

	public String createIdentityToken(String userId, String appName, String userName, Date expiresAt)
	{
		return JWT.create()
				.withIssuer(jwtIssuer)
				.withIssuedAt(new Date())
				.withExpiresAt(expiresAt)
				.withClaim("type", JWTTokenType.IDENTITY.toString())
				.withClaim("userId", userId)
				.withClaim("appName", appName)
				.withClaim("username", userName)
				.sign(currentAlgorithm);
	}

	public String createAuthorizationToken(String userId, String appName, String userName, String[] userRoles,
			String[] userEvents, Date expiresAt)
	{
		return JWT.create()
				.withIssuer(jwtIssuer)
				.withIssuedAt(new Date())
				.withExpiresAt(expiresAt)
				.withClaim("type", JWTTokenType.AUTHORIZATION.toString())
				.withClaim("userId", userId)
				.withClaim("appName", appName)
				.withClaim("username", userName)
				.withArrayClaim("userRoles", userRoles)
				.withArrayClaim("userEvents", userRoles)
				.sign(currentAlgorithm);
	}

	public JWTToken decodeToken(String token)
	{
		verifyToken(token);

		DecodedJWT decodedToken = JWT.decode(token);
		String jwtId = decodedToken.getId();


		String userId = decodedToken.getClaim("userId").asString();
		String appName = decodedToken.getClaim("appName").asString();
		String username = decodedToken.getClaim("username").asString();
		String[] userRoles = decodedToken.getClaim("userRoles").asArray(String.class);
		String[] userEvents = decodedToken.getClaim("userRoles").asArray(String.class);

		if ((userRoles != null && userRoles.length > 0) || (userEvents != null && userEvents.length > 0))
		{
			return JWTToken.createAuthorizationToken(this, appName, userId, username, userRoles, userEvents, jwtId, token);
		}
		else
		{
			return JWTToken.createIdentityToken(this, appName, userId, username, jwtId, token);
		}
	}

	public void verifyToken(String token)
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

	@LogRuntime
	public JWTToken retrieveAuthorizationTokenForUserInContext()
	{
		return decodeToken(iamServiceClient.retrieveAuthenticationTokenForUserInContext());
	}

	void updateSecret(Secret secret)
	{
		if (secret.previousSecret != null)
		{
			previousAlgorithm = createAlgorithm(secret.previousSecret);
			previousVerifier = createVerifier(previousAlgorithm);
		}

		currentAlgorithm = createAlgorithm(secret.currentSecret);
		currentVerifier = createVerifier(currentAlgorithm);
	}

	private Algorithm createAlgorithm(String secret)
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

	private JWTVerifier createVerifier(Algorithm algorithm)
	{
		InternalValidationUtils.getInstance().isNotTrimmedEmpty("jwtIssuer", jwtIssuer);

		return JWT.require(algorithm).withIssuer(jwtIssuer).acceptLeeway(jwtLeeway).build();
	}

}
