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

package org.tessatech.tessa.framework.transaction.context;

import org.tessatech.tessa.framework.exception.system.InternalException;

import java.util.Optional;

public class TransactionContextHolder
{
	private static final ThreadLocal<TransactionContext> transactionContextLocal = new ThreadLocal<>();

	public static void setContext(TransactionContext context)
	{
		transactionContextLocal.set(context);
	}

	public static void clearContext()
	{
		transactionContextLocal.set(null);
		transactionContextLocal.remove();
	}

	public static TransactionContext getContext()
	{
		TransactionContext context = transactionContextLocal.get();

		if(context == null)
		{
			throw new InternalException("TransactionContext was requested but is Null");
		}

		return context;
	}

	public static Optional<TransactionContext> getContextOptional()
	{
		return Optional.ofNullable(transactionContextLocal.get());
	}

}
