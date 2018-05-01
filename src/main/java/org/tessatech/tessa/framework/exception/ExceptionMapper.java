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

package org.tessatech.tessa.framework.exception;

import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.exception.system.DatabaseException;
import org.tessatech.tessa.framework.exception.system.UnknownException;

@Component
public class ExceptionMapper
{
	public static TessaException mapExceptionIntoTessaException(Throwable throwable)
	{
		if(throwable instanceof TessaException)
		{
			return (TessaException) throwable;
		}
		else if (throwable instanceof DataAccessException || throwable instanceof HibernateException)
		{
			return new DatabaseException("A database issue occurred.", throwable);
		}

		return new UnknownException("Throwable mapped into UnknownException via the ExceptionMapper.", throwable);
	}

}
