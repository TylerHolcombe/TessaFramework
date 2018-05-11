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
import org.tessatech.tessa.framework.core.exception.system.InternalException;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiConsumer;

public abstract class AbstractValidationUtils
{
	abstract RuntimeException generateExceptionForMessage(String message);

	abstract RuntimeException generateExceptionForMessage(String message, Throwable cause);

	public <U> AbstractValidationUtils ifPresent(String fieldName, U fieldValue, BiConsumer<String, U> function)
	{
		if(fieldValue != null)
		{
			function.accept(fieldName, fieldValue);
		}

		return this;
	}

	public <U, V> AbstractValidationUtils ifPresent(String fieldName, U fieldValue, V testValue,
			TriConsumer<String, U, V> function)
	{
		if(fieldValue != null)
		{
			function.accept(fieldName, fieldValue, testValue);
		}

		return this;
	}

	public AbstractValidationUtils isNotNull(String fieldName, Object fieldValue)
	{
		if (fieldValue == null)
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was null.");
		}

		return this;
	}

	public AbstractValidationUtils isNotEmpty(String fieldName, String fieldValue)
	{
		isNotNull(fieldName, fieldValue);

		if (fieldValue.isEmpty())
		{
			throw generateExceptionForMessage("Field: " + fieldName + " is empty.");
		}

		return this;
	}

	public AbstractValidationUtils isNotTrimmedEmpty(String fieldName, String fieldValue)
	{
		isNotEmpty(fieldName, fieldValue);

		if (fieldValue.trim().isEmpty())
		{
			throw generateExceptionForMessage("Field: " + fieldName + " is trimmed empty.");
		}

		return this;
	}

	public AbstractValidationUtils isLengthGreaterThan(String fieldName, String fieldValue, int testValue)
	{
		isNotNull(fieldName, fieldValue);

		isGreaterThan(fieldName, fieldValue.length(), testValue);

		return this;
	}

	public AbstractValidationUtils isLengthGreaterThanOrEqualTo(String fieldName, String fieldValue, int testValue)
	{
		isNotNull(fieldName, fieldValue);

		isGreaterThanOrEqualTo(fieldName, fieldValue.length(), testValue);

		return this;
	}

	public AbstractValidationUtils isLengthEqualTo(String fieldName, String fieldValue, int testValue)
	{
		isNotNull(fieldName, fieldValue);

		isEqualTo(fieldName, fieldValue.length(), testValue);

		return this;
	}

	public AbstractValidationUtils isLengthLessThan(String fieldName, String fieldValue, int testValue)
	{
		isNotNull(fieldName, fieldValue);

		isLessThan(fieldName, fieldValue.length(), testValue);

		return this;
	}

	public AbstractValidationUtils isLengthLessThanOrEqualTo(String fieldName, String fieldValue, int testValue)
	{
		isNotNull(fieldName, fieldValue);

		isLessThanOrEqualTo(fieldName, fieldValue.length(), testValue);

		return this;
	}

	public <T extends Comparable<T>> AbstractValidationUtils isGreaterThan(String fieldName, T fieldValue, T testValue)
	{
		isNotNull(fieldName, fieldValue);

		isTestValueValid(testValue);

		if (fieldValue.compareTo(testValue) != 1)
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was too small. The minimum value is greater than " + testValue);
		}
		return this;
	}

	public <T extends Comparable<T>> AbstractValidationUtils isGreaterThanOrEqualTo(String fieldName, T fieldValue,
			T testValue)
	{
		isNotNull(fieldName, fieldValue);

		isTestValueValid(testValue);

		if (fieldValue.compareTo(testValue) < 0)
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was too small. The minimum is " + testValue);
		}
		return this;
	}

	public <T extends Comparable<T>> AbstractValidationUtils isEqualTo(String fieldName, T fieldValue, T testValue)
	{
		isNotNull(fieldName, fieldValue);

		isTestValueValid(testValue);

		if (fieldValue.compareTo(testValue) != 0)
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was invalid. The only acceptable value was " + testValue);
		}
		return this;
	}

	public <T extends Comparable<T>> AbstractValidationUtils isLessThan(String fieldName, T fieldValue, T testValue)
	{
		isNotNull(fieldName, fieldValue);

		isTestValueValid(testValue);

		if (fieldValue.compareTo(testValue) != -1)
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was too large. The maximum value is less than " + testValue);
		}
		return this;
	}

	public <T extends Comparable<T>> AbstractValidationUtils isLessThanOrEqualTo(String fieldName, T fieldValue,
			T testValue)
	{
		isNotNull(fieldName, fieldValue);

		isTestValueValid(testValue);

		if (fieldValue.compareTo(testValue) > 0)
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was too large. The maximum is: " + testValue);
		}
		return this;
	}

	public <T> AbstractValidationUtils isInCollection(String fieldName, T fieldValue, Collection<T> testValues)
	{
		isNotNull(fieldName, fieldValue);

		areTestValuesValid(testValues);

		if (!testValues.contains(fieldValue))
		{
			throw generateExceptionForMessage("Field: " + fieldName + " did not contain an acceptable value. Acceptable values are: " + Arrays.toString(testValues.toArray()));
		}
		return this;
	}

	public <T> AbstractValidationUtils isInArray(String fieldName, T fieldValue, T[] testValues)
	{
		isInCollection(fieldName, fieldValue, Arrays.asList(testValues));

		return this;
	}

	public AbstractValidationUtils isTrue(String fieldName, Boolean value)
	{
		isNotNull(fieldName, value);

		if(!value.booleanValue())
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was false. Expected: true");
		}

		return this;
	}

	public AbstractValidationUtils isFalse(String fieldName, Boolean value)
	{
		isNotNull(fieldName, value);

		if(value.booleanValue())
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was true. Expected: false");
		}

		return this;
	}

	public <T extends Number> AbstractValidationUtils isParsable(String fieldName, String fieldValue,
			Class<T> numberClass)
	{
		parseNumber(fieldName, fieldValue, numberClass);
		return this;
	}

	public <T extends Number> T parseNumber(String fieldName, String fieldValue, Class<T> numberClass)
	{
		isNotTrimmedEmpty(fieldName, fieldValue);

		isTestValueValid(numberClass);

		try
		{
			return NumberUtils.parseNumber(fieldValue, numberClass);
		}
		catch (Exception e)
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was not of type " + numberClass.getSimpleName
					(), e);
		}
	}

	public <T extends Enum<T>> AbstractValidationUtils isInEnumeration(String fieldName, String fieldValue,
			Class<T> enumClass)
	{
		parseEnumeration(fieldName, fieldValue, enumClass);
		return this;
	}

	public <T extends Enum<T>> T parseEnumeration(String fieldName, String fieldValue, Class<T> enumClass)
	{
		isNotTrimmedEmpty(fieldName, fieldValue);

		isTestValueValid(enumClass);

		try
		{
			return Enum.valueOf(enumClass, fieldValue.toUpperCase());
		}
		catch (Exception e)
		{
			throw generateExceptionForMessage("Field: " + fieldName + " was not of type " + enumClass.getSimpleName(),
					e);
		}
	}

	public AbstractValidationUtils isSanitized(String fieldName, String fieldValue, SanitizeRegex sanatizationRegex)
	{
		isNotTrimmedEmpty(fieldName, fieldValue);

		if(!fieldValue.matches(sanatizationRegex.getRegex()))
		{
			throw generateExceptionForMessage("Field: " + fieldName + " has invalid characters.");
		}
		return this;
	}

	public AbstractValidationUtils isSanitized(String fieldName, String fieldValue, String sanatizationRegex)
	{
		isNotTrimmedEmpty(fieldName, fieldValue);

		if(!fieldValue.matches(sanatizationRegex))
		{
			throw generateExceptionForMessage("Field: " + fieldName + " has invalid characters.");
		}

		return this;
	}

	public AbstractValidationUtils isTestValueValid(Object testValue)
	{
		if (testValue == null)
		{
			throw new InternalException("Test value supplied to this validation is null.");
		}
		return this;
	}


	public AbstractValidationUtils areTestValuesValid(Collection<?> testValues)
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

		return this;
	}


}
