package org.servicedx.admin;

import static org.servicedx.util.ServerUtilFactory.CHANGEPASSWORDURL;
import static org.servicedx.util.ServerUtilFactory.ERRORPAGEURL;
import static org.servicedx.util.ServerUtilFactory.PASSWORDCHANGEDURL;
import static org.servicedx.util.ServerUtilFactory.PASSWORDLINKEXPIREDURL;
import static org.servicedx.util.ServerUtilFactory.RESETPASSWORDURL;
import static org.servicedx.util.ServerUtilFactory.URLSEPERATOR;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.servicedx.admin.bo.UserBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.servicedx.bean.UserFormBean;
import org.servicedx.bean.model.Users;
import org.servicedx.util.CommonValidator;
import org.servicedx.util.EnumInterface;
import org.servicedx.util.ServerUtilFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements IUserController
{
	private static final long	serialVersionUID	= -1046242631792313825L;

	private static final String UTF8ENCODER = "UTF-8";

	private final Logger		logger				= LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserBo						userBo;
	
	@Autowired
	private ServerUtilFactory serverUtil;
	
	private String getForgotPasswordUrl() {
		return ServerUtilFactory.constructUrl(serverUtil.getSomUiUrl(),URLSEPERATOR,RESETPASSWORDURL);
	}

	private String getChangePasswordUrl() {
		return ServerUtilFactory.constructUrl(serverUtil.getSomUiUrl(),URLSEPERATOR,CHANGEPASSWORDURL);
	}
	
	private String getErrorPageUrl() {
		return ServerUtilFactory.constructUrl(serverUtil.getSomUiUrl(),URLSEPERATOR,ERRORPAGEURL);
	}
	
	private String getPasswordChangedUrl() {
		return ServerUtilFactory.constructUrl(serverUtil.getSomUiUrl(),URLSEPERATOR,PASSWORDCHANGEDURL);
	}

	private String getPasswordLinkExpiredUrl() {
		return ServerUtilFactory.constructUrl(serverUtil.getSomUiUrl(),URLSEPERATOR,PASSWORDLINKEXPIREDURL);
	}
	
	@Override
	public ResponseEntity<?> addUser(Authentication auth,@RequestHeader(value="Authorization") String token, @RequestBody UserFormBean userFormBean)
	{
		try
		{
			logger.info("UserController addUser starts ::: ");
			userFormBean.authToken = token;
			userBo.saveUser(auth, userFormBean);
			logger.info("addUser ends ::: ");
			return new ResponseEntity<>(userFormBean, HttpStatus.OK);
		}
		catch (Exception excep)
		{
			userFormBean.user = null;
			userFormBean.messageCode = excep.getMessage();
			logger.error("Exception in UserController addUser ::: ", excep);
			return new ResponseEntity<>(userFormBean, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> blockUser(Authentication auth, @RequestBody UserFormBean userFormBean)
	{
		try
		{
			logger.info("UserController blockUser starts ::: ", userFormBean.user.getUserId());
			if (CommonValidator.isNotNullNotEmpty(userFormBean.user))
			{
				return new ResponseEntity<EnumInterface>(userBo.blockUser(auth, userFormBean), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			userFormBean.user = null;
			userFormBean.repoUser = null;
			userFormBean.messageCode = excep.getMessage();
			logger.error("Exception in UserController blockUser ::: ", excep);
			return new ResponseEntity<>(userFormBean, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> deleteUser(Authentication auth, @RequestBody UserFormBean userFormBean)
	{
		try
		{
			logger.info("UserController deleteUser starts ::: " , userFormBean.user.getUserId());
			if (CommonValidator.isNotNullNotEmpty(userFormBean.user))
			{
				return new ResponseEntity<EnumInterface>(userBo.deleteUser(auth, userFormBean), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);

		}
		catch (Exception excep)
		{
			userFormBean.user = null;
			userFormBean.repoUser = null;
			userFormBean.messageCode = excep.getMessage();
			logger.error("Exception in UserController deleteUser ::: ", excep);
			return new ResponseEntity<>(userFormBean, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> getUserByCustomer(Authentication auth)
	{
		try
		{
			logger.info("Inside UserController getUserByCustomer ::: ");
			return new ResponseEntity<List<Users>>(userBo.getUserByCustomer(auth), HttpStatus.OK);
		}
		catch (Exception excep)
		{
			UserFormBean userFormBean = new UserFormBean();
			userFormBean.user = null;
			userFormBean.repoUser = null;
			userFormBean.messageCode = excep.getMessage();
			logger.error("Exception in UserController getUserByCustomer ::: ", excep);
			return new ResponseEntity<>(userFormBean, HttpStatus.BAD_REQUEST);
		}
	}
	
	@Override
	public ResponseEntity<?> getUser(Authentication auth, @RequestBody UserFormBean userFormBean)
	{
		try
		{
			logger.info("UserController getUser starts ::: " , userFormBean.user.getUserId());
			if (CommonValidator.isNotNullNotEmpty(userFormBean.user))
			{
				return new ResponseEntity<Users>(userBo.getUser(userFormBean), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);

		}
		catch (Exception excep)
		{
			userFormBean.user = null;
			userFormBean.repoUser = null;
			userFormBean.messageCode = excep.getMessage();
			logger.error("Exception in UserController getUser ::: ", excep);
			return new ResponseEntity<>(userFormBean, HttpStatus.BAD_REQUEST);
		}
	}
	
	@Override
	public ResponseEntity<?> getActiveUser(Authentication auth, @RequestBody UserFormBean userFormBean)
	{
		try
		{
			logger.info("UserController getUser starts ::: " , userFormBean.user.getUserId());
			if (CommonValidator.isNotNullNotEmpty(userFormBean.user))
			{
				return new ResponseEntity<Users>(userBo.getActiveUser(userFormBean), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);

		}
		catch (Exception excep)
		{
			userFormBean.user = null;
			userFormBean.repoUser = null;
			userFormBean.messageCode = excep.getMessage();
			logger.error("Exception in UserController getUser ::: ", excep);
			return new ResponseEntity<>(userFormBean, HttpStatus.BAD_REQUEST);
		}
	}


	@Override
	public ResponseEntity<?> search(Authentication auth, @RequestBody UserFormBean userFormBean)
	{
		try
		{
			logger.info("Inside UserController search ::: ");
			return new ResponseEntity<List<Users>>(userBo.searchUser(userFormBean), HttpStatus.OK);
		}
		catch (Exception excep)
		{
			userFormBean.user = null;
			userFormBean.repoUser = null;
			userFormBean.messageCode = excep.getMessage();
			logger.error("Exception in UserController search ::: ", excep);
			return new ResponseEntity<>(userFormBean, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> updateUser(Authentication auth, @RequestBody UserFormBean userFormBean)
	{
		try
		{
			logger.info("Inside UserController updateUser ::: ");
			if (CommonValidator.isNotNullNotEmpty(userFormBean, userFormBean.user))
			{
				userBo.updateUser(auth, userFormBean);
				return new ResponseEntity<>(userFormBean, HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);

		}
		catch (Exception excep)
		{
			userFormBean.user = null;
			userFormBean.repoUser = null;
			userFormBean.messageCode = excep.getMessage();
			logger.error("Exception in UserController updateUser ::: ", excep);
			return new ResponseEntity<>(userFormBean, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public void validateUser(@PathVariable("token") String token,HttpServletResponse response)
	{
		try
		{
			logger.info("UserController validateUser starts :::");
			String redirectUri = null;
			UserFormBean ufBean = userBo.validateUser(token);
			EFormAction formAction = ESecurity.Token.getTokenAction(token);
			if(EFormAction.PasswordChanged.name().equals(ufBean.messageCode))
				formAction = EFormAction.PasswordChanged;
			else if(EFormAction.TokenExpired.name().equals(ufBean.messageCode))
				formAction = EFormAction.TokenExpired;
			
			switch (formAction) {
			case ForgotPassword:
				redirectUri= getForgotPasswordUrl()+URLEncoder.encode(token,UTF8ENCODER);
				break;
			case Verify:
				redirectUri= getChangePasswordUrl()+URLEncoder.encode(token,UTF8ENCODER);
				break;
			case PasswordChanged:
				redirectUri = getPasswordChangedUrl()+URLEncoder.encode(token,UTF8ENCODER); 
				break;
			case TokenExpired:
				redirectUri = getPasswordLinkExpiredUrl()+URLEncoder.encode(token,UTF8ENCODER);
				break;
			default:
				break;
			}
			logger.info("UserController validateUser ends ::: " ,redirectUri);
			response.sendRedirect(redirectUri);

		}
		catch (InvalidKeyException excep)
		{
			try {
				logger.error("InvalidKeyException in validateUser ::: ", excep);
				response.sendRedirect(getErrorPageUrl()+"?errorMsg="+URLEncoder.encode(excep.getMessage(),UTF8ENCODER));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			logger.error("Exception in UserController validateUser ::: ", e);
			e.printStackTrace();
		}
}

	@Override
	public ResponseEntity<?> getGroupUsers(Authentication auth, @RequestBody UserFormBean userFormBean)
	{
		try
		{
			logger.info("Inside UserController getGroupUsers ::: ");
			if (CommonValidator.isNotNullNotEmpty(userFormBean.searchParam) && userFormBean.searchParam.length() > 3)
			{
				return new ResponseEntity<>(userBo.getUserGroupSearch(auth, userFormBean), HttpStatus.OK);
			}

			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			userFormBean.user = null;
			userFormBean.repoUser = null;
			userFormBean.messageCode = excep.getMessage();
			logger.error("Exception in UserController getGroupUsers ::: ", excep);
			return new ResponseEntity<>(userFormBean, HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<?> getCountry(Authentication auth, @RequestBody UserFormBean userFormBean)
	{
		try
		{
			logger.info("Inside UserController getCountry ::: ");
			return new ResponseEntity<>(userBo.getCountryList(auth, userFormBean), HttpStatus.OK);
		}
		catch (Exception excep)
		{
			userFormBean.user = null;
			userFormBean.repoUser = null;
			userFormBean.messageCode = excep.getMessage();
			logger.error("Exception in UserController getCountry ::: ", excep);
			return new ResponseEntity<>(userFormBean, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> getStates(Authentication auth, @RequestBody UserFormBean userFormBean)
	{
		try
		{
			logger.info("Inside UserController getStates ::: ");
			return new ResponseEntity<>(userBo.getStateList(auth, userFormBean), HttpStatus.OK);
		}
		catch (Exception excep)
		{
			userFormBean.user = null;
			userFormBean.repoUser = null;
			userFormBean.messageCode = excep.getMessage();
			logger.error("Exception in UserController getStates ::: ", excep);
			return new ResponseEntity<>(userFormBean, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> getCities(Authentication auth, @RequestBody UserFormBean userFormBean)
	{
		try
		{
			logger.info("Inside UserController getCities ::: ");
			return new ResponseEntity<>(userBo.getCityList(auth, userFormBean), HttpStatus.OK);
		}
		catch (Exception excep)
		{
			userFormBean.user = null;
			userFormBean.repoUser = null;
			userFormBean.messageCode = excep.getMessage();
			logger.error("Exception in UserController getCities ::: ", excep);
			return new ResponseEntity<>(userFormBean, HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<?> getAllUsers(Authentication auth) 
	{
		try
		{
			logger.info("Inside UserController getAllUsers ::: ");
			return new ResponseEntity<List<Users>>(userBo.getAllUsers(),HttpStatus.OK);
		}
		catch(Exception e) 
		{
			UserFormBean userFormBean = new UserFormBean();
			userFormBean.messageCode = e.getMessage();
			logger.error("Exception in UserController getAllUsers ::: ", e);
			return new ResponseEntity<>(userFormBean, HttpStatus.BAD_REQUEST);
		}
	}
	
	@Override
	public ResponseEntity<?> resendActivationLink(Authentication auth,@RequestHeader(value="Authorization") String token,@RequestBody UserFormBean userFormBean) {
		try 
		{
			logger.info("Inside UserController resendActivationLink ::: ");
			userFormBean.authToken = token;
			return new ResponseEntity<>(userBo.resendActivationLink(auth, userFormBean),HttpStatus.OK);
		}
		catch(Exception e)
		{
			userFormBean.user = null;
			userFormBean.messageCode = e.getMessage();
			logger.error("Exception in UserController resendActivationLink ::: ", e);
			return new ResponseEntity<>(userFormBean, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> getUsersListByRoleName(Authentication auth, @RequestBody UserFormBean ufBean)
	{
		try
		{
			logger.info("Inside UserController getUsersList ::: ");
			return new ResponseEntity<List<Users>>(userBo.getUsersListByRoleName(auth,ufBean.roleName), HttpStatus.OK);
		}
		catch (Exception excep)
		{
			UserFormBean userFormBean = new UserFormBean();
			userFormBean.user = null;
			userFormBean.repoUser = null;
			userFormBean.messageCode = excep.getMessage();
			logger.error("Exception in UserController getUserByCustomer ::: ", excep);
			return new ResponseEntity<>(userFormBean, HttpStatus.BAD_REQUEST);
		}
	}
}
