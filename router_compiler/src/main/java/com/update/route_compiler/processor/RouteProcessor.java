package com.update.route_compiler.processor;

import com.google.auto.service.AutoService;
import com.update.route_compiler.utils.Consts;
import com.update.route_compiler.utils.Log;
import com.update.route_compiler.utils.Utils;
import com.update.router_annotation.model.RouteMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * 在这个类上添加了 @AutoService注解,
 * 作用是用来生成 META-INF/services/javax.annotation.processing.Processor文件
 * 也就是说 我们在使用注解处理器的时候需要手动添加 META-INF/services/javax.annotation.processing.Processor,
 * 而有了 @AutoService注解后, 他会帮我们自动生成.
 * AutoService是Google开发的一个库，使用时需要添加依赖
 */
@AutoService(Processor.class) // 注册注解处理器

/**
 * 处理器接收的参数 替代 {@link AbstractProcessor#getSupportedOptions()} 函数
 */
@SupportedOptions(Consts.ARGUMENTS_NAME)

/**
 * 指定使用的 Java版本 替代 {@link AbstractProcessor#getSupportedSourceVersion()} 函数
 * 声明我们注解支持的JDK版本
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)

/**
 * 注册给那些注解的 替代 {@link AbstractProcessor#getSupportedAnnotationTypes()} 函数
 * 声明我们要处理哪一些注解 该方法返回字符串的集合标识该处理器用于处理哪些注解
 */
@SupportedAnnotationTypes({Consts.ANN_TYPE_ROUTE})
public class RouteProcessor extends AbstractProcessor {

    /**
     * key: 组名
     * value:类名
     */
    private Map<String, String> rootMap = new TreeMap<>();

    /**
     * 分组
     * - key: 组名
     * - value: 对应组的路由信息
     */
    private Map<String, List<RouteMeta>> groupMap = new HashMap<>();

    /**
     * 节点工具类 (类, 函数, 属性都是节点)
     */
    private Elements elementUtils;

    /**
     * type工具类 (类信息)
     */
    private Types typeUtils;

    /**
     * 文件生成器 类/资源
     */
    private Filer filerUtils;

    /**
     * 参数
     */
    private String moduleName;

    private Log log;

    /**
     * 初始化 从 {@link ProcessingEnvironment} 中获得一系列处理器工具
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        // 获取 apt 日志输出
        log = Log.newLog(processingEnvironment.getMessager());
        log.i("init()");
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        filerUtils = processingEnv.getFiler();

        // 参数是模块名 为了防止 多模块/组件化 开发时, 生成相同的 xx$$ROOT$$ 文件
        Map<String, String> options = processingEnv.getOptions();
        if (Utils.isEmpty(options)) {
            moduleName = options.get(Consts.ARGUMENTS_NAME);
        }
        log.i("RouteProcessor Parmaters:" + moduleName);
        if (Utils.isEmpty(moduleName)) {
            throw new RuntimeException("Not set Processor Parameters.");
        }
    }

    /**
     * 相当于 main函数, 正式处理注解
     * @param set 使用了支持处理注解 的 节点集合
     * @param roundEnvironment 标识当前或是之前的的运行环境, 可以通过该对象查找找到的注解
     * @return true 标识后续处理器不会再处理 (已经处理)
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        return false;
    }
}
