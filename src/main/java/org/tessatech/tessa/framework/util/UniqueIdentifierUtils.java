package org.tessatech.tessa.framework.util;

import java.util.UUID;

public class UniqueIdentifierUtils
{

	public static long getUniqueId()
	{
		return UUID.randomUUID().getLeastSignificantBits() & Long.MAX_VALUE;
	}
}
