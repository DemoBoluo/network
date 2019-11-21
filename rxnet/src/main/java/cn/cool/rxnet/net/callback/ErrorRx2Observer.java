package cn.cool.rxnet.net.callback;

import cn.cool.rxnet.net.exception.ResponseThrowable;
import cn.cool.rxnet.net.util.Lutils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by TRZ on 2017/9/8.
 * 备注：
 */

public abstract class ErrorRx2Observer<T>  implements Observer<T>{
    @Override
    public abstract void onSubscribe(Disposable d);

    @Override
    public abstract void onNext(T t);

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
    public  void onComplete(){}
    /**
     * 错误回调
     */
    protected abstract void onError(ResponseThrowable ex);

}
