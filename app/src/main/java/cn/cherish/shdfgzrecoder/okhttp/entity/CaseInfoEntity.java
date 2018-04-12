package cn.cherish.shdfgzrecoder.okhttp.entity;

import java.io.Serializable;

public class CaseInfoEntity extends BaseApiEntity implements Serializable {

    private static final long serialVersionUID = -13444L;

    private Integer id;
    private String caseNo;                    // "案号",
    private String filingDate;                // "立案日期yyyy-HH-mm",
    private String causeOfCase;               // "实际案由",
    private String applyTarget;               // "申请标的",
    private String beExecuted;                // "诉讼人员",
    private String beExecutedPosition;        // "诉讼地位",
    private String applyUserName;             // "申请人",
    private String measureDescp;              // "执行措施",
    private Integer state;                     // 执行状态
    private String descp;                     // "描述",
    private String executorName;              // 承办人
    private String planExecutors;             // 计划人员
    private String planExecutorsId;           // 计划人员id
    private String planDate;                  // 计划日期
    private String rtmp;                        //手机推流地址
    private String channelId;                   //推流标志符

    // private String records:[{执行记录}......]}

    public CaseInfoEntity() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBeExecutedPosition() {
        return beExecutedPosition;
    }

    public void setBeExecutedPosition(String beExecutedPosition) {
        this.beExecutedPosition = beExecutedPosition;
    }

    public String getPlanExecutorsId() {
        return planExecutorsId;
    }

    public void setPlanExecutorsId(String planExecutorsId) {
        this.planExecutorsId = planExecutorsId;
    }

    public String getPlanExecutors() {
        return planExecutors;
    }

    public void setPlanExecutors(String planExecutors) {
        this.planExecutors = planExecutors;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getCauseOfCase() {
        return causeOfCase;
    }

    public void setCauseOfCase(String causeOfCase) {
        this.causeOfCase = causeOfCase;
    }

    public String getApplyTarget() {
        return applyTarget;
    }

    public void setApplyTarget(String applyTarget) {
        this.applyTarget = applyTarget;
    }

    public String getBeExecuted() {
        return beExecuted;
    }

    public void setBeExecuted(String beExecuted) {
        this.beExecuted = beExecuted;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getMeasureDescp() {
        return measureDescp;
    }

    public void setMeasureDescp(String measureDescp) {
        this.measureDescp = measureDescp;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(String filingDate) {
        this.filingDate = filingDate;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public String getRtmp() {
        return rtmp;
    }

    public void setRtmp(String rtmp) {
        this.rtmp = rtmp;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
