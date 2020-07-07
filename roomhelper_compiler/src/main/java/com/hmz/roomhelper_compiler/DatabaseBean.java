package com.hmz.roomhelper_compiler;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/***********************************************************
 * 创建时间:2020-07-02
 * 作   者: [hanmingze]
 * describe: 创建Room需要的Database类需要的信息类
 * 备注信息: {}
 **********************************************************/
public class DatabaseBean {
    private String mPackageName;
    private String mClassName;
    private String mClassFullName;
    private Element mClassElement;
    //该集合中存放的是表数据注解信息
    private List<Element> mEntityElements = new ArrayList<>();
    //该集合中存放的dao标签的信息
    private List<Element> daoElements = new ArrayList<>();
    //该集合用于储存@DaoHlp注解修饰在类的属性上的元素
    private List<VariableElement> daoHlpFieldElements = new ArrayList<>();
    //该集合存放的是ExAlter便签的信息
    private List<MigrateElementBean> migrateElementBeans = new ArrayList<>();

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
    }

    public String getClassName() {
        return mClassName + "_Hlp";
    }

    public void setClassName(String className) {
        this.mClassName = className;
    }

    public String getClassFullName() {
        if (mClassFullName == null) {
            TypeElement typeElement = (TypeElement) mClassElement;
            return typeElement.getQualifiedName().toString();
        }
        return mClassFullName;
    }

    public void setClassFullName(String classFullName) {
        this.mClassFullName = classFullName;
    }

    public Element getClassElement() {
        return mClassElement;
    }

    public void setClassElement(Element classElement) {
        this.mClassElement = classElement;
    }

    public List<Element> getEntityElements() {
        return mEntityElements;
    }

    public void setEntityElement(Element entityElement) {
        this.mEntityElements.add(entityElement);
    }

    public List<Element> getDaoElements() {
        return daoElements;
    }

    public void setDaoElement(Element daoElement) {
        this.daoElements.add(daoElement);
    }

    public List<VariableElement> getDaoHlpFieldElements() {
        return daoHlpFieldElements;
    }

    public void setDaoHlpFieldElement(VariableElement variableElement) {
        daoHlpFieldElements.add(variableElement);
    }

    public List<MigrateElementBean> getMigrateElementBeans() {
        return migrateElementBeans;
    }

    public void setMigrateElementBean(MigrateElementBean migrateElementBean) {
        this.migrateElementBeans.add(migrateElementBean);
    }
}
