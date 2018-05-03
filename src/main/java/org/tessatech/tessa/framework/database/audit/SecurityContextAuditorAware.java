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

package org.tessatech.tessa.framework.database.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.core.security.context.SecurityContext;
import org.tessatech.tessa.framework.core.security.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityContextAuditorAware implements AuditorAware<String>
{

	@Override
	public Optional<String> getCurrentAuditor()
	{
		String auditor = null;

		if (SecurityContextHolder.getContextOptional().isPresent())
		{
			auditor = SecurityContextHolder.getContext().getUserId();
		}

		return Optional.ofNullable(auditor);
	}
}
