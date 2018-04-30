package org.tessatech.tessa.framework.util;

import org.junit.Test;
import org.tessatech.tessa.framework.exception.logic.ValidationException;
import org.tessatech.tessa.framework.util.validation.RequestValidationUtils;

public class RequestValidationUtilsTest
{

	RequestValidationUtils util = RequestValidationUtils.getInstance();

	@Test(expected = ValidationException.class)
	public void isNull()
	{
		util.isNotNull("TestField", null);
	}

	@Test
	public void isNotNull()
	{
		util.isNotNull("TestField", "not null");
	}

	@Test
	public void isNotEmpty()
	{
		util.isNotNull("TestField", "not empty");
	}

	@Test(expected = ValidationException.class)
	public void isEmpty()
	{
		util.isNotNull("TestField", "not empty");
	}

	@Test
	public void isNotTrimmedEmpty()
	{
	}

	@Test
	public void isGreaterThan()
	{
		util.isGreaterThan("TestField", 5, 4);
	}
}