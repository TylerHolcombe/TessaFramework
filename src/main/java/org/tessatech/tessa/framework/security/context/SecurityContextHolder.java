package org.tessatech.tessa.framework.security.context;

import org.tessatech.tessa.framework.exception.system.InternalException;

import java.util.Optional;

public class SecurityContextHolder
{
	private static final ThreadLocal<SecurityContext> securityContextLocal = new ThreadLocal<>();

	public static void setContext(SecurityContext context)
	{
		securityContextLocal.set(context);
	}

	public static void clearContext()
	{
		securityContextLocal.set(null);
		securityContextLocal.remove();
	}

	public static SecurityContext getContext()
	{
		SecurityContext context = securityContextLocal.get();

		if(context == null)
		{
			throw new InternalException("SecurityContext was requested but is Null");
		}

		return context;
	}

	public static Optional<SecurityContext> getContextOptional()
	{
		return Optional.ofNullable(securityContextLocal.get());
	}

}
