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

package org.tessatech.tessa.framework.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.tessatech.tessa.framework.security.provider.SecurityProvider;
import org.tessatech.tessa.framework.transaction.TessaTransaction;

@Component
public class SecurityManager
{
	private static final Logger logger = LogManager.getLogger(SecurityManager.class);

	@Value("${securityEnabled:true}")
	private boolean securityEnabled = true;

	@Autowired
	private SecurityProvider securityProvider;

	public void secureRequest(TessaTransaction transaction,
							  RequestEntity<?> request)
	{

		if(securityEnabled)
		{
			securityProvider.loadAndVerifySecurityDetails(transaction.authorizedRoles(), request);
		}

	}
}
