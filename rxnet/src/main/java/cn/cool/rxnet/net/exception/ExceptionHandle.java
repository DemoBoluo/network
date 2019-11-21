package cn.cool.rxnet.net.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cn.cool.rxnet.net.util.Lutils;
import retrofit2.HttpException;


/**
 *  作者：
 *  版本号：1.0
 *  类描述：异常处理类
 *         展示友好UI界面给用户
 *  备注消息：
 *  修改时间：
 **/
public class ExceptionHandle {


    private final static String TAG = "ExceptionHandle";
    /**
     *  定义网络异常码
     */
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    /**
     * 未授权
     */
    public static final int UNPERMISSION = 401;

    /**
     * 请求参数错误
     */
    public static final int PARAM = 400;

    public static ResponseThrowable handleException(Throwable e) {
        Lutils.d(TAG,e.toString()+"======handleException====="+ e.getMessage());
        ResponseThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseThrowable(e,((HttpException) e).code());
            switch (httpException.code()) {
                case PARAM:
                    ex.message = "参数错误";
                    break;
                case INTERNAL_SERVER_ERROR:
                    ex.message = "邀请码无效";
                    break;
                case UNPERMISSION:
                    ex.message = "未授权";
                    break;
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.message = "无网络,请重试!";
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponseThrowable(resultException, resultException.code);
            ex.message = resultException.message;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ResponseThrowable(e, ERROR.PARSE_ERROR);
            ex.message = "解析异常";
            return ex;
        }    else if(e instanceof ConnectException || e instanceof UnknownHostException)
        {
            ex=new ResponseThrowable(e, ERROR.NETWORD_ERROR);
            ex.message="无网络,请重试!";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponseThrowable(e, ERROR.SSL_ERROR);
            ex.message = "证书验证异常";
            return ex;
        } else if (e instanceof ConnectTimeoutException ||e instanceof SocketTimeoutException){
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "连接超时";
            return ex;
        }else {
            ex = new ResponseThrowable(e, ERROR.UNKNOWN);
            return ex;
        }
    }
    /**
     * 约定异常
     */
  public static   class ERROR {

        /**
         * 未授权
         */
        public static final int UNPERMISSION = 401;

        /**
         * 请求参数错误
         */
        public static final int PARAM = 400;

        /**
         * 服务器错误
         */
        public static final int INTERNATSERVER = 500;

        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;


        /**
         * 没有缓存的数据
         */
        public static final int DATA_ERROR = 1007;
        
        /**
         * 没有缓存文件标签tag
         */
        public static final int TAG_ERROR = 1008;

        /**
         * 返回没有数据
         */
        public final static int NODATA = 1009;
    }

}

