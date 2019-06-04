package org.servicedx.admin;

import java.util.List;

import org.servicedx.admin.bo.GroupMemberBo;
import org.servicedx.admin.bo.UserBo;
import org.servicedx.bean.GroupMemberFormBean;
import org.servicedx.bean.model.GroupMember;
import org.servicedx.util.CommonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupMemberController implements IGroupMemberController
{

	private static final long	serialVersionUID	= 3191910671429713214L;

	@Autowired
	GroupMemberBo				groupMemberBo;

	@Autowired
	UserBo						userBo;

	@Override
	public ResponseEntity<?> deleteGroupMembers(Authentication auth, GroupMemberFormBean gmfBean)
	{
		try
		{

			if (CommonValidator.isNotNullNotEmpty(gmfBean.groupMemberId))
			{
				return new ResponseEntity<>(groupMemberBo.deleteGroupMembersByGroupId(auth, gmfBean), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			gmfBean.groupMemberList = null;
			gmfBean.groupMember = null;
			gmfBean.repoGroupMember = null;
			gmfBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(gmfBean, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> getGroupMembersByGroupId(Authentication auth, GroupMemberFormBean gmfBean)
	{
		try
		{
			if (CommonValidator.isNotNullNotEmpty(gmfBean.groupMember.getGroupId()))
			{
				return new ResponseEntity<List<GroupMember>>(groupMemberBo.getGroupMembersByGroupId(auth, gmfBean), HttpStatus.OK);
			}

			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			gmfBean.groupMemberList = null;
			gmfBean.groupMember = null;
			gmfBean.repoGroupMember = null;
			gmfBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(gmfBean, HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity<?> saveGroupMembers(Authentication auth, GroupMemberFormBean gmfBean)
	{
		try
		{
			if (CommonValidator.isNotNullNotEmpty(gmfBean))
			{
				return new ResponseEntity<>(groupMemberBo.saveGroupMembers(auth, gmfBean), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			gmfBean.groupMemberList = null;
			gmfBean.groupMember = null;
			gmfBean.repoGroupMember = null;
			gmfBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(gmfBean, HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity<?> updateGroupMembers(Authentication auth, GroupMemberFormBean gmfBean)
	{

		try
		{
			groupMemberBo.updateGroupMember(auth, gmfBean);

			return new ResponseEntity<>(gmfBean, HttpStatus.OK);
		}
		catch (Exception excep)
		{
			gmfBean.groupMemberList = null;
			gmfBean.groupMember = null;
			gmfBean.repoGroupMember = null;
			gmfBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(gmfBean, HttpStatus.BAD_REQUEST);
		}
	}
}
