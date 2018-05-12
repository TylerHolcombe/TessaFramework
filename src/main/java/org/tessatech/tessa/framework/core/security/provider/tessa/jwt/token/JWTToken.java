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

package org.tessatech.tessa.framework.core.security.provider.tessa.jwt.token;

import org.tessatech.tessa.framework.core.exception.system.InternalException;
import org.tessatech.tessa.framework.core.logging.context.LoggingContextHolder;
import org.tessatech.tessa.framework.core.security.context.SecurityContextHolder;
import org.tessatech.tessa.framework.core.security.provider.tessa.jwt.service.TokenService;
import org.tessatech.tessa.framework.core.security.token.SecurityToken;

import javax.transaction.Transactional;

public class JWTToken implements SecurityToken
{
	private TokenService tokenService;

	private JWTTokenType tokenType;
	private String appName;
	private String userId;
	private String username;
	private String[] roles;
	private String[] events;

	private String tokenId;
	private String rawTokenText;

	public JWTToken(TokenService tokenService, String appName, String userId, String username,
			String tokenId, String rawTokenText)
	{
		this.tokenType = JWTTokenType.IDENTITY;
		this.tokenService = tokenService;
		this.appName = appName;
		this.userId = userId;
		this.username = username;
		this.tokenId = tokenId;
		this.rawTokenText = rawTokenText;
	}

	public JWTToken(TokenService tokenService, String appName, String userId, String username,
			String[] roles, String[] events, String tokenId, String rawTokenText)
	{
		this.tokenType = JWTTokenType.AUTHORIZATION;
		this.tokenService = tokenService;
		this.appName = appName;
		this.userId = userId;
		this.username = username;
		this.roles = roles;
		this.events = events;
		this.tokenId = tokenId;
		this.rawTokenText = rawTokenText;
	}

	public static JWTToken createIdentityToken(TokenService tokenService, String appName, String userId,
			String username, String tokenId, String rawTokenText)
	{
		return new JWTToken(tokenService, appName, userId, username, tokenId, rawTokenText);
	}

	public static JWTToken createAuthorizationToken(TokenService tokenService, String appName, String userId,
			String username, String[] roles,
			String[] events, String tokenId, String rawTokenText)
	{
		return new JWTToken(tokenService, appName, userId, username, roles, events, tokenId, rawTokenText);
	}

	public static JWTToken createToken(TokenService tokenService, JWTTokenType tokenType, String appName, String
			userId,
			String username,
			String[] roles, String[] events, String tokenId, String rawTokenText)
	{
		if (JWTTokenType.AUTHORIZATION.equals(tokenType))
		{
			return createAuthorizationToken(tokenService,
					appName,
					userId,
					username,
					roles,
					events,
					tokenId,
					rawTokenText);
		}
		else if (JWTTokenType.IDENTITY.equals(tokenType))
		{
			return createIdentityToken(tokenService, appName, userId, username, tokenId, rawTokenText);
		}

		throw new InternalException("Cannot create a JWTTokenType of type: " + tokenType);
	}


	public JWTTokenType getTokenType()
	{
		return tokenType;
	}

	public boolean isIdentityToken()
	{
		return JWTTokenType.IDENTITY.equals(tokenType);
	}

	public boolean isAuthorizationToken()
	{
		return JWTTokenType.AUTHORIZATION.equals(tokenType);
	}


	@Override public String getTokenId()
	{
		return tokenId;
	}

	@Override public String getRawToken()
	{
		return rawTokenText;
	}

	@Override public String getAppName()
	{
		return appName;
	}

	@Override public String getUserId()
	{
		return userId;
	}

	@Override public String getUsername()
	{
		return username;
	}

	@Override public String[] getRoles()
	{
		convertToAuthToken();
		return roles;
	}

	@Override public String[] getEvents()
	{
		convertToAuthToken();
		return events;
	}
	
	private void convertToAuthToken()
	{
		if (!JWTTokenType.AUTHORIZATION.equals(this.tokenType))
		{
			if (SecurityContextHolder.getContext().getSecurityToken().equals(this))
			{
				LoggingContextHolder
						.getContextOptional()
						.ifPresent(loggingContext -> loggingContext.addEvent("Converting to Auth Token"));

				JWTToken token = tokenService.retrieveAuthorizationTokenForUserInContext();
				this.tokenType = JWTTokenType.AUTHORIZATION;
				this.appName = token.appName;
				this.userId = token.userId;
				this.username = token.username;
				this.roles = token.roles;
				this.events = token.events;
				this.tokenId = token.tokenId;
				this.rawTokenText = token.rawTokenText;
			}

			throw new InternalException(
					"Cannot retrieve Authorization Token for a different user than the user in context.");
		}
	}
}
