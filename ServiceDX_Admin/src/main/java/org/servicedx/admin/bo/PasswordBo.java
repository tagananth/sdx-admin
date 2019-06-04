package org.servicedx.admin.bo;

import java.io.Serializable;

import org.servicedx.bean.PasswordFormBean;
import org.servicedx.bean.UserFormBean;
import org.servicedx.util.EnumInterface;

public interface PasswordBo extends Serializable
{
	EnumInterface changePassword(PasswordFormBean password);

	EnumInterface forgotPassowrd(UserFormBean user);
	
	
	/**
	 * Should contain at least one digit
	 * Should contain at least one lower case letter
	 * Should contain at least one upper case letter
	 * Should contain at least one special character
	 * length should be between 6 to 20
	 * 
	 */
	public static String PASSWORD_VALIDATION_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{6,20}$";
}
