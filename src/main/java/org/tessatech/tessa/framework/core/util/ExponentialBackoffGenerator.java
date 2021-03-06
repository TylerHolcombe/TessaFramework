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

package org.tessatech.tessa.framework.core.util;

import java.util.concurrent.ThreadLocalRandom;

public class ExponentialBackoffGenerator
{
	private long initialInterval = 2000;
	private long maxInterval =  600000;
	private double multiplier = 2;

	private long currentInterval = 0;

	private double randomizationFactor = .25;

	public ExponentialBackoffGenerator()
	{

	}

	public ExponentialBackoffGenerator(long initialInterval, long maxInterval)
	{
		this.initialInterval = initialInterval;
		this.maxInterval = maxInterval;
	}

	public ExponentialBackoffGenerator(long initialInterval, long maxInterval, double multiplier)
	{
		this.initialInterval = initialInterval;
		this.maxInterval = maxInterval;
		this.multiplier = multiplier;
	}

	public ExponentialBackoffGenerator(long initialInterval, long maxInterval, double multiplier,
			double randomizationFactor)
	{
		this.initialInterval = initialInterval;
		this.maxInterval = maxInterval;
		this.multiplier = multiplier;
		this.randomizationFactor = randomizationFactor;
	}

	public long getNextInterval()
	{
		if(currentInterval == 0)
		{
			currentInterval = initialInterval;
		}
		else if (currentInterval < maxInterval)
		{
			currentInterval = (long) (currentInterval * multiplier);

			if (currentInterval > maxInterval)
			{
				currentInterval = maxInterval;
			}
		}

		long lowerBound = (long) (currentInterval * (1.0 - randomizationFactor));
		long upperBound = (long) (currentInterval * (1.0 + randomizationFactor));
		return ThreadLocalRandom.current().nextLong(lowerBound, upperBound);
	}



}
