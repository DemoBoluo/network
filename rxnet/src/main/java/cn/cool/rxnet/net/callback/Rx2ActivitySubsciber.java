package cn.cool.rxnet.net.callback;


import cn.cool.rxnet.net.exception.ResponseThrowable;

import org.reactivestreams.Subscription;

/**
 * Created by TRZ on 2017/9/21.
 * 备注：
 */

public abstract class Rx2ActivitySubsciber<T> extends ErrorRx2Subscriber<T> {
    @Override
    public void onNext(T t) {
        onSuccess(t);
    }
    @Override
    protected void onError(ResponseThrowable responeThrowable) {
        onErrorData(responeThrowable);
    }

    public abstract void onSubscribe(Subscription subscription);
    protected abstract void onSuccess(T t);
    protected abstract void onErrorData(ResponseThrowable responeThrowable);
    protected void onCompleteThings() {
    }
    @Override
    public void onComplete() {
        onCompleteThings();
    }
}
