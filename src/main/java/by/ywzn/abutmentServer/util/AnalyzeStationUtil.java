package by.ywzn.abutmentServer.util;

import by.ywzn.abutmentServer.pojo.ImmWorkstationInfoPojo;

/**
 * 解析工作站信息的util
 * 
 * @author Administrator
 *
 */
public class AnalyzeStationUtil {

	/**
	 * 解析工作站信息得到一个工作站的url
	 * 
	 * @param station
	 * @return
	 * @throws Exception
	 */
	public static String getUrlByStation(ImmWorkstationInfoPojo pojo) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("http://");
		sb.append(pojo.getStationHost());
		sb.append(":");
		sb.append(pojo.getStationPort());
		sb.append("/");
		return sb.toString();
	}
}
