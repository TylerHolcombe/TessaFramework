package org.tessatech.tessa.framework.security.utils.annotations;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.tessatech.tessa.framework.security.utils.SecurityUtils;

public class SecurityAspect
{

	@Pointcut("execution(public * *(..))")
	void anyPublicMethod()
	{
	}

	@Around("anyPublicMethod() && @annotation(hasAllRoles)")
	public Object hasAllRoles(ProceedingJoinPoint proceedingJoinPoint, HasAllRoles hasAllRoles) throws Throwable
	{
		SecurityUtils.validateUserHasAllRoles(hasAllRoles.authorizedRoles());

		return proceedingJoinPoint.proceed();
	}

	@Around("anyPublicMethod() && @annotation(hasRole)")
	public Object hasRole(ProceedingJoinPoint proceedingJoinPoint, hasRole hasRole) throws Throwable
	{
		SecurityUtils.validateUserHasRole(hasRole.authorizedRole());

		return proceedingJoinPoint.proceed();
	}

	@Around("anyPublicMethod() && @annotation(hasAnyRole)")
	public Object hasAnyRole(ProceedingJoinPoint proceedingJoinPoint, HasAnyRole hasAnyRole) throws Throwable
	{
		SecurityUtils.validateUserHasAnyRole(hasAnyRole.authorizedRoles());

		return proceedingJoinPoint.proceed();
	}
}
