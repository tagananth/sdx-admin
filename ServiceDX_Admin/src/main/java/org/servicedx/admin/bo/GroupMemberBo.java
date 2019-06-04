package org.servicedx.admin.bo;

import java.security.InvalidKeyException;
import java.util.List;

import org.servicedx.bean.GroupMemberFormBean;
import org.servicedx.bean.model.GroupMember;
import org.servicedx.util.EnumInterface;
import org.springframework.security.core.Authentication;

public interface GroupMemberBo
{
	EnumInterface deleteGroupMembersByGroupId(Authentication auth, GroupMemberFormBean gmfBean);

	List<GroupMember> getGroupMembersByGroupId(Authentication auth, GroupMemberFormBean gmfBean);

	GroupMember getGroupMemberById(Authentication auth, GroupMemberFormBean gmfBean);

	List<GroupMember> getGroupMemberList(Authentication auth);

	EnumInterface saveGroupMembers(Authentication auth, GroupMemberFormBean gmfBean) throws InvalidKeyException;

	EnumInterface updateGroupMember(Authentication auth, GroupMemberFormBean gmfBean) throws InvalidKeyException;

}
