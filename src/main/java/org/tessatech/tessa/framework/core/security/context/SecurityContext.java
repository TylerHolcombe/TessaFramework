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

package org.tessatech.tessa.framework.core.security.context;

public class SecurityContext
{
	private final AuthenticationType authenticationType;
	private final String authenticationId;
	private final String authenticationScheme;
	private final String authenticationToken;
	private final String userId;
	private final String userName;
	private final String[] userRoles;

	public SecurityContext(AuthenticationType authenticationType, String authenticationId, String authenticationScheme,
			String authenticationToken, String userId, String userName, String[] userRoles)
	{
		this.authenticationType = authenticationType;
		this.authenticationId = authenticationId;
		this.authenticationScheme = authenticationScheme;
		this.authenticationToken = authenticationToken;

		this.userId = userId;
		this.userName = userName;
		this.userRoles = userRoles;
	}

	public AuthenticationType getAuthenticationType()
	{
		return authenticationType;
	}

	public String getAuthenticationId()
	{
		return authenticationId;
	}

	public String getAuthenticationScheme()
	{
		return authenticationScheme;
	}

	public String getAuthenticationToken()
	{
		return authenticationToken;
	}

	public String getUserId()
	{
		return userId;
	}

	public String getUserName()
	{
		return userName;
	}

	public String[] getUserRoles()
	{
		return userRoles;
	}
}
