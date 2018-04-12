package cn.cherish.shdfgzrecoder.okhttp.entity;

/**
 * {@link BaseApiEntity} 的默认实现类；与api交互时如无字段扩展，可使用该类进行处理
 */
public final class DefaultApiEntity extends BaseApiEntity {
    public DefaultApiEntity() {
        super();
    }

    public DefaultApiEntity(Integer code, String msg) {
        super(code, msg);
    }
}
