package com.book.ireader.model.remote;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created with author.
 * Description:
 * Date: 2018-06-05
 * Time: 下午2:48
 */
public class IRemoteProxy implements InvocationHandler {

    private Object mRemoteRepository;

    public IRemoteProxy(Object mRemoteRepository) {
        this.mRemoteRepository = mRemoteRepository;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(mRemoteRepository, args);
    }
}
