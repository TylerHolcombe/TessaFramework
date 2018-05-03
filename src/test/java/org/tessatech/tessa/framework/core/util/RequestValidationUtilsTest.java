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

import org.junit.Test;
import org.tessatech.tessa.framework.core.exception.logic.ValidationException;
import org.tessatech.tessa.framework.core.exception.system.InternalException;
import org.tessatech.tessa.framework.core.util.validation.RequestValidationUtils;
import org.tessatech.tessa.framework.core.util.validation.SanitizeRegex;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class RequestValidationUtilsTest
{

	RequestValidationUtils util = RequestValidationUtils.getInstance();

	@Test(expected = ValidationException.class)
	public void isNotNull_Exception_Null()
	{
		util.isNotNull("TestField", null);
	}

	@Test
	public void isNotNull_Success()
	{
		util.isNotNull("TestField", "not null");
	}

	@Test
	public void isNotEmpty_Success()
	{
		util.isNotEmpty("TestField", "not empty");
	}

	@Test
	public void isNotEmpty_Success_Whitespace()
	{
		util.isNotEmpty("TestField", "  ");
	}

	@Test(expected = ValidationException.class)
	public void isNotEmpty_Empty()
	{
		util.isNotEmpty("TestField", "");
	}

	@Test(expected = ValidationException.class)
	public void isNotEmpty_Null()
	{
		util.isNotEmpty("TestField", null);
	}

	@Test
	public void isNotTrimmedEmpty_Success()
	{
		util.isNotTrimmedEmpty("TestField", "testValue");
	}

	@Test(expected = ValidationException.class)
	public void isNotTrimmedEmpty_TrimmedEmpty()
	{
		util.isNotTrimmedEmpty("TestField", "   ");
	}

	@Test(expected = ValidationException.class)
	public void isNotTrimmedEmpty_Empty()
	{
		util.isNotTrimmedEmpty("TestField", "");
	}

	@Test(expected = ValidationException.class)
	public void isNotTrimmedEmpty_Null()
	{
		util.isNotTrimmedEmpty("TestField", null);
	}

	@Test
	public void isGreaterThan_GreaterThan()
	{
		util.isGreaterThan("TestField", 5, 4);
	}

	@Test(expected = ValidationException.class)
	public void isGreaterThan_Exception_LessThan()
	{
		util.isGreaterThan("TestField", 5, 6);
	}

	@Test(expected = ValidationException.class)
	public void isGreaterThan_Exception_Equal()
	{
		util.isGreaterThan("TestField", 5, 5);
	}

	@Test
	public void isGreaterThanOrEqualTo_GreaterThan()
	{
		util.isGreaterThanOrEqualTo("TestField", 5, 4);
	}

	@Test(expected = ValidationException.class)
	public void isGreaterThanOrEqualTo_Exception_LessThan()
	{
		util.isGreaterThanOrEqualTo("TestField", 5, 6);
	}

	@Test
	public void isGreaterThanOrEqualTo_Exception_Equal()
	{
		util.isGreaterThanOrEqualTo("TestField", 5, 5);
	}

	@Test(expected = ValidationException.class)
	public void isEqualTo_GreaterThan()
	{
		util.isEqualTo("TestField", 5, 4);
	}

	@Test(expected = ValidationException.class)
	public void isEqualTo_LessThan()
	{
		util.isEqualTo("TestField", 5, 6);
	}

	@Test
	public void isEqualTo_Equal()
	{
		util.isEqualTo("TestField", 5, 5);
	}

	@Test(expected = ValidationException.class)
	public void isLessThanOrEqualTo_GreaterThan()
	{
		util.isLessThanOrEqualTo("TestField", 5, 4);
	}

	@Test
	public void isLessThanOrEqualTo_LessThan()
	{
		util.isLessThanOrEqualTo("TestField", 5, 6);
	}

	@Test
	public void isLessThanOrEqualTo_Equal()
	{
		util.isLessThanOrEqualTo("TestField", 5, 5);
	}


	@Test(expected = ValidationException.class)
	public void isLessThan_GreaterThan()
	{
		util.isLessThan("TestField", 5, 4);
	}

	@Test
	public void isLessThan_LessThan()
	{
		util.isLessThan("TestField", 5, 6);
	}

	@Test(expected = ValidationException.class)
	public void isLessThan_Equal()
	{
		util.isLessThan("TestField", 5, 5);
	}

	@Test
	public void isInCollection_True()
	{
		List<String> list = Arrays .asList("a", "b", "c");

		util.isInCollection("TestField", "a", list);
	}

	@Test(expected = ValidationException.class)
	public void isInCollection_False()
	{
		List<String> list = Arrays .asList("a", "b", "c");

		util.isInCollection("TestField", "d", list);
	}

	@Test
	public void isInArray_True()
	{
		String[] list = new String[] { "a", "b", "c"};

		util.isInArray("TestField", "a", list);
	}

	@Test(expected = ValidationException.class)
	public void isInArray_False()
	{
		String[] list = new String[] { "a", "b", "c"};

		util.isInArray("TestField", "d", list);
	}

	@Test
	public void isParseable_True()
	{
		float result = util.isParsable("testField", "5.02", Float.class);
		assertEquals(5.02, result, 0.0001);
	}

	@Test(expected = ValidationException.class)
	public void isParseable_False()
	{
		util.isParsable("testField", "5.02", Long.class);
	}

	enum TestEnum
	{
		A, B, C;
	}

	@Test
	public void isInEnumeration_True()
	{
		TestEnum val = util.isInEnumeration("TestField", "A", TestEnum.class);
		assertEquals(TestEnum.A, val);
	}

	@Test
	public void isInEnumeration_Uppercases()
	{
		TestEnum val = util.isInEnumeration("TestField", "a", TestEnum.class);
		assertEquals(TestEnum.A, val);
	}

	@Test(expected = ValidationException.class)
	public void isInEnumeration_False()
	{
		util.isInEnumeration("TestField", "D", TestEnum.class);
	}

	@Test
	public void isSanitized_True()
	{
		util.isSanitized("Test", "abcABC123", SanitizeRegex.ALPHA_NUMERIC_ONLY);
	}

	@Test(expected = ValidationException.class)
	public void isSanitized_False()
	{
		util.isSanitized("Test", "abcABC123}", SanitizeRegex.ALPHA_NUMERIC_ONLY);
	}

	@Test
	public void isSanitizedRegex_True()
	{
		util.isSanitized("Test", "abcABC123~`!@#$%^&*()-_=+|?,.", SanitizeRegex.NO_BRACES.getRegex());
	}

	@Test(expected = ValidationException.class)
	public void isSanitizedRegex_False()
	{
		util.isSanitized("Test", "abcABC123}", SanitizeRegex.NO_BRACES.getRegex());
	}


	@Test
	public void areTestValuesValid()
	{
		List<String> list = Arrays .asList("a", "b", "c");
		util.areTestValuesValid(list);
	}

	@Test(expected = InternalException.class)
	public void areTestValuesValid_NulList()
	{
		List<String> list = null;
		util.areTestValuesValid(list);
	}

	@Test(expected = InternalException.class)
	public void areTestValuesValid_NulValue()
	{
		List<String> list = Arrays .asList("a", "b", null, "c");
		util.areTestValuesValid(list);
	}



}