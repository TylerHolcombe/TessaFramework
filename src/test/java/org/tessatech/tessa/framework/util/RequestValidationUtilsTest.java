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