package com.update.router_core.template;

import java.util.Map;

/**
 * @author : liupu
 * date   : 2019/12/18
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
public interface IRouteRoot {
    void loadInto(Map<String,Class<? extends IRouteGroup>> routes);
}
