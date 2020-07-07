package com.hmz.roomhelper_compiler;

import androidx.room.migration.Migration;

import com.hmz.roomhelper_annotation.EntityHlp;
import com.hmz.roomhelper_annotation.FieldHlp;
import com.hmz.roomhelper_compiler.utils.Utils;
import com.squareup.javapoet.FieldSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/***********************************************************
 * 创建时间:2020-07-02
 * 作   者: [hanmingze]
 * 功能描述: <用于处理数据库的增删改标签的功能>
 * 备注信息: {}
 * @see
 **********************************************************/
// TODO: 2020-07-03 更好名称，将RoomHelper 改成RoomHelper，将Ex改为H等名称
public class MigrateCreater {
    private MigrateElementBean migrateElementBean;
    private Elements elements;
    private MakeExecSQL makeExecSQL;

    public MigrateCreater(Elements elements, MigrateElementBean bean) {
        this.elements = elements;
        this.migrateElementBean = bean;
        makeExecSQL = new MakeExecSQL();
    }


    public FieldSpec fieldMigration() {
        int startVersion = migrateElementBean.getStartVersion();
        int endVersion = migrateElementBean.getEndVersion();
        String database = "database";
        String fieldName = "MIGRATION_" + startVersion + "_" + endVersion;
        FieldSpec.Builder builder = FieldSpec.builder(Migration.class, fieldName);
        builder.initializer("new $T($L,$L){ @Override\n  public void migrate(androidx.sqlite.db.SupportSQLiteDatabase database){$L}}",
                Migration.class, startVersion, endVersion, getMigrationExexSql(database));
        return builder.build();
    }

    //该变量用于储存修改了表字段名的class类的全路径名称
    //当该类的其实字段进行其他修改、增加的时候，如果开始、结束版本号
    //相同那么将不会再次生成修改或增加的相关代码，因为该类在同本版中已经修改过一次
    //修改字段的执行流程是 1、修改该类表名 2、新建一个和该类修改前名称相同的一个表（即新的表中的字段名已被修改）
    //3.复制该表中的数据带新建的同名表中4.删除旧表
    private String changeFieldNameclass;


    private String getMigrationExexSql(String database) {
        StringBuilder migrateBuilder = new StringBuilder();
        List<List<Element>> sameFieldClassElements = sameFieldClassElements();
        for (List<Element> sameFieldClassElementList : sameFieldClassElements) {
            int migrateType = getElementMigrateType(sameFieldClassElementList);
            Element element = sameFieldClassElementList.get(0);
            migrateBuilder.append("\n");
            if (migrateType == 0) {//增加字段
                String addColumns = makeExecSQL.addColumn(database, (VariableElement) element);
                migrateBuilder.append(addColumns);

            } else if (migrateType == 2) {//修改字段名
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                changeFieldNameclass = getFieldClassFullName(typeElement);
                String provisionalName = typeElement.getSimpleName().toString() + "_pro";
                //修改表名
                String chaneName = makeExecSQL.changeTableName(database, provisionalName, typeElement);
                migrateBuilder.append(chaneName);
                migrateBuilder.append("\n");
                //创建新表
                String creatTable = makeExecSQL.creatTable(database, typeElement);
                migrateBuilder.append(creatTable);
                migrateBuilder.append("\n");
                //复制
                String insert = makeExecSQL.insertInto(database, typeElement, provisionalName, typeElement);
                migrateBuilder.append(insert);
                migrateBuilder.append("\n");
                //删除旧表
                String deletTable = makeExecSQL.deletTable(database, provisionalName);
                migrateBuilder.append(deletTable);
            } else if (migrateType == 3) {//添加表
                String creatTable = makeExecSQL.creatTable(database, (TypeElement) element);
                migrateBuilder.append(creatTable);
            }
        }
        migrateBuilder.append("\n");
        return migrateBuilder.toString();
    }

    public List<List<Element>> sameFieldClassElements() {
        List<Element> souceElements = migrateElementBean.getMigrates();
        //用于存放相同类中的所有元素，该集合中的每一个子集合，都是同一个类中元素
        List<List<Element>> sameElementsElements = new ArrayList<>();
        //存放同一类中的子元素
        List<Element> sameElements;
        //两次循环遍历出所有相同类的元素，并将统一个类的元素放到统一类中
        for (int i = 0; i < souceElements.size(); i++) {
            sameElements = new ArrayList<>();
            Element sE = migrateElementBean.getMigrates().get(i);
            sameElements.add(sE);
            TypeElement typeElement = Utils.getTypeElement(sE);
            String fieldClassName = getFieldClassFullName(typeElement);
            for (int j = i + 1; j < souceElements.size(); j++) {
                Element element = souceElements.get(j);
                String fullName = getFieldClassFullName(Utils.getTypeElement(element));
                //全类名相同，为同一个类的元素
                if (fieldClassName.equals(fullName)) {
                    //把该元素从souce集合中移除，并添加他放到和它相同类的元素集合中
                    sameElements.add(souceElements.remove(j));
                }
            }
            sameElementsElements.add(sameElements);
        }
        return sameElementsElements;
    }

    /**
     * 判断集合元素中是否含有修改或者删除元素
     *
     * @param elements 判断的集合元素
     * @return -1 代表什么都不是，0只是增加属性,1存在删除, 2 为存在修改，3增加表，4删除表
     */
    private int getElementMigrateType(List<Element> elements) {
        for (Element element : elements) {
            FieldHlp fieldHlp = element.getAnnotation(FieldHlp.class);
            if (fieldHlp != null) {
                if (element.getKind().isClass()) {
                    //如果该标签修饰着类上那么他就是删除字段操作
                    return 1;
                } else if (!fieldHlp.oldFieldName().equals("")) {
                    //修改
                    return 2;
                }
                return 0;
            }
            EntityHlp entityHlp = element.getAnnotation(EntityHlp.class);
            if (entityHlp != null) {
                if (element.getKind().isClass()) {
                    //增加表
                    return 3;
                } else {
                    //删除表
                    return 4;
                }
            }
        }
        return -1;
    }


    public String getFieldClassName(TypeElement element) {
        return element.getSimpleName().toString();
    }

    public String getFieldClassPackageName(Element element) {
        PackageElement packageOf = elements.getPackageOf(element);
        return packageOf.getQualifiedName().toString();
    }

    public String getFieldClassFullName(TypeElement element) {
        PackageElement packageOf = elements.getPackageOf(element);
        String classPackageName = packageOf.getQualifiedName().toString();
        String className = element.getSimpleName().toString();
        return classPackageName + "." + className;
    }

}
