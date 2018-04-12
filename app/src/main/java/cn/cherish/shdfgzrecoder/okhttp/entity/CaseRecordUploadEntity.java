package cn.cherish.shdfgzrecoder.okhttp.entity;

import java.io.Serializable;

public class CaseRecordUploadEntity extends BaseApiEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1829233272095502736L;
    private String url;

    // private String records:[{执行记录}......]}

    public CaseRecordUploadEntity() {
        super();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
