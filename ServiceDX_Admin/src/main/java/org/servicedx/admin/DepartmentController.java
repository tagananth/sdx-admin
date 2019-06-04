package org.servicedx.admin;

import java.util.List;

import org.servicedx.admin.bo.DepartmentBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.servicedx.bean.DepartmentFormBean;
import org.servicedx.bean.model.Department;
import org.servicedx.util.CommonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartmentController implements IDepartmentController
{
	private static final long	serialVersionUID	= -1046242631792313825L;


	private final Logger		logger				= LoggerFactory.getLogger(DepartmentController.class);

	@Autowired
	DepartmentBo				departmentBo;

	public ResponseEntity<?> getDepartmentList(Authentication auth)
	{
		try
		{
			logger.info("Inside DepartmentController getAllUsers ::: ");
			return new ResponseEntity<List<Department>>(departmentBo.getDepartmentList(auth), HttpStatus.OK);
		}
		catch (Exception e)
		{
			DepartmentFormBean deptFormBean = new DepartmentFormBean();
			deptFormBean.messageCode = e.getMessage();
			logger.error("Exception in UserController getAllUsers ::: ", e);
			return new ResponseEntity<>(deptFormBean, HttpStatus.BAD_REQUEST);
		}
	}
	

	@Override
	public ResponseEntity<?> getDepartment(Authentication auth,@RequestBody DepartmentFormBean deptFormBean)
	{
		try
		{
			if (CommonValidator.isNotNullNotEmpty(deptFormBean.department,deptFormBean.department.getDepartmentId()))
			{
				return new ResponseEntity<>(departmentBo.getDepartment(auth, deptFormBean), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);

		}
		catch (Exception excep)
		{
			deptFormBean.department = null;
			deptFormBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(deptFormBean, HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity<?> addDepartment(Authentication auth, @RequestBody DepartmentFormBean deptFormBean)
	{
		try
		{
			if (CommonValidator.isNotNullNotEmpty(deptFormBean.department))
			{
				return new ResponseEntity<>(departmentBo.saveDepartment(auth, deptFormBean), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);

		}
		catch (Exception excep)
		{
			deptFormBean.department = null;
			deptFormBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(deptFormBean, HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity<?> updateDepartment(Authentication auth, @RequestBody DepartmentFormBean deptFormBean)
	{
		try
		{
			if (CommonValidator.isNotNullNotEmpty(deptFormBean.department))
			{
				return new ResponseEntity<>(departmentBo.updateDepartment(auth, deptFormBean), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);

		}
		catch (Exception excep)
		{
			deptFormBean.department = null;
			deptFormBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(deptFormBean, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> blockDepartment(Authentication auth,@RequestBody DepartmentFormBean deptFormBean)
	{
		try
		{
			if (CommonValidator.isNotNullNotEmpty(deptFormBean.department))
			{
				return new ResponseEntity<>(departmentBo.blockDepartment(auth, deptFormBean), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);

		}
		catch (Exception excep)
		{
			deptFormBean.department = null;
			deptFormBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(deptFormBean, HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity<?> checkDepartmentExists(Authentication auth, DepartmentFormBean deptFormBean)
	{
		// TODO Auto-generated method stub
		return null;
	}


}
