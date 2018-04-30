package org.tessatech.tessa.framework.transaction;

import org.tessatech.tessa.framework.security.utils.SecurityUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TessaTransaction
{
	String serviceName() default "unknown";
	String serviceVersion() default "unknown";
	String serviceMethodName() default "unknown";

	String[] authorizedRoles() default SecurityUtils.DEFAULT_NO_AUTHORIZATION_REQUIRED;
}
