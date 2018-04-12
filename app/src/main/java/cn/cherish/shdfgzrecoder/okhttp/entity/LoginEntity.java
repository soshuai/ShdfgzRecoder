package cn.cherish.shdfgzrecoder.okhttp.entity;


import java.util.List;

import cn.cherish.shdfgzrecoder.okhttp.bean.UserBean;
import cn.cherish.shdfgzrecoder.okhttp.utils.AESEncrypt;

public class LoginEntity extends BaseApiEntity {

    private UserBean userinfo;
    private SysPara                sysParas;
    private String token;
    private AESEncrypt aesEncrypt;
    private String loginCode;

    public LoginEntity() {
        super();
    }

    public LoginEntity(Integer result, String msg) {
        super(result, msg);
    }

    public LoginEntity(UserBean userEntity, SysPara sysParas, String token,
                       AESEncrypt aesEncrypt, String loginCode) {
        this.userinfo = userEntity;
        this.token = token;
        this.sysParas = sysParas;
        this.aesEncrypt = aesEncrypt;
        this.loginCode = loginCode;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public SysPara getSysParas() {
        return sysParas;
    }

    public String getToken() {
        return token;
    }

    public UserBean getUserinfo() {
        return userinfo;
    }


    public void setUserinfo(UserBean userinfo) {
        this.userinfo = userinfo;
    }

    public AESEncrypt getAesEncrypt() {
        return aesEncrypt;
    }
}
