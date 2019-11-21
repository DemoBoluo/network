package cn.cool.rxnet.net.commonnetintercept;

import android.content.Context;

import java.io.IOException;

import cn.cool.rxnet.net.base.NetWorkApi;
import cn.cool.rxnet.net.config.NetWorkConfiguration;
import cn.cool.rxnet.net.util.NetworkUtil;
import cn.cool.rxnet.net.util.ObjectHelper;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by TRZ on 2019/11/19.
 * 备注：
 */
public class CacheIntercept implements Interceptor {
    private Context mContext;
    private boolean isLoadDiskCache;
    private boolean isLoadMemoryCache;
    private int memoryCacheTime;
    private int diskCacheTime;

    public CacheIntercept(Context context, NetWorkConfiguration netWorkConfiguration) {
        ObjectHelper.requireNonNull(context, "CacheIntercept  constructor context is null");
        ObjectHelper.requireNonNull(context, "CacheIntercept  constructor netWorkConfiguration is null");
        mContext = context;
        isLoadDiskCache = netWorkConfiguration.getIsDiskCache();
        isLoadMemoryCache = netWorkConfiguration.getIsMemoryCache();
        memoryCacheTime = netWorkConfiguration.getMemoryCacheTime();
        diskCacheTime = netWorkConfiguration.getDiskCacheTime();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        /**
         *  断网后是否加载本地缓存数据
         *
         */
        if (!NetworkUtil.isNetworkAvailable(mContext) && isLoadDiskCache) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
//            加载内存缓存数据
        else if (!NetworkUtil.isNetworkAvailable(mContext) && isLoadMemoryCache) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        /**
         *  加载网络数据
         */
        else if (NetworkUtil.isNetworkAvailable(mContext)) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();
        }
        Response response = chain.proceed(request);
//            有网进行内存缓存数据
        if (NetWorkApi.sNetWorkConfiguration != null) {
            if (NetworkUtil.isNetworkAvailable(mContext) && isLoadMemoryCache) {
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + memoryCacheTime)
                        .build();
            } else {
//              进行本地缓存数据
                if (isLoadDiskCache) {
                    response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + diskCacheTime)
                            .build();
                }
            }
        }
        return response;
    }
}
