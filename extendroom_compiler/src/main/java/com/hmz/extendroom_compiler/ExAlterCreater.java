package com.hmz.extendroom_compiler;


import androidx.room.migration.Migration;

import com.hmz.extendroom_annotation.ExAlter;
import com.squareup.javapoet.FieldSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;

public class ExAlterCreater {
    private Elements elements;

    private Element alterElment;


    private String className;
    private String classPackageName;

    public ExAlterCreater(Elements elements, Element alterElment) {
        this.elements = elements;
        this.alterElment = alterElment;
        Element classElement = alterElment.getEnclosingElement();

        PackageElement packageOf = elements.getPackageOf(classElement);
        classPackageName = packageOf.getQualifiedName().toString();
        className = classElement.getSimpleName().toString();
    }


    public FieldSpec alterMigration() {
        ExAlter annotation = alterElment.getAnnotation(ExAlter.class);
        String oldFileName = annotation.oldFileName();
        int startVersion = annotation.startVersion();
        int endVersion = annotation.endVersion();
        String fieldName = "MIGRATION_" + startVersion + "_" + endVersion;
        FieldSpec.Builder builder = FieldSpec.builder(Migration.class, fieldName);
//        builder.initializer()


        // TODO: 2020-06-23 增删改查还未实现
        return builder.build();
    }


    public String getAlterClassName() {
        return className;

    }

    public String getAlterClassPackageName() {
        return classPackageName;
    }

    public String getAlterClassFullName() {
        return classPackageName + "." + className;
    }

}
