package com.update.route_compiler.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.update.route_compiler.utils.Consts;
import com.update.route_compiler.utils.LoadExtraBuilder;
import com.update.route_compiler.utils.Log;
import com.update.route_compiler.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author : liupu
 * date   : 2019/12/18
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
@AutoService(Processor.class)
@SupportedOptions(Consts.ARGUMENTS_NAME)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes(Consts.ANN_TYPE_EXTRA)
public class ExtraProcessor extends AbstractProcessor {

    /**
     * 节点工具类 (类、函数、属性都是节点)
     */
    private Elements elementUtils;

    /**
     * type(类信息)工具类
     */
    private Types typeUtils;
    /**
     * 类/资源生成器
     */
    private Filer filerUtils;

    /**
     * 记录所有需要注入的属性 key:类节点 value:需要注入的属性节点集合
     */
    private Map<TypeElement, List<Element>> parentAndChild = new HashMap<>();
    private Log log;

    /**
     * 初始化 从 {@link ProcessingEnvironment} 中获得一系列处理器工具
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //获得apt的日志输出
        log = Log.newLog(processingEnvironment.getMessager());
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        filerUtils = processingEnv.getFiler();
    }

    /**
     * 相当于 main函数, 正式处理注解
     *
     * @param set              使用了支持处理注解 的 节点集合
     * @param roundEnvironment 标识当前或是之前的的运行环境, 可以通过该对象查找找到的注解
     * @return true 标识后续处理器不会再处理 (已经处理)
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        return false;
    }

    private void generateAutoWired() throws IOException {
        TypeMirror type_Activity = elementUtils.getTypeElement(Consts.ACTIVITY).asType();
        TypeElement IExtra = elementUtils.getTypeElement(Consts.IEXTRA);

        // 参数 Object target
        ParameterSpec objectParameterSpec = ParameterSpec.builder(TypeName.OBJECT, "target").build();
        if (!Utils.isEmpty(parentAndChild)) {
            // 遍历 所有需要注入的 类: 属性
            for (Map.Entry<TypeElement, List<Element>> entry : parentAndChild.entrySet()) {
                // 类
                TypeElement rawClassElement = entry.getKey();
                if (!typeUtils.isSubtype(rawClassElement.asType(), type_Activity)) {
                    throw new RuntimeException("[Just Support Activity Field]:" + rawClassElement);
                }

                // 封装的 函数生成类
                LoadExtraBuilder loadExtra = new LoadExtraBuilder(objectParameterSpec);
                loadExtra.setElementUtils(elementUtils);
                loadExtra.setTypeUtils(typeUtils);
                ClassName className = ClassName.get(rawClassElement);
                loadExtra.injectTarget(className);

                // 遍历属性
                for (int i = 0; i < entry.getValue().size(); i++) {
                    Element element = entry.getValue().get(i);
                    loadExtra.buildStatement(element);
                }

                // 生成 Java类名
                String extraClassName = rawClassElement.getSimpleName() + Consts.NAME_OF_EXTRA;

                // 生成 XX$$Autowired
                JavaFile.builder(className.packageName(),
                        TypeSpec.classBuilder(extraClassName)
                                .addSuperinterface(ClassName.get(IExtra))
                                .addModifiers(Modifier.PUBLIC)
                                .addMethod(loadExtra.build())
                                .build())
                        .build().writeTo(filerUtils);
                log.i("Generated Extra: " + className.packageName() + "." + extraClassName);
            }
        }
    }

    /**
     * 记录需要生成的类和属性
     */
    private void categories(Set<? extends Element> elements) throws IllegalAccessException {
        for (Element element : elements) {
            // 获得父节点 (类)
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            if (parentAndChild.containsKey(enclosingElement)) {
                parentAndChild.get(enclosingElement).add(element);
            } else {
                List<Element> childs = new ArrayList<>();
                childs.add(element);
                parentAndChild.put(enclosingElement, childs);
            }
        }
    }
}
