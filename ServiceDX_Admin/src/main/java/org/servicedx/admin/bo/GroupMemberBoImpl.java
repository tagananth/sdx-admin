package org.servicedx.admin.bo;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.common.errors.InvalidRequestException;
import org.servicedx.security.resource.IErrorAdmin;
import org.servicedx.security.resource.IPath;
import org.servicedx.bean.GroupMemberFormBean;
import org.servicedx.bean.model.GroupMember;
import org.servicedx.dao.GroupMemberDao;
import org.servicedx.dao.SequenceDao;
import org.servicedx.util.CommonValidator;
import org.servicedx.util.EBusinessKey.EKey;
import org.servicedx.util.EnumInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class GroupMemberBoImpl implements GroupMemberBo, IErrorAdmin, IPath
{

	private static final long	serialVersionUID	= 7302354768695998731L;

	@Autowired
	private GroupMemberDao		groupMemberDao;

	@Autowired
	private SequenceDao			sequenceDao;

	@Override
	public EnumInterface deleteGroupMembersByGroupId(Authentication auth, GroupMemberFormBean gmfBean)
	{
		if (CommonValidator.isNotNullNotEmpty(gmfBean.groupId))
		{
			groupMemberDao.deleteGroupMembersByGroupId(EAuth.User.getCustomerId(auth), EWrap.PercentWithHash.enclose(gmfBean.groupId));
			return EReturn.Success;
		}

		if (CommonValidator.isNotNullNotEmpty(gmfBean.groupMemberId))
		{
			groupMemberDao.deleteGroupMemberById(Long.parseLong(gmfBean.groupMemberId));
			return EReturn.Success;
		}

		throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
	}
	
	@Override
	public List<GroupMember> getGroupMembersByGroupId(Authentication auth, GroupMemberFormBean gmfBean)
	{
		System.out.println(EAuth.User.getCustomerId(auth)+"-------------"+ EWrap.PercentWithHash.enclose(gmfBean.groupMember.getGroupId()));
		return groupMemberDao.findGroupMembersByGroupId(EAuth.User.getCustomerId(auth), EWrap.PercentWithHash.enclose(gmfBean.groupMember.getGroupId()));
	}

	@Override
	public List<GroupMember> getGroupMemberList(Authentication auth)
	{
		return groupMemberDao.findGroupMembersByGroupId(EAuth.User.getCustomerId(auth), EWrap.Percent.enclose(""));
	}

	@Override
	public GroupMember getGroupMemberById(Authentication auth, GroupMemberFormBean gmfBean)
	{
		if (CommonValidator.isNotNullNotEmpty(gmfBean.groupMemberId))
		{
			return groupMemberDao.findByGroupMemberId(EAuth.User.getCustomerId(auth), "#"+Long.parseLong(gmfBean.groupMemberId)+"#");
		}
		throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
	}

	@Override
	public EnumInterface saveGroupMembers(Authentication auth, GroupMemberFormBean gmfBean) throws InvalidKeyException
	{
		// Need to Fix
		List<GroupMember> groupMemberList = gmfBean.groupMemberList;
		List<GroupMember> saveList = new ArrayList<>();
		for (GroupMember grpMember : gmfBean.groupMemberList)
		{
			if (grpMember.getParentGroupId() != null)
			{
				gmfBean.groupMember.setGroupId(grpMember.getGroupId());
				for (GroupMember existingGrpMember : getGroupMembersByGroupId(auth, gmfBean))
				{
					existingGrpMember.setGroupMemberId(Long.parseLong(EKey.AutoUnMask("")));
					if (CommonValidator.isNullOrEmpty(existingGrpMember.getParentGroupId()))
						existingGrpMember.setParentGroupId("#"+String.valueOf(grpMember.getGroupId())+"#");
					else
						existingGrpMember.setParentGroupId("#"+grpMember.getParentGroupId() + "# > #" + existingGrpMember.getParentGroupId()+"#");
					existingGrpMember.setGroupId(Long.valueOf(grpMember.getParentGroupId()));
					existingGrpMember.setGroupName(grpMember.getGroupName());
					existingGrpMember.setActive(grpMember.isActive());
					existingGrpMember.setComments(grpMember.getComments());
				//	existingGrpMember.setUserContact(grpMember.getUserContact());
					existingGrpMember.createdUserCustomerInfo(auth);
					saveList.add(existingGrpMember);
				}
			}
			else
			{
				grpMember.setGroupMemberId(Long.parseLong(EKey.AutoUnMask("")));
				grpMember.setParentGroupId("#"+String.valueOf(grpMember.getGroupId())+"#");
				grpMember.createdUserCustomerInfo(auth);
				saveList.add(grpMember);
			}
		}
		if (!saveList.isEmpty())
			groupMemberDao.insert(saveList).size();

		return EReturn.Success;

	}

	@Override
	public EnumInterface updateGroupMember(Authentication auth, GroupMemberFormBean gmfBean) throws InvalidKeyException
	{
		List<GroupMember> groupMemberList = getGroupMembersByGroupId(auth, gmfBean);
		if (deleteGroupMembersByGroupId(auth, gmfBean) == EReturn.Success)
		{
			try
			{
				return saveGroupMembers(auth, gmfBean);
			}
			catch (Exception excep)
			{
				gmfBean.groupMemberList = groupMemberList;
				return saveGroupMembers(auth, gmfBean);
			}
		}
		throw new InvalidRequestException(GROUPMEMBER_NOT_FOUND);
	}

}
