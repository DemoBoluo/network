package cn.cool.rxnet.net.callback;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import org.reactivestreams.Subscription;

import java.util.List;

import cn.cool.rxnet.net.exception.ResponseThrowable;

/**
 *  作者：boluo
 *  版本号：1.0
 *  类描述：
 *  备注消息：
 *  修改时间：2016/11/24 上午10:56
 **/
public  abstract class Rx2Subscriber<T> extends ErrorRx2Subscriber<T> {
    private Fragment mFragment;


    public Rx2Subscriber(Fragment fragment) {
        this.mFragment = fragment;
    }

    /**
     * 请求网络完成
     */
    public void onCompleted() {
        onCompleteThings();
    }

    /**
     * @param responeThrowable
     */
    @Override
    protected void onError(ResponseThrowable responeThrowable) {
        if (mFragment != null){
//            if (mFragment instanceof BaseFragment){
//                ((BaseFragment) mFragment).setNetFinish(true);
//                    showUI(null);
//            }
        }
        onErrorData(responeThrowable);
    }

    /**
     *  获取网络数据
     * @param t
     */
    @Override
    public void onNext(T t) {
        onSuccess(t);
        if (t instanceof List && t != null && ((List) t).size() == 0){
            showUI("");
        }else if (t instanceof List && t != null && ((List) t).size() >= 0){
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
    public abstract void onSubscribe(Subscription subscription);
    public abstract  void onSuccess(T t);
    protected abstract void onErrorData(ResponseThrowable responeThrowable);
    //没用compose DefaultTransformer，一定要重写这个方法，避免网络请求失败没有缓存，
    public Object getNetData() {
        return "";
    }
    protected void onCompleteThings() {
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