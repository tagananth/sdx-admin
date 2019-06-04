package org.servicedx.admin;

import org.servicedx.security.resource.IPathAdmin;
import org.servicedx.bean.DepartmentFormBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public interface IDepartmentController extends IPathAdmin
{
	
	@PostMapping
	@RequestMapping(value = GET_DEPARTMENT_LIST, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> getDepartmentList(Authentication auth);
	
	@PostMapping
	@RequestMapping(value = GET_DEPARTMENT, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> getDepartment(Authentication auth, DepartmentFormBean deptFormBean);

	@PostMapping
	@RequestMapping(value = ADD_DEPARTMENT, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> addDepartment(Authentication auth, DepartmentFormBean deptFormBean);

	@PostMapping
	@RequestMapping(value = UPDATE_DEPARTMENT, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> updateDepartment(Authentication auth, DepartmentFormBean deptFormBean);
	
	@PostMapping
	@RequestMapping(value = BLOCK_DEPARTMENT, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> blockDepartment(Authentication auth, DepartmentFormBean deptFormBean);
	
	@PostMapping
	@RequestMapping(value = CHECK_DEPARTMENT_EXIST, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> checkDepartmentExists(Authentication auth, DepartmentFormBean deptFormBean);

}