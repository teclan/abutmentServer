package by.ywzn.abutmentServer.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import by.ywzn.abutmentServer.dao.AbutmentDao;
import by.ywzn.abutmentServer.pojo.ImmWorkstationInfoPojo;
import by.ywzn.abutmentServer.pojo.UserBriefInfo;
import by.ywzn.abutmentServer.util.Objects;

@Repository("abutmentDao")
public class AbutmentDaoImpl implements AbutmentDao {
	private static final Logger logger = LoggerFactory.getLogger(AbutmentDaoImpl.class);

	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ImmWorkstationInfoPojo> getWorkstationByCode(String code, String devId) throws Exception {
		logger.info("init getWorkstationByCode dao ,code:[{}],devId:[{}]", code, devId);
		String sql = "select * from imm_workstation_info where stationNum in (select stationNum from imm_alarm_forward where devId = ?) and sysCode like ?";
		return jdbcTemplate.query(sql, new Object[] { devId, "%" + code + "%" },
				new RowMapper<ImmWorkstationInfoPojo>() {
					@Override
					public ImmWorkstationInfoPojo mapRow(ResultSet rs, int rowNum) throws SQLException {
						ImmWorkstationInfoPojo pojo = new ImmWorkstationInfoPojo();
						pojo.setId(rs.getInt("id"));
						pojo.setStationNum(rs.getString("stationNum"));
						pojo.setStationName(rs.getString("stationName"));
						pojo.setStationHost(rs.getString("stationHost"));
						pojo.setStationPort(rs.getInt("stationPort"));
						pojo.setSysCode(rs.getString("sysCode"));
						pojo.setfMemo(rs.getString("fMemo"));
						return pojo;
					}
				});
	}

	@Override
	public List<UserBriefInfo> getUserInfoByUserIds(JSONArray userIds) throws Exception {
		logger.info("init getUserInfoByUserIds dao,userIds: {}", userIds);
		/**
		 * 拼接用户表的sql
		 */
		StringBuffer userInfoSqlSb = new StringBuffer();
		userInfoSqlSb.append("select userId,userName,areaId from imm_userinfo where userId in (");
		for (int i = 0; i < userIds.size(); i++)
			userInfoSqlSb.append("?,");
		userInfoSqlSb.delete(userInfoSqlSb.length() - 1, userInfoSqlSb.length());
		userInfoSqlSb.append(")");
		String sql = "select userInfo.userId userId,userInfo.userName userName,area.areaName areaName,"
				+ "attr.userAddr userAddr,attr.contact contact,attr.cPhone cPhone,attr.cMobile cMobile,attr.pnlTel pnlTel,attr.pnlHdTel pnlHdTel "
				+ "FROM (#userInfoSql) userInfo "
				+ "LEFT JOIN  (select areaId,areaName from imm_area) area ON (userInfo.areaId = area.areaId) "
				+ "LEFT JOIN (select userId,userAddr,contact,cPhone,cMobile,pnlTel,pnlHdTel from imm_customerAttr) attr "
				+ "ON(userInfo.userId =attr.userId)";
		String newSql = sql.replace("#userInfoSql", userInfoSqlSb.toString());

		return jdbcTemplate.query(newSql, userIds.toArray(), new RowMapper<UserBriefInfo>() {
			@Override
			public UserBriefInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserBriefInfo info = new UserBriefInfo();
				info.setUserId(rs.getString("userId"));
				info.setUserName(rs.getString("userName"));
				info.setAreaName(rs.getString("areaName"));
				info.setUserAddr(rs.getString("userAddr"));
				info.setContact(rs.getString("contact"));
				info.setCPhone(rs.getString("cPhone"));
				info.setCMobile(rs.getString("cMobile"));
				info.setPnlTel(rs.getString("pnlTel"));
				info.setPnlHdTel(rs.getString("pnlHdTel"));
				return info;
			}
		});

	}

	@Override
	public ImmWorkstationInfoPojo getWorkstationById(String id) throws Exception {
		logger.info("init getWorkstationById dao ,id:[{}]", id);
		String sql = "select * from imm_workstation_info where id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { id },
				new BeanPropertyRowMapper<>(ImmWorkstationInfoPojo.class));
	}

	@Override
	public List<ImmWorkstationInfoPojo> getWorkstationsByIds(JSONArray ids) throws Exception {
		logger.info("init getWorkstationByCode dao ,ids:[{}]", ids);
		/**
		 * 拼接用户表的sql
		 */
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("select * from imm_workstation_info where stationNum in (");
		for (int i = 0; i < ids.size(); i++)
			sqlSb.append("?,");
		sqlSb.delete(sqlSb.length() - 1, sqlSb.length());
		sqlSb.append(")");

		return jdbcTemplate.query(sqlSb.toString(), ids.toArray(), new RowMapper<ImmWorkstationInfoPojo>() {
			@Override
			public ImmWorkstationInfoPojo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ImmWorkstationInfoPojo pojo = new ImmWorkstationInfoPojo();
				pojo.setId(rs.getInt("id"));
				pojo.setStationNum(rs.getString("stationNum"));
				pojo.setStationName(rs.getString("stationName"));
				pojo.setStationHost(rs.getString("stationHost"));
				pojo.setStationPort(rs.getInt("stationPort"));
				pojo.setSysCode(rs.getString("sysCode"));
				pojo.setfMemo(rs.getString("fMemo"));
				return pojo;
			}
		});
	}

	@Override
	public boolean saveToForwardHistory(List<ImmWorkstationInfoPojo> wsList, JSONObject alertPojo) {

		List<String> wsIds = new ArrayList<String>();

		for (ImmWorkstationInfoPojo ws : wsList) {
			wsIds.add(ws.getStationNum());
		}

		String sql = "insert into imm_forward_history (stationNums,alarmEvent) values (?,?)";

		try {
			jdbcTemplate.update(sql, Objects.Joiner(",", wsIds), alertPojo.toJSONString().getBytes("UTF-8"));
			logger.info("事件`{}`存档成功：{} ...", alertPojo.getString("eventNum"), alertPojo.toJSONString());
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	public String getStationNumByHostAndPort(String host, String port) {
		String sql = "select stationNum from imm_workstation_info where stationHost=? and stationPort=? ";
		try {

			List<Map<String, Object>> lists = jdbcTemplate.queryForList(sql, host, port);

			if (Objects.isNotNull(lists)) {
				return lists.get(0).get("stationNum").toString();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	public List<Map<String, Object>> getForwardHistoryByStationNumAndDate(String stationNum, String timeStart,
			String timeEnd) {

		String sql = "select alarmEvent from imm_forward_history where locate('" + stationNum + "',stationNums)>0 ";

		String timeSql = "";
		if (Objects.isNotNullString(timeStart)) {
			timeSql += " AND updateAt > DATE_SUB('" + timeStart + "',INTERVAL 1 DAY) ";
		}
		if (Objects.isNotNullString(timeEnd)) {
			timeSql += " AND updateAt < DATE_SUB('" + timeEnd + "',INTERVAL -1 DAY) ";
		}

		try {

			List<Map<String, Object>> lists = jdbcTemplate.queryForList(sql + timeSql);

			return lists;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new ArrayList<Map<String, Object>>();
	}

	public boolean cleanForwardHistory() {
		Date date = new Date();
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.MONTH, -3);
		String timeBefor = DATE_FORMAT.format(rightNow.getTime());

		String sql = "delete from imm_forward_history where  updateAt < DATE_SUB('" + timeBefor + "',INTERVAL -1 DAY)";

		try {
			int affected = jdbcTemplate.update(sql);

			logger.info("清理历史转发记录，删除 `{}` 之前的数据，受影响记录数：{}", timeBefor, affected);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return false;

	}
}
