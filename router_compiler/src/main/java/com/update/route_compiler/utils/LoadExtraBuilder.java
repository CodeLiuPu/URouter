package com.update.route_compiler.utils;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author : liupu
 * date   : 2019/12/18
 * desc   :
 * github : https://github.com/CodeLiuPu/
 */
public class LoadExtraBuilder {
    private static final String INJECT_TARGET = "$T t = ($T)target";
    private MethodSpec.Builder builder;
    private Elements elementUtils;
    private Types typeUtils;

    private TypeMirror parcelableType;
    private TypeMirror iServiceType;

    public LoadExtraBuilder(ParameterSpec parameterSpec) {
        builder = MethodSpec.methodBuilder(
                Consts.METHOD_LOAD_EXTRA)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(parameterSpec);
    }

    public void setElementUtils(Elements elementUtils) {
        this.elementUtils = elementUtils;
        parcelableType = elementUtils
                .getTypeElement(Consts.PARCELABLE)
                .asType();
        iServiceType = elementUtils
                .getTypeElement(Consts.ISERVICE)
                .asType();
    }

    public void setTypeUtils(Types typeUtils) {
        this.typeUtils = typeUtils;
    }


}
