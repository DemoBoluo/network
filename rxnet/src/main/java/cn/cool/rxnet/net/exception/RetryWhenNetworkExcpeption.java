package cn.cool.rxnet.net.exception;


import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 *@author boluo
 *类描述：失败尝试重新请求
 *版本号：1.0
 *修改时间： 2019/3/6
 */

public class RetryWhenNetworkExcpeption implements Function<Observable<? extends Throwable>, Observable<?>> {
    private int count = 3;//retry count
    private long delay = 3000;//delay time

    public RetryWhenNetworkExcpeption() {

    }

    public RetryWhenNetworkExcpeption(int count) {
        this.count = count;
    }

    public RetryWhenNetworkExcpeption(int count, long delay) {
        this.count = count;
        this.delay = delay;
    }
    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        return observable
                .zipWith(Observable.range(1, count + 1), (BiFunction<Throwable, Integer, Wrapper>) Wrapper::new).flatMap((Function<Wrapper, ObservableSource<?>>) wrapper -> {
                    if ((wrapper.throwable instanceof ConnectException
                            || wrapper.throwable instanceof SocketTimeoutException
                            || wrapper.throwable instanceof TimeoutException)
                            && wrapper.index < count + 1) {
                        return Observable.timer(delay + (wrapper.index - 1) * delay, TimeUnit.MILLISECONDS);
                    }
                    return Observable.error(wrapper.throwable);
                });
    }


    public static class Wrapper {
        public int index;
        public Throwable throwable;

        public Wrapper(Throwable throwable, int index) {
            this.index = index;
            this.throwable = throwable;
        }
    }
}
