package cn.cool.rxnet.net.exception;

/**
 * 作者：${User}
 */

public class ServerException extends RuntimeException {
    public int code;
    public String message;

    public ServerException(String message, int code) {
        this.message=message;
        this.code = code;
    }
}
