package org.servicedx.admin.bo;

import java.security.InvalidKeyException;
import java.util.List;

import org.apache.kafka.common.errors.InvalidRequestException;
import org.servicedx.bean.CustomerFormBean;
import org.servicedx.bean.model.Customer;
import org.servicedx.util.EnumInterface;
import org.springframework.security.core.Authentication;

public interface CustomerBo
{
	Customer getCustomer(CustomerFormBean request);

	EnumInterface saveCustomer(Authentication auth, CustomerFormBean request) throws InvalidKeyException;

	EnumInterface updateCustomer(Authentication auth, CustomerFormBean request);

	EnumInterface blockCustomer(Authentication auth, CustomerFormBean request);

	List<Customer> getCustomerByName(CustomerFormBean request);

	EnumInterface checkCustomerExist(CustomerFormBean request);

	List<Customer>  getCustomerList();
	
	EnumInterface blockCustomerById(Authentication auth, CustomerFormBean request);
	
	EnumInterface deleteCustomer(Authentication auth, CustomerFormBean cfBean) throws InvalidRequestException;
}
