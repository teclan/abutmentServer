package by.ywzn.abutmentServer.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import by.ywzn.abutmentServer.service.AbutmentService;
import by.ywzn.abutmentServer.util.HttpTool;
import by.ywzn.abutmentServer.util.Objects;

/**
 * 本类提供中转站对外接口
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/abutmentServer/abutment/")
public class AbutmentCtrl {

	private static final Logger logger = LoggerFactory.getLogger(AbutmentCtrl.class);

	@Resource(name = "abutmentService")
	private AbutmentService abutmentService;

	/**
	 * 联网报警系统调用,发送指定事件到指定工作站
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("sendEventToStation")
	public JSONObject sendEventToStation(HttpServletRequest request, HttpServletResponse response) {
		logger.info("init sendEventToStation ctrl ...");
		try {
			JSONObject jsonParam = HttpTool.readJSONParam(request);
			logger.info("sendEventToStation param is {}", jsonParam);
			return abutmentService.sendEventToStation(jsonParam);
		} catch (Exception e) {
			JSONObject jso = new JSONObject();
			jso.put("code", 201);
			jso.put("message", "失败");
			jso.put("warn", e.getMessage());
			return jso;
		}
	}

	/**
	 * 工作站调用,传入用户编号列表,查询出用户简要信息返回给工作站
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getUserInfoByUserIds")
	public JSONObject getUserInfoByUserIds(HttpServletRequest request, HttpServletResponse response) {
		logger.info("init getUserInfoByUserIds ctrl ...");
		try {
			JSONObject jsonParam = HttpTool.readJSONParam(request);
			logger.info("getUserInfoByUserIds param is {}", jsonParam);
			return abutmentService.getUserInfoByUserIds(jsonParam);
		} catch (Exception e) {
			JSONObject jso = new JSONObject();
			jso.put("code", 201);
			jso.put("message", "失败");
			jso.put("warn", e.getMessage());
			return jso;
		}
	}

	/**
	 * 中心调用,传入用户编号列表和工作站,查询出用户简要信息给指定工作站
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("sendUserInfo")
	public JSONObject sendUserInfo(HttpServletRequest request, HttpServletResponse response) {
		logger.info("init sendUserInfo ctrl ...");
		try {
			JSONObject jsonParam = HttpTool.readJSONParam(request);
			logger.info("sendUserInfo param is {}", jsonParam);
			return abutmentService.sendUserInfo(jsonParam);
		} catch (Exception e) {
			JSONObject jso = new JSONObject();
			jso.put("code", 201);
			jso.put("message", "失败");
			jso.put("warn", e.getMessage());
			return jso;
		}
	}

	/**
	 * 工作站调用，同步事件列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("syncAlarmEvent")
	public JSONObject syncAlarmEvent(HttpServletRequest request, HttpServletResponse response) {

		try {
			JSONObject jsonParam = HttpTool.readJSONParam(request);
			
			logger.info("[同步事件列表] syncAlarmEvent.do 参数:{}", jsonParam);

			if (Objects.isNullString(jsonParam.getString("stationHost"))) {
				jsonParam.put("stationHost", request.getRemoteAddr());
			}
			
			if (Objects.isNullString(jsonParam.getString("stationPort"))) {
				jsonParam.put("stationPort", request.getRemoteAddr());
			}

			return abutmentService.syncAlarmEvent(jsonParam);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			JSONObject jso = new JSONObject();
			jso.put("code", 201);
			jso.put("message", "失败");
			jso.put("warn", e.getMessage());
			return jso;
		}
	}

}
