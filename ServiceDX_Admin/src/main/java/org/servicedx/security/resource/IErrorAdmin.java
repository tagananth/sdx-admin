package org.servicedx.security.resource;

import java.io.Serializable;

public interface IErrorAdmin extends Serializable
{
	public String	CUSTOMER_ALREADY_EXISTS					= "customer.already.exists";
	public String	CUSTOMER_BLOCKED_SUCCESSFULLY			= "customer.blocked.successfully";
	public String	CUSTOMER_CREATED_SUCCESSFULLY			= "customer.created.successfully";
	public String	CUSTOMER_DATA_UPDATED_RECENTLY			= "customer.data.updated.recently";
	public String	CUSTOMER_NOT_FOUND						= "customer.not.found";
	public String	CUSTOMER_UPDATED_SUCCESSFULLY			= "customer.updated.successfully";

	public String	GROUP_ALREADY_EXISTS					= "group.already.exists";
	public String	GROUP_BLOCKED_SUCCESSFULLY				= "group.blocked.successfully";
	public String	GROUP_CREATED_SUCCESSFULLY				= "group.created.successfully";
	public String	GROUP_DATA_UPDATED_RECENTLY				= "group.data.updated.recently";
	public String	GROUP_NOT_FOUND							= "group.not.found";
	public String	GROUP_UPDATED_SUCCESSFULLY				= "group.updated.successfully";

	public String	GROUPMEMBER_ALREADY_EXISTS				= "groupmember.already.exists";
	public String	GROUPMEMBER_BLOCKED_SUCCESSFULLY		= "groupmember.blocked.successfully";
	public String	GROUPMEMBER_CREATED_SUCCESSFULLY		= "groupmember.created.successfully";
	public String	GROUPMEMBER_DATA_UPDATED_RECENTLY		= "groupmember.data.updated.recently";
	public String	GROUPMEMBER_NOT_FOUND					= "groupmember.not.found";
	public String	GROUPMEMBER_UPDATED_SUCCESSFULLY		= "groupmember.updated.successfully";

	public String	INVALID_REQUEST_PARAMETERS				= "invalid.request.parameters";

	public String	USER_ALREADY_EXISTS						= "user.already.exists";
	public String	USER_BLOCKED_SUCCESSFULLY				= "user.blocked.successfully";
	public String	USER_CREATED_SUCCESSFULLY				= "user.created.successfully";
	public String	ACTIVATION_LINK_SENT_SUCCESSFULLY		= "activation.link.sent.successfully";
	public String	USER_DATA_UPDATED_RECENTLY				= "user.data.updated.recently";
	public String	USER_NOT_FOUND							= "user.not.found";
	public String	USER_TOKEN_KEY_GENERATE_ISSUE			= "user.key.generate.issue";
	public String	USER_TOKEN_KEY_NOT_AVAILABLE_IN_REQUEST	= "user.token.key.not.available.in.request";
	public String	USER_TOKEN_KEY_NOT_EXISTS				= "user.token.key.not.exists";
	public String	USER_UPDATED_SUCCESSFULLY				= "user.updated.successfully";

	public String	FORGOT_PASSWORD_MAIL_SUCCESS			= "password.reset.mail.sent.successfully";
	public String	FORGOT_PASSWORD_MAIL_FAILURE			= "password.reset.invalid.mail";

	public String	PASSWORD_SAME_AS_OLD_PASSWORD			= "password.cannot.be.same.as.old.password";
	public String	PASSWORD_STRENGTH_FAILURE				= "password.strength.failure";
	public String	PASSWORD_INVALID_OTP					= "password.invalid.otp";

	public String	DEPARTMENT_ALREADY_EXISTS				= "department.already.exists";
	public String	DEPARTMENT_BLOCKED_SUCCESSFULLY			= "department.blocked.successfully";
	public String	DEPARTMENT_CREATED_SUCCESSFULLY			= "department.created.successfully";
	public String	DEPARTMENT_DATA_UPDATED_RECENTLY		= "department.data.updated.recently";
	public String	DEPARTMENT_NOT_FOUND					= "department.not.found";
	public String	DEPARTMENT_UPDATED_SUCCESSFULLY			= "department.updated.successfully";
	public String	DEPARTMENT_UPDATED_ISSUE				= "department.update.issue";

}
