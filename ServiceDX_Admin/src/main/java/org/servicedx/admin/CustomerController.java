package org.servicedx.admin;

import java.util.List;

import org.servicedx.admin.bo.CustomerBo;
import org.servicedx.bean.CustomerFormBean;
import org.servicedx.bean.model.Customer;
import org.servicedx.util.CommonValidator;
import org.servicedx.util.EnumInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController implements ICustomerController
{
	private static final long	serialVersionUID	= 7322526333735035930L;

	@Autowired
	CustomerBo					customerBo;

	@Override
	public ResponseEntity<?> addCustomer(Authentication auth, CustomerFormBean customerForm)
	{
		try
		{
			if (CommonValidator.isNotNullNotEmpty(customerForm.customer))
			{
				customerBo.saveCustomer(auth, customerForm);
				return new ResponseEntity<>(customerForm, HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			customerForm.customer = null;
			customerForm.messageCode = excep.getMessage();
			return new ResponseEntity<>(customerForm, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> blockCustomer(Authentication auth, CustomerFormBean customerForm)
	{
		try
		{
			if (CommonValidator.isNotNullNotEmpty(customerForm.customer))
			{
				return new ResponseEntity<>(customerBo.blockCustomer(auth, customerForm), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			customerForm.customer = null;
			customerForm.messageCode = excep.getMessage();
			return new ResponseEntity<>(customerForm, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> deleteCustomer(Authentication auth, CustomerFormBean customerForm)
	{
		try
		{
			if (CommonValidator.isNotNullNotEmpty(customerForm.customer))
			{
				return new ResponseEntity<>(customerBo.deleteCustomer(auth, customerForm), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			customerForm.customer = null;
			customerForm.messageCode = excep.getMessage();
			return new ResponseEntity<>(customerForm, HttpStatus.BAD_REQUEST);
		}
	}
	@Override
	public ResponseEntity<?> checkCustomerExist(Authentication auth, CustomerFormBean customerForm)
	{
		try
		{
			if (CommonValidator.isNotNullNotEmpty(customerForm, customerForm.customer))
			{
				return new ResponseEntity<>(customerBo.checkCustomerExist(customerForm), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			customerForm.customer = null;
			customerForm.messageCode = excep.getMessage();
			return new ResponseEntity<>(customerForm, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> getCustomer(Authentication auth, CustomerFormBean customerForm)
	{
		try
		{
			if (CommonValidator.isNotNullNotEmpty(customerForm.customer))
			{
				return new ResponseEntity<Customer>(customerBo.getCustomer(customerForm), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			customerForm.customer = null;
			customerForm.messageCode = excep.getMessage();
			return new ResponseEntity<>(customerForm, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> getCustomerByName(Authentication auth, CustomerFormBean customerForm)
	{
		try
		{
			if (CommonValidator.isNotNullNotEmpty(customerForm.customer))
			{
				return new ResponseEntity<List<Customer>>(customerBo.getCustomerByName(customerForm), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			customerForm.customer = null;
			customerForm.messageCode = excep.getMessage();
			return new ResponseEntity<>(customerForm, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> updateCustomer(Authentication auth, CustomerFormBean customerForm)
	{
		try
		{
			if (CommonValidator.isNotNullNotEmpty(customerForm.customer))
			{
				return new ResponseEntity<EnumInterface>(customerBo.updateCustomer(auth, customerForm), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			customerForm.customer = null;
			customerForm.messageCode = excep.getMessage();
			return new ResponseEntity<>(customerForm, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> getCustomerList(Authentication auth) {
		try
		{
			
				return new ResponseEntity<List<Customer>>(customerBo.getCustomerList(), HttpStatus.OK);
			
		}
		catch (Exception excep)
		{
			return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
		}
	}
}
