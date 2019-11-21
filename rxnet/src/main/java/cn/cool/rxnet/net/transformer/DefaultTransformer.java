package cn.cool.rxnet.net.transformer;


import android.text.TextUtils;

import org.reactivestreams.Publisher;

import cn.cool.rxnet.net.base.NetWorkApi;
import cn.cool.rxnet.net.cache.NetCacheDao;
import cn.cool.rxnet.net.errorHandler.FlowableHttpErrorHandler;
import cn.cool.rxnet.net.errorHandler.ObservableHttpErrorHandler;
import cn.cool.rxnet.net.exception.ExceptionHandle;
import cn.cool.rxnet.net.exception.ServerException;
import cn.cool.rxnet.net.response.HttpResponse;
import cn.cool.rxnet.net.util.GsonUtil;
import cn.cool.rxnet.net.util.Lutils;
import cn.cool.rxnet.net.util.ObjectHelper;
import cn.cool.rxnet.net.util.RxToolTaskUtil;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：
 * 版本号：2.0
 * 类描述：  预处理结果信息
 * 备注消息：
 * 修改时间：2019/6/28 下午7:22
 **/
public class DefaultTransformer<T> implements ObservableTransformer<T, T>, FlowableTransformer<T, T> {

    private final static String TAG = "DefaultTransformer";
    private boolean isCache;
    private String tag;
    private Class<T> clazz;

    public DefaultTransformer() {
        this(null, null, false);
    }

    public DefaultTransformer(String tag, Class<T> clazz, boolean isCache) {
        if (isCache) {
            ObjectHelper.requireNonNull(tag, "save netData, ErrorTransformer constructor tag can not null");
            ObjectHelper.requireNonNull(clazz, "save netData, ErrorTransformer constructor clazz can not null");
        }
        this.tag = tag;
        this.clazz = clazz;
        this.isCache = isCache;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> httpResponseObservable) {
        return httpResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<T, T>() {
                    @Override
                    public T apply(T t) throws Exception {
                        dealHttpResponse(t);
                        return t;
                    }
                })
                .onErrorResumeNext(new ObservableHttpErrorHandler<T>(isCache, tag, clazz));

    }

    @Override
    public Publisher<T> apply(Flowable<T> httpResponseObservable) {
        return httpResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<T, T>() {
                    @Override
                    public T apply(T t) throws Exception {
                        dealHttpResponse(t);
                        return t;
                    }
                }).onErrorResumeNext(new FlowableHttpErrorHandler<T>(isCache, tag, clazz));

    }

    private void dealHttpResponse(T httpResponse) {
        if (httpResponse instanceof HttpResponse) {
            dealHttpResponseCode(httpResponse);
            if (isCache) {
                if (!TextUtils.isEmpty( tag)
                        && ((HttpResponse) httpResponse).meta.code == 200) {
                    saveNetCache(httpResponse);
                }
            }
        }
    }

    protected void dealHttpResponseCode(T httpResponse) {
        HttpResponse.Meta meta = ((HttpResponse) httpResponse).meta;
        ObjectHelper.requireNonNull(meta, "httpResponse.meta is null");
        Lutils.d(TAG, httpResponse.toString());
        if (meta.code != 200) {
            switch (meta.code) {
                case ExceptionHandle.ERROR.PARAM:
                    throw new ServerException("请求参数错误", ExceptionHandle.ERROR.PARAM);
                case ExceptionHandle.ERROR.INTERNATSERVER:
                    throw new ServerException("服务器错误", ExceptionHandle.ERROR.INTERNATSERVER);
                case ExceptionHandle.ERROR.UNPERMISSION:
                    throw new ServerException("未授权", ExceptionHandle.ERROR.UNPERMISSION);
            }
        }
    }

    protected void saveNetCache(T httpResponse) {
        String dataToJson = GsonUtil.parseBeanToJson(httpResponse);
        if (!TextUtils.isEmpty(dataToJson)
                && !TextUtils.equals("{}", dataToJson)) {
            RxToolTaskUtil.doTask(new RxToolTaskUtil.Task() {
                @Override
                public void doOnUIThread(Object object) {

                }

                @Override
                public Object doOnIOThread() {
                    //保存到db
                    return NetCacheDao.getInstance(NetWorkApi.getContext()).saveNetData(tag, httpResponse);
                }
            });
        }
    }

    @Override
    public String toString() {
        return "ErrorTransformer{" +
                "tag='" + tag + '\'' +
                ", isCache=" + isCache +
                '}';
    }
}
