package cn.cherish.shdfgzrecoder.okhttp.bean;

import java.io.Serializable;

import cn.cherish.shdfgzrecoder.okhttp.entity.BaseEntity;

public class UserBean extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -123345667L;

	private Integer id;// 帐号id
	private String name; // "用户姓名",
	private String vidyoAccount; // "视频会议账号",
	private String vidyoPassword;// "视频会议密码",
	private String contentToken;// "token"

	public UserBean() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVidyoAccount() {
		return vidyoAccount;
	}

	public void setVidyoAccount(String vidyoAccount) {
		this.vidyoAccount = vidyoAccount;
	}

	public String getVidyoPassword() {
		return vidyoPassword;
	}

	public void setVidyoPassword(String vidyoPassword) {
		this.vidyoPassword = vidyoPassword;
	}

	public String getContentToken() {
		return contentToken;
	}

	public void setContentToken(String contentToken) {
		this.contentToken = contentToken;
	}

}
