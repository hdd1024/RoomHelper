package com.hmz.extendroom_compiler;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

public class ExDatabaseBean {
    private String mPackageName;
    private String mClassName;
    private String mClassFullName;
    private Element mClassElement;
    private List<Element> mEntityElements = new ArrayList<>();
    private List<Element> daoElements = new ArrayList<>();

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
}
