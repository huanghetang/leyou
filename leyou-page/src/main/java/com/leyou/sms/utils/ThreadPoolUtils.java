package com.leyou.sms.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhoumo
 * @datetime 2018/7/31 14:53
 * @desc
 */
public class ThreadPoolUtils {
    //初始化固定10个大小的线程池
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void execute(Runnable runnable){
       executorService.submit(runnable);
    }
}
