package org.servicedx.security.resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityAnonymousAccessConfig extends WebSecurityConfigurerAdapter  implements IPathAdmin
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7281928899149367733L;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().mvcMatchers(FORGOT_PASSWORD,RESET_PASSWORD, VALIDATE_USER+SLASH_STARS,GENERATE_OTP,VALIDATE_OTP,CHANGE_PASSWORD);
	}
}