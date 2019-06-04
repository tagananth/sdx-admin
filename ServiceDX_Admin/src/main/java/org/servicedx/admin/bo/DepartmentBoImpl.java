package org.servicedx.admin.bo;

import java.security.InvalidKeyException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.apache.kafka.common.errors.InvalidRequestException;
import org.servicedx.event.service.AdminProducer;
import org.servicedx.security.resource.IErrorAdmin;
import org.servicedx.security.resource.IPathAdmin;
import org.servicedx.security.resource.IPath.EReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.servicedx.bean.DepartmentFormBean;
import org.servicedx.bean.UserFormBean;
import org.servicedx.bean.model.Department;
import org.servicedx.bean.model.Users;
import org.servicedx.dao.DepartmentDao;
import org.servicedx.dao.SequenceDao;
import org.servicedx.util.CommonValidator;
import org.servicedx.util.EnumInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepartmentBoImpl implements DepartmentBo, IErrorAdmin, IPathAdmin
{

	private static final long	serialVersionUID	= 1501193832767227636L;

	private final Logger		logger				= LoggerFactory.getLogger(DepartmentBoImpl.class);

	@Value("${server.domain.url}")
	public String				domainURL;

	@Autowired
	AdminProducer				producer;

	@Autowired
	private SequenceDao			sequenceDao;

	@Autowired
	private DepartmentDao		departmentDao;

	@Autowired
	private UserBo				userBo;

	@Override
	public List<Department> getDepartmentList(Authentication auth)
	{
		return departmentDao.getDepartmentList(EAuth.User.getCustomerId(auth));
	}

	@Override
	public EnumInterface saveDepartment(Authentication auth, DepartmentFormBean deptFormBean) throws InvalidKeyException
	{
		boolean userUpdate = true;
		int count = departmentDao.checkDepartmentExists(EAuth.User.getCustomerId(auth), deptFormBean.department.getDepartmentName());
		if (count == 0)
		{
			deptFormBean.department.setDepartmentId(sequenceDao.getPrimaryKey(Department.class));
			deptFormBean.department.createdUserCustomerInfo(auth);
			Department department = departmentDao.insert(deptFormBean.department);

			if (CommonValidator.isNotNullNotEmpty(deptFormBean.userList))
			{
				userUpdate = updateUser(auth, deptFormBean.userList, department);
			}

			if (CommonValidator.isNotNullNotEmpty(department) && userUpdate)
			{
				deptFormBean.messageCode = DEPARTMENT_CREATED_SUCCESSFULLY;
				return EReturn.Success;
			}
		}
		throw new InvalidRequestException(DEPARTMENT_ALREADY_EXISTS);
	}

	private boolean updateUser(Authentication auth, List<Users> userList, Department department)
	{
		try
		{
			userList.forEach(user -> {
				user.setDepartmentId(department.getDepartmentId());
				user.setDepartmentName(department.getDepartmentName());
				UserFormBean ufBean = new UserFormBean();
				ufBean.user = user;
				userBo.updateUser(auth, ufBean);
			});
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	@Override
	public EnumInterface updateDepartment(Authentication auth, DepartmentFormBean deptFormBean)
	{
		boolean userUpdate = true;
		deptFormBean.department.modifiedUserInfo(auth);
		Department department = departmentDao.save(deptFormBean.department);
		if (CommonValidator.isNotNullNotEmpty(deptFormBean.userList))
		{
			userUpdate = updateUser(auth, deptFormBean.userList, department);
		}
		if (CommonValidator.isNotNullNotEmpty(department) && userUpdate)
		{
			deptFormBean.messageCode = DEPARTMENT_UPDATED_SUCCESSFULLY;
			return EReturn.Success;
		}
		throw new InvalidRequestException(DEPARTMENT_UPDATED_ISSUE);
	}

	@Override
	public EnumInterface blockDepartment(Authentication auth, DepartmentFormBean deptFormBean)
	{

		if (isRecentlyUpdated(deptFormBean))
		{
			try
			{
				deptFormBean.department.setStatus(false);
				deptFormBean.department.modifiedUserInfo(auth);
				departmentDao.save(deptFormBean.department);

				deptFormBean.messageCode = DEPARTMENT_BLOCKED_SUCCESSFULLY;
				return EReturn.Success;
			}
			finally
			{
				deptFormBean.department = null;
			}
		}
		throw new InvalidRequestException(DEPARTMENT_DATA_UPDATED_RECENTLY);
	}

	@Override
	public Department getDepartment(Authentication auth, DepartmentFormBean deptFormBean)
	{
		return departmentDao.findByDepartmentId(deptFormBean.department.getDepartmentId());
	}

	private boolean isRecentlyUpdated(DepartmentFormBean deptFormBean)
	{

		if (CommonValidator.isNotNullNotEmpty(deptFormBean.department))
		{
			Department department = departmentDao.findByDepartmentId(deptFormBean.department.getDepartmentId());
			if (CommonValidator.isNotNullNotEmpty(department))
			{
				if (ChronoUnit.NANOS.between(deptFormBean.department.getModifiedDate(), department.getModifiedDate()) == 0)
				{
					deptFormBean.repodepartment = department;
					return true;
				}
				return false;
			}
		}
		throw new InvalidRequestException(DEPARTMENT_NOT_FOUND);
	}

}
