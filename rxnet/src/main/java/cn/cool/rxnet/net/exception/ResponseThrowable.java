package cn.cool.rxnet.net.exception;

import android.text.TextUtils;

/**
 * 作者：${User}
 * 版本号：
 * 类描述：
 * 修改时间：
 */

public class ResponseThrowable extends Exception {
    public int code;
    public String message;

    public ResponseThrowable(Throwable throwable, int code) {
        super(throwable);
        this.message = throwable.getMessage();
        this.code = code;
    }

    public String getMessage(){
        if (TextUtils.isEmpty(message)){
            return super.getMessage();
        }else{
            return message;
        }
    }

    @Override
    public String toString() {
        return "ResponeThrowable{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
