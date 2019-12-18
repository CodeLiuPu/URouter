package com.update.router_core;

import android.app.Application;

/**
 * @author : liupu
 * date   : 2019/12/18
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
public class URouter {

    private static final String TAG = "URouter";
    private static final String ROUTE_ROOT_PACKAGE = "com.update.router_core";
    private static final String SDK_NAME = "URouter";
    private static final String SEPARATOR = "$$";
    private static final String SUFFIX_ROOT = "Root";
    private static Application mApp;

    private URouter() {
    }

    public static URouter get() {
        return Holder.INSTANCE;
    }

    /**
     * 初始化
     */
    public static void init(Application application) {
        mApp = application;
    }

    private static final class Holder {
        private static final URouter INSTANCE = new URouter();
    }
}
