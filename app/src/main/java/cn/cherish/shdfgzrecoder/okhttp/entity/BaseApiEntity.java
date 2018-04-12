package cn.cherish.shdfgzrecoder.okhttp.entity;

import android.util.SparseArray;

import cn.cherish.shdfgzrecoder.okhttp.AppException;


/**
 * api交互实体类基类，该类的默认实现为{@link DefaultApiEntity}
 */
public abstract class BaseApiEntity extends BaseEntity {
    protected int                           result;
    protected String msg;
    // ////////////////////////////////////////////////////////////
    //
    // 异常定义
    // 0:正常
    // <0:移动端使用
    // >0:服务端使用
    //
    public static final int                 RESULT_OK                              = 0;
    // 2141346
    public static final int                 ERROR_CODE_UNKOWN                      = -10086;
    public static final int                 ERROR_CODE_NET                         = -100;
    public static final int                 ERROR_CODE_ENCRYPT                     = -101;
    public static final int                 ERROR_CODE_JSON                        = -102;
    public static final int                 ERROR_CODE_IO                          = -103;

    public static final int                 ERROR_CODE_USER_NOTFOUND               = 1;                        // 用户没找到
    public static final int                 ERROR_CODE_USER_PASSWORD_ERROR         = 2;                        // 用户密码错误
    public static final int                 ERROR_CODE_USER_EXPIRED                = 3;                        // 用户已过有效期
    public static final int                 ERROR_CODE_EMAIL_DUPLICATE             = 11;                       // 邮件地址重复
    public static final int                 ERROR_CODE_MOBILE_DUPLICATE            = 12;                       // 手机号码重复
    public static final int                 ERROR_CODE_MOBILE_MESSAGE_FAILED       = 13;                       // 短信发送失败
    public static final int                 ERROR_CODE_MOBILE_NOT_BIND             = 14;
    // 手机号码未绑定
    public static final int                 ERROR_CODE_USERNAME_DUPLICATE          = 104;

    public static final int                 ERROR_CODE_VCODE_ERROR                 = 110;                      // 验证码错误

    public static final int                 ERROR_CODE_PUB_CHANNEL_ERROR           = 120;
    public static final int                 ERROR_CODE_PUB_HAS_NOPAY               = 121;

    public static final int                 ERROR_CODE_TIME_CONFLICT               = 130;
    public static final int                 ERROR_CODE_WORK_REPEAT                 = 131;
    public static final int                 ERROR_CODE_WORK_PERSON_OK              = 132;
    public static final int                 ERROR_CODE_WORK_JK_CARD                = 133;
    public static final int                 ERROR_CODE_WORK_LOCK                   = 134;

    public static final int                 ERROR_CODE_WORK_SEX                    = 135;
    public static final int                 ERROR_CODE_WORK_EVAL                   = 136;
    public static final int                 ERROR_CODE_WORK_AGE                    = 137;
    public static final int                 ERROR_CODE_WORK_HEIGHT                 = 138;
    public static final int                 ERROR_CODE_WORK_SKILL                  = 139;

    public static final int                 ERROR_CODE_SALARY_STATE_ERROR          = 140;
    public static final int                 ERROR_CODE_SALARY_CSTATE_ERROR         = 141;
    public static final int                 ERROR_CODE_SALARY_PERSON_ERROR         = 142;
    public static final int                 ERROR_CODE_SALARY_TIME_ERROR           = 143;

    public static final int                 ERROR_CODE_ICODE_ERROR                 = 150;
    public static final int                 ERROR_CODE_ICODE_NO_HEAD               = 151;

    public static final int                 FAILED_CODE_RECORD_VIDYO_RECORD_FAILED = 301;                      // vidyo通道录像失败

    public static final int                 ERROR_CODE_SESSTION_TIMOUT             = 1001;                     // 会话超时
    public static final int                 ERROR_CODE_PARAMETER_ERROR             = 1100;
    public static final int                 ERROR_CODE_NO_TOKEN                    = 1101;                     // 缺少令牌参数
    public static final int                 ERROR_CODE_TOKEN_NOTMATCH              = 1102;                     // 参数不足
    public static final int                 ERROR_CODE_PARAMETER_LOST              = 1110;                     // 参数不足
    public static final int                 ERROR_CODE_PARAMETER_EXPIERD           = 1111;
    public static final int                 ERROR_CODE_PARAMETER_DATE              = 1112;
    public static final int                 ERROR_CODE_PARAMETER_JSON_ERROR        = 1113;
    public static final int                 ERROR_CODE_FILE_DELETE                 = 1120;
    public static final int                 ERROR_CODE_FILE_TOO_BIG                = 1201;
    public static final int                 ERROR_CODE_FILE_TYPE_ERROR             = 1202;

    public static final int                 ERROR_CODE_INTERNAL                    = 10000;                    // 内部错误

    public static final SparseArray<String> ERROR_DESCS                            = new SparseArray<String>();

    static {
        ERROR_DESCS.put(ERROR_CODE_UNKOWN, "未知异常");
        ERROR_DESCS.put(ERROR_CODE_NET, "网络连接失败，请检查网络设置");
        ERROR_DESCS.put(ERROR_CODE_JSON, "JSON解析失败");
        ERROR_DESCS.put(ERROR_CODE_USER_NOTFOUND, "用户没找到");
        ERROR_DESCS.put(ERROR_CODE_USER_PASSWORD_ERROR, "用户密码错误");
        ERROR_DESCS.put(ERROR_CODE_USER_EXPIRED, "用户已过有效期");
        ERROR_DESCS.put(ERROR_CODE_EMAIL_DUPLICATE, "邮件地址重复");
        ERROR_DESCS.put(ERROR_CODE_MOBILE_DUPLICATE, "手机号码重复");
        ERROR_DESCS.put(ERROR_CODE_MOBILE_MESSAGE_FAILED, "短信发送失败");
        ERROR_DESCS.put(ERROR_CODE_MOBILE_NOT_BIND, "手机号未绑定");
        ERROR_DESCS.put(ERROR_CODE_USERNAME_DUPLICATE, "登录名重复");
        ERROR_DESCS.put(ERROR_CODE_VCODE_ERROR, "错误验证码");
        ERROR_DESCS.put(ERROR_CODE_SESSTION_TIMOUT, "您的账号已在其他设备上登录，请确保账号安全。");
        ERROR_DESCS.put(ERROR_CODE_NO_TOKEN, "APP已异常终止，请退出后重新进入");
        ERROR_DESCS.put(ERROR_CODE_PARAMETER_EXPIERD, "参数已过期");
        ERROR_DESCS.put(ERROR_CODE_TOKEN_NOTMATCH, "您的系统已异常终止，请退出后重新进入");
        ERROR_DESCS.put(ERROR_CODE_PARAMETER_LOST, "参数不足");
        ERROR_DESCS.put(ERROR_CODE_PARAMETER_JSON_ERROR, "提交信息格式错误");
        ERROR_DESCS.put(ERROR_CODE_PARAMETER_ERROR, "提交信息错误");
        ERROR_DESCS.put(ERROR_CODE_PARAMETER_DATE, "日期参数格式错误");
        ERROR_DESCS.put(ERROR_CODE_INTERNAL, "内部错误");
        ERROR_DESCS.put(ERROR_CODE_TIME_CONFLICT, "你已经在这个时间安排其他工作了！");
        ERROR_DESCS.put(ERROR_CODE_PUB_CHANNEL_ERROR, "当前无空闲的视频通道，请稍后再试");
        ERROR_DESCS.put(ERROR_CODE_PUB_HAS_NOPAY, "请先结算已完成的工作再发布新的");
        ERROR_DESCS.put(ERROR_CODE_WORK_REPEAT, "你已经申请过这个工作了!");
        ERROR_DESCS.put(ERROR_CODE_WORK_PERSON_OK, "名额满了，下次赶早吧！");
        ERROR_DESCS.put(ERROR_CODE_WORK_JK_CARD, "没有健康证，不能申请哦!");
        ERROR_DESCS.put(ERROR_CODE_WORK_LOCK, "已经过了申请期限了，不能申请哦！");
        ERROR_DESCS.put(ERROR_CODE_WORK_SEX, "性别不对，不能申请哦！");
        ERROR_DESCS.put(ERROR_CODE_WORK_EVAL, "评价不够，不能申请哦！");
        ERROR_DESCS.put(ERROR_CODE_WORK_AGE, "年龄不符，不能申请哦！");
        ERROR_DESCS.put(ERROR_CODE_WORK_HEIGHT, "身高不符，不能申请哦！");
        ERROR_DESCS.put(ERROR_CODE_WORK_SKILL, "从业经历不符，不能申请哦！");
        ERROR_DESCS.put(ERROR_CODE_SALARY_STATE_ERROR, "工作状态错误，无法结算");
        ERROR_DESCS.put(ERROR_CODE_SALARY_CSTATE_ERROR, "工作合同状态错误，无法结算");
        ERROR_DESCS.put(ERROR_CODE_SALARY_PERSON_ERROR, "本次结算未结算所有的合同");
        ERROR_DESCS.put(ERROR_CODE_SALARY_TIME_ERROR, "没到结算时间哦!");
        ERROR_DESCS.put(ERROR_CODE_ICODE_ERROR, "邀请码错误");
        ERROR_DESCS.put(ERROR_CODE_ICODE_NO_HEAD, "本邀请码对应的用户没有蛇头标志");
        ERROR_DESCS.put(FAILED_CODE_RECORD_VIDYO_RECORD_FAILED, "vidyo通道录像失败");
        ERROR_DESCS.put(ERROR_CODE_FILE_DELETE, "记录已被删除");
        ERROR_DESCS.put(ERROR_CODE_FILE_TOO_BIG, "文件大小超过限制");
        ERROR_DESCS.put(ERROR_CODE_FILE_TYPE_ERROR, "文件类型不对");
    }

    public BaseApiEntity() {
        result = 0;
        msg = "";
    }

    public BaseApiEntity(Integer result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public boolean isOk() {
        return 0 == result;
    }

    /**
     * 检测结果是否正常，注意：这里主要对服务端的返回进行 检查，移动端应该自行检查
     * 
     * @throws AppException
     */
    public void checkServerResult() throws AppException {
        if (isOk())
            return;
        if (result != 12) { // 手机号码重复的特殊处理
            throw AppException.server(result); //
        }
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "result : " + result + " , msg : " + msg;
    }

}
