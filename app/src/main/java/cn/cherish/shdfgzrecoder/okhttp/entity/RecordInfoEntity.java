package cn.cherish.shdfgzrecoder.okhttp.entity;

public class RecordInfoEntity extends BaseEntity implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4104770978676774681L;
    private int               recordId;
    private String beExecuted;
    private String vidyoRoomAccount;
    private String vidyoRoomExtension;
    private Boolean isRecorderSuccess;


    public RecordInfoEntity() {
        super();
    }

    public Boolean getIsRecorderSuccess() {
        return isRecorderSuccess;
    }

    public void setIsRecorderSuccess(Boolean isRecorderSuccess) {
        this.isRecorderSuccess = isRecorderSuccess;
    }

    public String getBeExecuted() {
        return beExecuted;
    }

    public void setBeExecuted(String beExecuted) {
        this.beExecuted = beExecuted;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getVidyoRoomAccount() {
        return vidyoRoomAccount;
    }

    public void setVidyoRoomAccount(String vidyoRoomAccount) {
        this.vidyoRoomAccount = vidyoRoomAccount;
    }

    public String getVidyoRoomExtension() {
        return vidyoRoomExtension;
    }

    public void setVidyoRoomExtension(String vidyoRoomExtension) {
        this.vidyoRoomExtension = vidyoRoomExtension;
    }
}
