package cn.cool.rxnet.net.base;

import android.app.Application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import cn.cool.rxnet.net.commonnetintercept.CacheIntercept;
import cn.cool.rxnet.net.commonnetintercept.CommonRequestIntercept;
import cn.cool.rxnet.net.commonnetintercept.CommonResponseIntercept;
import cn.cool.rxnet.net.config.NetWorkConfiguration;
import cn.cool.rxnet.net.environment.NetEnvironment;
import cn.cool.rxnet.net.http.HttpsUtils;
import cn.cool.rxnet.net.http.SSLSocketFactoryCompat;
import cn.cool.rxnet.net.util.ObjectHelper;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by TRZ on 2019/11/18.
 * 备注：
 */
public abstract class NetWorkApi implements NetEnvironment {
    private static INetworkInfo sINetworkInfo;
    public static NetWorkConfiguration sNetWorkConfiguration;
    private static boolean sFormal = false;
    private static OkHttpClient sOkHttpClient;
    private OkHttpClient mOkHttpClient;
    private String mBaseUrl;
    private boolean mHttpLog;
    private boolean mDiskCache;

    public NetWorkApi() {
        ObjectHelper.requireNonNull(sINetworkInfo, "NetWorkApi init method must application initialized");
        if (sFormal) {
            mBaseUrl = getFormal();
        } else {
            mBaseUrl = getTest();
        }
        mHttpLog = sINetworkInfo.isDebug();
    }

    public static Application getContext() {
        ObjectHelper.requireNonNull(sINetworkInfo, "NetWorkApi init method must application initialized");
        return sINetworkInfo.getApplication();
    }

    public synchronized static void init(INetworkInfo iNetworkInfo) {
        init(iNetworkInfo, null);
    }

    public synchronized static void init(INetworkInfo iNetworkInfo, NetWorkConfiguration netWorkConfiguration) {
        if (sINetworkInfo == null) {
            synchronized (NetWorkApi.class) {
                sINetworkInfo = iNetworkInfo;
                if (netWorkConfiguration != null) {
                    sNetWorkConfiguration = netWorkConfiguration;
                }
                sFormal = sINetworkInfo.isFormal();
            }
        }
    }

    private synchronized OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            synchronized (NetWorkApi.class) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.addInterceptor(new CommonRequestIntercept(sINetworkInfo));
                builder.addInterceptor(new CommonResponseIntercept());
                if (sNetWorkConfiguration != null) {
                    builder.connectTimeout(sNetWorkConfiguration.getConnectTimeOut(), TimeUnit.SECONDS)
                            .connectionPool(sNetWorkConfiguration.getConnectionPool())
                            .build();
                }
                sOkHttpClient = builder.build();
            }
        }
        return sOkHttpClient;
    }

    public synchronized NetWorkApi setOkHttpClient(OkHttpClient okHttpClient) {
        ObjectHelper.requireNonNull(okHttpClient, "NetWorkApi setOkHttpClient param okHttpClient is null");
        this.mOkHttpClient = okHttpClient;
        return this;
    }

    public <T> T builder(Class<T> service) {
        ObjectHelper.requireNonNull(service, "NetWorkApi builder api Service is null!");
        ObjectHelper.requireNonNull(mBaseUrl, "api baseUrl is null!");
        if (mOkHttpClient == null) {
            mOkHttpClient = getOkHttpClient();
        }
        OkHttpClient.Builder builder = mOkHttpClient.newBuilder();
        if (mBaseUrl.startsWith("https")) {
            setSSL(builder);
        }
        setIntercept(builder);
        if (!mBaseUrl.endsWith("/")) {
            mBaseUrl = mBaseUrl + "/";
        }
        String bs = mBaseUrl + service.getName();
        if (SingletonApi.sApis.containsKey(bs)) {
            return (T) SingletonApi.sApis.get(bs);
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(mOkHttpClient)
                    .baseUrl(mBaseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            T t = retrofit.create(service);
            SingletonApi.sApis.put(bs, t);
            return t;
        }
    }

    private void setIntercept(OkHttpClient.Builder builder) {
        if (getInterceptors() != null && getInterceptors().size() > 0) {
            for (Interceptor interceptor : getInterceptors()) {
                builder.addInterceptor(interceptor);
            }
        }
        if (getInterceptor() != null) {
            builder.addInterceptor(getInterceptor());
        }
        if (mHttpLog) {
            builder.addNetworkInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        if (mDiskCache && sNetWorkConfiguration != null) {
            builder.cache(sNetWorkConfiguration.getDiskCache());
            builder.addInterceptor(new CacheIntercept(sINetworkInfo.getApplication(), sNetWorkConfiguration));
        }
    }

    private void setSSL(OkHttpClient.Builder builder) {
        Object certificate = null;
        if (sNetWorkConfiguration != null) {
            Map moreCertificates = sNetWorkConfiguration.getMapCertificate();
            if (moreCertificates != null && !moreCertificates.isEmpty()) {
                if (moreCertificates.containsKey(mBaseUrl)) {
                    certificate = moreCertificates.get(mBaseUrl);
                }
            }
            if (certificate == null) {
                certificate = sNetWorkConfiguration.getCertificates();
                if (certificate == null) {
                    certificate = sNetWorkConfiguration.getCertificate();
                }
            }
        }
        if (certificate != null) {
            mOkHttpClient = builder
                    .sslSocketFactory(HttpsUtils.getSslSocketFactory(certificate, null, null))
                    .hostnameVerifier(HttpsUtils.getHostnameVerifier())
                    .build();
        } else {
            X509TrustManager trustAllCert = HttpsUtils.getX509TrustManager();
            SSLSocketFactory sslSocketFactory = new SSLSocketFactoryCompat(trustAllCert);
            mOkHttpClient = builder
                    .sslSocketFactory(sslSocketFactory, trustAllCert)
                    .hostnameVerifier(HttpsUtils.getHostnameVerifier())
                    .build();
        }
    }

    protected List<Interceptor> getInterceptors() {
        return null;
    }

    protected Interceptor getInterceptor() {
        return null;
    }

    protected NetWorkApi setDiskCache(boolean diskCache) {
        if (diskCache) {
            ObjectHelper.requireNonNull(sNetWorkConfiguration, "before setDiskCache, NetWorkConfiguration should initialize");
        }
        this.mDiskCache = diskCache;
        return this;
    }

    public NetWorkApi setDebugLog(boolean httpLog) {
        this.mHttpLog = httpLog;
        return this;
    }

    private static class SingletonApi {
        private final static Map<String, Object> sApis = new HashMap<>();
    }
}
