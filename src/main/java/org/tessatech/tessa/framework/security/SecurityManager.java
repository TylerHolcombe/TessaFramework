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
