package cn.cool.rxnet.net.callback;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import cn.cool.rxnet.net.exception.ResponseThrowable;
import cn.cool.rxnet.net.util.Lutils;


/**
 * 作者：${User}
 * 版本号：
 * 类描述：
 * 修改时间：
 */

public abstract class ErrorRx2Subscriber<T> implements Subscriber<T> {
    @Override
    public void onError(Throwable e) {
        Lutils.e("错误信息:"+e.getMessage());
        if(e instanceof ResponseThrowable){
            onError((ResponseThrowable)e);
        }else{
            onError(new ResponseThrowable(e,1000));
        }
    }
    @Override
    public abstract void onSubscribe(Subscription subscription);
    @Override
    public abstract void onNext(T t);
    @Override
    public  void onComplete(){}
    /**
     * 错误回调
     */
    protected abstract void onError(ResponseThrowable ex);
}

