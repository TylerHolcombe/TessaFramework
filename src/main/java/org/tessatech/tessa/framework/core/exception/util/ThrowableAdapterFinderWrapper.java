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

package org.tessatech.tessa.framework.core.exception.util;

import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.core.exception.adapter.ThrowableAdapter;
import org.tessatech.tessa.framework.core.exception.adapter.ThrowableAdapterFinder;
import org.tessatech.tessa.framework.core.logging.annotation.LogRuntime;

/**
 * Provides a Spring-Managed wrapper around ThrowableAdapterFinder.
 */
@Component
public class ThrowableAdapterFinderWrapper
{

	public void registerAdapter(ThrowableAdapter adapter, Class... classes)
	{
		ThrowableAdapterFinder.registerAdapter(adapter, classes);
	}

	public void registerDefaultAdapter(ThrowableAdapter adapter, Class... classes)
	{
		ThrowableAdapterFinder.registerDefaultAdapter(adapter, classes);
	}

	public void registerFallbackAdapter(ThrowableAdapter adapter)
	{
		ThrowableAdapterFinder.registerFallbackAdapter(adapter);
	}

	@LogRuntime(debugOnly = false)
	public ThrowableAdapter getThrowableAdapter(Throwable throwable)
	{
		return ThrowableAdapterFinder.getThrowableAdapter(throwable);
	}
}
