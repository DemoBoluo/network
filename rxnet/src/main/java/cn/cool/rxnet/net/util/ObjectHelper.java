package cn.cool.rxnet.net.util;

import android.text.TextUtils;

/**
 *@author boluo
 *classdescrption：帮助类
 *version：1.0
 *date： 2019/7/4
 */
public class ObjectHelper {
    public static <T> void requireNonNull(T object, String message) {
        if (object instanceof String){
            if (TextUtils.isEmpty((String)object)){
                throw new NullPointerException(message);
            }
        } else if (object == null) {
            throw new NullPointerException(message);
        }
    }
}
