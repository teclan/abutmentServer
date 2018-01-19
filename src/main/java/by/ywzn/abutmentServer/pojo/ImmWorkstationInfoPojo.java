package by.ywzn.abutmentServer.pojo;

public class ImmWorkstationInfoPojo {
	/**
	 * id
	 */
	private Integer id;

	/**
	 * 工作站编号
	 */
	private String stationNum;

	/**
	 * 工作站名称
	 */
	private String stationName;

	/**
	 * 工作站ip
	 */
	private String stationHost;

	/**
	 * 工作站端口
	 */
	private Integer stationPort;

	/**
	 * 事件订阅系统码
	 */
	private String sysCode;

	/**
	 * 备注
	 */
	private String fMemo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStationNum() {
		return stationNum;
	}

	public void setStationNum(String stationNum) {
		this.stationNum = stationNum;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationHost() {
		return stationHost;
	}

	public void setStationHost(String stationHost) {
		this.stationHost = stationHost;
	}

	public Integer getStationPort() {
		return stationPort;
	}

	public void setStationPort(Integer stationPort) {
		this.stationPort = stationPort;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getfMemo() {
		return fMemo;
	}

	public void setfMemo(String fMemo) {
		this.fMemo = fMemo;
	}

}
