package cn.cool.rxnet.net.base;

import android.app.Application;

/**
 * Created by TRZ on 2019/11/18.
 * 备注：
 */
public interface INetworkInfo {
     String getAppVersion();
     String getAppSerial();
     boolean isDebug();
     boolean isFormal();
     Application getApplication();

}
