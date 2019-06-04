package org.servicedx.admin.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.servicedx.security.resource.IErrorAdmin;
import org.servicedx.security.resource.IPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.servicedx.bean.GroupUserBean;
import org.servicedx.bean.UserFormBean;
import org.servicedx.bean.model.City;
import org.servicedx.bean.model.Country;
import org.servicedx.bean.model.States;
import org.servicedx.dao.CityDao;
import org.servicedx.dao.CountryDao;
import org.servicedx.dao.GroupDao;
import org.servicedx.dao.StateDao;
import org.servicedx.dao.UserDao;
import org.servicedx.util.CommonValidator;
import org.servicedx.util.LabelValueBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.datastax.driver.core.Row;

public abstract class UserBoComboBoxImpl implements UserBo, IErrorAdmin, IPath
{
	private static final String	FIRSTNAME			= "firstName";
	private static final String	USERID				= "userid";
	private static final String	USERNAME			= "username";
	private static final String	GROUPID				= "groupid";
	private static final String	GROUPNAME			= "groupname";
	private static final String	LASTNAME			= "lastName";
	private static final String	P_EMAIL				= "emailId";
	private static final String	P_MOBILE			= "mobileNo";
	private static final String	S_MOBILE			= "mobileNo1";
	private static final String	S_EMAIL				= "emailId1";

	private static final long	serialVersionUID	= 1160466715298198052L;

	private final Logger		logger				= LoggerFactory.getLogger(UserBoComboBoxImpl.class);
	
	@Autowired
	protected UserDao			userDao;

	@Autowired
	protected CountryDao		countryDao;

	@Autowired
	protected StateDao			stateDao;

	@Autowired
	protected CityDao			cityDao;

	@Autowired
	private GroupDao			groupDao;

	public UserBoComboBoxImpl()
	{
		super();
	}

	@Override
	public List<LabelValueBean> getCountryList(Authentication auth, UserFormBean userFormBean)
	{
		logger.info("UserBoComboBoxImpl getCountryList starts:::", userFormBean.searchParam);
		List<Country> countryList = new ArrayList<Country>();
		List<LabelValueBean> _LBList = new ArrayList<LabelValueBean>();
		if (CommonValidator.isNotNullNotEmpty(userFormBean.searchParam))
		{
			countryList = countryDao.getCountryList(EWrap.Percent.enclose(userFormBean.searchParam));
		}
		else
		{
			countryList = countryDao.getCountryList();
		}

		Collections.sort(countryList);

		for (Country country : countryList)
		{
			_LBList.add(new LabelValueBean(country, country.getCountryName(), country.getCountry(), country.getCountryCode()));
		}
		logger.info("UserBoComboBoxImpl getCountryList ends :::", _LBList.size());
		return _LBList;
	}

	@Override
	public List<LabelValueBean> getStateList(Authentication auth, UserFormBean userFormBean)
	{

		logger.info("UserBoComboBoxImpl getStateList starts:::", userFormBean.searchParam);
		List<States> stateList = new ArrayList<States>();
		List<LabelValueBean> _LBList = new ArrayList<LabelValueBean>();
		if (CommonValidator.isNotNullNotEmpty(userFormBean.searchParam))
		{
			stateList = stateDao.getStateList(EWrap.Percent.enclose(userFormBean.searchParam), userFormBean.country);
		}
		else
		{
			stateList = stateDao.getStateList(userFormBean.country);
		}

		Collections.sort(stateList);

		for (States state : stateList)
		{
			_LBList.add(new LabelValueBean(state, state.getStateName(), state.getState()));
		}
		logger.info("UserBoComboBoxImpl getStateList ends :::", _LBList.size());
		return _LBList;
	}

	@Override
	public List<LabelValueBean> getCityList(Authentication auth, UserFormBean userFormBean)
	{
		logger.info("UserBoComboBoxImpl getCityList starts:::", userFormBean.searchParam);
		List<City> cityList = new ArrayList<City>();
		List<LabelValueBean> _LBList = new ArrayList<LabelValueBean>();
		if (CommonValidator.isNotNullNotEmpty(userFormBean.searchParam))
		{
			cityList = cityDao.getCityList(userFormBean.state, EWrap.Percent.enclose(userFormBean.searchParam));
		}
		else
		{
			cityList = cityDao.getCityList(userFormBean.state);
		}

		Collections.sort(cityList);

		for (City city : cityList)
		{
			_LBList.add(new LabelValueBean(city, city.getCity() + EWrap.Brace.enclose(city.getState()), city.getZipCode()));
		}
		logger.info("UserBoComboBoxImpl getCityList ends :::", _LBList.size());
		return _LBList;
	}

	@Override
	public Collection<LabelValueBean> getUsersBySearchParam(Authentication auth, UserFormBean gmfBean)
	{
		logger.info("UserBoComboBoxImpl getUsersBySearchParam starts:::", gmfBean.searchParam);
		Map<String, LabelValueBean> userMap = new TreeMap<String, LabelValueBean>();
		List<Object> row = null;

		if (StringUtils.isAlphanumeric(gmfBean.searchParam.trim().toLowerCase()) || gmfBean.searchParam.indexOf("@") > 0)
		{
			if (gmfBean.searchParam.indexOf("@") < 0)
			{
				row = userDao.getUsersByLastName(EAuth.User.getCustomerId(auth), EWrap.Percent.enclose(gmfBean.searchParam));
				insertInMap(LASTNAME, gmfBean.media, userMap, row);

				row = userDao.getUsersByFirstName(EAuth.User.getCustomerId(auth), EWrap.Percent.enclose(gmfBean.searchParam));
				insertInMap(FIRSTNAME, gmfBean.media, userMap, row);
			}

			if (CommonValidator.isEqual(gmfBean.media, EMedia.Email))
			{
				row = userDao.getUsersByEmailId(EAuth.User.getCustomerId(auth), EWrap.Percent.enclose(gmfBean.searchParam));
				insertInMap(P_EMAIL, gmfBean.media, userMap, row);

				row = userDao.getUsersByEmailId1(EAuth.User.getCustomerId(auth), EWrap.Percent.enclose(gmfBean.searchParam));
				insertInMap(S_EMAIL, gmfBean.media, userMap, row);
			}

		}
		if (StringUtils.isNumeric(gmfBean.searchParam.trim()) && CommonValidator.isEqual(gmfBean.media, EMedia.SMS))
		{
			row = userDao.getUsersByMobileNo(EAuth.User.getCustomerId(auth), EWrap.Percent.enclose(gmfBean.searchParam));
			insertInMap(P_MOBILE, gmfBean.media, userMap, row);

			row = userDao.getUsersByMobileNo1(EAuth.User.getCustomerId(auth), EWrap.Percent.enclose(gmfBean.searchParam));
			insertInMap(S_MOBILE, gmfBean.media, userMap, row);
		}
		logger.info("UserBoComboBoxImpl getUsersBySearchParam ends :::", userMap.size());
		return userMap.values();
	}

	public Collection<LabelValueBean> getUserGroupSearch(Authentication auth, UserFormBean gmfBean)
	{
		logger.info("UserBoComboBoxImpl getUsersBySearchParam starts:::", gmfBean.entity);
		Collection<LabelValueBean> list = null;
		switch ( EEntity.valueOf(gmfBean.entity) )
		{
			case User :
				list = getUsers(auth, gmfBean);
				break;
			case Group :
				list = getGroups(auth, gmfBean);
				break;
		}
		logger.info("UserBoComboBoxImpl getUsersBySearchParam ends:::", list.size());
		return list;
	}

	public Collection<LabelValueBean> getGroups(Authentication auth, UserFormBean gmfBean)
	{
		logger.info("UserBoComboBoxImpl getGroups starts:::", gmfBean.media);
		List<LabelValueBean> labelList = new ArrayList<LabelValueBean>();
		List<Object> rows = null;
		String media = gmfBean.media;
		long groupId;
		String groupname = null;
		rows = groupDao.fetchByCustomerIdAndType(EAuth.User.getCustomerId(auth), media);
		Row row = null;
		for (Object object : rows)
		{
			row = (Row) object;
			groupId = row.getLong(GROUPID);
			groupname = row.getString(GROUPNAME);
			GroupUserBean groupUserBean = new GroupUserBean(groupId, groupname);
			labelList.add(new LabelValueBean(groupUserBean, groupname, groupname));
		}
		logger.info("UserBoComboBoxImpl getGroups ends:::", labelList.size());
		return labelList;

	}

	public Collection<LabelValueBean> getUsers(Authentication auth, UserFormBean gmfBean)
	{
		logger.info("UserBoComboBoxImpl getUsers starts:::", gmfBean.media);
		Map<String, LabelValueBean> userMap = new TreeMap<String, LabelValueBean>();
		List<Object> rows = null;
		rows = userDao.getUsersByCustomerId(EAuth.User.getCustomerId(auth));
		String media = gmfBean.media;

		if (CommonValidator.isListFirstNotEmpty(rows))
		{
			String key = "";
			long userId;
			String name = null;
			String lastName = null;
			String userName = null;
			String emailId = null;
			String emailId1 = null;
			String mobileNo = null;
			String mobileNo1 = null;

			Row row = null;
			for (Object object : rows)
			{
				row = (Row) object;
				userId = row.getLong(USERID);
				userName = row.getString(USERNAME);
				name = row.getString(FIRSTNAME);
				lastName = row.getString(LASTNAME);
				emailId = row.getString(P_EMAIL);
				emailId1 = row.getString(S_EMAIL);
				mobileNo = row.getString(P_MOBILE);
				mobileNo1 = row.getString(S_MOBILE);

				GroupUserBean groupUser = new GroupUserBean(userId, userName, emailId, emailId1, mobileNo, mobileNo1);

				// For Email
				if (CommonValidator.isEqual(media, EMedia.Email))
				{
					if (CommonValidator.isNotNullNotEmpty(emailId))
					{
						key = EMediaType.Primary + media + userId;

						if (userMap.containsKey(key) == false)
						{
							groupUser.setUserContact(EMediaType.Primary);
							userMap.put(key, new LabelValueBean(groupUser, name +" "+ EWrap.Brace.enclose(emailId), emailId));
						}
					}

					if (CommonValidator.isNotNullNotEmpty(emailId1))
					{
						key = EMediaType.Secondary + media + userId;
						if (userMap.containsKey(key) == false)
						{
							GroupUserBean copyBean = new GroupUserBean(groupUser);
							copyBean.getGroupUserBean().setUserContact(EMediaType.Secondary);
							userMap.put(key, new LabelValueBean(copyBean.getGroupUserBean(), name +" "+ EWrap.Brace.enclose(emailId1), emailId1));
						}
					}

				}
				else if (CommonValidator.isEqual(media, EMedia.SMS))
				{
					// For Mobile
					if (CommonValidator.isNotNullNotEmpty(mobileNo))
					{
						key = EMediaType.Primary + media + userId;
						if (userMap.containsKey(key) == false)
						{
							GroupUserBean copyBean = new GroupUserBean(groupUser);

							copyBean.getGroupUserBean().setUserContact(EMediaType.Primary);
							userMap.put(key, new LabelValueBean(copyBean.getGroupUserBean(), name +" "+ EWrap.Brace.enclose(mobileNo), mobileNo));
						}
					}
					if (CommonValidator.isNotNullNotEmpty(mobileNo1))
					{
						key = EMediaType.Secondary + media + userId;
						if (userMap.containsKey(key) == false)
						{
							GroupUserBean copyBean = new GroupUserBean(groupUser);
							copyBean.getGroupUserBean().setUserContact(EMediaType.Secondary);
							userMap.put(key, new LabelValueBean(copyBean.getGroupUserBean(), name +" "+ EWrap.Brace.enclose(mobileNo1), mobileNo1));
						}
					}
				}

			}
		}
		logger.info("UserBoComboBoxImpl getUsers ends:::", userMap.size());
		return userMap.values();
	}

	private void insertInMap(String dataFrom, String media, Map<String, LabelValueBean> userMap, List<Object> rows)
	{
		logger.info("UserBoComboBoxImpl insertInMap starts ::: ", dataFrom);
		if (CommonValidator.isListFirstNotEmpty(rows))
		{
			String key = "";
			long userId;
			String name = null;
			String lastName = null;
			String userName = null;
			String emailId = null;
			String emailId1 = null;
			String mobileNo = null;
			String mobileNo1 = null;

			Row row = null;
			for (Object object : rows)
			{
				row = (Row) object;
				userId = row.getLong(USERID);
				userName = row.getString(USERNAME);
				name = row.getString(FIRSTNAME);
				lastName = row.getString(LASTNAME);
				emailId = row.getString(P_EMAIL);
				emailId1 = row.getString(S_EMAIL);
				mobileNo = row.getString(P_MOBILE);
				mobileNo1 = row.getString(S_MOBILE);

				key = dataFrom + userId;

				if (userMap.containsKey(key) == false)
				{
					GroupUserBean groupUser = new GroupUserBean(userId, userName, emailId, emailId1, mobileNo, mobileNo1);

					if (CommonValidator.isNotNullNotEmpty(lastName))
						name = lastName + COMMA_SPACE + name;
					
					switch ( dataFrom )
					{
						case FIRSTNAME :
						case LASTNAME :
						{
							// For Email
							if (CommonValidator.isEqual(media, EMedia.Email))
							{
								if (CommonValidator.isNotNullNotEmpty(emailId))
								{
									key = EMediaType.Primary + media + userId;

									if (userMap.containsKey(key) == false)
									{
										groupUser.setUserContact(EMediaType.Primary);
										userMap.put(key, new LabelValueBean(groupUser, name + EWrap.Brace.enclose(emailId)));
									}
								}

								if (CommonValidator.isNotNullNotEmpty(emailId1))
								{
									key = EMediaType.Secondary + media + userId;
									if (userMap.containsKey(key) == false)
									{
										GroupUserBean copyBean = new GroupUserBean(groupUser);
										copyBean.getGroupUserBean().setUserContact(EMediaType.Secondary);
										userMap.put(key, new LabelValueBean(copyBean.getGroupUserBean(), name + EWrap.Brace.enclose(emailId1)));
									}
								}

							}

							else if (CommonValidator.isEqual(media, EMedia.SMS))
							{
								// For Mobile
								if (CommonValidator.isNotNullNotEmpty(mobileNo))
								{
									key = EMediaType.Primary + media + userId;
									if (userMap.containsKey(key) == false)
									{
										GroupUserBean copyBean = new GroupUserBean(groupUser);

										copyBean.getGroupUserBean().setUserContact(EMediaType.Primary);
										userMap.put(key, new LabelValueBean(copyBean.getGroupUserBean(), name + EWrap.Brace.enclose(mobileNo)));
									}
								}
								if (CommonValidator.isNotNullNotEmpty(mobileNo1))
								{
									key = EMediaType.Secondary + media + userId;
									if (userMap.containsKey(key) == false)
									{
										GroupUserBean copyBean = new GroupUserBean(groupUser);
										copyBean.getGroupUserBean().setUserContact(EMediaType.Secondary);
										userMap.put(key, new LabelValueBean(copyBean.getGroupUserBean(), name + EWrap.Brace.enclose(mobileNo1)));
									}
								}
							}
							break;
						}
						case P_EMAIL :
						{
							groupUser.setUserContact(EMediaType.Primary);
							userMap.put(key, new LabelValueBean(groupUser, name + EWrap.Brace.enclose(emailId)));
							break;
						}
						case S_EMAIL :
						{
							groupUser.setUserContact(EMediaType.Secondary);
							userMap.put(key, new LabelValueBean(groupUser, name + EWrap.Brace.enclose(emailId1)));
							break;
						}
						case P_MOBILE :
						{
							groupUser.setUserContact(EMediaType.Primary);
							userMap.put(key, new LabelValueBean(groupUser, name + EWrap.Brace.enclose(mobileNo)));
							break;
						}
						case S_MOBILE :
						{
							groupUser.setUserContact(EMediaType.Secondary);
							userMap.put(key, new LabelValueBean(groupUser, name + EWrap.Brace.enclose(mobileNo1)));
							break;
						}
					}

				}
			}
			logger.info("UserBoComboBoxImpl insertInMap ends ::: ");
		}
	}
}