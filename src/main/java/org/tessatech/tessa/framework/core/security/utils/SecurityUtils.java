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

package org.tessatech.tessa.framework.core.security.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.core.exception.logic.InsufficientAuthorizationException;
import org.tessatech.tessa.framework.core.exception.system.InternalException;
import org.tessatech.tessa.framework.core.security.context.SecurityContextHolder;
import org.tessatech.tessa.framework.core.security.token.SecurityToken;
import org.tessatech.tessa.framework.core.util.validation.InternalValidationUtils;

import java.util.Arrays;
import java.util.List;

@Component
public class SecurityUtils
{
	private static final InternalValidationUtils validationUtils = InternalValidationUtils.getInstance();

	@Value("${security.tessa.iam.app.name}")
	private String appName;

	SecurityUtils()
	{

	}

	public SecurityUtils(String appName)
	{
		validationUtils.isNotTrimmedEmpty("AppName", appName);
		this.appName = appName;
	}

	public boolean isUserSameAsUserInContext(String appName, String username)
	{
		validateUserIsSignedIntoApp();
		return appName.equals(getThreadSecurityToken().getAppName())
				&& username.equals(getThreadSecurityToken().getUsername());
	}

	public boolean isUserSameAsUserInContext(String userId)
	{
		validateUserIsSignedIntoApp();
		return userId.equals(getThreadSecurityToken().getUserId());
	}

	public void validateUserIsSignedIntoApp()
	{
		validateSecurityContextHasCredentials();
		validateAppIsSameAsContext();
	}

	public void validateUserHasUserId(String userId)
	{
		validateUserIsSignedIntoApp();

		try
		{
			validationUtils.isEqualTo("UserId", userId, getThreadSecurityToken().getUserId());
		}
		catch (InternalException exception)
		{
			throw new InsufficientAuthorizationException("User is signed in as a different user.", exception);
		}
	}

	public void validateUserHasUsername(String username)
	{
		validateUserIsSignedIntoApp();

		validationUtils.isNotTrimmedEmpty("username", username);

		try
		{
			validationUtils.isEqualTo("Username", username, getThreadSecurityToken().getUsername());
		}
		catch (InternalException exception)
		{
			throw new InsufficientAuthorizationException("User is signed in as a different user.", exception);
		}
	}

	public void validateUserHasRole(String roleName)
	{
		validateUserIsSignedIntoApp();

		validateUserHasAnyRole(new String[]{roleName});
	}

	public void validateUserHasAnyRole(String[] roles)
	{
		validateUserHasAnyRole(Arrays.asList(roles));
	}

	public void validateUserHasAnyRole(List<String> roles)
	{
		validateUserIsSignedIntoApp();

		try
		{
			validationUtils.isAnyValueInCollection("Roles", Arrays.asList(getThreadSecurityToken().getRoles()), roles);
		}
		catch (InternalException exception)
		{
			throw new InsufficientAuthorizationException("User is not authorized for any of the necessary roles.",
					exception);
		}
	}


	public void validateUserHasAllRoles(String[] validRoles)
	{
		validateUserHasAllRoles(Arrays.asList(validRoles));
	}

	public void validateUserHasAllRoles(List<String> validRoles)
	{
		validateUserIsSignedIntoApp();

		try
		{
			validationUtils.areAllValuesInCollection("Roles",
					Arrays.asList(getThreadSecurityToken().getRoles()),
					validRoles);
		}
		catch (InternalException exception)
		{
			throw new InsufficientAuthorizationException("User is not authorized for all of the necessary roles.",
					exception);
		}
	}

	public void validateUserHasEvent(String eventName)
	{
		validateUserIsSignedIntoApp();

		validateUserHasAnyEvent(new String[]{eventName});
	}

	public void validateUserHasAnyEvent(String[] events)
	{
		validateUserHasAnyEvent(Arrays.asList(events));
	}

	public void validateUserHasAnyEvent(List<String> events)
	{
		validateUserIsSignedIntoApp();

		try
		{
			validationUtils.isAnyValueInCollection("Roles",
					Arrays.asList(getThreadSecurityToken().getEvents()),
					events);
		}
		catch (InternalException exception)
		{
			throw new InsufficientAuthorizationException("User is not authorized for any of the necessary events.",
					exception);
		}
	}


	public void validateUserHasAllEvents(String[] validEvents)
	{
		validateUserHasAllEvents(Arrays.asList(validEvents));
	}

	public void validateUserHasAllEvents(List<String> validEvents)
	{
		validateUserIsSignedIntoApp();

		try
		{
			validationUtils.areAllValuesInCollection("Roles",
					Arrays.asList(getThreadSecurityToken().getEvents()),
					validEvents);
		}
		catch (InternalException exception)
		{
			throw new InsufficientAuthorizationException("User is not authorized for all of the necessary events.",
					exception);
		}
	}

	private void validateSecurityContextHasCredentials()
	{
		if (!SecurityContextHolder.getContextOptional().isPresent())
		{
			throw new InsufficientAuthorizationException("User is not signed in.");
		}
	}

	private void validateAppIsSameAsContext()
	{
		try
		{
			validationUtils.isEqualTo("AppName", appName, getThreadSecurityToken().getAppName());
		}
		catch (InternalException exception)
		{
			throw new InsufficientAuthorizationException("User is not signed into this application.", exception);
		}
	}

	private SecurityToken getThreadSecurityToken()
	{
		return SecurityContextHolder.getContext().getSecurityToken();
	}
}
