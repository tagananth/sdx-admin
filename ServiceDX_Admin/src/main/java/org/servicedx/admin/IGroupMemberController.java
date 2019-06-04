package org.servicedx.admin;

import org.servicedx.security.resource.IPathAdmin;
import org.servicedx.bean.GroupMemberFormBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

public interface IGroupMemberController extends IPathAdmin
{
	@PostMapping
	@RequestMapping(value = DELETE_GROUPMEMBERS, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> deleteGroupMembers(Authentication auth, @RequestBody GroupMemberFormBean gmfBean);

	@PostMapping
	@RequestMapping(value = GET_GROUPMEMBERS_BY_GROUP, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> getGroupMembersByGroupId(Authentication auth, @RequestBody GroupMemberFormBean gmfBean);

	@PostMapping
	@RequestMapping(value = ADD_GROUPMEMBERS, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> saveGroupMembers(Authentication auth, @RequestBody GroupMemberFormBean gmfBean);

	@PostMapping
	@RequestMapping(value = UPDATE_GROUPMEMBERS, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> updateGroupMembers(Authentication auth, @RequestBody GroupMemberFormBean gmfBean);

}