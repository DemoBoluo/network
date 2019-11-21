package cn.cool.rxnet.net.response;

/**
 *  作者：
 *  版本号：1.0
 *  类描述： 约定服务器公共的json数据
 *  备注消息：
 *  修改时间：
 *
 *
 */

public class HttpResponse<T> {
    public Meta meta;
    public T data;
    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }


    public  T getData() {
        return  data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "meta=" + meta +
                ", data=" + data +
                '}';
    }

    public static class Meta {
       public int code;
       public String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "Meta{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
