package org.servicedx.security.resource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import javax.management.openmbean.InvalidKeyException;

import org.apache.commons.codec.binary.Base64;
import org.servicedx.bean.UserFormBean;
import org.servicedx.bean.model.Users;
import org.servicedx.dao.UserDao;
import org.servicedx.util.CommonValidator;
import org.servicedx.util.EnumInterface;

public interface IPathAdmin extends IPath, IErrorAdmin
{
	// User Admin Module
	public String	PRESEARCH_USER				= "/preSearchUser";
	public String	GET_USERS_LIST_BY_ROLE		= "/getUsersListByRoleName";
	public String	GET_ACTIVE_USER				= "/getActiveUser";
	public String	SEARCH_COUNTRY				= "/searchCountry";
	public String	SEARCH_STATE				= "/searchState";
	public String	SEARCH_CITY					= "/searchCity";
	public String	SEARCH_USER					= "/searchUser";
	public String	SEARCH_GROUP_USER			= "/searchGroupUser";
	public String	PREADD_USER					= "/preAddUser";
	public String	ADD_USER					= "/addUser";
	public String	PREUPDATE_USER				= "/preUpdateUser";
	public String	UPDATE_USER					= "/updateUser";
	public String	BLOCK_USER					= "/blockUser";
	public String	DELETE_USER					= "/deleteUser";
	public String	VALIDATE_USER_BASE			= "/validateUser";
	public String	VALIDATE_USER				= VALIDATE_USER_BASE + "/{token}";
	public String 	RESEND_ACTIVATION_LINK		= "/resendActivationLink";
	public String 	GET_ALL_USERS				= "getAllUsers";
	public String 	GET_USER_BY_CUSTID			= "/getUserByCustomer";
	// Group
	public String	GET_GROUP_LIST				= "/getGroupList";
	public String	GET_GROUP					= "/getGroup";
	public String	GET_GROUP_BY_CUST_ID		= "/getGroupByCustomer";
	public String	ADD_GROUP					= "/addGroup";
	public String	UPDATE_GROUP				= "/updateGroup";
	public String	DELETE_GROUP				= "/deleteGroup";
	public String	BLOCK_GROUP					= "/blockGroup";
	public String	SEARCH_GROUP				= "/searchGroup";
	public String	CHECK_GROUP_EXISTS			= "/checkGroupExists";

	// Group
	public String	GET_GROUPMEMBER_LIST		= "/getGroupMemberList";
	public String	GET_GROUPMEMBERS_BY_GROUP	= "/getGroupMembersByGroup";
	public String	GET_GROUPMEMBERS			= "/getGroupMembers";
	public String	ADD_GROUPMEMBERS			= "/addGroupMembers";
	public String	UPDATE_GROUPMEMBERS			= "/updateGroupMembers";
	public String	DELETE_GROUPMEMBERS			= "/deleteGroupMembers";
	public String	BLOCK_GROUPMEMBERS			= "/deleteGroupMemberDataById";

	// Customer
	public String	PRESEARCH_CUSTOMER			= "/preSearchCustomer";
	public String	SEARCH_CUSTOMER				= "/searchCustomer";
	public String	ADD_CUSTOMER				= "/addCustomer";
	public String	PREUPDATE_CUSTOMER			= "/getCustomer";
	public String	UPDATE_CUSTOMER				= "/updateCustomer";
	public String	CUSTOMER_LIST				= "/getCustomerList";
	public String	BLOCK_CUSTOMER				= "/blockCustomer";
	public String	DELETE_CUSTOMER				= "/deleteCustomer";
	public String	CHECK_CUSTOMER_EXIST		= "/checkCustomerExist";

	// Password
	public String	GENERATE_OTP				= "/generateOTP";
	public String	VALIDATE_OTP				= "/validateOTP";
	public String	CHANGE_PASSWORD				= "/changePassword";
	public String	FORGOT_PASSWORD				= "/forgotPassword";
	public String	RESET_PASSWORD				= "/resetPassword";
	public long		TOKEN_EXPIRY_DURATION		= 86400000l;
	
	
	//Department
	public String	UPDATE_DEPARTMENT			= "/updateDepartment";
	public String	BLOCK_DEPARTMENT			= "/blockDepartment";
	public String	CHECK_DEPARTMENT_EXIST		= "/checkDepartmentExist";
	public String	ADD_DEPARTMENT				= "/addDepartment";
	public String 	GET_DEPARTMENT_LIST			= "/getDepartmentList";
	public String 	GET_DEPARTMENT				= "/getDepartment";

	public enum EPathAdmin implements EnumResourceInterface
	{
		PreSearchCustomer(PRESEARCH_CUSTOMER, ERole.Administrator), //
		SearchCustomer(SEARCH_CUSTOMER, ERole.Administrator), //
		AddCustomer(ADD_CUSTOMER, ERole.Administrator), //
		PreUpdateCustomer(PREUPDATE_CUSTOMER, ERole.Administrator), //
		UpdateCustomer(UPDATE_CUSTOMER, ERole.Administrator), //
		BlockCustomer(BLOCK_CUSTOMER, ERole.Administrator), //
		CheckCustomerExist(CHECK_CUSTOMER_EXIST, ERole.Administrator), //

//		GenerateOTP(GENERATE_OTP, ERole.Administrator, ERole.Supervisor, ERole.User), //
//		ValidateOTP(VALIDATE_OTP, ERole.Administrator, ERole.Supervisor, ERole.User), //
		//ChangePassword(CHANGE_PASSWORD, ERole.Administrator, ERole.Supervisor, ERole.User), //

		PreSearchUser(PRESEARCH_USER, ERole.Administrator, ERole.Supervisor), //
		GetActiveUser(GET_ACTIVE_USER, ERole.Administrator, ERole.Supervisor), //
		SearchUser(SEARCH_USER, ERole.Administrator, ERole.Supervisor), //
		SearchGroupUser(SEARCH_GROUP_USER, ERole.Administrator, ERole.Supervisor), //
		PreAddUser(PREADD_USER, ERole.Administrator), //
		AddUser(ADD_USER, ERole.Administrator), //
		PreUpdateUser(PREUPDATE_USER, ERole.Administrator), //
		UpdateUser(UPDATE_USER, ERole.Administrator), //
		BlockUser(BLOCK_USER, ERole.Administrator, ERole.Supervisor), //
		DeleteUser(DELETE_USER, ERole.Administrator),
		GetAllUsers(GET_ALL_USERS,ERole.Administrator, ERole.Supervisor),
		GetUsersByCustomer(GET_USER_BY_CUSTID,ERole.Administrator, ERole.Supervisor),
		//ValidateUser(VALIDATE_USER),

		// Group
		getGroupList(GET_GROUP_LIST, ERole.Administrator), // Dummy To Delete
		GetGroup(GET_GROUP, ERole.Administrator, ERole.Supervisor), //
		GetGroupByCustomer(GET_GROUP_BY_CUST_ID, ERole.Administrator, ERole.Supervisor), //
		SaveGroup(ADD_GROUP, ERole.Administrator), //
		UpdateGroup(UPDATE_GROUP, ERole.Administrator), //
		BlockGroup(BLOCK_GROUP, ERole.Administrator), //
		SearchGroup(SEARCH_GROUP, ERole.Administrator), //
		CheckGroupExists(CHECK_GROUP_EXISTS, ERole.Administrator, ERole.Supervisor), //

		// Group Member
		getGroupMemberList(GET_GROUPMEMBER_LIST, ERole.Administrator), // Dummy To Delete
		GetGroupMembers(GET_GROUPMEMBERS, ERole.Administrator, ERole.Supervisor), //
		GetGroupMembersByGroup(GET_GROUPMEMBERS_BY_GROUP, ERole.Administrator, ERole.Supervisor), //
		SaveGroupMembers(ADD_GROUPMEMBERS, ERole.Administrator), //
		UpdateGroupMembers(UPDATE_GROUPMEMBERS, ERole.Administrator), //
		BlockGroupMembers(BLOCK_GROUPMEMBERS, ERole.Administrator), //
		DeleteGroupMembers(DELETE_GROUPMEMBERS, ERole.Administrator), //
		
		//Department
		updateDepartment(UPDATE_DEPARTMENT, ERole.Administrator), //
		blockDepartment(BLOCK_DEPARTMENT, ERole.Administrator), //
		checkDepartmentExist(CHECK_DEPARTMENT_EXIST, ERole.Administrator), //
		getDepartmentList(GET_DEPARTMENT_LIST, ERole.Administrator), //
		addDepartment(ADD_DEPARTMENT, ERole.Administrator); //


		String	path;

		ERole	roles[];

		EPathAdmin(String path, ERole... roles)
		{
			this.path = path;
			this.roles = roles;
		}

		@Override
		public String getPath()
		{
			return this.path;
		}

		@Override
		public ERole[] getRoles()
		{
			return this.roles;
		}
	}

	public enum ESecurity implements EnumInterface
	{
		Token;

		public String generate(Users user,EFormAction eFormAction)
		{
			String tokenKey = UUID.randomUUID().toString();
			user.setTokenKey(tokenKey);
			user.setTokenKeyExpiryDate(LocalDateTime.now());
			user.setPwdChangeFlag(false);
			tokenKey = Base64.encodeBase64String((user.getEmailId() + HASH + user.getTokenKey() + HASH + eFormAction.name()).getBytes());
			StringBuffer tokenURL = new StringBuffer();
			tokenURL.append(VALIDATE_USER_BASE+SLASH);
			tokenURL.append(new StringBuffer(tokenKey).reverse());
			return tokenURL.toString();
		}

		public UserFormBean validate(UserDao userDao, String tokenKey, long expiryDuration)
		{
			UserFormBean ufBean = new UserFormBean();
			tokenKey = decodeToken(tokenKey);

			String token[] = tokenKey.split(HASH);

			ufBean.user = userDao.getByEmailId(token[0]);

			if (CommonValidator.isNotNullNotEmpty(ufBean.user))
			{				
				if (CommonValidator.isEqual(token[1], ufBean.user.getTokenKey()))
				{
					if (ufBean.user.isPwdChangeFlag() || EFormAction.ForgotPassword.name().equalsIgnoreCase(token[2])
							|| EFormAction.Verify.name().equalsIgnoreCase(token[2]))
					{
						long difference = ChronoUnit.MILLIS.between(ufBean.user.getTokenKeyExpiryDate(),LocalDateTime.now());
	
						if (difference <= expiryDuration)
						{
							ufBean.messageCode = EReturn.Success.name();
							return ufBean;
						}
						else
						{
							ufBean.messageCode = EFormAction.TokenExpired.name();
							return ufBean;
						}
					}
				}
				else if(CommonValidator.isNotNullNotEmpty(ufBean.user, ufBean.user.getPassword()))
				{
					ufBean.messageCode = EFormAction.PasswordChanged.name();
					return ufBean;
				}
			}
			throw new InvalidKeyException(USER_TOKEN_KEY_NOT_EXISTS);
		}
		
		private String decodeToken(String tokenKey) {
			tokenKey = new StringBuffer(tokenKey).reverse().toString();
			return new String(Base64.decodeBase64(tokenKey));
		
		}
		
		public EFormAction getTokenAction(String tokenKey) {
			tokenKey = decodeToken(tokenKey);
			String token[] = tokenKey.split(HASH);
			if(token.length > 2) {
				return EFormAction.valueOf(token[2]);
			}
				throw new InvalidKeyException(USER_TOKEN_KEY_NOT_EXISTS);
		}
	}
}
