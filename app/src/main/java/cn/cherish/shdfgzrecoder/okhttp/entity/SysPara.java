package cn.cherish.shdfgzrecoder.okhttp.entity;

import java.io.Serializable;

public class SysPara extends BaseEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4238957397558926809L;
    public static final int   QINIU_UPLOAD     = 1;
    public static final int   FTP_UPLOAD       = 2;

    private String ftpAccount;
    private String ftpPassword;
    private String ftpIP;
    private int               uploadType;
    private String connectPrefix;

    public SysPara() {
        super();
    }

    public String getConnectPrefix() {
        return connectPrefix;
    }

    public void setConnectPrefix(String connectPrefix) {
        this.connectPrefix = connectPrefix;
    }

    public String getFtpAccount() {
        return ftpAccount;
    }

    public void setFtpAccount(String ftpAccount) {
        this.ftpAccount = ftpAccount;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getFtpIP() {
        return ftpIP;
    }

    public void setFtpIP(String ftpIP) {
        this.ftpIP = ftpIP;
    }

    public int getUploadType() {
        return uploadType;
    }

    public void setUploadType(int uploadType) {
        this.uploadType = uploadType;
    }

}
