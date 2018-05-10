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

package org.tessatech.tessa.framework.core.util.validation;

import org.apache.logging.log4j.util.TriConsumer;
import org.springframework.util.NumberUtils;
import org.springframework.validation.ValidationUtils;
import org.tessatech.tessa.framework.core.exception.system.InternalException;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

abstract class AbstractValidationUtils
{
	abstract RuntimeException generateExceptionForMessage(String message);

	abstract RuntimeException generateExceptionForMessage(String message, Throwable cause);

	public <U> void ifPresent(String fieldName, U fieldValue, BiConsumer<String, U> function)
	{
		if(fieldValue != null)
		{
			function.accept(fieldName, fieldValue);
		}
	}

	public <U, V> void ifPresent(String fieldName, U fieldValue, V testValue, TriConsumer<String, U, V> function)
	{
		if(fieldValue != null)
		{
			function.accept(fieldName, fieldValue, testValue);
		}
	}

	public void isNotNull(String fieldName, Object fieldValue)
	{
		if (fieldValue == null)
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was null.");
		}
	}

	public void isNotEmpty(String fieldName, String fieldValue)
	{
		isNotNull(fieldName, fieldValue);

		if (fieldValue.isEmpty())
		{
			throw generateExceptionForMessage("Field: " + fieldName + " is empty.");
		}
	}

	public void isNotTrimmedEmpty(String fieldName, String fieldValue)
	{
		isNotEmpty(fieldName, fieldValue);

		if (fieldValue.trim().isEmpty())
		{
			throw generateExceptionForMessage("Field: " + fieldName + " is trimmed empty.");
		}
	}

	public void isLengthGreaterThan(String fieldName, String fieldValue, int testValue)
	{
		isNotNull(fieldName, fieldValue);

		isGreaterThan(fieldName, fieldValue.length(), testValue);
	}

	public void isLengthGreaterThanOrEqualTo(String fieldName, String fieldValue, int testValue)
	{
		isNotNull(fieldName, fieldValue);

		isGreaterThanOrEqualTo(fieldName, fieldValue.length(), testValue);
	}

	public void isLengthEqualTo(String fieldName, String fieldValue, int testValue)
	{
		isNotNull(fieldName, fieldValue);

		isEqualTo(fieldName, fieldValue.length(), testValue);
	}

	public void isLengthLessThan(String fieldName, String fieldValue, int testValue)
	{
		isNotNull(fieldName, fieldValue);

		isLessThan(fieldName, fieldValue.length(), testValue);
	}

	public void isLengthLessThanOrEqualTo(String fieldName, String fieldValue, int testValue)
	{
		isNotNull(fieldName, fieldValue);

		isLessThanOrEqualTo(fieldName, fieldValue.length(), testValue);
	}

	public <T extends Comparable<T>> void isGreaterThan(String fieldName, T fieldValue, T testValue)
	{
		isNotNull(fieldName, fieldValue);

		isTestValueValid(testValue);

		if (fieldValue.compareTo(testValue) != 1)
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was too small. The minimum value is greater than " + testValue);
		}
	}

	public <T extends Comparable<T>> void isGreaterThanOrEqualTo(String fieldName, T fieldValue, T testValue)
	{
		isNotNull(fieldName, fieldValue);

		isTestValueValid(testValue);

		if (fieldValue.compareTo(testValue) < 0)
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was too small. The minimum is " + testValue);
		}
	}

	public <T extends Comparable<T>> void isEqualTo(String fieldName, T fieldValue, T testValue)
	{
		isNotNull(fieldName, fieldValue);

		isTestValueValid(testValue);

		if (fieldValue.compareTo(testValue) != 0)
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was invalid. The only acceptable value was " + testValue);
		}
	}

	public <T extends Comparable<T>> void isLessThan(String fieldName, T fieldValue, T testValue)
	{
		isNotNull(fieldName, fieldValue);

		isTestValueValid(testValue);

		if (fieldValue.compareTo(testValue) != -1)
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was too large. The maximum value is less than " + testValue);
		}
	}

	public <T extends Comparable<T>> void isLessThanOrEqualTo(String fieldName, T fieldValue, T testValue)
	{
		isNotNull(fieldName, fieldValue);

		isTestValueValid(testValue);

		if (fieldValue.compareTo(testValue) > 0)
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was too large. The maximum is: " + testValue);
		}
	}

	public <T> void isInCollection(String fieldName, T fieldValue, Collection<T> testValues)
	{
		isNotNull(fieldName, fieldValue);

		areTestValuesValid(testValues);

		if (!testValues.contains(fieldValue))
		{
			throw generateExceptionForMessage("Field: " + fieldName + " did not contain an acceptable value. Acceptable values are: " + Arrays.toString(testValues.toArray()));
		}
	}

	public <T> void isInArray(String fieldName, T fieldValue, T[] testValues)
	{
		isInCollection(fieldName, fieldValue, Arrays.asList(testValues));
	}

	public <T extends Number> T isParsable(String fieldName, String fieldValue, Class<T> numberClass)
	{
		isNotTrimmedEmpty(fieldName, fieldValue);

		isTestValueValid(numberClass);

		try
		{
			return NumberUtils.parseNumber(fieldValue, numberClass);
		}
		catch (Exception e)
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was not of type " + numberClass.getSimpleName(), e);
		}
	}

	public <T extends Enum<T>> T isInEnumeration(String fieldName, String fieldValue, Class<T> enumClass)
	{
		isNotTrimmedEmpty(fieldName, fieldValue);

		isTestValueValid(enumClass);

		try
		{
			return Enum.valueOf(enumClass, fieldValue.toUpperCase());
		}
		catch (Exception e)
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was not of type " + enumClass.getSimpleName(), e);
		}
	}

	public void isSanitized(String fieldName, String fieldValue, SanitizeRegex sanatizationRegex)
	{
		isNotTrimmedEmpty(fieldName, fieldValue);

		if(!fieldValue.matches(sanatizationRegex.getRegex()))
		{
			throw generateExceptionForMessage("Field: " + fieldName + " has invalid characters.");
		}
	}

	public void isSanitized(String fieldName, String fieldValue, String sanatizationRegex)
	{
		isNotTrimmedEmpty(fieldName, fieldValue);

		if(!fieldValue.matches(sanatizationRegex))
		{
			throw generateExceptionForMessage("Field: " + fieldName + " has invalid characters.");
		}
	}

	public void isTestValueValid(Object testValue)
	{
		if (testValue == null)
		{
			throw new InternalException("Test value supplied to this validation is null.");
		}
	}


	public void areTestValuesValid(Collection<?> testValues)
	{
		isTestValueValid((Object) testValues);

		if (testValues.isEmpty())
		{
			throw new InternalException("The collection of test values supplied to this validation was empty.");
		}

		for (Object testValue : testValues)
		{
			isTestValueValid(testValue);
		}
	}


}
