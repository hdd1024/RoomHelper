package com.hmz.roomhelper_compiler;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

/***********************************************************
 * 创建时间:2020-07-02
 * 作   者: [hanmingze]
 * describe: 该类保存着对表进行增删改的@ExAlter标签的信息
 * 备注信息: {该类中只保存这同意次版本修改的信息}
 **********************************************************/
public class MigrateElementBean implements Comparable<MigrateElementBean> {

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

    /**
     * 该方法用于判断中结束版本的大小，可用于自动升级
     *
     * @param migrateElementBean 比较计算的元素
     * @return 结束版本
     */
    @Override
    public int compareTo(@NotNull MigrateElementBean migrateElementBean) {
        return this.endVersion - migrateElementBean.endVersion;
    }
}
