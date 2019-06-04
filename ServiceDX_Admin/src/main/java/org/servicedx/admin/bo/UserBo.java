package org.servicedx.admin.bo;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.util.Collection;
import java.util.List;

import org.apache.kafka.common.errors.InvalidRequestException;
import org.servicedx.bean.UserFormBean;
import org.servicedx.bean.model.Users;
import org.servicedx.util.EnumInterface;
import org.servicedx.util.LabelValueBean;
import org.springframework.security.core.Authentication;

public interface UserBo extends Serializable
{
	EnumInterface blockUser(Authentication auth, UserFormBean ufBean) throws InvalidRequestException;

	EnumInterface deleteUser(Authentication auth, UserFormBean ufBean) throws InvalidRequestException;

	Users getUser(UserFormBean ufBean) throws InvalidRequestException;
	
	Users getActiveUser (UserFormBean ufBean) throws InvalidRequestException;

	List<Users> getUserByCustomer(Authentication auth);
	
	List<Users> getUsersListByRoleName(Authentication auth, String roleName);
	
	EnumInterface saveUser(Authentication auth, UserFormBean ufBean) throws InvalidRequestException, InvalidKeyException;

	List<Users> searchUser(UserFormBean ufBean) throws InvalidRequestException;

	EnumInterface updateUser(Authentication auth, UserFormBean ufBean) throws InvalidRequestException;

	UserFormBean validateUser(String token) throws InvalidKeyException;

	Collection<LabelValueBean> getUsersBySearchParam(Authentication auth, UserFormBean gmfBean);
	
	Collection<LabelValueBean> getUserGroupSearch(Authentication auth, UserFormBean gmfBean);

	List<LabelValueBean> getCountryList(Authentication auth, UserFormBean userFormBean);

	List<LabelValueBean> getStateList(Authentication auth, UserFormBean userFormBean);

	List<LabelValueBean> getCityList(Authentication auth, UserFormBean userFormBean);

	List<Users> getAllUsers();
	
	EnumInterface resendActivationLink(Authentication auth, UserFormBean userFormBean);
}
