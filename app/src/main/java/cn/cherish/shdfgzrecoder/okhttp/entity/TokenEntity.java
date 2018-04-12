package cn.cherish.shdfgzrecoder.okhttp.entity;

import java.io.Serializable;

public class TokenEntity extends BaseApiEntity implements Serializable {

    private static final long serialVersionUID = -13444L;
    private String token;

    public TokenEntity() {
        super();
    }

    public TokenEntity(Integer result, String msg) {
        super(result, msg);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
