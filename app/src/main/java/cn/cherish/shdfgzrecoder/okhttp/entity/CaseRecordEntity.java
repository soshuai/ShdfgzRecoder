package cn.cherish.shdfgzrecoder.okhttp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class CaseRecordEntity extends BaseApiEntity implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2528218006567908151L;
    private int               id;
    private String descp;
    private int               recordType;
    private String fileUrl;
    private String beginTime;
    private String beExecutedName;
    private String executorName;
    private String longitude;                               // 经度
    private String latitude;                                // 纬度;
    private String caseNo;                                  // 案号

    @JsonIgnore
    private double            progress         = -1.0d;
    @JsonIgnore
    private int               progressType     = 0;
    @JsonIgnore
    private int               progressResult   = 0;

    public CaseRecordEntity() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public int getRecordType() {
        return recordType;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getBeExecutedName() {
        return beExecutedName;
    }

    public void setBeExecutedName(String beExecutedName) {
        this.beExecutedName = beExecutedName;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public int getProgressType() {
        return progressType;
    }

    public void setProgressType(int progressType) {
        this.progressType = progressType;
    }

    public int getProgressResult() {
        return progressResult;
    }

    public void setProgressResult(int progressResult) {
        this.progressResult = progressResult;
    }

}
