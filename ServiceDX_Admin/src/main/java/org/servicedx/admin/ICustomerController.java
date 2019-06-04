package org.servicedx.admin;

import org.servicedx.security.resource.IPathAdmin;
import org.servicedx.bean.CustomerFormBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

public interface ICustomerController extends IPathAdmin
{
	@PostMapping
	@RequestMapping(value = ADD_CUSTOMER)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> addCustomer(Authentication auth, @RequestBody CustomerFormBean customerForm);

	@PostMapping
	@RequestMapping(value = BLOCK_CUSTOMER)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> blockCustomer(Authentication auth, @RequestBody CustomerFormBean customerForm);

	@PostMapping
	@RequestMapping(value = DELETE_CUSTOMER)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> deleteCustomer(Authentication auth, @RequestBody CustomerFormBean customerForm);

	@PostMapping
	@RequestMapping(value = CHECK_CUSTOMER_EXIST)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> checkCustomerExist(Authentication auth, @RequestBody CustomerFormBean customerForm);

	@PostMapping
	@RequestMapping(value = PREUPDATE_CUSTOMER)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> getCustomer(Authentication auth, @RequestBody CustomerFormBean customerForm);

	@PostMapping
	@RequestMapping(value = SEARCH_CUSTOMER)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> getCustomerByName(Authentication auth, @RequestBody CustomerFormBean customerForm);

	@PostMapping
	@RequestMapping(value = UPDATE_CUSTOMER)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> updateCustomer(Authentication auth, @RequestBody CustomerFormBean customerForm);

	@PostMapping
	@RequestMapping(value = CUSTOMER_LIST)
	@PreAuthorize(HAS_AUTHORITY_ADMINISTRATOR)
	ResponseEntity<?> getCustomerList(Authentication auth);
}