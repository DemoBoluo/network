package cn.cool.rxnet.net.exception;


import org.reactivestreams.Publisher;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * Created by hjd on 2017/9/4.
 * 备注：
 */

public class RetryWhenNetworkExcpeptionFlowable implements Function<Flowable<? extends Throwable>, Publisher<?>> {
    private int count = 3;//retry count
    private long delay = 3000;//delay time

    public RetryWhenNetworkExcpeptionFlowable() {

    }

    public RetryWhenNetworkExcpeptionFlowable(int count) {
        this.count = count;
    }

    public RetryWhenNetworkExcpeptionFlowable(int count, long delay) {
        this.count = count;
        this.delay = delay;
    }
    @Override
    public Publisher<?> apply(Flowable<? extends Throwable> flowable) throws Exception {
        return flowable.zipWith(Flowable.range(1, count + 1), new BiFunction<Throwable, Integer, RetryWhenNetworkExcpeption.Wrapper>() {
            @Override
            public RetryWhenNetworkExcpeption.Wrapper apply(Throwable throwable, Integer integer) throws Exception {
                return new RetryWhenNetworkExcpeption.Wrapper(throwable, integer);
            }
        }).flatMap(new Function<RetryWhenNetworkExcpeption.Wrapper, Publisher<?>>() {
            @Override
            public Publisher<?> apply(RetryWhenNetworkExcpeption.Wrapper wrapper) throws Exception {
                if ((wrapper.throwable instanceof ConnectException
                        || wrapper.throwable instanceof SocketTimeoutException
                        || wrapper.throwable instanceof TimeoutException)
                        && wrapper.index < count + 1) {
                    return Flowable.timer(delay + (wrapper.index - 1) * delay, TimeUnit.MILLISECONDS);
                }
                return Flowable.error(wrapper.throwable);
            }
        });
    }
}
