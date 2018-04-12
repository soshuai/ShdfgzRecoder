package cn.cherish.shdfgzrecoder.okhttp.entity;

import java.util.ArrayList;

/**
 * 基于list的数据实体
 * 
 * @param <T>
 *            list项数据元
 */
public class BaseApiListEntity<T extends BaseEntity> extends BaseApiEntity {

    private ArrayList<T> dataList;

    public BaseApiListEntity() {
        super();
    }

    public BaseApiListEntity(Integer code, String msg) {
        super(code, msg);
    }

    public ArrayList<T> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<T> dataList) {
        this.dataList = dataList;
    }

}
