package org.tessatech.tessa.framework.security.utils.annotations;

import org.tessatech.tessa.framework.security.utils.SecurityUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasAllRoles
{
	String[] authorizedRoles() default SecurityUtils.DEFAULT_NO_AUTHORIZATION_REQUIRED;
}