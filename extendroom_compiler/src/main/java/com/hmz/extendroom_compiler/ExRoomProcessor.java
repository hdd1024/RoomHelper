package com.hmz.extendroom_compiler;

import androidx.room.Dao;
import androidx.room.Entity;

import com.hmz.extendroom_annotation.ExAlter;
import com.hmz.extendroom_annotation.ExDao;
import com.hmz.extendroom_annotation.ExDatabase;
import com.hmz.extendroom_annotation.ExEntity;
import com.google.auto.service.AutoService;
import com.hmz.extendroom_compiler.utils.Utils;

import java.util.HashMap;
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
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.hmz.extendroom_annotation.ExEntity",
        "com.hmz.extendroom_annotation.ExAlter",
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
//        Set<? extends Element> exAlterElements = roundEnvironment.getElementsAnnotatedWith(ExAlter.class);
//        putExAlterElements(exAlterElements);
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
            //判断该元素是否包含@ExAlter
            List<? extends Element> enclosedElements = element.getEnclosedElements();
            for (Element enclosedElement : enclosedElements) {
                ExAlter annotation = enclosedElement.getAnnotation(ExAlter.class);
                if (annotation != null) {
                    putExAlterElement((VariableElement) enclosedElement);
                }
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

    /**
     * 处理{@link ExAlter 注解}如果startversion和endversion相同，则认为它们是一次修改
     * 那么在生产Migration 代码是将会把它们写在同一个你函数中
     *
     * @param elements ExAlter元素集合
     */
    // TODO: 2020-06-24 还未调用该方法
    private void putExAlterElements(Set<? extends Element> elements) {
        for (Element element : elements) {
            putExAlterElement((VariableElement) element);
        }

    }

    private void putExAlterElement(VariableElement varElement) {
        ExAlter annotation = varElement.getAnnotation(ExAlter.class);
        int startVersion = annotation.startVersion();
        int endVersion = annotation.endVersion();
        for (ExDatabaseBean databaseBean : mExDatabaseBeabMap.values()) {
            //获取用不存放修改元素的集合，该集合中的MigrateElementBean元素盛放这同一次修改的数据集
            List<MigrateElementBean> migrateElementBeans = databaseBean.getMigrateElementBeans();
            if (migrateElementBeans.size() == 0) {
                //如果修改元素的集合中没有元素，那么就创建个修改元素的bean类，并添加到ExDatabaseBean的修改元素集合中
                MigrateElementBean bean = setMigrateElmentBean(startVersion, endVersion, varElement);
                databaseBean.setMigrateElementBean(bean);
            } else {
                //遍历修改元素集合，比较当前startversion和endversion是否相同
                //如相同则代表为统一修改，那么就将该元素存放到当前遍历出出的MigrateElementBean的集合中
                for (MigrateElementBean migrateElementBean : migrateElementBeans) {
                    int beanEndVersion = migrateElementBean.getEndVersion();
                    int beanStartVersion = migrateElementBean.getStartVersion();
                    if (startVersion == beanStartVersion && endVersion == beanEndVersion) {
                        migrateElementBean.setMigrates(varElement);
                    } else {
                        MigrateElementBean bean = setMigrateElmentBean(startVersion, endVersion, varElement);
                        databaseBean.setMigrateElementBean(bean);
                    }
                }
            }
        }
    }

    /**
     * 该方法用于配置MigrateElementBean 类，该类中盛放这同一次修改的元素
     *
     * @param startVersion 开始版本
     * @param endVersion   结束版本
     * @param element      @ExAlter元素
     */
    private MigrateElementBean setMigrateElmentBean(int startVersion, int endVersion, Element element) {
        MigrateElementBean migrateBean = new MigrateElementBean();
        migrateBean.setStartVersion(startVersion);
        migrateBean.setEndVersion(endVersion);
        migrateBean.setMigrates(element);
        return migrateBean;
    }

}
