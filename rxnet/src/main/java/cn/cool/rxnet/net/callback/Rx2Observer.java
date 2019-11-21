package cn.cool.rxnet.net.callback;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.util.List;

import cn.cool.rxnet.net.exception.ResponseThrowable;
import io.reactivex.disposables.Disposable;

/**
 * Created by boluo on 2017/9/8.
 * 备注：
 */

public abstract class Rx2Observer<T> extends ErrorRx2Observer<T> {
    private Fragment mFragment;

    public Rx2Observer(Fragment fragment) {
        this.mFragment = fragment;
    }
    @Override
    public  void onNext(T t){
        onSuccess(t);
        if (t instanceof List && ((List) t).size() == 0){
            showUI("");
        }else if (t instanceof List && ((List) t).size() >= 0){
            showUI(t);
        }else if (t instanceof String){
            if (TextUtils.isEmpty((CharSequence) t) || t.equals("")){
                showUI("");
            }else {
                showUI(t);
            }
        }else{
          showUI(getNetData());
        }
    }

    @Override
    public  void onComplete(){
        onCompleteThings();
    }
    @Override
    protected void onError(ResponseThrowable responeThrowable) {
        if (mFragment != null){
//            if (mFragment instanceof BaseFragment){
//                showUI(null);
//            }
        }
        onErrorData(responeThrowable);
    }
    @Override
    public abstract void onSubscribe(Disposable disposable);
    protected abstract void onSuccess(T t);
    protected abstract void onErrorData(ResponseThrowable responeThrowable);
    protected void onCompleteThings() {}
    //没用compose DefaultTransformer，一定要重写这个方法
    public Object getNetData(){
        return "";
    }
    public void showUI(Object object) {
        if (mFragment != null){
//            if (mFragment instanceof BaseFragment){
//                ((BaseFragment) mFragment).setNetFinish(true);
//                ((BaseFragment) mFragment).setNetDatas(object);
//                ((BaseFragment) mFragment).showNetData();
//            }
        }
    }
}
