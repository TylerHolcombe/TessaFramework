package org.tessatech.tessa.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

public class TessaConfig
{
	@Configuration
	@Profile("dev")
	@PropertySource({"classpath:application.properties", "classpath:application-dev.properties"})
	public class DevConfig { }

	@Configuration
	@Profile("prod")
	@PropertySource({"classpath:application.properties", "classpath:application-prod.properties"})
	public class ProdConfig { }

}
