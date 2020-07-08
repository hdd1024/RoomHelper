package com.hmz.roomhelper_compiler;


import androidx.room.Dao;
import androidx.room.Database;

import com.hmz.roomhelper_annotation.DaoHlp;
import com.hmz.roomhelper_annotation.DatabaseHlp;
import com.hmz.roomhelper_compiler.utils.Utils;
import com.squareup.javapoet.*;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import static com.hmz.roomhelper_compiler.Constant.DAO_FIELD_MAP;

public class DatabaseCreater {
    private DatabaseBean databaseBean;
    private Elements elements;
    private Messager mMessager;
    private int version;

    public DatabaseCreater(Elements elements) {
        this.elements = elements;
    }

    public void createJavaClass(Filer filer, DatabaseBean baseEntity) {
        this.databaseBean = baseEntity;
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
        List<Element> databaseElements = databaseBean.getEntityElements();
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
        Map<String, Object> annValues = Utils.getAnnotationValus(databaseBean.getClassElement());
        //获取数据库的版本号
        version = databaseBean.getClassElement().getAnnotation(DatabaseHlp.class).version();
        if (version == 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(databaseBean.getClassFullName());
            List<MigrateElementBean> elementBeans = databaseBean.getMigrateElementBeans();
            if (elementBeans.size() != 0) {
                Collections.sort(elementBeans);
                MigrateElementBean max = Collections.max(elementBeans);
                version = max.getEndVersion();
            } else {
                version = 1;
                builder.append(">>@DatabaseHlp注解中指定版本号version的值");
                if (mMessager != null) {
                    mMessager.printMessage(Diagnostic.Kind.WARNING, builder.toString());
                }

            }
        }
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
     * 在类中添加版本属性
     *
     * @return
     */
    private FieldSpec generateVersionFieldSpec() {

        Element classElement = databaseBean.getClassElement();
        //  TypeName instance = TypeName.get(classElement.asType());
        // TypeName baseBuilder = TypeName.get(RoomDatabase.Builder.class);
        FieldSpec fieldSpec = FieldSpec.builder(int.class, "CURRENT_VERSION",
                Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$L", version).build();

        return fieldSpec;
    }

    /**
     * 条件用于保存 注入dao的信息
     *
     * @param typeSpecBuilder
     */
    private void generateDaoFileMap(TypeSpec.Builder typeSpecBuilder) {
        ParameterizedTypeName mapTypeName = ParameterizedTypeName.get(Map.class, String.class, String.class);
        FieldSpec.Builder builder = FieldSpec.builder(mapTypeName, DAO_FIELD_MAP);
        builder.addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL);
        builder.initializer("new $T<$T,$T>()", HashMap.class, String.class, String.class);
        typeSpecBuilder.addField(builder.build());

        MethodSpec.Builder methodBuilder = MethodSpec.constructorBuilder();
        methodBuilder.addModifiers(Modifier.PUBLIC);
        for (VariableElement daoHlpFieldElement : databaseBean.getDaoHlpFieldElements()) {
            TypeElement typeElement = (TypeElement) daoHlpFieldElement.getEnclosingElement();
            String simpleName = daoHlpFieldElement.getSimpleName().toString();
            methodBuilder.addCode("$L.put($S,$S);\n", DAO_FIELD_MAP, typeElement.getQualifiedName(), simpleName);
        }
        typeSpecBuilder.addMethod(methodBuilder.build());
    }

    /**
     * 生成RoomDatabase.Builder的静态方法
     *
     * @return
     */
    private MethodSpec generateMethod_RoomInit() {
        ClassName baseBuilderType = ClassName.get("androidx.room",
                "RoomDatabase", "Builder");
        ClassName contextType = ClassName.get("android.content", "Context");
        String classNameClass = databaseBean.getClassName() + ".class";
        String roomPath = "androidx.room.Room";
        DatabaseHlp annotation = databaseBean.getClassElement().getAnnotation(DatabaseHlp.class);
        String dbName = annotation.name();
        if (dbName.equals("")) {
            dbName = databaseBean.getClassElement().getSimpleName().toString();
        }

        MethodSpec methodSpec = MethodSpec.methodBuilder("roomInit")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(baseBuilderType)
                .addParameter(contextType, "context")
                .addStatement("return $L.databaseBuilder(context,$L,$S)",
                        roomPath, classNameClass, dbName)
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
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(databaseBean.getClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
        if (annotationSpec != null) {
            typeSpecBuilder.addAnnotation(generateAnSpec());
        }
        ClassName roomDatabaseType = ClassName.get("androidx.room", "RoomDatabase");

        typeSpecBuilder.superclass(roomDatabaseType);
        //添加版本号属性
        if (version != 0) {
            typeSpecBuilder.addField(generateVersionFieldSpec());
        }

        //添加@DaoHlp属性，也就是注入@dao 的属性
        if (databaseBean.getDaoHlpFieldElements().size() != 0) {
            generateDaoFileMap(typeSpecBuilder);
        }
        //添加或RoomDatabase.Builder的静态方法方法
        typeSpecBuilder.addMethod(generateMethod_RoomInit());
        //添加迁移数据库属性
        for (MigrateElementBean elementBean : databaseBean.getMigrateElementBeans()) {
            MigrateCreater migrateCreater = new MigrateCreater(elements, elementBean);
            migrateCreater.fieldMigration(typeSpecBuilder);
        }
        //添加dao接口的抽象方法
        for (Element dao : databaseBean.getDaoElements()) {
            typeSpecBuilder.addMethod(generateMethod_Dao(dao));
        }
        return typeSpecBuilder.build();
    }

    public void setMessager(Messager messager) {
        this.mMessager = messager;
    }
}
