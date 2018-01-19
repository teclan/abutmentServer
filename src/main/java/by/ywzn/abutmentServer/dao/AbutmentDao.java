package by.ywzn.abutmentServer.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import by.ywzn.abutmentServer.pojo.ImmWorkstationInfoPojo;
import by.ywzn.abutmentServer.pojo.UserBriefInfo;

public interface AbutmentDao {
	/**
	 * 通过系统码和设备获取工作站
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public List<ImmWorkstationInfoPojo> getWorkstationByCode(String code,String devId) throws Exception;

	/**
	 * 通过工作站id获取工作站信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ImmWorkstationInfoPojo getWorkstationById(String id) throws Exception;
	
	/**
	 * 通过id数组获取工作站
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public List<ImmWorkstationInfoPojo> getWorkstationsByIds(JSONArray ids) throws Exception;

	/**
	 * 通过用户编号列表,查询出用户简要信息
	 * 
	 * @param userIds
	 * @return
	 * @throws Exception
	 */
	public List<UserBriefInfo> getUserInfoByUserIds(JSONArray userIds) throws Exception;

	/**
	 * 将转发记录存至管理平台的 imm_forward_history 表
	 * 
	 * @param wsList
	 * @param alertPojo
	 * @return
	 */
	public boolean saveToForwardHistory(List<ImmWorkstationInfoPojo> wsList, JSONObject alertPojo);

	/**
	 * 根据IP和端口查询工作站编号
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	public String getStationNumByHostAndPort(String host, String port);

	/**
	 * 通过工作站编号和时间，查询历史事件转发记录
	 * 
	 * @param stationNum
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 */
	public List<Map<String, Object>> getForwardHistoryByStationNumAndDate(String stationNum, String timeStart,
			String timeEnd);

	public boolean cleanForwardHistory();
}
