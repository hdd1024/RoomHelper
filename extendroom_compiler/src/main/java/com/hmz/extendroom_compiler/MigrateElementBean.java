package com.hmz.extendroom_compiler;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

/***********************************************************
 * 创建时间:2020-07-02
 * 作   者: [hanmingze]
 * 功能描述: <该类保存着对表进行增删改的@ExAlter标签的信息>
 * 备注信息: {该类中只保存这同意次版本修改的信息}
 * @see
 **********************************************************/
public class MigrateElementBean {

    private int startVersion;
    private int endVersion;
    private List<Element> migrates = new ArrayList<>();


    public int getStartVersion() {
        return startVersion;
    }

    public void setStartVersion(int startVersion) {
        this.startVersion = startVersion;
    }

    public int getEndVersion() {
        return endVersion;
    }

    public void setEndVersion(int endVersion) {
        this.endVersion = endVersion;
    }

    public List<Element> getMigrates() {
        return migrates;
    }

    public void setMigrates(Element migrate) {
        this.migrates.add(migrate);
    }
}
