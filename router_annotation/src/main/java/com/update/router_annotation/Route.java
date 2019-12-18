package com.update.router_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : liupu
 * date   : 2019/12/18
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */

// 添加元注解
// @Target(ElementType.TYPE) // 接口, 类, 枚举, 注解
// @Target(ElementType.FIELD) // 字段, 枚举常量
// @Target(ElementType.METHOD) // 方法
// @Target(ElementType.PARAMETER) // 方法参数
// @Target(ElementType.CONSTRUCTOR) // 构造参数
// @Target(ElementType.LOCAL_VARIABLE) // 局部变量
// @Target(ElementType.ANNOTATION_TYPE) // 注解
// @Target(ElementType.PACKAGE) // 包

@Target(ElementType.TYPE)

// 注解的声明周期
// @Retention(RetentionPolicy.SOURCE) // 编码阶段
// @Retention(RetentionPolicy.CLASS) // 编译阶段
// @Retention(RetentionPolicy.RUNTIME) // 运行阶段

@Retention(RetentionPolicy.CLASS)
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
