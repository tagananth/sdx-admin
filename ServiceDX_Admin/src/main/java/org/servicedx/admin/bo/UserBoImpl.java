package org.servicedx.admin.bo;

import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.apache.kafka.common.errors.InvalidRequestException;
import org.servicedx.event.service.AdminProducer;
import org.servicedx.security.resource.IErrorAdmin;
import org.servicedx.security.resource.IPathAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.servicedx.bean.UserFormBean;
import org.servicedx.bean.model.Users;
import org.servicedx.bean.util.RestClientUtil;
import org.servicedx.dao.SequenceDao;
import org.servicedx.util.CommonValidator;
import org.servicedx.util.EnumInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datastax.driver.core.Row;

@Service
@Transactional
public class UserBoImpl extends UserBoComboBoxImpl implements UserBo, IErrorAdmin,IPathAdmin
{

	private static final long	serialVersionUID	= 7255672818512788055L;
	
	private static final String UTF8ENCODER = "UTF-8";

	private final Logger		logger				= LoggerFactory.getLogger(UserBoImpl.class);
	
	@Value("${server.domain.url}")
	public String				domainURL;

	@Autowired
	AdminProducer				producer;

	@Autowired
	private SequenceDao			sequenceDao;

	@Override
	public EnumInterface blockUser(Authentication auth, UserFormBean ufBean) throws InvalidRequestException
	{
		if (isRecentlyUpdated(ufBean))
		{
			try
			{
				logger.info("Inside UserBoImpl blockUser ::: ",ufBean.user.getUserId());
				ufBean.repoUser.setStatus(!ufBean.user.isStatus());// Negate Current Status
				ufBean.repoUser.modifiedUserInfo(auth);
				userDao.save(ufBean.repoUser);

				ufBean.messageCode = USER_BLOCKED_SUCCESSFULLY;
				return EReturn.Success;
			}
			finally
			{
				ufBean.tokenURL = null;
				ufBean.user = null;
			}
		}
		throw new InvalidRequestException(USER_DATA_UPDATED_RECENTLY);
	}

	@Override
	public EnumInterface deleteUser(Authentication auth, UserFormBean ufBean) throws InvalidRequestException
	{
		logger.info("Inside UserBoImpl deleteUser ::: ",ufBean.user.getUserId());
		userDao.deleteById(ufBean.user.getUserId());
		return EReturn.Success;
	}

	@Override
	public Users getUser(UserFormBean ufBean) throws InvalidRequestException
	{
		return userDao.findByUserId(ufBean.user.getUserId());
	}
	
	@Override
	public Users getActiveUser(UserFormBean ufBean) throws InvalidRequestException
	{
		return userDao.findByActiveUserId(ufBean.user.getUserId());
	}

	@Override
	public List<Users> getUserByCustomer(Authentication auth) throws InvalidRequestException
	{		
		return userDao.fetchByCustomer(EAuth.User.getCustomerId(auth));
	}

	
	private boolean isRecentlyUpdated(UserFormBean ufBean)
	{
		logger.info("Inside UserBoImpl isRecentlyUpdated ::: ",ufBean.user.getUserId());
		if (CommonValidator.isNotNullNotEmpty(ufBean.user))
		{
			Users user = userDao.findByUserId(ufBean.user.getUserId());
			if (CommonValidator.isNotNullNotEmpty(user))
			{
				if (ChronoUnit.NANOS.between(ufBean.user.getModifiedDate(), user.getModifiedDate()) == 0)
				{
					ufBean.repoUser = user;
					return true;
				}
				return false;
			}
		}
		throw new InvalidRequestException(USER_NOT_FOUND);
	}

	@Override
	public EnumInterface saveUser(Authentication auth, UserFormBean ufBean) throws InvalidRequestException, InvalidKeyException
	{

		logger.info("UserBoImpl saveUser starts ::: ");
		Row row = userDao.checkUserName(ufBean.user.getUserName());
		if (CommonValidator.isNullOrEmpty(row) || EAuth.User.getCustomerId(auth) != row.getLong(1))
		{
			if (EAuth.User.isSuperAdmin(auth))
			{
				ufBean.user.setRoleId(ERole.Administrator.name());
			}
			else 
			{
				ufBean.user.setRoleId(ERole.valueOf(ufBean.user.getRoleName()).name());
			}
			
			ufBean.tokenURL = domainURL + ESecurity.Token.generate(ufBean.user,EFormAction.Verify);
			
			ufBean.user.setUserId(sequenceDao.getPrimaryKey(Users.class));
			ufBean.user.setActive(false);
			ufBean.user.setPwdChangeFlag(true);
			ufBean.user.createdUserCustomerInfo(auth);
			ufBean.user = userDao.insert(ufBean.user);

			if (CommonValidator.isNotNullNotEmpty(ufBean.user, ufBean.tokenURL))
			{
				try
				{
					String status = RestClientUtil.sendUserCreationMail(ufBean);
					logger.info("saveUser status ::: ",status);
					
					/*producer.sendEmailMessage(ETemplate.User_Create_Admin, ufBean);
					producer.sendEmailMessage(ETemplate.User_Create_Employee, ufBean);
					producer.sendSMSMessage(ETemplate.SMS_Create_Employee, ufBean);*/
					
					ufBean.messageCode = USER_CREATED_SUCCESSFULLY;
					return EReturn.Success;
				}
				finally
				{
					ufBean.tokenURL = null;
					ufBean.user = null;
				}
			}
			throw new InvalidKeyException(USER_TOKEN_KEY_GENERATE_ISSUE);
		}
		throw new InvalidRequestException(USER_ALREADY_EXISTS);
	}

	@Override
	public List<Users> searchUser(UserFormBean ufBean) throws InvalidRequestException
	{
		if (CommonValidator.isNullOrEmpty(ufBean.user.getUserName()))
		{
			ufBean.user.setUserName("");
		}

		return userDao.findByUserName(EWrap.Percent.enclose(ufBean.user.getUserName()));
	}

	@Override
	public EnumInterface updateUser(Authentication auth, UserFormBean ufBean) throws InvalidRequestException
	{
		if (isRecentlyUpdated(ufBean))
		{
			try
			{
				logger.info("UserBoImpl updateUser starts ::: ");
				if (ufBean.whetherPrimaryMediaUpdated())
				{
//					producer.sendEmailMessage(ETemplate.User_Primary_Media_Update, ufBean);
				}
				ufBean.updateRepoUser(auth);
				userDao.save(ufBean.repoUser);

				ufBean.messageCode = USER_UPDATED_SUCCESSFULLY;
				logger.info("UserBoImpl updateUser ends ::: ");
				return EReturn.Success;
			}
			finally
			{
				ufBean.tokenURL = null;
				ufBean.user = null;
			}
		}
		throw new InvalidRequestException(USER_DATA_UPDATED_RECENTLY);
	}

	@Override
	public UserFormBean validateUser(String tokenKey) throws InvalidKeyException
	{
		if (CommonValidator.isNotNullNotEmpty(tokenKey))
		{
			logger.info("Inside UserBoImpl validateUser ::: ");
			UserFormBean ufBean = ESecurity.Token.validate(userDao, tokenKey, TOKEN_EXPIRY_DURATION);

			if (CommonValidator.isNotNullNotEmpty(ufBean))
			{
				return ufBean;
			}
		}
		throw new InvalidKeyException(USER_TOKEN_KEY_NOT_AVAILABLE_IN_REQUEST);
	}

	@Override
	public List<Users> getAllUsers()
	{
		return userDao.getAllUsers();
	}

	@Override
	public EnumInterface resendActivationLink(Authentication auth,UserFormBean ufBean) {
		ufBean.user = getUser(ufBean);
		ufBean.tokenURL = domainURL + ESecurity.Token.generate(ufBean.user,EFormAction.Verify);
		ufBean.user.setModifiedDate(LocalDateTime.now());
		ufBean.user.setActive(false);
		userDao.save(ufBean.user);
		if (CommonValidator.isNotNullNotEmpty(ufBean.user, ufBean.tokenURL))
		{
			try
			{
				String status = RestClientUtil.sendUserCreationMail(ufBean);
				logger.info("SendMail status ::: ",status);
				producer.sendEmailMessage(ETemplate.User_Create_Admin, ufBean);
				producer.sendEmailMessage(ETemplate.User_Create_Employee, ufBean);
				producer.sendSMSMessage(ETemplate.SMS_Create_Employee, ufBean);
				
				ufBean.messageCode = ACTIVATION_LINK_SENT_SUCCESSFULLY;
				return EReturn.Success;
			}
			finally
			{
				ufBean.tokenURL = null;
				ufBean.user = null;
			}
		}
		return EReturn.Success;
	}

	@Override
	public List<Users> getUsersListByRoleName(Authentication auth, String roleName)
	{
		return userDao.fetchUsersListByRole(roleName, EAuth.User.getCustomerId(auth));
	}
	
}
