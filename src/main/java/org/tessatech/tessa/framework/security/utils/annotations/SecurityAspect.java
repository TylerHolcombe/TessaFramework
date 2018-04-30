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
