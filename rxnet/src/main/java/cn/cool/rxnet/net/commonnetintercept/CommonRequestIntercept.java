package cn.cool.rxnet.net.commonnetintercept;

import java.io.IOException;

import cn.cool.rxnet.net.base.INetworkInfo;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by TRZ on 2019/11/18.
 * 备注：
 */
public class CommonRequestIntercept implements Interceptor {

    private INetworkInfo mINetworkInfo;

    public CommonRequestIntercept(INetworkInfo networkInfo){
        this.mINetworkInfo = networkInfo;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("os","android");
        builder.addHeader("appversion",mINetworkInfo.getAppVersion());
        builder.addHeader("serial",mINetworkInfo.getAppSerial());
        return chain.proceed(builder.build());
    }
}
