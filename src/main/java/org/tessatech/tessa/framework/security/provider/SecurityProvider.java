package org.tessatech.tessa.framework.security.provider;

import org.springframework.http.RequestEntity;
import org.tessatech.tessa.framework.exception.logic.InsufficientAuthorizationException;
import org.tessatech.tessa.framework.exception.logic.InvalidAuthenticationException;

public interface SecurityProvider
{

	public void loadAndVerifySecurityDetails(String[] validRoles, RequestEntity requestEntity) throws InvalidAuthenticationException;

}
