package com.update.router_core.template;

import com.update.router_annotation.model.RouteMeta;

import java.util.Map;

/**
 * @author : liupu
 * date   : 2019/12/18
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
public interface IRouteGroup {
    void loadInto(Map<String, RouteMeta> atlas);
}
