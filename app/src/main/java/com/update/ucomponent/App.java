package com.update.ucomponent;

import android.app.Application;

import com.update.router_core.URouter;

/**
 * @author : liupu
 * date   : 2019/12/20
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        URouter.init(this);
    }
}
