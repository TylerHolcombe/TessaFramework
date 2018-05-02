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

package org.tessatech.tessa.framework.core.security.context;

import org.tessatech.tessa.framework.core.exception.system.InternalException;
import org.tessatech.tessa.framework.core.exception.system.InternalException;

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
