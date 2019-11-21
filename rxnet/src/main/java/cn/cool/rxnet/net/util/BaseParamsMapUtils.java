package cn.cool.rxnet.net.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *@author boluo
 *类描述：公共参数
 *版本号：1.1.1
 *修改时间： 2017/10/20
 */

public class BaseParamsMapUtils {
    static Map<String, String> paramsmap;
    /**
     * 公共的参数集合
     *
     * @return
     */
    public synchronized static Map<String, String> getParamsMap() {
        if(paramsmap == null){
            synchronized (BaseParamsMapUtils.class){
                if (paramsmap == null){
                    paramsmap = new LinkedHashMap<>();
                }
            }
        }
        paramsmap.put("client_sys", "android");
        paramsmap.put("aid", "android1");
        paramsmap.put("time", System.currentTimeMillis()+"");
        return paramsmap;
    }
}
