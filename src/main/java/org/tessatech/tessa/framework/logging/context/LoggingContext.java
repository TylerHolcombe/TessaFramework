package org.tessatech.tessa.framework.logging.context;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class LoggingContext
{
	private long startTime;

	private Map<String, Object> keyValueFields = new HashMap<>();
	private Map<String, Long> runtimes = new HashMap<>();
	private List<ExternalCallBuilder> externalLogAttributes = new ArrayList<>();

	private Throwable throwable;

	public LoggingContext()
	{
		startTime = System.currentTimeMillis();
	}

	public long getStartTime()
	{
		return startTime;
	}

	public void addField(String key, Object value)
	{
		keyValueFields.put(key, value);
	}

	public void addFieldMasked(String key, String value)
	{
		keyValueFields.put(key, StringUtils.right(value, 4));
	}

	public void addFieldHashed(String key, String value)
	{
		keyValueFields.put(key, StringUtils.right(DigestUtils.sha1Hex(value), 16));
	}

	public void addEmailMasked(String key, String email)
	{
		String[] splitEmail = StringUtils.split(email, "@.");

		StringBuilder builder = new StringBuilder();
		for (String part : splitEmail)
		{
			builder.append(StringUtils.left(part, 3));
		}

		keyValueFields.put(key, builder.toString());
	}

	public Set<Map.Entry<String, Object>> getKeyValueFields()
	{
		return keyValueFields.entrySet();
	}

	public void addRuntime(String methodName, long runtime)
	{
		runtimes.put(methodName, runtime);
	}

	public Set<Map.Entry<String, Long>> getRuntimes()
	{
		return runtimes.entrySet();
	}

	public void addExternalCall(ExternalCallBuilder attributes)
	{
		if (attributes != null)
		{
			externalLogAttributes.add(attributes);
		}
	}

	public List<ExternalCallBuilder> getExternalLogAttributes()
	{
		return externalLogAttributes;
	}

	public void addThrowable(Throwable throwable)
	{
		this.throwable = throwable;
	}

	public Throwable getThrowable()
	{
		return throwable;
	}
}
