package cn.cherish.shdfgzrecoder.okhttp.utils;

import android.content.Context;

import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * https工具类<br>
 * <br>
 * 添加服务器白名单步骤：<br>
 * 1. 添加域名白名单：修改本类中mTrustedHostArr变量<br>
 * 2. 在raw中放入crt证书<br>
 * 3. 在getTrustedFactory()方法中对应地方添加代码<br>
 * <br>
 * <p/>
 * ps.关于电子证书可参见：http://blog.csdn.net/googling/article/details/6698255
 */
public class HttpsHandler extends BaseHttpRequstHandler {
    private static HttpsHandler mInstance;

    public static HttpsHandler initialize(Context context) {
        if (null == mInstance)
            mInstance = new HttpsHandler(context);
        return mInstance;
    }

    private SSLSocketFactory mTrustedFactory;
    private HostnameVerifier mTrustedVerifier;
    // api白名单
    private final String[]   mTrustedHostArr = {};

    private Context mContext;

    private HttpsHandler(Context context) {
        mContext = context;
        mInstance = this;
    }

    @Override
    protected void httpRequestConfig(HttpRequest request) {
        super.httpRequestConfig(request);
        trustCertsAndHosts(request);
    }

    private void trustCertsAndHosts(HttpRequest request) {
        final HttpURLConnection connection = request.getConnection();
        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(getTrustedFactory());//getmTrustedFactory());
            ((HttpsURLConnection) connection).setHostnameVerifier(getTrustedVerifier());
        }
    }

    private SSLSocketFactory getTrustedFactory() {
        if (mContext == null) {
            String simpleName = HttpsHandler.class.getClass().getSimpleName();
            throw new RuntimeException(String.format("%s not initialize. Please run %s.initialize() first !%s",
                    simpleName, simpleName, simpleName));
        }

        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[] { new NoneTrustManager() }, null);
            mTrustedFactory = context.getSocketFactory();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mTrustedFactory;
    }

    /*
    private SSLSocketFactory getmTrustedFactory() throws HttpRequestException {
        if (mContext == null) {
            String simpleName = HttpsHandler.class.getClass().getSimpleName();
            throw new RuntimeException(String.format("%s not initialize. Please run %s.initialize() first !%s",
                    simpleName, simpleName, simpleName));
        }
        // http://developer.android.com/training/articles/security-ssl.html#Concepts
        try {
            // Load CAs from an InputStream
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = mContext.getResources().openRawResource(R.raw.studyfun);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
            } finally {
                try {
                    caInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            mTrustedFactory = context.getSocketFactory();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return mTrustedFactory;
    }
    */

    private HostnameVerifier getTrustedVerifier() {
        if (mTrustedVerifier == null)
            mTrustedVerifier = new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    /*
                     * boolean isTrust = false; for (String host : mTrustedHostArr) { if (host.equalsIgnoreCase(hostname)) { isTrust = true; break; } }
                     */
                    return true;
                }
            };
        return mTrustedVerifier;
    }

    private class NoneTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
