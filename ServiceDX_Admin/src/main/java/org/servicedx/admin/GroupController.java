package org.servicedx.admin;

import java.util.List;

import org.servicedx.admin.bo.GroupBo;
import org.servicedx.bean.GroupFormBean;
import org.servicedx.bean.model.Group;
import org.servicedx.util.CommonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupController implements IGroupController
{

	private static final long	serialVersionUID	= -1224989977174154781L;

	@Autowired
	GroupBo						groupBo;

	@Override
	public ResponseEntity<?> addGroup(Authentication auth, @RequestBody GroupFormBean grpFormBean)
	{
		try
		{
			groupBo.saveGroup(auth, grpFormBean);

			return new ResponseEntity<>(grpFormBean, HttpStatus.OK);
		}
		catch (Exception excep)
		{
			grpFormBean.group = null;
			grpFormBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(grpFormBean, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> blockGroup(Authentication auth, @RequestBody GroupFormBean grpFormBean)
	{
		try
		{

			if (CommonValidator.isNotNullNotEmpty(grpFormBean.group))
			{
				return new ResponseEntity<String>(groupBo.blockGroup(auth, grpFormBean).name(), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			grpFormBean.group = null;
			grpFormBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(grpFormBean, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> checkGroupExists(Authentication auth, @RequestBody GroupFormBean grpFormBean)
	{
		try
		{

			if (CommonValidator.isNotNullNotEmpty(grpFormBean.group))
			{
				return new ResponseEntity<>(groupBo.checkGroupExists(auth, grpFormBean), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			grpFormBean.group = null;
			grpFormBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(grpFormBean, HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity<?> deleteGroup(Authentication auth, @RequestBody GroupFormBean grpFormBean)
	{
		try
		{

			if (CommonValidator.isNotNullNotEmpty(grpFormBean.group))
			{
				return new ResponseEntity<>(groupBo.deleteGroup(auth, grpFormBean), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			grpFormBean.group = null;
			grpFormBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(grpFormBean, HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity<?> getGroup(Authentication auth, @RequestBody GroupFormBean grpFormBean)
	{
		try
		{
			if (CommonValidator.isNotNullNotEmpty(grpFormBean.group))
			{
				return new ResponseEntity<Group>(groupBo.getGroup(grpFormBean.group), HttpStatus.OK);
			}
			throw new InvalidRequestException(INVALID_REQUEST_PARAMETERS);
		}
		catch (Exception excep)
		{
			grpFormBean.group = null;
			grpFormBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(grpFormBean, HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity<?> getGroupByCustomer(Authentication auth)
	{
		try
		{
				return new ResponseEntity<List<Group>>(groupBo.getGroupByCustomer(auth), HttpStatus.OK);
		}
		catch (Exception excep)
		{
			GroupFormBean grpFormBean = new GroupFormBean();
			grpFormBean.group = null;
			grpFormBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(grpFormBean, HttpStatus.BAD_REQUEST);
		}

	}
	
	@Override
	public ResponseEntity<?> getGroupList(Authentication auth, @RequestBody GroupFormBean grpFormBean)
	{
		try
		{
			return new ResponseEntity<List<Group>>(groupBo.getGroupByIdList(auth, grpFormBean), HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
	}

	@Override
	public ResponseEntity<?> searchGroup(Authentication auth, @RequestBody GroupFormBean grpFormBean)
	{
		try
		{
			return new ResponseEntity<List<Group>>(groupBo.getGroupByNameList(auth, grpFormBean), HttpStatus.OK);
		}
		catch (Exception excep)
		{
			grpFormBean.group = null;
			grpFormBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(grpFormBean, HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> updateGroup(Authentication auth, @RequestBody GroupFormBean grpFormBean)
	{
		try
		{
			groupBo.updateGroup(auth, grpFormBean);

			return new ResponseEntity<>(grpFormBean, HttpStatus.OK);
		}
		catch (Exception excep)
		{
			grpFormBean.group = null;
			grpFormBean.messageCode = excep.getMessage();
			return new ResponseEntity<>(grpFormBean, HttpStatus.BAD_REQUEST);
		}
	}

}
