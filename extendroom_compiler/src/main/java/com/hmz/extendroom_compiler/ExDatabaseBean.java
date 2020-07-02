package com.hmz.extendroom_compiler;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

/***********************************************************
 * 创建时间:2020-07-02
 * 作   者: [hanmingze]
 * 功能描述: <创建Room需要的Database类需要的信息类>
 * 备注信息: {}
 * @see
 **********************************************************/
public class ExDatabaseBean {
    private String mPackageName;
    private String mClassName;
    private String mClassFullName;
    private Element mClassElement;
    //该集合中存放的是表数据注解信息
    private List<Element> mEntityElements = new ArrayList<>();
    //该集合中存放的dao标签的信息
    private List<Element> daoElements = new ArrayList<>();
    //该集合存放的是ExAlter便签的信息
    private List<MigrateElementBean> migrateElementBeans = new ArrayList<>();

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
    }

    public String getClassName() {
        return mClassName + "_ExRoom";
    }

    public void setClassName(String className) {
        this.mClassName = className;
    }

    public String getClassFullName() {
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

    public List<MigrateElementBean> getMigrateElementBeans() {
        return migrateElementBeans;
    }

    public void setMigrateElementBean(MigrateElementBean migrateElementBean) {
        this.migrateElementBeans.add(migrateElementBean);
    }
}
