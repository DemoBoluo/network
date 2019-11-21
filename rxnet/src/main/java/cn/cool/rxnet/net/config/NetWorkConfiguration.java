package cn.cool.rxnet.net.config;

import android.app.Application;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.cool.rxnet.net.util.Lutils;
import okhttp3.Cache;
import okhttp3.ConnectionPool;


/**
 * 作者：
 * 版本号：1.0
 * 类描述：
 * 备注消息：   对网络进行配置
 **/
public class NetWorkConfiguration {
    //    是否进行磁盘缓存
    private boolean isDiskCache;
    //        是否进行内存缓存
    private boolean isMemoryCache;
    //    内存缓存时间单位S （默认为60s）
    private int memoryCacheTime;

    //    本地缓存时间单位S (默认为4周)
    private int diskCacheTime;

    //    缓存本地大小 单位字节（默认为30M）
    private int maxDiskCacheSize;
    //      缓存路径
    private Cache diskCache;

    //     设置超时时间
    private int connectTimeout;
    //    设置网络最大连接数
    private ConnectionPool connectionPool;
    //    设置HttpS客户端带证书访问
    private InputStream[] certificates;
    public Application mApplication;

    private InputStream certificate;
    private Map<String, Object> mMoreCertificate;

    public NetWorkConfiguration(Application application) {
        this.isDiskCache = false;
        this.isMemoryCache = false;
        this.memoryCacheTime = 60;
        this.diskCacheTime = 60 * 60 * 24 * 28;
        this.maxDiskCacheSize = 30 * 1024 * 1024;
        this.mApplication = application;
        this.diskCache = new Cache(new File(this.mApplication.getCacheDir() , "network"), maxDiskCacheSize);
        this.connectTimeout = 30;
        this.connectionPool = new ConnectionPool(50, 60, TimeUnit.SECONDS);
        certificates = null;
    }

    /**
     * 是否进行磁盘缓存
     *
     * @param diskcache
     * @return
     */
    public NetWorkConfiguration isDiskCache(boolean diskcache) {
        this.isDiskCache = diskcache;
        return this;
    }

    public boolean getIsDiskCache() {
        return this.isDiskCache;
    }

    /**
     * 是否进行内存缓存
     *
     * @param memoryCache
     * @return
     */
    public NetWorkConfiguration isMemoryCache(boolean memoryCache) {
        this.isMemoryCache = memoryCache;
        return this;
    }

    public boolean getIsMemoryCache() {
        return this.isMemoryCache;
    }

    /**
     * 设置内存缓存时间
     *
     * @param memoryCacheTime
     * @return
     */
    public NetWorkConfiguration memoryCacheTime(int memoryCacheTime) {
        if (memoryCacheTime <= 0) {

            Lutils.e("NetWorkConfiguration", " configure memoryCacheTime  exception!");
            return this;
        }
        this.memoryCacheTime = memoryCacheTime;
        return this;
    }

    public int getMemoryCacheTime() {
        return this.memoryCacheTime;
    }

    /**
     * 设置本地缓存时间
     *
     * @param diskCacheTime
     * @return
     */
    public NetWorkConfiguration diskCacheTime(int diskCacheTime) {
        if (diskCacheTime <= 0) {
            Lutils.e("NetWorkConfiguration", " configure diskCacheTime  exception!");
            return this;
        }
        this.diskCacheTime = diskCacheTime;
        return this;
    }

    public int getDiskCacheTime() {
        return this.diskCacheTime;
    }

    /**
     * 设置本地缓存路径以及 缓存大小
     *
     * @param saveFile         本地路径
     * @param maxDiskCacheSize 大小
     * @return
     */
    public NetWorkConfiguration diskCaChe(File saveFile, int maxDiskCacheSize) {
        if (!saveFile.exists() && maxDiskCacheSize <= 0) {
            Lutils.e("s", " configure connectTimeout  exception!");
            return this;
        }
        diskCache = new Cache(saveFile, maxDiskCacheSize);
        return this;
    }

    public Cache getDiskCache() {
        return this.diskCache;
    }

    /**
     * 设置网络超时时间
     *
     * @param timeout
     * @return
     */
    public NetWorkConfiguration connectTimeOut(int timeout) {
        if (connectTimeout <= 0) {
            Lutils.e("NetWorkConfiguration", " configure connectTimeout  exception!");
            return this;
        }
        this.connectTimeout = timeout;
        return this;
    }

    public int getConnectTimeOut() {
        return this.connectTimeout;
    }

    /**
     * 设置网络线程池
     *
     * @param connectionCount 线程个数
     * @param connectionTime  连接时间
     * @param unit            时间单位
     * @return
     */
    public NetWorkConfiguration connectionPool(int connectionCount, int connectionTime, TimeUnit unit) {
        /**
         *    线程池 线程个数和连接时间设置过小
         */
        if (connectionCount <= 0 && connectionTime <= 0) {
            Lutils.e("NetWorkConfiguration", " configure connectionPool  exception!");
            return this;
        }
        this.connectionPool = new ConnectionPool(connectionCount, connectionTime, unit);
        return this;
    }

    public ConnectionPool getConnectionPool() {
        return this.connectionPool;
    }

    /**
     * 设置Https客户端带证书访问
     *
     * @param certificates
     * @return
     */
    public NetWorkConfiguration certificates(@Nullable InputStream... certificates) {
        if (certificates != null) {
            this.certificates = certificates;
        } else {

            Lutils.e("NetWorkConfiguration", "no  certificates");
        }
        return this;
    }

    public InputStream[] getCertificates() {
        return this.certificates;
    }

    public InputStream getCertificate() {
        return this.certificate;
    }

    public NetWorkConfiguration certificate(@Nullable InputStream certificate) {
        if (certificate != null) {
            this.certificate = certificate;
        }else{
            Lutils.e("NetWorkConfiguration", "no  certificate");
        }
        return this;
    }

    public NetWorkConfiguration moreCertificate(CertificateConfig certificateConfig) {
        if (certificateConfig != null){
            mMoreCertificate = certificateConfig.certificates;
        }
        return this;
    }

    public Map getMapCertificate() {
        return mMoreCertificate;
    }

    @Override
    public String toString() {
        return "NetWorkConfiguration{" +
                "isDiskCache=" + isDiskCache +
                ", isMemoryCache=" + isMemoryCache +
                ", memoryCacheTime=" + memoryCacheTime +
                ", diskCacheTime=" + diskCacheTime +
                ", maxDiskCacheSize=" + maxDiskCacheSize +
                ", diskCache=" + diskCache +
                ", connectTimeout=" + connectTimeout +
                ", connectionPool=" + connectionPool +
                ", certificates=" + Arrays.toString(certificates) +
                ", context=" + mApplication +
                ", baseUrl='" + mMoreCertificate + '\'' +
                '}';
    }

}
