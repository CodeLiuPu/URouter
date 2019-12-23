package com.update.router_core.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : liupu
 * date   : 2019/12/18
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
public class DefaultPoolExecutor {

    private DefaultPoolExecutor() {
    }

    private static final ThreadFactory mThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "URouter #" + mCount.getAndIncrement());
        }
    };

    // 核心线程 和 最大线程 都是 cpu数 + 1
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int MAX_CORE_POOL_SIZE = CPU_COUNT + 1;

    // 存活30秒 回收线程
    private static final long SURPLUS_THREAD_LIFE = 30L;

    public static ThreadPoolExecutor newDefaultPoolExecutor(int corePoolSize) {
        if (0 == corePoolSize) {
            return null;
        }

        corePoolSize = Math.min(corePoolSize, MAX_CORE_POOL_SIZE);

        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(corePoolSize, corePoolSize,
                        SURPLUS_THREAD_LIFE, TimeUnit.SECONDS,
                        new ArrayBlockingQueue<Runnable>(64), mThreadFactory);

        // 核心线程允许被销毁
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }
}
