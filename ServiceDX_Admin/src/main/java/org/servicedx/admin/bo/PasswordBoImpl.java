package org.servicedx.admin.bo;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import org.servicedx.event.service.AdminProducer;
import org.servicedx.security.resource.IErrorAdmin;
import org.servicedx.security.resource.IPathAdmin;
import org.servicedx.bean.PasswordFormBean;
import org.servicedx.bean.UserFormBean;
import org.servicedx.bean.model.Users;
import org.servicedx.bean.util.RestClientUtil;
import org.servicedx.dao.UserDao;
import org.servicedx.util.CommonValidator;
import org.servicedx.util.EnumInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Transactional
public class PasswordBoImpl implements PasswordBo,IErrorAdmin,IPathAdmin
{
	private static final long	serialVersionUID	= 1949352771664273090L;
	
	@Value("${server.domain.url}")
	public String				domainURL;
	
	@Autowired
	AdminProducer				producer;

	@Autowired
	protected UserDao			userDao;
	
	@Autowired
	OTPBo						otpBo;

//	@Value("${password.url.expiry.time}")
//	private long passwordLinkExpirationTime;

	@Override
	public EnumInterface changePassword(@RequestBody PasswordFormBean pfBean)
	{
		Users users = userDao.findByUserId(pfBean.userId);
		pfBean.otp.user = users;
		try {
			if (( (EFormAction.ChangePassword == pfBean.eFormAction) || (EFormAction.ForgotPassword == pfBean.eFormAction) ||
					(EFormAction.Verify == pfBean.eFormAction)) && EReturn.Success == validatePassword(pfBean,users)) {
				users.setPassword(new BCryptPasswordEncoder().encode(pfBean.newPassword));
				users.setPwdChangeFlag(false);
				users.setActive(true);
				users.setPwdChangeDate(LocalDateTime.now());
				users.setOtp(null);
				users.setTokenKey(null);
				users.setTokenKeyExpiryDate(null);
				userDao.save(users);
				pfBean.messageCode = EReturn.Success.name();
				return EReturn.Success;
			}
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return EReturn.Failure;
	}

	private EReturn validatePassword(PasswordFormBean pfBean,Users users) throws ExecutionException {
		if(!Pattern.compile(PASSWORD_VALIDATION_REGEX).matcher(pfBean.newPassword).matches()){
			pfBean.messageCode = PASSWORD_STRENGTH_FAILURE;
			return EReturn.Failure;
		}
		else if(CommonValidator.isEqual(new BCryptPasswordEncoder().encode(pfBean.newPassword), users.getPassword())){
			pfBean.messageCode = PASSWORD_SAME_AS_OLD_PASSWORD;
			return EReturn.Failure;
		}
		else if(!(EReturn.Success.name().equals(otpBo.validateOTP(pfBean.otp)))) {
			pfBean.messageCode = PASSWORD_INVALID_OTP;
			return EReturn.Failure;
		}
		return EReturn.Success;
	}

	@Override
	public EnumInterface forgotPassowrd(@RequestBody UserFormBean ufBean) {
		

		Users users = userDao.getByEmailId(ufBean.user.getEmailId());
		if (users != null)
		{
			String token = domainURL + ESecurity.Token.generate(users,EFormAction.ForgotPassword);
			ufBean.tokenURL = token;
			ufBean.user = users;
			userDao.save(users);
			String status = RestClientUtil.sendPasswordResetMail(ufBean);
			System.out.println("Mail send status"+status);
			producer.sendEmailMessage(ETemplate.User_Reset_Password, ufBean.user);
			ufBean.messageCode = FORGOT_PASSWORD_MAIL_SUCCESS;
			return EReturn.Success;
		}

		return EReturn.Failure;
	
		
	}

}
