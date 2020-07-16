package com.hmz.roomhelper_compiler;

import androidx.room.Dao;
import androidx.room.Entity;

import com.hmz.roomhelper_annotation.DaoHlp;
import com.hmz.roomhelper_annotation.DatabaseHlp;
import com.hmz.roomhelper_annotation.EntityHlp;
import com.hmz.roomhelper_annotation.FieldHlp;
import com.google.auto.service.AutoService;
import com.hmz.roomhelper_compiler.utils.Utils;

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
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes(value = {Constant.DATABASE_HLP_PATH,
        Constant.ENTITY_HLP_PATH, Constant.FEILD_HLP_PATH,
        Constant.DAO_HLP_PATH})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class RoomHplProcessor extends AbstractProcessor {
    private Messager mMessager;
    private Elements mElements;
    private Filer mFiler;
    private Map<String, DatabaseBean> mDatabaseBeabMap = new HashMap<>();

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
        Set<? extends Element> exDatabaseElements = roundEnvironment.getElementsAnnotatedWith(DatabaseHlp.class);
        for (Element databseElement : exDatabaseElements) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "------>DatabaseHlp：" + exDatabaseElements);
            //拿到包节点信息
            PackageElement packageOf = mElements.getPackageOf(databseElement);
            String fuilPackageName = packageOf.getQualifiedName().toString();
            //拿到类节点信息
            String typeName = databseElement.getSimpleName().toString();
            String fullTypeName = fuilPackageName + "." + typeName;
            //拿到注解
            DatabaseHlp annotation = databseElement.getAnnotation(DatabaseHlp.class);
            String setTarget = annotation.setTarget();
            DatabaseBean databaseBean;
            if (setTarget.equals("")) {
                String mapKey = fullTypeName + "_NullSetTarget";
                databaseBean = mDatabaseBeabMap.get(mapKey);
                if (databaseBean == null) {
                    databaseBean = new DatabaseBean();
                    mDatabaseBeabMap.put(mapKey, databaseBean);
                }
            } else {
                databaseBean = mDatabaseBeabMap.get(setTarget);
                if (databaseBean == null) {
                    databaseBean = new DatabaseBean();
                    mDatabaseBeabMap.put(setTarget, databaseBean);
                }
            }
            databaseBean.setPackageName(fuilPackageName);
            databaseBean.setClassName(typeName);
            databaseBean.setClassElement(databseElement);
        }
        Set<? extends Element> entityElements = roundEnvironment.getElementsAnnotatedWith(Entity.class);
        try {
            putEntityElements(entityElements);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        Set<? extends Element> daoElements = roundEnvironment.getElementsAnnotatedWith(Dao.class);
        putDaoElements(daoElements);

        Set<? extends Element> daoHlpElements = roundEnvironment.getElementsAnnotatedWith(DaoHlp.class);
        putDaoHlpAtField(daoHlpElements);

        for (DatabaseBean databaseBean : mDatabaseBeabMap.values()) {
            DatabaseCreater databaseCreater = new DatabaseCreater(mElements);
            databaseCreater.setMessager(mMessager);
            databaseCreater.createJavaClass(mFiler, databaseBean);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "创建完毕喽！");
        }
        return true;
    }

    /**
     * 添加Entity元素
     *
     * @param elements {@link Entity 元素集合}该集合中
     *                 可能存在包含{@link EntityHlp}元素
     */
    private void putEntityElements(Set<? extends Element> elements) throws NoSuchFieldException {
        for (Element element : elements) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "putEntityElements:" + element);
            String target = "";
            //该@Entity类中是否有使用@ExEntity注解
            boolean isEntityHlp = Utils.annotionIs(element, EntityHlp.class);
            if (isEntityHlp) {
                //获取直接中的ExEntity值
                EntityHlp annotation = element.getAnnotation(EntityHlp.class);
                target = annotation.target();
                putClassMigrateElement((TypeElement) element);
            }
            if (target.equals("")) {
                //target为""代表该注解的类可以在所有@Batabase注解上的entities集合中添加
                for (DatabaseBean value : mDatabaseBeabMap.values()) {
                    value.setEntityElement(element);
                }
            } else {
                DatabaseBean exDatabaseBean = mDatabaseBeabMap.get(target);
                exDatabaseBean.setEntityElement(element);

            }
            //判断该元素是否包含@FieldHlp
            List<? extends Element> enclosedElements = element.getEnclosedElements();
            for (Element enclosedElement : enclosedElements) {
                FieldHlp annotation = enclosedElement.getAnnotation(FieldHlp.class);
                if (annotation != null) {
                    putFieldHlpElement((VariableElement) enclosedElement);

                }
            }

        }
    }

    private void putDaoElements(Set<? extends Element> elements) {
        for (Element element : elements) {
            String target = "";
            boolean isDao = Utils.annotionIs(element, DaoHlp.class);
            if (isDao) {
                DaoHlp annotation = element.getAnnotation(DaoHlp.class);
                target = annotation.target();
            }
            if (target.equals("")) {
                for (DatabaseBean value : mDatabaseBeabMap.values()) {
                    value.setDaoElement(element);
                }
            } else {
                DatabaseBean exDatabaseBean = mDatabaseBeabMap.get(target);
                exDatabaseBean.setDaoElement(element);
            }
        }
    }

    private void putDaoHlpAtField(Set<? extends Element> elements) {
        for (Element element : elements) {
            if (element.getKind().isField()) {
                for (DatabaseBean databaseBean : mDatabaseBeabMap.values()) {
                    databaseBean.setDaoHlpFieldElement((VariableElement) element);
                }
            }
        }
    }

    /**
     * 处理{@link FieldHlp 注解}如果startversion和endversion相同，则认为它们是一次修改
     * 那么在生产Migration 代码是将会把它们写在同一个你函数中
     *
     * @param elements ExAlter元素集合
     */
    private void putExAlterElements(Set<? extends Element> elements) {
        for (Element element : elements) {
            putFieldHlpElement((VariableElement) element);
        }

    }

    /**
     * 取出@EntityHlp、@FieldHlp修饰类的时候标记的元素
     */
    private void putClassMigrateElement(TypeElement typeElement) {
        StringBuilder builder = new StringBuilder();
        builder.append(typeElement.getQualifiedName());
        EntityHlp entityHlp = typeElement.getAnnotation(EntityHlp.class);
        mMessager.printMessage(Diagnostic.Kind.NOTE, "ccccccc>>>>cccccc");
        if (entityHlp.addTable() || entityHlp.deletTable() ||
                !entityHlp.oldTableName().equals("")) {
            if (entityHlp.startVersion() == -1) {
                builder.append("类注解@EntityHlp中未填写startVersion开始版本号！");
                builder.append("\n");
                throw new IllegalArgumentException(builder.toString());
            }
            if (entityHlp.endVersion() == -1) {
                builder.append("@EntityHlp注解中未填写endVersion结束版本号！");
                builder.append("\n");
                throw new IllegalArgumentException(builder.toString());
            }
            int startVersion = entityHlp.startVersion();
            int endVersion = entityHlp.endVersion();
            addMigrateElement(typeElement, startVersion, endVersion);
        }
    }

    /**
     * 取出@EntityHlp类中@FieldHlp标记的属性
     *
     * @param varElement 属性元素
     */
    private void putFieldHlpElement(VariableElement varElement) {
        FieldHlp annotation = varElement.getAnnotation(FieldHlp.class);
        int startVersion = annotation.startVersion();
        int endVersion = annotation.endVersion();
        addMigrateElement(varElement, startVersion, endVersion);
    }

    private void addMigrateElement(Element element, int startVersion, int endVersion) {
        for (DatabaseBean databaseBean : mDatabaseBeabMap.values()) {
            //获取用不存放修改元素的集合，该集合中的MigrateElementBean元素盛放这同一次修改的数据集
            List<MigrateElementBean> migrateElementBeans = databaseBean.getMigrateElementBeans();
            if (migrateElementBeans.size() == 0) {
                //如果修改元素的集合中没有元素，那么就创建个修改元素的bean类，并添加到ExDatabaseBean的修改元素集合中
                MigrateElementBean bean = setMigrateElmentBean(startVersion, endVersion, element);
                databaseBean.setMigrateElementBean(bean);
            } else {

                //遍历修改元素集合，比较当前startversion和endversion是否相同
                //如相同则代表为统一修改，那么就将该元素存放到当前遍历出出的MigrateElementBean的集合中
                MigrateElementBean migrateElementBean = findSameVersionElement(startVersion, endVersion, migrateElementBeans);
                if (migrateElementBean != null) {
                    migrateElementBean.setMigrates(element);
                } else {
                    MigrateElementBean bean = setMigrateElmentBean(startVersion, endVersion, element);
                    databaseBean.setMigrateElementBean(bean);
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

    private MigrateElementBean findSameVersionElement(int startVersion, int endVersion,
                                                      List<MigrateElementBean> migrateElementBeans) {
        for (MigrateElementBean migrateElementBean : migrateElementBeans) {
            int star = migrateElementBean.getStartVersion();
            int end = migrateElementBean.getEndVersion();
            if (startVersion == star && endVersion == end) {
                return migrateElementBean;
            }
        }
        return null;
    }
}
