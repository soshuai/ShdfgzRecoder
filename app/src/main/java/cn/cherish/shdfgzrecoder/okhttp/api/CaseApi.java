package cn.cherish.shdfgzrecoder.okhttp.api;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import cn.cherish.shdfgzrecoder.AppContext;
import cn.cherish.shdfgzrecoder.okhttp.AppException;
import cn.cherish.shdfgzrecoder.okhttp.BaseApi;
import cn.cherish.shdfgzrecoder.okhttp.entity.ApplyRecordEntity;
import cn.cherish.shdfgzrecoder.okhttp.entity.BaseApiDataEntity;
import cn.cherish.shdfgzrecoder.okhttp.entity.BaseApiListEntity;
import cn.cherish.shdfgzrecoder.okhttp.entity.CaseInfoEntity;
import cn.cherish.shdfgzrecoder.okhttp.entity.CaseRecordEntity;
import cn.cherish.shdfgzrecoder.okhttp.entity.CaseRecordUploadEntity;
import cn.cherish.shdfgzrecoder.okhttp.entity.DefaultApiEntity;
import cn.cherish.shdfgzrecoder.okhttp.entity.TokenEntity;
import cn.cherish.shdfgzrecoder.okhttp.utils.AESEncrypt;
import cn.cherish.shdfgzrecoder.okhttp.utils.EncryptUtil;
import cn.cherish.shdfgzrecoder.okhttp.utils.MyLocation;
import cn.cherish.shdfgzrecoder.okhttp.utils.RSAEncrypt;
import cn.cherish.shdfgzrecoder.okhttp.utils.CaseRecordUtils;
import cn.cherish.shdfgzrecoder.utils.LogUtils;

/**
 * 用户相关api合集
 * 
 * @author Cz
 */
public class CaseApi extends BaseApi {

    private static final String TAG = CaseApi.class.getSimpleName();
    private static CaseApi      mInstance;

    public static CaseApi getInstance() {
        if (null == mInstance)
            mInstance = new CaseApi();
        return mInstance;
    }

    private CaseApi() {
        super();
    }

    public DefaultApiEntity updatePlandate(Context context, int caseId, String dateString) {
        // TODO Auto-generated method stub
        String url = hostUrl + "case/updateCasePlanDate.action";
        HashMap pars = new HashMap<String, String>();
////        pars.put("token", AppContext.getInstance().getToken());
        pars.put("caseId", caseId);
        pars.put("planDate", dateString);
        LogUtils.d(TAG, "updatePlandate.pars=" + pars);
        return this.get(context, DefaultApiEntity.class, url, pars, null);
    }

    public DefaultApiEntity updatePlanperson(Context context, int caseId, String ids) {
        // TODO Auto-generated method stub
        String url = hostUrl + "case/updateCasePlanExecutors.action";
        HashMap pars = new HashMap<String, String>();
// //       pars.put("token", AppContext.getInstance().getToken());
        pars.put("caseId", caseId);
        pars.put("executorIds", ids);
        LogUtils.d(TAG, "updatePlanperson.pars=" + pars);
        return this.get(context, DefaultApiEntity.class, url, pars, null);
    }

    /**
     * 获取七牛token
     */
    public String getQiniuToken(Context context, final AssetManager manager) throws AppException {
        // 获取RSA公钥
        RSAEncrypt rsaEncrypt = new RSAEncrypt();
        InputStream is;
        try {
            is = manager.open("pubkey.pem");
            rsaEncrypt.loadPublicKey(is);
            is.close();
        } catch (IOException e) {
            throw AppException.io(e);
        } catch (Exception e) {
            throw AppException.encrypt(e);
        }

        // 随机生成AES秘钥，并用RSA公钥加密
        String aesKey = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        byte[] cipher;
        try {
            cipher = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), aesKey.getBytes());
        } catch (Exception e) {
            throw AppException.encrypt(e);
        }
        String securityMsg = EncryptUtil.byteArr2HexStr(cipher);

        // 上传AES秘钥,获取token
        String url = hostUrl + "user/getQiniuToken.action?accessSign=" + securityMsg;

        TokenEntity entity = this.get(context, TokenEntity.class, url);
        AESEncrypt aesEncrypt = new AESEncrypt(aesKey);
        String currentQiniuToken;
        try {
            currentQiniuToken = aesEncrypt.decryptorFromString(entity.getToken(), "utf8");
            return currentQiniuToken;
        } catch (InvalidKeyException e) {
            throw AppException.encrypt(e);
        } catch (IllegalBlockSizeException e) {
            throw AppException.encrypt(e);
        } catch (BadPaddingException e) {
            throw AppException.encrypt(e);
        } catch (IOException e) {
            throw AppException.io(e);
        }
    }

    public ApplyRecordEntity record(Context context, int caseId, int recordType, String beginTime, double longitude,
                                    double latitude, String executedName, int measureId, int subMeasureId) throws AppException {
        // TODO Auto-generated method stub
        String url = hostUrl + "case/record.action";
        HashMap pars = new HashMap<String, String>();
//        pars.put("token", AppContext.getInstance().getToken());
        pars.put("caseId", caseId);
        pars.put("recordType", recordType);
        pars.put("beginTime", beginTime);
        pars.put("longitude", longitude);
        pars.put("latitude", latitude);
        pars.put("executedName", executedName);
        pars.put("measureId", measureId);
        if (recordType==5){
//            pars.put("channelId", AppContext.getChannelId());
        }else{
            pars.put("channelId", "");
        }
        if (subMeasureId != 0) {
            pars.put("subMeasureId", subMeasureId);
        } else {
            pars.put("subMeasureId", "");
        }
        LogUtils.d(TAG, "record.pars=" + pars);
        ApplyRecordEntity apiResult = this.post(context, ApplyRecordEntity.class, url, pars, null);
        apiResult.checkServerResult();
        return apiResult;
    }

    public BaseApiListEntity<CaseInfoEntity> findList(Context context, String caseNo, Integer executorId,
                                                      Integer pageStart, Integer length) throws AppException {
        // TODO Auto-generated method stub
        String url = hostUrl + "case/findList.action";

        HashMap pars = new HashMap<String, String>();
//        pars.put("token", AppContext.getInstance().getToken());
        pars.put("caseNo", caseNo);
        pars.put("executorId", executorId);
        pars.put("from", pageStart);
        pars.put("length", length);
        LogUtils.d(TAG, "findList.pars=" + pars);
        return this.requestApiList(context, url, CaseInfoEntity.class, pars);
    }

    /*
     * 获取案件详情
     */
    public BaseApiDataEntity<CaseInfoEntity> findDetail(Context context, Integer caseId) throws AppException {
        // TODO Auto-generated method stub
        String url = hostUrl + "case/findDetail.action";
        HashMap pars = new HashMap<String, String>();
//        pars.put("token", AppContext.getInstance().getToken());
        pars.put("caseId", caseId);
        LogUtils.d(TAG, "findDetail.pars=" + pars);
        return this.requestApiData(context, url, CaseInfoEntity.class, pars);
    }

    public BaseApiListEntity<CaseRecordEntity> findRecordList(Context context, int caseId, int pageStart, int length)
            throws AppException {
        // TODO Auto-generated method stub
        String url = hostUrl + "case/findRecordList.action";
        HashMap pars = new HashMap<String, String>();
//        pars.put("token", AppContext.getInstance().getToken());
        pars.put("caseId", caseId);
        pars.put("from", pageStart);
        pars.put("length", length);
        LogUtils.d(TAG, "findRecordList.pars=" + pars);
//        Log.i("ssss","url="+url);
//        Log.i("ssss","token=="+AppContext.getInstance().getToken());
//        Log.i("ssss","caseId=="+caseId);
//        Log.i("ssss","from="+pageStart);
//        Log.i("ssss","length="+length);
        return this.requestApiList(context, url, CaseRecordEntity.class, pars);
    }

    public DefaultApiEntity deleteRecord(Context context, int recordId) {
        // TODO Auto-generated method stub
        String url = hostUrl + "case/deleteRecord.action";
        HashMap pars = new HashMap<String, String>();
//        pars.put("token", AppContext.getInstance().getToken());
        pars.put("recordId", recordId);
        LogUtils.d(TAG, "deleteRecord.pars=" + pars);
//        Log.i("ssss","token=="+ AppContext.getInstance().getToken()+" recordId=="+recordId);
        return this.get(context, DefaultApiEntity.class, url, pars, null);
    }

    public BaseApiDataEntity<CaseRecordUploadEntity> uploadRecord(Context context, int recordId, String fileUrl,
                                                                  int type) throws AppException {
        Log.i("ssss", "submitRecordFile====="+fileUrl);
//        Toast.makeText(context, "submitRecordFile====="+fileUrl, Toast.LENGTH_SHORT).show();
        // TODO Auto-generated method stub
        String url = hostUrl + "case/submitRecordFile.action";
        HashMap pars = new HashMap<String, String>();
//        pars.put("token", AppContext.getInstance().getToken());
        pars.put("recordId", recordId);
        pars.put("fileUrl", fileUrl);
        pars.put("type", type);
        LogUtils.d(TAG, "uploadRecord.pars=" + pars);
        return this.requestApiData(context, url, CaseRecordUploadEntity.class, pars);
    }

    public void endRecord(Context context, int recordId) {
        // TODO Auto-generated method stub
        String url = hostUrl + "case/endRecord.action";
        HashMap pars = new HashMap<String, String>();
//        pars.put("token", AppContext.getInstance().getToken());
        pars.put("recordId", recordId);
        LogUtils.d(TAG, "endRecord.pars=" + pars);
        this.get(context, DefaultApiEntity.class, url, pars, null);
    }

    public void stopPublish(Context context, String channelId, int publishFlag) {
        // TODO Auto-generated method stub
        String url = hostUrl + "case/endPublish.action";
        HashMap pars = new HashMap<String, String>();
        pars.put("channelId", channelId);
        pars.put("publishFlag", publishFlag);
//        LogUtils.d(TAG, "stopPublish.pars=" + pars);
        this.get(context, DefaultApiEntity.class, url, pars, null);
    }


    public void submitLocation(Context context, int recordId, double longitude, double latitude) {
        // TODO Auto-generated method stub
        String url = hostUrl + "case/submitPosition.action";
        HashMap pars = new HashMap<String, String>();
//        pars.put("token", AppContext.getInstance().getToken());
        pars.put("recordId", recordId);
        pars.put("longitude", longitude);
        pars.put("latitude", latitude);
        LogUtils.d(TAG, "submitPosition.pars=" + pars);
        this.get(context, DefaultApiEntity.class, url, pars, null);
    }

    public BaseApiListEntity<CaseRecordEntity> findMyRecordList(Context context, int pageStart, int length)
            throws AppException {
        // TODO Auto-generated method stub
        String url = hostUrl + "case/findMyRecordList.action";
        HashMap pars = new HashMap<String, String>();
//        pars.put("token", AppContext.getInstance().getToken());
        pars.put("from", pageStart);
        pars.put("length", length);
        LogUtils.d(TAG, "findRecordList.pars=" + pars);
        return this.requestApiList(context, url, CaseRecordEntity.class, pars);
    }

    public void submitUserAction(Context context, int action) {
        submitUserAction(context, action, null, null);
    }

    public void submitUserAction(Context context, int action, Integer caseId, String caseName) {
        // TODO Auto-generated method stub
        if (AppContext.getInstance().isLogin()) {
            MyLocation location = CaseRecordUtils.getCurrentLocation(context);

            String url = hostUrl + "user/submitUserAction.action";
            HashMap pars = new HashMap<String, String>();
//            pars.put("token", AppContext.getInstance().getToken());
            pars.put("action", action);
            if (location != null) {
                pars.put("longitude", location.getLongitude());
                pars.put("latitude", location.getLatitude());
            }
            if (caseId != null) {
                pars.put("caseId", caseId);
                pars.put("caseName", caseName);
            }

            LogUtils.d(TAG, "submitUserAction.pars=" + pars);
            this.post(context, DefaultApiEntity.class, url, pars, null);
        }
    }

}
