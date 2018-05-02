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

import org.tessatech.tessa.framework.core.exception.logic.InsufficientAuthorizationException;
import org.tessatech.tessa.framework.core.security.context.SecurityContextHolder;
import org.tessatech.tessa.framework.core.util.validation.InternalValidationUtils;

import java.util.Arrays;
import java.util.List;

public class SecurityUtils
{
	public static final String DEFAULT_NO_AUTHORIZATION_REQUIRED = "DEFAULT_NO_AUTH_REQUIRED";

	public static void validateUserIsSignedIn()
	{
		if (!SecurityContextHolder.getContextOptional().isPresent())
		{
			throw new InsufficientAuthorizationException("User is not signed in.");
		}
	}

	public static void validateUserHasRole(String roleName)
	{
		validateUserIsSignedIn();

		validateUserHasRoleInternal(roleName);
	}

	public static void validateUserHasAllRoles(String[] validRoles)
	{
		validateUserHasAllRoles(Arrays.asList(validRoles));
	}

	public static void validateUserHasAllRoles(List<String> validRoles)
	{
		validateUserIsSignedIn();

		for (String role : validRoles)
		{
			validateUserHasRoleInternal(role);
		}
	}

	public static void validateUserHasAnyRole(String[] roles)
	{
		validateUserHasAnyRole(Arrays.asList(roles));
	}

	public static void validateUserHasAnyRole(List<String> roles)
	{
		validateUserIsSignedIn();

		InternalValidationUtils.getInstance().areTestValuesValid(roles);

		String[] userRoles = SecurityContextHolder.getContextOptional().get().getUserRoles();

		InternalValidationUtils.getInstance().areTestValuesValid(Arrays.asList(userRoles));

		for (String userRole : userRoles)
		{
			if (roles.contains(userRole))
			{
				return;
			}
		}

		throw new InsufficientAuthorizationException("User is not authorized for any of the valid roles.");
	}

	private static void validateUserHasRoleInternal(String role)
	{
		String[] userRoles = SecurityContextHolder.getContextOptional().get().getUserRoles();

		try
		{
			InternalValidationUtils.getInstance().isInArray("RoleName", role, userRoles);
		}
		catch (InternalError e)
		{
			throw new InsufficientAuthorizationException("User is not authorized for role: " + role, e);
		}
	}

	public static boolean containsOnlyTheDefaultRole(String[] roles)
	{
		return roles != null && roles.length == 1 && DEFAULT_NO_AUTHORIZATION_REQUIRED.equals(roles[0]);
	}

	public static boolean containsNonDefaultRoles(String[] roles)
	{
		return roles != null && roles.length > 0 && !containsOnlyTheDefaultRole(roles);
	}

	public static List<String> removeDefaultRole(String[] roles)
	{
		return removeDefaultRole(Arrays.asList(roles));
	}

	public static List<String> removeDefaultRole(List<String> roles)
	{
		roles.remove(DEFAULT_NO_AUTHORIZATION_REQUIRED);
		return roles;
	}
}
