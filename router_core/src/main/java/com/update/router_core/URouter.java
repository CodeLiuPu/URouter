package com.update.router_core;

import android.app.Application;

/**
 * @author : liupu
 * date   : 2019/12/18
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
public class URouter {

    private URouter() {
    }

    public static URouter get() {
        return Holder.INSTANCE;
    }

    public static void init(Application application){

    }

    private static final class Holder {
        private static final URouter INSTANCE = new URouter();
    }
}
