package com.update.router_core;

import com.update.router_annotation.model.RouteMeta;
import com.update.router_core.template.IRouteGroup;
import com.update.router_core.template.IService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : liupu
 * date   : 2019/12/18
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
public class Warehouse {
    // root 映射表 保存分组信息
    static Map<String, Class<? extends IRouteGroup>> groupsIndex = new HashMap<>();

    // group 映射表 保存组中的所有数据
    static Map<String, RouteMeta> routes = new HashMap<>();

    // group 映射表 保存组中的所有数据
    static Map<Class, IService> services = new HashMap<>();
}
