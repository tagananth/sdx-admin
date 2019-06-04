package org.servicedx.admin;

import org.springframework.http.MediaType;
import org.servicedx.security.resource.IPathAdmin;
import org.servicedx.bean.PasswordFormBean;
import org.servicedx.bean.UserFormBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public interface IPasswordController extends IPathAdmin
{
	@PostMapping
	@RequestMapping(value = CHANGE_PASSWORD)
	ResponseEntity<?> changePassword(PasswordFormBean password);
	
	@PostMapping
	@RequestMapping(value = FORGOT_PASSWORD)
	ResponseEntity<?> forgotPassword(UserFormBean user);

	@PostMapping
	@RequestMapping(value = RESET_PASSWORD, consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<?> resetPassword(PasswordFormBean pfBean);

}