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

public class FallbackThrowableAdapter extends ThrowableAdapter
{
	public FallbackThrowableAdapter()
	{
		super(true, Throwable.class);
	}

	@Override
	public long getExceptionCode(Throwable throwable)
	{
		return -1;
	}

	@Override
	public String getExceptionMessage(Throwable throwable)
	{
		return "An unknown error occurred";
	}
}
