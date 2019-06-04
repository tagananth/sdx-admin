package org.servicedx.security.resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends OAuth2ResourceServerConfigBase implements IPathAdmin
{
	private static final long serialVersionUID = -177116146310386350L;

	@Override
	public void configure(HttpSecurity http) throws Exception
	{
		configure(http, EPathAdmin.values());
		http.formLogin().loginPage("/login").permitAll()//
				.and().logout().permitAll();
	}
}
