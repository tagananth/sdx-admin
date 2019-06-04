package org.servicedx.admin;

import org.servicedx.security.resource.IPathAdmin;
import org.servicedx.bean.OTPFormBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

public interface IOTPController extends IPathAdmin
{
	@PostMapping
	@RequestMapping(value = GENERATE_OTP, produces = MediaType.APPLICATION_JSON_VALUE)
//	@PreAuthorize(HAS_ALL_AUTHORITY)
	ResponseEntity<?> generateOTP(@RequestBody OTPFormBean otpForm);

	@PostMapping
	@RequestMapping(value = VALIDATE_OTP, produces = MediaType.APPLICATION_JSON_VALUE)
//	@PreAuthorize(HAS_ALL_AUTHORITY)
	ResponseEntity<?> validateOTP(@RequestBody OTPFormBean otpForm);

}