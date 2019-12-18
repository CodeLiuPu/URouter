package com.update.router_annotation;

/**
 * @author : liupu
 * date   : 2019/12/18
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
public @interface Route {
    /**
     * 路由的标识
     * - 标识一个路由节点
     */
    String path();

    /**
     * 将路由节点分组
     * - 可以实现按组动态配置
     */
    String group();
}
