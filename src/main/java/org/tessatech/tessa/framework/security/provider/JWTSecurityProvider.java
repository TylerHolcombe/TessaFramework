package org.tessatech.tessa.framework.security.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.exception.system.InternalException;
import org.tessatech.tessa.framework.exception.logic.InvalidAuthenticationException;
import org.tessatech.tessa.framework.security.context.SecurityContext;
import org.tessatech.tessa.framework.security.context.SecurityContextHolder;
import org.tessatech.tessa.framework.security.utils.SecurityUtils;
import org.tessatech.tessa.framework.util.validation.InternalValidationUtils;

import java.io.UnsupportedEncodingException;

@Component
public class JWTSecurityProvider implements SecurityProvider
{
	private static final Logger logger = LogManager.getLogger(JWTSecurityProvider.class);

	public static final String JWT_TOKEN_HEADER_NAME = "Jwt-Token";

	@Value("${jwt.issuer}")
	private String jwtIssuer = null;

	@Value("${jwt.secret}")
	private String jwtSecret = null;

	@Value("${jwt.leeway:0}")
	private long jwtLeeway = 0;

	private Algorithm algorithmHS;

	@Override
	public void loadAndVerifySecurityDetails(String[] validRoles, RequestEntity requestEntity) throws InvalidAuthenticationException
	{
		String token  = requestEntity.getHeaders().getFirst(JWT_TOKEN_HEADER_NAME);

		if(isTokenPresent(token))
		{
			getVerifier().verify(token);

			DecodedJWT decodedToken = JWT.decode(token);
			String jwtId = decodedToken.getId();

			String userId = decodedToken.getClaim("userId").asString();
			String userName = decodedToken.getClaim("userName").asString();
			String[] userRoles = decodedToken.getClaim("userRoles").asArray(String.class);


			SecurityContext context = new SecurityContext("jwtToken", jwtId, userId, userName, userRoles);
			SecurityContextHolder.setContext(context);
		}

		if(SecurityUtils.containsNonDefaultRoles(validRoles))
		{
			SecurityUtils.validateUserHasAnyRole(validRoles);
		}
	}

	private boolean isTheOnlyRoleTheDefault(String[] validRoles)
	{
		return validRoles.length == 1 && validRoles[0] == SecurityUtils.DEFAULT_NO_AUTHORIZATION_REQUIRED;
	}


	private boolean isTokenPresent(String token)
	{
		return token != null && !token.trim().isEmpty();
	}

	private Algorithm getAlgorithm()
	{
		if (algorithmHS == null)
		{
			InternalValidationUtils.getInstance().isNotTrimmedEmpty("jwtSecret", jwtSecret);

			try
			{
				algorithmHS = Algorithm.HMAC256(jwtSecret);
			}
			catch (UnsupportedEncodingException e)
			{
				throw new InternalException("Could not load JWT HMAC-SHA256 Algorithm", e);
			}
		}

		return algorithmHS;
	}

	private JWTVerifier getVerifier()
	{
		InternalValidationUtils.getInstance().isNotTrimmedEmpty("jwtIssuer", jwtIssuer);

		return JWT.require(getAlgorithm())
				.withIssuer(jwtIssuer)
				.acceptLeeway(jwtLeeway)
				.build();
	}
}
