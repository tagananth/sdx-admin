package org.servicedx.admin.bo;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.util.List;

import org.servicedx.bean.DepartmentFormBean;
import org.servicedx.bean.model.Department;
import org.servicedx.util.EnumInterface;
import org.springframework.security.core.Authentication;

public interface DepartmentBo extends Serializable
{
	List<Department> getDepartmentList(Authentication auth);

	EnumInterface saveDepartment(Authentication auth, DepartmentFormBean deptFormBean) throws InvalidKeyException;

	EnumInterface updateDepartment(Authentication auth, DepartmentFormBean deptFormBean);

	EnumInterface blockDepartment(Authentication auth, DepartmentFormBean deptFormBean);

	Department getDepartment(Authentication auth, DepartmentFormBean deptFormBean);
}
