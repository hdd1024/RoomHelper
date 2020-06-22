package com.example.jbgf_room_compiler;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.lang.model.element.Element;

public class JbRoomBaseEntity {
    private String mPackageName;
    private String mClassName;
    private String mClassFullName;
    private Element mClassElement;
    private List<Element> mJbEntityElement = new ArrayList<>();
    private List<Element> mJbDaoElement = new ArrayList<>();

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
    }

    public String getClassName() {
        return mClassName + "_JbRoomBase";
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

    public List<Element> getJbEntityElement() {
        return mJbEntityElement;
    }

    public void setJbEntityElement(Element jbEntityElement) {
        this.mJbEntityElement.add(jbEntityElement);
    }

    public List<Element> getJbDaoElement() {
        return mJbDaoElement;
    }

    public void setmJbDaoElement(Element jbDaoElement) {
        this.mJbDaoElement.add(jbDaoElement);
    }
}
