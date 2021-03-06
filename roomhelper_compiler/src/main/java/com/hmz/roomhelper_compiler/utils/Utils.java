package com.hmz.roomhelper_compiler.utils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static String getClazzsFroma(String... classFullName) {
        return getClazzsFroma(Arrays.asList(classFullName));
    }

    /**
     * 该方法用于处理<code>Class[]</code>的结果值，将传入的类的全名字符串处理成
     * {com.example.extendroom.DataBaseViews.class
     * , com.example.extendroom.DataBaseViews2.class}的形式，以便于使用
     * JavaPot提供的$T占位符赋值
     *
     * @param classFullNames 要处理的类的全类名
     * @return 返回加工好的String字符串
     */
    public static String getClazzsFroma(List<String> classFullNames) {
        StringBuffer stringBuffer = new StringBuffer("{");
        for (int i = 0; i < classFullNames.size(); i++) {
            if (i != 0) {
                stringBuffer.append(",");
            }
            String typeFullName = classFullNames.get(i);
            if (typeFullName.contains(".class")) {
                stringBuffer.append(typeFullName + "\n");
            } else {
                stringBuffer.append(typeFullName + ".class\n");
            }
        }
        stringBuffer.append("}");
        return stringBuffer.toString();
    }

    /**
     * 获取注解中的参数，如果非数组和Class[]的参数值建议还是使用{@link Element#getAnnotation}
     * 中的返回的Annotation中取值。因为该方法会将Class[]中的值转化成String[]返回，丢失类class提供的类信息。
     *
     * @param element 用于获取注解的映射集合的元素
     * @return 返回的Map是以注解中值的名字作为Key，值作为Value
     */
    public static Map<String, Object> getAnnotationValus(Element element) {
        List<? extends AnnotationMirror> mirrors = element.getAnnotationMirrors();
        Map<String, Object> annotationValusMap = new HashMap<>();
        for (AnnotationMirror mirror : mirrors) {
            Map<? extends ExecutableElement, ? extends AnnotationValue> valuesMap = mirror.getElementValues();
            for (ExecutableElement valusKey : valuesMap.keySet()) {
                AnnotationValue annotationValue = valuesMap.get(valusKey);
                String simpleName = valusKey.getSimpleName().toString();
                TypeMirror returnType = valusKey.getReturnType();
                String typStr = returnType.toString();
                if (typStr.contains("[]") && typStr.contains(".Class")) {
                    String objStr = annotationValue.getValue().toString();
                    String[] split = objStr.split(",");
                    annotationValusMap.put(simpleName, split);
                } else {
                    Object valueObj = annotationValue.getValue();
                    annotationValusMap.put(simpleName, valueObj);
                }
            }
        }
        return annotationValusMap;
    }

    /**
     * 判断某个目标是否和cheks想匹配
     *
     * @param tage   目标
     * @param checks cheks
     * @return 只有有一个符合，就会跳出循环
     */
    public static boolean strEuqals(String tage, String... checks) {
        for (String check : checks) {
            if (tage.contains(check)) {
                return true;
            }
        }
        return false;
    }


    public static boolean annotionIs(Element element, Class<? extends Annotation> ano) {
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            DeclaredType annotationType = annotationMirror.getAnnotationType();
            String toString = annotationType.toString();
            return toString.equals(ano.getName());
        }
        return false;
    }

    public static <T extends Annotation> T forAno(Element element, Class<? extends Annotation> ano) {
        VariableElement variableElement= (VariableElement) element;
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            DeclaredType annotationType = annotationMirror.getAnnotationType();
            String toString = annotationType.toString();
            if (toString.equals(ano.getName())) {

                return (T) element.getAnnotation(ano);
            }
        }
        return null;
    }


    public static TypeElement getTypeElement(Element element) {
        TypeElement tElement = null;
        if (element.getKind().isClass()) {
            tElement = (TypeElement) element;
        } else if (element.getKind().isField()) {
            tElement = (TypeElement) element.getEnclosingElement();
        }
        return tElement;
    }


}
