package cn.cool.rxnet.net.callback;

import cn.cool.rxnet.net.exception.ResponseThrowable;
import cn.cool.rxnet.net.util.Lutils;
import io.reactivex.disposables.Disposable;

/**
 * Created by TRZ on 2017/9/21.
 * 备注：
 */

public abstract class Rx2ActivityObserver<T> extends ErrorRx2Observer<T> {

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    protected void onError(ResponseThrowable responseThrowable) {
        Lutils.d("ResponseThrowable :"+responseThrowable.toString());
        onErrorData(responseThrowable);
    }

    public abstract void onSubscribe(Disposable disposable);
    protected abstract void onSuccess(T t);
    protected abstract void onErrorData(ResponseThrowable responseThrowable);
    protected void onCompleteThings()
    {

    }
    @Override
    public void onComplete() {
        onCompleteThings();
    }

    // 上传/下载文件进度回调
    public  void onProgress(long progress,long total)
    {

    }

}
