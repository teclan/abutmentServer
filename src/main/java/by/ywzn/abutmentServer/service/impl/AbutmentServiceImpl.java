package by.ywzn.abutmentServer.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import by.ywzn.abutmentServer.dao.AbutmentDao;
import by.ywzn.abutmentServer.pojo.ImmWorkstationInfoPojo;
import by.ywzn.abutmentServer.pojo.UserBriefInfo;
import by.ywzn.abutmentServer.service.AbutmentService;
import by.ywzn.abutmentServer.util.AnalyzeStationUtil;
import by.ywzn.abutmentServer.util.HttpClientTool;
import by.ywzn.abutmentServer.util.Objects;

@Service("abutmentService")
public class AbutmentServiceImpl implements AbutmentService {

	private static final Logger logger = LoggerFactory.getLogger(AbutmentServiceImpl.class);
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private @Value("${pushUserInfo}") String pushUserInfo;
	private @Value("${pushAlarmEvent}") String pushAlarmEvent;

	@Resource(name = "abutmentDao")
	private AbutmentDao abutmentDao;

	@Override
	public JSONObject sendEventToStation(JSONObject parames) throws Exception {
		logger.info("init sendEventToStation service ...");
		// String wsId = parames.getString("stationNum");
		JSONArray wsIds = JSONArray.parseArray(parames.getString("wsIds"));
		JSONObject alertPojo = parames.getJSONObject("AlertPojo");
		// 获取工作站信息
		// ImmWorkstationInfoPojo wsPojo = abutmentDao.getWorkstationById(wsId);
		List<ImmWorkstationInfoPojo> wsList = abutmentDao.getWorkstationsByIds(wsIds);

		abutmentDao.saveToForwardHistory(wsList, alertPojo);

		// 分发到各个工作站
		JSONObject Jso = new JSONObject();
		JSONArray result = new JSONArray();
		for (ImmWorkstationInfoPojo wsPojo : wsList) {
			// 每个工作站的转发状况
			JSONObject wsJso = new JSONObject();
			String host = AnalyzeStationUtil.getUrlByStation(wsPojo);
			String url = host + pushAlarmEvent;
			logger.info("事件转发到工作站[{}]......", host);
			wsJso.put("addr", host);
			try {
				String response = HttpClientTool.post(url, alertPojo.toJSONString());
				logger.info("事件转发到工作站[{}]的返回为:{}", host, response);
				JSONObject respJso = JSONObject.parseObject(response);
				String code = respJso.getString("code");
				if (code != null && code.equals("200"))
					wsJso.put("isSucc", true);
				else
					wsJso.put("isSucc", false);
			} catch (Exception e) {
				logger.warn("事件转发到工作站[{}]失败:{}", host, e.getMessage(), e);
				wsJso.put("isSucc", false);
			}
			result.add(wsJso);
		}
		Jso.put("result", result);
		Jso.put("code", "200");
		Jso.put("message", "成功");
		logger.info("sendEventToStation result:" + Jso.toJSONString());
		return Jso;
	}

	@Override
	public JSONObject getUserInfoByUserIds(JSONObject parames) throws Exception {
		logger.info("init getUserInfoByUserIds service...");
		// 用户编号
		JSONArray userIds = JSONArray.parseArray(parames.getString("userIds"));
		if (userIds == null || userIds.isEmpty())
			throw new Exception("用户编号为空!");
		List<UserBriefInfo> list = abutmentDao.getUserInfoByUserIds(userIds);

		JSONObject jso = new JSONObject();
		jso.put("code", 200);
		jso.put("message", "成功");
		jso.put("userBriefs", list);
		return jso;
	}

	@Override
	public JSONObject sendUserInfo(JSONObject parames) throws Exception {
		logger.info("init sendUserInfo service ...");
		String wsId = parames.getString("stationNum");
		JSONArray userIds = JSONArray.parseArray(parames.getString("userIdArry"));
		// 获取工作站信息
		ImmWorkstationInfoPojo wsPojo = abutmentDao.getWorkstationById(wsId);

		// 获取用户简要信息
		JSONObject requestJso = new JSONObject();
		List<UserBriefInfo> list = abutmentDao.getUserInfoByUserIds(userIds);
		requestJso.put("userBriefs", list);
		// 发往工作站
		String host = AnalyzeStationUtil.getUrlByStation(wsPojo);
		String url = host + pushUserInfo;
		String response = HttpClientTool.post(url, requestJso.toJSONString());
		logger.info("用户信息转发到工作站[{}]的返回为:{}", host, response);
		JSONObject result = JSONObject.parseObject(response);
		return result;
	}

	public JSONObject syncAlarmEvent(JSONObject parames) {
		
		String stationHost = parames.getString("stationHost");
		String stationPort = parames.getString("stationPort");

		Date date = new Date();

		// 如果开始时间为空，则取当前时间往前推三个月作为开始时间
		String timeStart = parames.getString("timeStart");
		if (Objects.isNullString(timeStart)) {
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(date);
			rightNow.add(Calendar.MONTH, -3);
			timeStart = DATE_FORMAT.format(rightNow.getTime());
		}
		// 如果结束时间为空，则取当前时间作为结束时间
		String timeEnd = Objects.isNullString(parames.getString("timeEnd")) ? DATE_FORMAT.format(new Date())
				: parames.getString("timeEnd");

		String stationNum = abutmentDao.getStationNumByHostAndPort(stationHost, stationPort);

		logger.info("\n{}:{} 对应的工作站编号为:{}", stationHost, stationPort, stationNum);

		List<Map<String, Object>> lists = abutmentDao.getForwardHistoryByStationNumAndDate(stationNum, timeStart,
				timeEnd);

		JSONArray array = new JSONArray();
		for (Map<String, Object> map : lists) {
			array.add(map.get("alarmEvent").toString());
		}

		JSONObject result = new JSONObject();
		
		result.put("alertPojos", array);
		result.put("code", "200");
		result.put("message", "查询成功");

		String str = result.toJSONString();
		str = str.replace("\\\"", "\"").replace("\"{", "{").replace("}\"", "}").replace("\"[", "[").replace("]\"", "]");
		result = JSON.parseObject(str);
		return result;
	}

}
