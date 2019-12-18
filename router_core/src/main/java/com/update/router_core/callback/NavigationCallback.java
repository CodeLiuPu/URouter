package com.update.router_core.callback;

/**
 * @author : liupu
 * date   : 2019/12/18
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
public interface NavigationCallback {
    void onFound();

    void onLost();

    void onArrival();
}
