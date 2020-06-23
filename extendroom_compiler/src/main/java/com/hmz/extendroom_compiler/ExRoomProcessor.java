package com.hmz.extendroom_compiler;

import androidx.room.Dao;
import androidx.room.Entity;

import com.hmz.extendroom_annotation.ExDao;
import com.hmz.extendroom_annotation.ExDatabase;
import com.hmz.extendroom_annotation.ExEntity;
import com.google.auto.service.AutoService;
import com.hmz.extendroom_compiler.utils.Utils;

import java.util.HashMap;
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
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.hmz.extendroom_annotation.ExEntity",
        "com.hmz.extendroom_annotation.ExDatabase",
        "com.hmz.extendroom_annotation.ExDao"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ExRoomProcessor extends AbstractProcessor {
    private Messager mMessager;
    private Elements mElements;
    private Filer mFiler;
    private Map<String, ExDatabaseBean> mExDatabaseBeabMap = new HashMap<>();

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
        Set<? extends Element> exDatabaseElements = roundEnvironment.getElementsAnnotatedWith(ExDatabase.class);
        for (Element databseElement : exDatabaseElements) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "------>ExDatabase：" + exDatabaseElements);
            //拿到包节点信息
            PackageElement packageOf = mElements.getPackageOf(databseElement);
            String fuilPackageName = packageOf.getQualifiedName().toString();
            //拿到类节点信息
            String typeName = databseElement.getSimpleName().toString();
            String fullTypeName = fuilPackageName + "." + typeName;
            //拿到注解
            ExDatabase annotation = databseElement.getAnnotation(ExDatabase.class);
            String setTarget = annotation.setTarget();
            ExDatabaseBean databaseBean;
            if (setTarget.equals("")) {
                String mapKey = fullTypeName + "_NullSetTarget";
                databaseBean = mExDatabaseBeabMap.get(mapKey);
                if (databaseBean == null) {
                    databaseBean = new ExDatabaseBean();
                    mExDatabaseBeabMap.put(mapKey, databaseBean);
                }
            } else {
                databaseBean = mExDatabaseBeabMap.get(setTarget);
                if (databaseBean == null) {
                    databaseBean = new ExDatabaseBean();
                    mExDatabaseBeabMap.put(setTarget, databaseBean);
                }
            }
            databaseBean.setPackageName(fuilPackageName);
            databaseBean.setClassName(typeName);
            databaseBean.setClassElement(databseElement);
        }
        Set<? extends Element> entityElements = roundEnvironment.getElementsAnnotatedWith(Entity.class);
        putEntityElements(entityElements);
        Set<? extends Element> daoElements = roundEnvironment.getElementsAnnotatedWith(Dao.class);
        putDaoElements(daoElements);
        for (ExDatabaseBean databaseBean : mExDatabaseBeabMap.values()) {
            ExDatabaseCreater exDatabaseCreater = new ExDatabaseCreater(mElements);
            exDatabaseCreater.createJavaClass(mFiler, databaseBean);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "创建完毕喽！");
        }
        return true;
    }

    /**
     * 添加Entity元素
     *
     * @param elements {@link Entity 元素集合}该集合中
     *                 可能存在包含{@link ExEntity}元素
     */
    private void putEntityElements(Set<? extends Element> elements) {
        for (Element element : elements) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "putEntityElements:" + element);
            String target = "";
            //该@Entity类中是否有使用@ExEntity注解
            boolean isEXEntity = Utils.annotionIs(element, ExEntity.class);
            if (isEXEntity) {
                //获取直接中的ExEntity值
                ExEntity annotation = element.getAnnotation(ExEntity.class);
                target = annotation.target();
            }
            if (target.equals("")) {
                //target为""代表该注解的类可以在所有@Batabase注解上的entities集合中添加
                for (ExDatabaseBean value : mExDatabaseBeabMap.values()) {
                    value.setEntityElement(element);
                }
            } else {
                ExDatabaseBean exDatabaseBean = mExDatabaseBeabMap.get(target);
                exDatabaseBean.setEntityElement(element);

            }
        }
    }

    private void putDaoElements(Set<? extends Element> elements) {
        for (Element element : elements) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "putDaoElements:" + element);
            String target = "";
            boolean isDao = Utils.annotionIs(element, ExDao.class);
            if (isDao) {
                ExDao annotation = element.getAnnotation(ExDao.class);
                target = annotation.target();
            }
            if (target.equals("")) {
                for (ExDatabaseBean value : mExDatabaseBeabMap.values()) {
                    value.setDaoElement(element);
                }
            } else {
                ExDatabaseBean exDatabaseBean = mExDatabaseBeabMap.get(target);
                exDatabaseBean.setDaoElement(element);
            }
        }
    }

}
