package com.vodafone.gno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.vodafone.kramax.bpm.extension.entra.plugin.EntraIdentityProviderPlugin;*/

@SpringBootApplication
public class GnoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GnoApplication.class, args);
	}
	/*@Bean
	@ConfigurationProperties(prefix = "plugin.identity.entra")
	public EntraIdentityProviderPlugin entraIdentityProviderPlugin() {
		return new EntraIdentityProviderPlugin();
	}
*/
}
