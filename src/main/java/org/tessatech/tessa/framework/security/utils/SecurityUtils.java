package org.tessatech.tessa.framework.security.utils;

import org.tessatech.tessa.framework.exception.logic.InsufficientAuthorizationException;
import org.tessatech.tessa.framework.security.context.SecurityContextHolder;
import org.tessatech.tessa.framework.util.validation.InternalValidationUtils;

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
