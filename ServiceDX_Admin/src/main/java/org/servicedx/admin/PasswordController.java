package org.servicedx.admin;

import org.servicedx.admin.bo.PasswordBo;
import org.servicedx.security.resource.IPathAdmin.ESecurity;
import org.servicedx.bean.APIStatus;
import org.servicedx.bean.PasswordFormBean;
import org.servicedx.bean.UserFormBean;
import org.servicedx.dao.UserDao;
import org.servicedx.util.CommonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordController implements IPasswordController
{
	private static final long	serialVersionUID	= 1355253953332836713L;

	@Autowired
	protected PasswordBo		passwordBo;

	@Autowired
	private UserDao userDao;

	@Override
	public ResponseEntity<?> changePassword(@RequestBody PasswordFormBean pfBean)
	{
		try
		{
			if(CommonValidator.isNotNullNotEmpty(pfBean, pfBean.tokenUrl)) { // if tokenurl is sent instead of user id
				UserFormBean ufBean = ESecurity.Token.validate(userDao, pfBean.tokenUrl, TOKEN_EXPIRY_DURATION);
				pfBean.userId = ufBean.user.getUserId();
				pfBean.eFormAction = ESecurity.Token.getTokenAction(pfBean.tokenUrl);
			}
			if (CommonValidator.isNotNullNotEmpty(pfBean, pfBean.userId, pfBean.newPassword))
			{
				passwordBo.changePassword(pfBean);
				return new ResponseEntity<>((APIStatus)pfBean, HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			pfBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(pfBean, HttpStatus.BAD_REQUEST);
		}
	}
	
	@Override
	public ResponseEntity<?> resetPassword(@RequestBody PasswordFormBean pfBean)
	{
		try
		{
			if(CommonValidator.isNotNullNotEmpty(pfBean, pfBean.tokenUrl)) { // if tokenurl is sent instead of user id
				UserFormBean ufBean = ESecurity.Token.validate(userDao, pfBean.tokenUrl, TOKEN_EXPIRY_DURATION);
				pfBean.userId = ufBean.user.getUserId();
				pfBean.otp.user = ufBean.user;
				pfBean.eFormAction = EFormAction.ForgotPassword;
			}
			if (CommonValidator.isNotNullNotEmpty(pfBean, pfBean.userId, pfBean.newPassword))
			{
				passwordBo.changePassword(pfBean);
				return new ResponseEntity<>((APIStatus)pfBean, HttpStatus.OK); // send only message code and mask other values
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			pfBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(pfBean, HttpStatus.BAD_REQUEST);
		}
	}


	@Override
	public ResponseEntity<?> forgotPassword(@RequestBody UserFormBean user) {
		try
		{
			if (CommonValidator.isNotNullNotEmpty(user.user.getEmailId()))
			{
				return new ResponseEntity<>(passwordBo.forgotPassowrd(user), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			return new ResponseEntity<>(excep.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
