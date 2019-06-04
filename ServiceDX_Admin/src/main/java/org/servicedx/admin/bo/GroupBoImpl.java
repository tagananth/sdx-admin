package org.servicedx.admin.bo;

import java.security.InvalidKeyException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.apache.kafka.common.errors.InvalidRequestException;
import org.servicedx.security.resource.IErrorAdmin;
import org.servicedx.security.resource.IPath.EAuth;
import org.servicedx.security.resource.IPath.EReturn;
import org.servicedx.bean.GroupFormBean;
import org.servicedx.bean.model.Group;
import org.servicedx.dao.GroupDao;
import org.servicedx.dao.SequenceDao;
import org.servicedx.util.CommonValidator;
import org.servicedx.util.EnumInterface;
import org.servicedx.util.IConstProperty.EWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class GroupBoImpl implements GroupBo, IErrorAdmin
{
	private static final long	serialVersionUID	= 707067441871182593L;

	@Autowired
	private GroupDao			groupDao;

	@Autowired
	SequenceDao					sequenceDao;

	@Override
	public EnumInterface blockGroup(Authentication auth, GroupFormBean grpFormBean)
	{

		//if (isRecentlyUpdated(auth, grpFormBean))
		//{
			try
			{
				//grpFormBean.repoGroup.setStatus(!grpFormBean.group.isStatus());// Negate_Current_Status
				//grpFormBean.repoGroup.modifiedUserInfo(auth);
				//groupDao.save(grpFormBean.repoGroup);

				grpFormBean.group.modifiedUserInfo(auth);
				groupDao.blockGroup(grpFormBean.group.getModifiedDate(),
						grpFormBean.group.getModifiedBy(),grpFormBean.group.getModifiedByName(),grpFormBean.group.getGroupId());

				
				grpFormBean.messageCode = GROUP_BLOCKED_SUCCESSFULLY;
				return EReturn.Success;
			}
			finally
			{
				grpFormBean.group = null;
			}
		//} 		throw new InvalidRequestException(GROUP_DATA_UPDATED_RECENTLY);
	}

	@Override
	public EnumInterface checkGroupExists(Authentication auth, GroupFormBean grpFormBean)
	{
		if (CommonValidator.isNotNullNotEmpty(grpFormBean.group.getGroupName()))
		{
			int row = groupDao.checkGroupExists(EAuth.User.getCustomerId(auth), grpFormBean.group.getGroupName());

			return row > 0 ? EReturn.Exists : EReturn.Not_Exists;
		}

		throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
	}

	@Override
	public EnumInterface deleteGroup(Authentication auth, GroupFormBean grpFormBean)
	{
		if (CommonValidator.isNotNullNotEmpty(grpFormBean.group.getGroupId()))
		{
			groupDao.deleteGroup(EAuth.User.getCustomerId(auth), grpFormBean.group.getGroupId());
			return EReturn.Success;
		}
		throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
	}

	@Override
	public List<Group> getGroupByNameList(Authentication auth, GroupFormBean grpFormBean)
	{
		if (CommonValidator.isNullOrEmpty(grpFormBean.group.getGroupName()))
		{
			grpFormBean.group.setGroupName("");
		}
		return groupDao.findByGroupName(EAuth.User.getCustomerId(auth), EWrap.Percent.enclose(grpFormBean.group.getGroupName()));
	}

	@Override
	public Group getGroup(Group grpBean)
	{
		if (CommonValidator.isNotNullNotEmpty(grpBean.getGroupId()))
		{
			return groupDao.findByGroupId(grpBean.getGroupId());
		}
		throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
	}

	@Override
	public List<Group> getGroupByCustomer(Authentication auth)
	{
			return groupDao.fetchByCustomerId(EAuth.User.getCustomerId(auth));
	}

	@Override
	public List<Group> getGroupByIdList(Authentication auth, GroupFormBean grpFormBean)
	{
		if (CommonValidator.isNotNullNotEmpty(grpFormBean.groupIds))
		{
			return groupDao.getGroupList(auth, grpFormBean.groupIds);
		}
		throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
	}

	private boolean isRecentlyUpdated(Authentication auth, GroupFormBean grpFormBean)
	{

		if (CommonValidator.isNotNullNotEmpty(grpFormBean.group))
		{
			Group group = groupDao.findByGroupId(grpFormBean.group.getGroupId());

			if (CommonValidator.isNotNullNotEmpty(group))
			{
				if (ChronoUnit.NANOS.between(grpFormBean.group.getModifiedDate(), group.getModifiedDate()) == 0)
				{
					grpFormBean.repoGroup = group;
					return true;
				}
				return false;
			}
		}
		throw new InvalidRequestException(GROUP_NOT_FOUND);

	}

	@Override
	public EnumInterface saveGroup(Authentication auth, GroupFormBean grpFormBean) throws InvalidKeyException
	{
		List<Group> groupList = groupDao.findByGroupName(EAuth.User.getCustomerId(auth), grpFormBean.group.getGroupName());

		if (CommonValidator.isNullOrEmpty(groupList))
		{
			grpFormBean.group.setGroupId(sequenceDao.getPrimaryKey(Group.class));
			//Y False ?
			//grpFormBean.group.setActive(false);
			grpFormBean.group.createdUserCustomerInfo(auth);
			grpFormBean.group = groupDao.insert(grpFormBean.group);

			if (CommonValidator.isNotNullNotEmpty(grpFormBean.group))
			{
				grpFormBean.messageCode = GROUP_CREATED_SUCCESSFULLY;
				return EReturn.Success;
			}
		}
		throw new InvalidRequestException(GROUP_ALREADY_EXISTS);

	}

	@Override
	public EnumInterface updateGroup(Authentication auth, GroupFormBean grpFormBean)
	{
		if (isRecentlyUpdated(auth, grpFormBean))
		{
			try
			{
				grpFormBean.updateRepoGroup(auth);
				groupDao.save(grpFormBean.repoGroup);

				grpFormBean.messageCode = GROUP_UPDATED_SUCCESSFULLY;
				return EReturn.Success;
			}
			finally
			{
				grpFormBean.group = null;
			}
		}
		throw new InvalidRequestException(GROUP_DATA_UPDATED_RECENTLY);
	}
}
