package com.xw.selector.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 公共线程池
 */
public class CommonExcutor {

    private static CommonExcutor instance = null;
    private ExecutorService mService;

    public CommonExcutor() {
        mService = Executors.newCachedThreadPool(r -> {
            Thread thread = new Thread(r);
            thread.setName("XW_CommonExcutor");
            return thread;
        });
    }

    public static CommonExcutor getInstance() {
        if (instance == null) {
            synchronized (CommonExcutor.class) {
                if (instance == null) {
                    instance = new CommonExcutor();
                }
            }
        }
        return instance;
    }

    public void excute(Runnable runnable) {
        mService.execute(runnable);
    }
}
