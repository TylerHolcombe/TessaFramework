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

package org.tessatech.tessa.framework.core.exception.details;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThrowableAdapterFinder
{

	private static final Map<String, ThrowableAdapter> exceptionsToAdapters = new ConcurrentHashMap<>();

	private static ThrowableAdapter fallbackThrowableAdapter = new FallbackThrowableAdapter();

	private ThrowableAdapterFinder()
	{

	}

	public static void registerExceptions(ThrowableAdapter adapter, Class... classes)
	{
		for (Class exception : classes)
		{
			exceptionsToAdapters.put(exception.getCanonicalName(), adapter);
		}
	}

	public static void registerDefaultDetails(ThrowableAdapter adapter, Class... classes)
	{
		for (Class exception : classes)
		{
			String exceptionClassName = exception.getCanonicalName();

			if(!exceptionsToAdapters.containsKey(exceptionClassName))
			{
				exceptionsToAdapters.put(exceptionClassName, adapter);
			}
		}
	}

	public static void registerFallbackExceptionDetails(ThrowableAdapter adapter)
	{
		fallbackThrowableAdapter = adapter;
	}

	public static ThrowableAdapter getThrowableAdapter(Throwable throwable)
	{
		Class clazz = throwable.getClass();

		if(exceptionsToAdapters.containsKey(clazz.getCanonicalName()))
		{
			return exceptionsToAdapters.get(clazz.getCanonicalName());
		}
		else
		{
			ThrowableAdapter adapter = null;
			clazz = clazz.getSuperclass();

			while (clazz != null)
			{
				if (exceptionsToAdapters.containsKey(clazz.getCanonicalName()))
				{
					adapter = exceptionsToAdapters.get(clazz.getCanonicalName());
					break;
				}
			}

			if(adapter == null)
			{
				adapter = fallbackThrowableAdapter;
			}

			// Register details so we do not need to look them up again
			registerExceptions(adapter, throwable.getClass());

			return adapter;
		}
	}
}
