package org.tessatech.tessa.framework.security.context;

public class SecurityContext
{
	private final String authenticationType;
	private final String authenticationId;

	private final String userId;
	private final String userName;
	private final String[] userRoles;

	public SecurityContext(String authenticationType, String authenticationId, String userId, String userName, String[] userRoles)
	{
		this.authenticationType = authenticationType;
		this.authenticationId = authenticationId;
		this.userId = userId;
		this.userName = userName;
		this.userRoles = userRoles;
	}

	public String getAuthenticationType()
	{
		return authenticationType;
	}

	public String getAuthenticationId()
	{
		return authenticationId;
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
