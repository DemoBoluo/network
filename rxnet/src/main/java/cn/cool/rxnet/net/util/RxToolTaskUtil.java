package cn.cool.rxnet.net.util;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 *@author boluo
 *类描述：RxJava处理线程
 *版本号：1.0
 *修改时间： 2019/6/6
 */
public class RxToolTaskUtil {
    public static Disposable doTask(Task task){
        return  Observable.just(task)
                .map(new Function<Task, Object>() {
                    @Override
                    public Object apply(Task task){
                        try {
                            return task.doOnIOThread();
                        }catch (Exception e){
                            throw new AssertionError(e);
                        }
                    }
                })
                .compose(TransformUtils.mainSchedulers())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {
                        task.doOnUIThread(object);
                    }
                });
    }

    public interface Task{
         void doOnUIThread(Object object);
         Object doOnIOThread();
    }
}
