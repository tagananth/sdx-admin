package org.servicedx.admin;

import org.servicedx.security.resource.IPathAdmin;
import org.servicedx.bean.GroupFormBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

public interface IGroupController extends IPathAdmin
{

	@PostMapping
	@RequestMapping(value = BLOCK_GROUP, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> blockGroup(Authentication auth, GroupFormBean grpFormBean);

	@PostMapping
	@RequestMapping(value = CHECK_GROUP_EXISTS, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> checkGroupExists(Authentication auth, GroupFormBean grpFormBean);

	@PostMapping
	@RequestMapping(value = DELETE_GROUP, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> deleteGroup(Authentication auth, GroupFormBean grpFormBean);

	@PostMapping
	@RequestMapping(value = GET_GROUP_BY_CUST_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> getGroupByCustomer(Authentication auth);
	
	@PostMapping
	@RequestMapping(value = GET_GROUP, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> getGroup(Authentication auth, GroupFormBean grpFormBean);

	@PostMapping
	@RequestMapping(value = GET_GROUP_LIST, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> getGroupList(Authentication auth, @RequestBody GroupFormBean grpFormBean);

	@PostMapping
	@RequestMapping(value = ADD_GROUP, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> addGroup(Authentication auth, GroupFormBean grpFormBean);

	@PostMapping
	@RequestMapping(value = SEARCH_GROUP, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> searchGroup(Authentication auth, GroupFormBean grpFormBean);

	@PostMapping
	@RequestMapping(value = UPDATE_GROUP, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> updateGroup(Authentication auth, GroupFormBean grpFormBean);

}