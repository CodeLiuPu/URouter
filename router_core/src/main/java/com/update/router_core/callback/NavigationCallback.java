package com.update.router_core.callback;

import com.update.router_core.Postcard;

/**
 * @author : liupu
 * date   : 2019/12/18
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
public interface NavigationCallback {
    /**
     * 找到跳转页面
     */
    void onFound(Postcard postcard);

    /**
     * 未找到跳转页面
     */
    void onLost(Postcard postcard);

    /**
     * 跳转成功
     */
    void onArrival(Postcard postcard);
}
