package org.servicedx.admin.bo;

import java.security.InvalidKeyException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.apache.kafka.common.errors.InvalidRequestException;
import org.servicedx.security.resource.IErrorAdmin;
import org.servicedx.security.resource.IPath.EReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.servicedx.bean.CustomerFormBean;
import org.servicedx.bean.model.Customer;
import org.servicedx.dao.CustomerDao;
import org.servicedx.dao.SequenceDao;
import org.servicedx.util.CommonValidator;
import org.servicedx.util.EnumInterface;
import org.servicedx.util.IConstProperty.EWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.datastax.driver.core.Row;

@Service
@Transactional
public class CustomerBoImpl implements CustomerBo, IErrorAdmin
{
	private static final long	serialVersionUID	= 6078462669851402422L;
	private final Logger		logger				= LoggerFactory.getLogger(CustomerBoImpl.class);

	@Autowired
	protected CustomerDao		customerDao;

	@Autowired
	SequenceDao					sequenceDao;

	@Override
	public EnumInterface blockCustomer(Authentication auth, CustomerFormBean cfBean)
	{
		if (isRecentlyUpdated(cfBean))
		{
			try
			{
				cfBean.repoCustomer.setStatus(!cfBean.customer.isStatus());// Negate Current Status
				cfBean.repoCustomer.modifiedUserInfo(auth);
				customerDao.save(cfBean.repoCustomer);

				cfBean.messageCode = CUSTOMER_BLOCKED_SUCCESSFULLY;
				return EReturn.Success;
			}
			finally
			{
				cfBean.customer = null;
			}
		}
		throw new InvalidRequestException(CUSTOMER_DATA_UPDATED_RECENTLY);

	}
	@Override
	public EnumInterface blockCustomerById(Authentication auth, CustomerFormBean cfBean) throws InvalidRequestException
	{
		if (isRecentlyUpdated(cfBean))
		{
			try
			{
				cfBean.repoCustomer.setStatus(!cfBean.customer.isStatus());// Negate Current Status
				cfBean.repoCustomer.modifiedUserInfo(auth);
				customerDao.save(cfBean.repoCustomer);

				cfBean.messageCode = USER_BLOCKED_SUCCESSFULLY;
				return EReturn.Success;
			}
			finally
			{
				cfBean.repoCustomer = null;
				cfBean.customer = null;
			}
		}
		throw new InvalidRequestException(USER_DATA_UPDATED_RECENTLY);
	}
	@Override
	public EnumInterface deleteCustomer(Authentication auth, CustomerFormBean cfBean) throws InvalidRequestException
	{
		logger.info("Inside CustomerBoImpl deleteCustomer ::: ",cfBean.customer.getCustomerId());
		//customerDao.deleteById(cfBean.customer.getCustomerId());
		cfBean.repoCustomer =customerDao.findByCustomerId(cfBean.customer.getCustomerId());
		cfBean.repoCustomer.setStatus(!cfBean.repoCustomer.isStatus());// Negate Current Status
		
		cfBean.repoCustomer.modifiedUserInfo(auth);
		customerDao.save(cfBean.repoCustomer);
		return EReturn.Success;
	}
	@Override
	public EnumInterface checkCustomerExist(CustomerFormBean cfBean)
	{
		int row = customerDao.countByCustomerName(cfBean.customer.getCustomerName());
		return row > 0 ? EReturn.Exists : EReturn.Not_Exists;
	}

	@Override
	public Customer getCustomer(CustomerFormBean cfBean)
	{
		return customerDao.findByCustomerId(cfBean.customer.getCustomerId());
	}

	@Override
	public List<Customer> getCustomerByName(CustomerFormBean cfBean)
	{
		return customerDao.findByCustomerName(EWrap.Percent.enclose(cfBean.customer.getCustomerName()));
	}

	private boolean isRecentlyUpdated(CustomerFormBean cfBean)
	{
		if (CommonValidator.isNotNullNotEmpty(cfBean, cfBean.customer))
		{
			Customer customer = customerDao.findByCustomerId(cfBean.customer.getCustomerId());
			if (CommonValidator.isNotNullNotEmpty(customer))
			{
				if (ChronoUnit.NANOS.between(cfBean.customer.getModifiedDate(), customer.getModifiedDate()) == 0)
				{
					cfBean.repoCustomer = customer;
					return true;
				}
				return false;
			}
		}
		throw new InvalidRequestException(CUSTOMER_NOT_FOUND);
	}

	@Override
	public EnumInterface saveCustomer(Authentication auth, CustomerFormBean cfBean) throws InvalidKeyException
	{

		Row row = customerDao.checkCustomerName(cfBean.customer.getCustomerName());
		if (CommonValidator.isNullOrEmpty(row))
		{
			cfBean.customer.setCustomerId(sequenceDao.getPrimaryKey(Customer.class));
			cfBean.customer.setActive(true);
			cfBean.customer.createdUserCustomerInfo(auth);
			cfBean.customer = customerDao.insert(cfBean.customer);

			if (CommonValidator.isNotNullNotEmpty(cfBean.customer))
			{
				try
				{
					cfBean.messageCode = CUSTOMER_CREATED_SUCCESSFULLY;
					return EReturn.Success;
				}
				finally
				{
					cfBean.customer = null;
					cfBean.repoCustomer = null;
				}
			}
		}
		throw new InvalidRequestException(CUSTOMER_ALREADY_EXISTS);

	}

	@Override
	public EnumInterface updateCustomer(Authentication auth, CustomerFormBean cfBean)
	{

		if (isRecentlyUpdated(cfBean))
		{
			try
			{
				cfBean.updateRepoCustomer(auth);
				customerDao.save(cfBean.repoCustomer);

				cfBean.messageCode = CUSTOMER_UPDATED_SUCCESSFULLY;
				return EReturn.Success;
			}
			finally
			{
				cfBean.customer = null;
			}
		}
		throw new InvalidRequestException(CUSTOMER_DATA_UPDATED_RECENTLY);

	}

	@Override
	public List<Customer> getCustomerList() {
		return customerDao.getCustomerList();
	}
}
