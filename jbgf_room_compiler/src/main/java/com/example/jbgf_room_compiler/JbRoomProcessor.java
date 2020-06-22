package com.example.jbgf_room_compiler;

import com.example.jbgf_room_annotation.JbDao;
import com.example.jbgf_room_annotation.JbEntity;
import com.example.jbgf_room_annotation.JbRoomBase;
import com.example.jbgf_room_compiler.utils.Utils;
import com.google.auto.service.AutoService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.example.jbgf_room_annotation.JbEntity",
        "com.example.jbgf_room_annotation.JbRoomBase",
        "com.example.jbgf_room_annotation.JbDao"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class JbRoomProcessor extends AbstractProcessor {
    private Messager mMessager;
    private Elements mElements;
    private Filer mFiler;
    private Map<String, JbRoomBaseEntity> mRoomBaseEntityMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment pEn) {
        super.init(pEn);
        this.mMessager = pEn.getMessager();
        this.mElements = pEn.getElementUtils();
        this.mFiler = pEn.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set.isEmpty()) return false;
        Set<? extends Element> jbRoomBaseElements = roundEnvironment.getElementsAnnotatedWith(JbRoomBase.class);
        for (Element roomBase : jbRoomBaseElements) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "------>jbRoomBaseElements：" + jbRoomBaseElements);
            //拿到包节点信息
            PackageElement packageOf = mElements.getPackageOf(roomBase);
            String fuilPackageName = packageOf.getQualifiedName().toString();
            //拿到类节点信息
            String typeName = roomBase.getSimpleName().toString();
            String fullTypeName = fuilPackageName + "." + typeName;
            //拿到注解
            JbRoomBase annotation = roomBase.getAnnotation(JbRoomBase.class);
            String setTarget = annotation.setTarget();
            JbRoomBaseEntity baseEntity;
            if (setTarget.equals("")) {
                String mapKey = fullTypeName + "_NullSetTarget";
                baseEntity = mRoomBaseEntityMap.get(mapKey);
                if (baseEntity == null) {
                    baseEntity = new JbRoomBaseEntity();
                    mRoomBaseEntityMap.put(mapKey, baseEntity);
                }
            } else {
                baseEntity = mRoomBaseEntityMap.get(setTarget);
                if (baseEntity == null) {
                    baseEntity = new JbRoomBaseEntity();
                    mRoomBaseEntityMap.put(setTarget, baseEntity);
                }
            }
            baseEntity.setPackageName(fuilPackageName);
            baseEntity.setClassName(typeName);
            baseEntity.setClassElement(roomBase);
        }
        Set<? extends Element> jbEntityElements = roundEnvironment.getElementsAnnotatedWith(JbEntity.class);
        putEntityElements(jbEntityElements);
        Set<? extends Element> jbDaoElements = roundEnvironment.getElementsAnnotatedWith(JbDao.class);
        putDaoElements(jbDaoElements);
        for (JbRoomBaseEntity baseEntity : mRoomBaseEntityMap.values()) {
            JbRoomBaseCreater jbRoomBaseCreater = new JbRoomBaseCreater(mElements);
            jbRoomBaseCreater.createJavaClass(mFiler, baseEntity);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "创建完毕喽！");
        }
        return true;
    }


    private void putEntityElements(Set<? extends Element> elements) {
        for (Element element : elements) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "putEntityElements:" + element);
            JbEntity annotation = element.getAnnotation(JbEntity.class);
            String target = annotation.target();
            if (target.equals("")) {
                for (JbRoomBaseEntity value : mRoomBaseEntityMap.values()) {
                    value.setJbEntityElement(element);
                }
            } else {
                JbRoomBaseEntity baseEntity = mRoomBaseEntityMap.get(target);
                baseEntity.setJbEntityElement(element);

            }
        }
    }

    private void putDaoElements(Set<? extends Element> elements) {
        for (Element element : elements) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "putDaoElements:" + element);
            JbDao annotation = element.getAnnotation(JbDao.class);
            String target = annotation.target();
            if (target.equals("")) {
                for (JbRoomBaseEntity value : mRoomBaseEntityMap.values()) {
                    value.setmJbDaoElement(element);
                }
            } else {
                JbRoomBaseEntity baseEntity = mRoomBaseEntityMap.get(target);
                baseEntity.setmJbDaoElement(element);
            }
        }
    }

}
