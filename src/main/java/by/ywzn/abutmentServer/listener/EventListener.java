package by.ywzn.abutmentServer.listener;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import by.ywzn.abutmentServer.dao.AbutmentDao;
import by.ywzn.abutmentServer.pojo.ImmWorkstationInfoPojo;
import by.ywzn.abutmentServer.util.AnalyzeStationUtil;
import by.ywzn.abutmentServer.util.HttpClientTool;

/**
 * 事件监听的监听器
 * 
 * @author Administrator
 *
 */
@Component
public class EventListener {

	private static final Logger logger = LoggerFactory.getLogger(EventListener.class);

	private @Value("${pushAlarmEvent}") String pushAlarmEvent;

	@Resource(name = "abutmentDao")
	private AbutmentDao abutmentDao;

	/**
	 * 监听事件topic
	 * 
	 * @param text
	 */
	@JmsListener(destination = "${mq.reciveTopic}")
	public void receive(String text) {
		logger.info("Consumer收到的报文为: {}", text);
		try {
			JSONObject messageJso = JSONObject.parseObject(text);
			// 只有mode为add时的事件才被处理
			if (messageJso.containsKey("mode") && "add".equals(messageJso.getString("mode"))) {
				logger.info("mode is add, continue.AlertPojo is :" + messageJso.getString("alertPojo"));
				JSONObject parames = JSONObject.parseObject(messageJso.getString("alertPojo"));
				String sysCode = parames.getString("sysCode");
				if (sysCode == null || sysCode.equals(""))
					throw new Exception("系统码为空!");
				String devId = parames.getString("devId");
				if (devId == null || devId.equals(""))
					throw new Exception("设备编号为空!");
				
				// 获取此系统码的工作站
				String realSysCode = sysCode.substring(1);
				List<ImmWorkstationInfoPojo> wsList = abutmentDao.getWorkstationByCode(realSysCode,devId);
				logger.info("需要转发到[{}]个工作站", wsList.size());

				abutmentDao.saveToForwardHistory(wsList, parames);

				// 转发到编号绑定的工作站
				for (ImmWorkstationInfoPojo pojo : wsList) {
					String host = AnalyzeStationUtil.getUrlByStation(pojo);
					String url = host + pushAlarmEvent;
					String response = HttpClientTool.post(url, parames.toJSONString());
					logger.info("事件转发到工作站[{}]的返回为:{}", host, response);
				}
			}
		} catch (Exception e) {
			logger.warn("解析处理报文出错:" + e.getMessage(), e);
		}

	}
}
