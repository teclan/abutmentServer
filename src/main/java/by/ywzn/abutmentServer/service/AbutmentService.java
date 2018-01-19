package by.ywzn.abutmentServer.service;

import com.alibaba.fastjson.JSONObject;

public interface AbutmentService {
	/**
	 * 发送指定事件到指定工作站
	 * @param parames
	 * @return
	 * @throws Exception
	 */
	public JSONObject sendEventToStation(JSONObject parames) throws Exception;
	
	/**
	 * 传入用户编号列表,查询出用户简要信息返回给工作站
	 * @param parames
	 * @return
	 * @throws Exception
	 */
	public JSONObject getUserInfoByUserIds(JSONObject parames) throws Exception;
	
	/**
	 *  中心调用,传入用户编号列表和工作站,查询出用户简要信息给指定工作站
	 * @param parames
	 * @return
	 * @throws Exception
	 */
	public JSONObject sendUserInfo(JSONObject parames) throws Exception;

	/**
	 * 传入工作站IP，端口，开始时间和结束时间，查询历史转发记录
	 * 
	 * @param parames
	 * @return
	 */
	public JSONObject syncAlarmEvent(JSONObject parames);
}
