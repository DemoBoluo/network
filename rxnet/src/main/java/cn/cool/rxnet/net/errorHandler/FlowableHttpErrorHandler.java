package cn.cool.rxnet.net.errorHandler;

import android.content.Context;

import org.reactivestreams.Publisher;

import cn.cool.rxnet.net.base.NetWorkApi;
import cn.cool.rxnet.net.cache.NetCacheDao;
import cn.cool.rxnet.net.exception.ExceptionHandle;
import cn.cool.rxnet.net.exception.ResponseThrowable;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * Created by TRZ on 2019/11/18.
 * 备注：
 */
public class FlowableHttpErrorHandler<T> implements Function<Throwable, Publisher<? extends T>>{
    private  boolean mIsCache;
    private  String mTag;
    private  Class<T> mClazz;

    public FlowableHttpErrorHandler(boolean isCache,String tag, Class<T> clazz){
        this.mIsCache = isCache;
        this.mTag = tag;
        this.mClazz = clazz;
    }

    @Override
    public Publisher<? extends T> apply(Throwable throwable) throws Exception {
        ResponseThrowable responseThrowable = ExceptionHandle.handleException(throwable);
        if (mIsCache){
            T netData = (T) NetCacheDao.getInstance(NetWorkApi.getContext()).getSaveNetData(mTag,responseThrowable,mClazz);
            if (netData != null){
                return Flowable.just(netData);
            }
        }
        return Flowable.error(responseThrowable);
    }
}
