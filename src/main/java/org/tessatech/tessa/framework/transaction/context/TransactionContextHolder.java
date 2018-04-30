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
