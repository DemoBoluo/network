package cn.cool.rxnet.net.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by TRZ on 2019/7/18.
 * 备注：
 */
public class NetParamUtil {
    private final static String TAG = "NetParamUtil";
    private static String MEDIA_TYPE_JSON = "application/json; charset=UTF-8";

    public static String getUrlByParam(String url, HashMap<String,String> hashMap){
        StringBuilder tempParams = new StringBuilder();
        try {
            //处理参数
            int pos = 0;
            for (String key : hashMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                //对参数进行URLEncoder
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(hashMap.get(key), "utf-8")));
                pos++;
            }
            //补全请求地址
           return String.format("%s?%s", url, tempParams.toString());
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            Log.e(TAG,"getUrlByParam UnsupportedEncodingException:"+e.getMessage());
        }
        return null;
    }

    public static RequestBody postFormByParam(LinkedHashMap<String,String> paramMap){
        if (paramMap != null){
            try {
                //创建一个FormBody.Builder
                FormBody.Builder builder = new FormBody.Builder();
                for (String key : paramMap.keySet()) {
                    //追加表单信息
                    builder.add(key, paramMap.get(key));
                }
                //补全请求地址
                return builder.build();
            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG,"postFormByParam exception:"+e.getMessage());
            }
        }
        return null;
    }

    public static RequestBody postJsonByParam(Object object){
        ObjectHelper.requireNonNull(object,"NetParamUtil postJsonByParam param object not null");
            try {
                //创建一个请求实体对象 RequestBody
                return RequestBody.create(MediaType.parse(MEDIA_TYPE_JSON), GsonUtil.parseBeanToJson(object));
            } catch (Exception e) {
                Log.e(TAG, "postJsonByParam Exception"+e.getMessage());
            }
        return null;
    }
}
