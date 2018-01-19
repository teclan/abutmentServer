package by.ywzn.abutmentServer;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import by.ywzn.abutmentServer.util.HttpClientTool;

public class TanTest {

	@Test
	public void test() {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("stationHost", "192.168.3.134");
		jsonObject.put("stationPort", "9090");

		String result = HttpClientTool.post("http://localhost:8088/abutmentServer/abutment/syncAlarmEvent.do",
				jsonObject.toJSONString());

		JSONObject object = JSON.parseObject(result);

		System.err.println(object);


	}

}
