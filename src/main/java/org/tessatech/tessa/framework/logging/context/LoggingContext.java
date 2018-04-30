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
