package by.ywzn.abutmentServer.pojo;

/**
 * 用户简要信息实体类
 * 
 * @author Administrator
 *
 */
public class UserBriefInfo {
	/**
	 * 机主用户ID
	 */
	private String userId;
	/**
	 * 机主用户名称
	 */
	private String userName;
	/**
	 * 用户地址
	 */
	private String userAddr;
	/**
	 * 所属区域
	 */
	private String areaName;
	/**
	 * 负责人
	 */
	private String contact;
	/**
	 * 负责人电话
	 */
	private String cPhone;
	/**
	 * 负责人手机
	 */
	private String cMobile;
	/**
	 * 联网电话
	 */
	private String pnlTel;
	/**
	 * 无线卡号
	 */
	private String pnlHdTel;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAddr() {
		return userAddr;
	}

	public void setUserAddr(String userAddr) {
		this.userAddr = userAddr;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCPhone() {
		return cPhone;
	}

	public void setCPhone(String cPhone) {
		this.cPhone = cPhone;
	}

	public String getCMobile() {
		return cMobile;
	}

	public void setCMobile(String cMobile) {
		this.cMobile = cMobile;
	}

	public String getPnlTel() {
		return pnlTel;
	}

	public void setPnlTel(String pnlTel) {
		this.pnlTel = pnlTel;
	}

	public String getPnlHdTel() {
		return pnlHdTel;
	}

	public void setPnlHdTel(String pnlHdTel) {
		this.pnlHdTel = pnlHdTel;
	}
}
