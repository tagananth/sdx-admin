package org.servicedx.admin;

import org.servicedx.admin.bo.OTPBo;
import org.servicedx.bean.OTPFormBean;
import org.servicedx.bean.UserFormBean;
import org.servicedx.dao.UserDao;
import org.servicedx.util.CommonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OTPController implements IOTPController
{
	private static final long	serialVersionUID	= 1355253953332836713L;

	@Autowired
	protected OTPBo otpBo;

	@Autowired
	private UserDao userDao;

	public ResponseEntity<?> generateOTP(OTPFormBean otpForm)
	{
		try
		{
			OTPFormBean response = new OTPFormBean();
			if (CommonValidator.isNotNullNotEmpty(otpForm, otpForm.tokenUrl))
			{
				UserFormBean ufBean = ESecurity.Token.validate(userDao, otpForm.tokenUrl, TOKEN_EXPIRY_DURATION);
				otpForm.user = ufBean.user;
				response.id = otpBo.generateOTP(otpForm);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			else if (CommonValidator.isNotNullNotEmpty(otpForm, otpForm.user,otpForm.user.getUserId()))
			{
				response.id = otpBo.generateOTP(otpForm);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<String>(EReturn.Failure.name(), HttpStatus.BAD_REQUEST);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<?> validateOTP(OTPFormBean otpForm)
	{
		try
		{
			if (CommonValidator.isNotNullNotEmpty(otpForm, otpForm.id,otpForm.user.getUserId(),otpForm.otp))
			{
				return new ResponseEntity<String>(otpBo.validateOTP(otpForm), HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>(EReturn.Failure.name(), HttpStatus.BAD_REQUEST);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}
}
