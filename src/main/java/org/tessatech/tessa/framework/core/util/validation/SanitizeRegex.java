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

public enum SanitizeRegex
{
	ALPHA_ONLY("[A-Za-z]+"),
	NUMERIC_ONLY("[0-9]+"),
	ALPHA_NUMERIC_ONLY("[0-9A-Za-z]+"),
	NO_BRACES("[0-9A-Za-z~`!@#$%^&*()_=+|?,.-]+");

	private String regex;

	private SanitizeRegex(String regex)
	{
		this.regex = regex;
	}

	public String getRegex()
	{
		return this.regex;
	}
}
