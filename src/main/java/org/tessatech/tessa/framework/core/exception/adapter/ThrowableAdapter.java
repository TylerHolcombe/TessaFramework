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

package org.tessatech.tessa.framework.core.exception.adapter;

public abstract class ThrowableAdapter
{

	public ThrowableAdapter(boolean isDefault, Class... exceptionClasses)
	{
		if(isDefault)
		{
			ThrowableAdapterFinder.registerDefaultAdapter(this, exceptionClasses);
		}
		else
		{
			ThrowableAdapterFinder.registerAdapter(this, exceptionClasses);;
		}
	}

	public abstract long getExceptionCode(Throwable throwable);
	public abstract String getExceptionMessage(Throwable throwable);


}
