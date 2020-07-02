package com.hmz.extendroom_compiler;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hmz.extendroom_annotation.ExDatabase;
import com.hmz.extendroom_compiler.utils.Utils;
import com.squareup.javapoet.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;

public class ExDatabaseCreater {
    private ExDatabaseBean exDatabaseBean;
    private Elements elements;

    public ExDatabaseCreater(Elements elements) {
        this.elements = elements;
    }

    public void createJavaClass(Filer filer, ExDatabaseBean baseEntity) {
        this.exDatabaseBean = baseEntity;
        JavaFile javaFile = JavaFile.builder(baseEntity.getPackageName(), generateJbRoomBase())
                .build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 在类上添加注解
     *
     * @return
     */
    private AnnotationSpec generateAnSpec() {
        //获取@Entity标记的类信息
        List<Element> databaseElements = exDatabaseBean.getEntityElements();
        List<String> classFullNames = new ArrayList<>();
        //获取所有标记@Entity类的名称，并将他们放到集合中
        for (Element element : databaseElements) {
            String packageName = elements.getPackageOf(element).getQualifiedName().toString();
            String typeName = element.getSimpleName().toString();
            String typeFullName = packageName + "." + typeName;
            classFullNames.add(typeFullName);
        }
        //将@Entity标记类全名城集合包装成{com.xxx.class,com.xxx.class}的集合
        String entityClazzs = Utils.getClazzsFroma(classFullNames);
        //获取@RoomBase注解中的值
        Map<String, Object> annValues = Utils.getAnnotationValus(exDatabaseBean.getClassElement());
        //获取数据库的版本号
        int version = (int) annValues.get("version");

        AnnotationSpec.Builder annotationSpecBuilder = AnnotationSpec.builder(Database.class)
                .addMember("entities", "$L", entityClazzs)
                .addMember("version", "$L", version);
        String[] views = (String[]) annValues.get("views");
        //添加Views视图
        if (views != null) {
            String viewsValus = Utils.getClazzsFroma(views);
            annotationSpecBuilder.addMember("views", "$L", viewsValus);
        }
        //添加是否到处json
        boolean exportSchema = annValues.containsKey("exportSchema");
        annotationSpecBuilder.addMember("exportSchema", "$L", exportSchema);
        return annotationSpecBuilder.build();
    }

    /**
     * 在类中添加属性
     *
     * @return
     */
    private FieldSpec generateFidldSpec() {
        Element classElement = exDatabaseBean.getClassElement();
        TypeName instance = TypeName.get(classElement.asType());
        TypeName baseBuilder = TypeName.get(RoomDatabase.Builder.class);
        FieldSpec fieldSpec = FieldSpec.builder(baseBuilder, "baseBuilder", Modifier.PUBLIC, Modifier.FINAL)
                .initializer("androidx.room.Room.databaseBuilder( $T();", baseBuilder).build();

        return fieldSpec;
    }
//    android.content

    /**
     * 生成RoomDatabase.Builder的静态方法
     *
     * @return
     */
    private MethodSpec generateMethod_RoomInit() {
        TypeName baseBuilderType = TypeName.get(RoomDatabase.Builder.class);
        TypeName contextType = TypeName.get(Context.class);
        String classNameClass = exDatabaseBean.getClassName() + ".class";
        String roomPath = "androidx.room.Room";
        ExDatabase annotation = exDatabaseBean.getClassElement().getAnnotation(ExDatabase.class);
        String dbName = annotation.name();
        if (dbName.equals("")) {
            dbName = exDatabaseBean.getClassElement().getSimpleName().toString();
            System.out.println("----->数据库名称" + dbName);
        }

        MethodSpec methodSpec = MethodSpec.methodBuilder("roomInit")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(baseBuilderType)
                .addParameter(contextType, "context")
                .addStatement("return $L.databaseBuilder(context,$L,$S)", roomPath, classNameClass, dbName)
                .build();
        return methodSpec;
    }

    /**
     * 生产dao接口的抽象方法
     *
     * @param daoElement
     * @return
     */
    private MethodSpec generateMethod_Dao(Element daoElement) {
        Name daoName = daoElement.getSimpleName();
        MethodSpec.Builder builder = MethodSpec.methodBuilder("get" + daoName.toString());
        builder.addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
        TypeName daoType = TypeName.get(daoElement.asType());
        builder.returns(daoType);
        return builder.build();
    }

    /**
     * 该方法主要用于生类信息
     *
     * @return
     */
    private TypeSpec generateJbRoomBase() {
        AnnotationSpec annotationSpec = generateAnSpec();
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(exDatabaseBean.getClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
        if (annotationSpec != null) {
            typeSpecBuilder.addAnnotation(generateAnSpec());
        }
        typeSpecBuilder.superclass(RoomDatabase.class);
        for (MigrateElementBean elementBean : exDatabaseBean.getMigrateElementBeans()) {

            typeSpecBuilder.addField(new ExAlterCreater(elements, elementBean).alterMigration());
        }
        typeSpecBuilder.addMethod(generateMethod_RoomInit());
        //添加dao接口的抽象方法
        for (Element dao : exDatabaseBean.getDaoElements()) {
            typeSpecBuilder.addMethod(generateMethod_Dao(dao));
        }
        return typeSpecBuilder.build();
    }
}
