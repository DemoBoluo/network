package cn.cool.rxnet.net.commonnetintercept;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by TRZ on 2019/11/18.
 * 备注：
 */
public class CommonResponseIntercept implements Interceptor {

    private static String TAG = "CommonResponseIntercept";
    @Override
    public Response intercept(Chain chain) throws IOException {
        long requestTime = System.currentTimeMillis();
        Response response = chain.proceed(chain.request());
        Log.d(TAG,"request takes time"+(System.currentTimeMillis() - requestTime));
        return response;
    }
}
