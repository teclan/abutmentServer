package by.ywzn.abutmentServer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import by.ywzn.abutmentServer.util.HttpClientTool;

public class Test1 {
	public static void main(String[] args) {
	//	 {"stationNum":"1010","userIdArry":["100000001","100000004","100000005"]}
		 JSONObject jso = new JSONObject();
		 JSONArray jsa = new JSONArray();
		 jsa.add("100000001");
		 jsa.add("1000F031A");
		 jso.put("stationNum", "1");
		 jso.put("userIds", jsa);
		 
		 String s = "{'wsIds':['1','2'],'AlertPojo':{'eventNum':'5','eventTime':'userName2','eventLevel':'userAddr2','evtWay':'areaName2','eventDesc':'contact2','recieiveTime':'cPhone2','sysCode':'cMobile2','codeTypeId':'pnlTel2','accountNum':'pnlHdTel2','accountName':'','accountAddr':'','accountZone':'','usrAlmType':'','devSubSys':'','cameraName':'','userMonitorId':'','cameraModelId':'','alarmAddr':'', 'atPos':'', 'devId':'','devZoneId':'', 'devModelName':'','zoneAtPos':'','snType':'', 'almType':'', 'wantDo':'', 'areaId':'', 'areaName':'', 'snModelName':''}}";
		 
//		String result = HttpClientTool.post("http://10.0.10.234:8080/IntegratedMM/Workstation/userDataTrans.do",
//				jso.toJSONString());
//		String result = HttpClientTool.post("http://10.0.10.234:8088/abutmentServer/abutment/sendUserInfo.do",
//				jso.toJSONString());
		String result = HttpClientTool.post("http://10.0.10.234:8088/abutmentServer/abutment/getUserInfoByUserIds.do",
					jso.toJSONString());
//		String result = HttpClientTool.post("http://10.0.10.234:8088/abutmentServer/abutment/sendEventToStation.do",
//				s);
		System.out.println(result);
	}


}
