package com.update.router_core.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.update.router_core.thread.DefaultPoolExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

import dalvik.system.DexFile;

/**
 * @author : liupu
 * date   : 2019/12/18
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
public class ClassUtils {

    private ClassUtils(){}

    /**
     * 路由表
     */
    public static Set<String> getFileNameByPackageName(Application context, final String packageName)
            throws PackageManager.NameNotFoundException, InterruptedException {
        final Set<String> classNames = new HashSet<>();
        List<String> paths = getSourcePaths(context);

        // 使用同步计数器判断 处理完成
        final CountDownLatch parserCtl = new CountDownLatch(paths.size());
        ThreadPoolExecutor threadPoolExecutor = DefaultPoolExecutor.newDefaultPoolExecutor(paths.size());

        for (final String path: paths){
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    DexFile dexFile = null;
                    try {
                        // 加载 apk 中的 dex, 并遍历 获得所有包名为 {packageName} 的类
                        dexFile = new DexFile(path);
                        Enumeration<String> dexEntries = dexFile.entries();
                        while (dexEntries.hasMoreElements()){
                            String  className = dexEntries.nextElement();
                            if (className.startsWith(packageName)){
                                classNames.add(className);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (null != dexFile){
                            try {
                                dexFile.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        // 释放1个
                        parserCtl.countDown();
                    }
                }
            });
        }
        // 等待执行完成
        parserCtl.await();
        return classNames;
    }

    /**
     * 获取程序所有的apk
     * - instant run 会产生很多 split apk
     */
    @SuppressLint("NewApi")
    public static List<String> getSourcePaths(Context context) throws PackageManager.NameNotFoundException {
        ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),0);

        List<String> sourcePaths = new ArrayList<>();
        sourcePaths.add(applicationInfo.sourceDir);

        // instant run
        if (null != applicationInfo.splitSourceDirs){
            sourcePaths.addAll(Arrays.asList(applicationInfo.splitSourceDirs));
        }
        return sourcePaths;
    }
}
