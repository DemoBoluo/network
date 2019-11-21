package cn.cool.rxnet.net.errorHandler;

import cn.cool.rxnet.net.base.NetWorkApi;
import cn.cool.rxnet.net.cache.NetCacheDao;
import cn.cool.rxnet.net.exception.ExceptionHandle;
import cn.cool.rxnet.net.exception.ResponseThrowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by TRZ on 2019/11/18.
 * 备注：
 */
public class ObservableHttpErrorHandler<T> implements Function<Throwable, ObservableSource<? extends T>>{

    private  boolean mIsCache;
    private  String mTag;
    private  Class<T> mClazz;

    public ObservableHttpErrorHandler(boolean isCache,String tag, Class<T> clazz){
        this.mIsCache = isCache;
        this.mTag = tag;
        this.mClazz = clazz;
    }
    public ObservableSource<? extends T> apply(Throwable throwable) throws Exception {
        ResponseThrowable responseThrowable = ExceptionHandle.handleException(throwable);
        T netData = (T) NetCacheDao.getInstance(NetWorkApi.getContext()).getSaveNetData(mTag,responseThrowable,mClazz);
        if (netData != null){
            return Observable.just(netData);
        }
        return Observable.error(responseThrowable);
    }
}
