package com.update.router_core.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

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
    public static Set<String> getFileNameByPackageName(Application context, String packageName) throws PackageManager.NameNotFoundException {
        Set<String> classNames = new HashSet<>();
        List<String> paths = getSourcePaths(context);

        // 使用同步计数器判断 处理完成
        CountDownLatch parserCtl = new CountDownLatch(paths.size());



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
