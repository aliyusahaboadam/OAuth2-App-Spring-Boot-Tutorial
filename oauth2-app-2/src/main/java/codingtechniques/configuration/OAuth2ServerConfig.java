package codingtechniques.configuration;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

@Configuration
public class OAuth2ServerConfig {
	
	@Bean
	public RegisteredClientRepository registeredClientRepository () {
		return new InMemoryRegisteredClientRepository(this.registeredClient());
	}
	
	
	private RegisteredClient registeredClient () {
	return	RegisteredClient.withId(UUID.randomUUID().toString())
		.clientId("client")
		.clientSecret("{noop}secret")
		.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
		.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
		.redirectUri("http://spring.io")
		.scope("read")
		.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
	    .build();
	}
	
	@Bean
	@Order(1)
	public SecurityFilterChain authorizationServerSecurityFilterChain (HttpSecurity httpSecurity) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);
		httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class);
		
		httpSecurity.exceptionHandling(e -> e.defaultAuthenticationEntryPointFor(
				new LoginUrlAuthenticationEntryPoint("/login"),
			new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));
	return	httpSecurity.build();
	}
	
	@Bean
	public AuthorizationServerSettings authorizationServerSettings () {
		return AuthorizationServerSettings.builder().build();
	}
  
}
