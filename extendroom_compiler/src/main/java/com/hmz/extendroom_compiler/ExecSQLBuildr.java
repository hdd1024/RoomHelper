package com.hmz.extendroom_compiler;

import androidx.room.Entity;

import com.hmz.extendroom_annotation.ExAlter;

import java.util.Date;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * ALTER TABLE user_info CHANGE NAME name VARCHAR(10);
 */
public class ExecSQLBuildr {

    private final String _EXEC_SQL = ".execSQL(\"";
    private final String CMD_CREAT_TABLE = "CREATE TABLE ";
    private final String CMD_ALTER_TABLE = " ALTER TABLE ";
    private final String AGU_ADD_COLUMN = " ADD COLUMN ";
    private final String AGU_CHANGE_NAME = " CHANGE NAME ";
    private final String AGU_COLUMN = " COLUMN ";
    private final String AGU_TO = " TO ";
    private final String AGU_PRIMARY_KEY = " PRIMARY KEY ";
    private final String FT_NOT_NULL = " NOT NULL ";
    private final String FT_INTEGER = " INTEGER ";
    private final String FT_NUMERIC = " NUMERIC ";
    private final String FT_REAL = " REAL ";
    private final String FT_BLOB = " BLOB ";
    private final String FT_TEXT = " TEXT ";
    private final String FT_DATE = " DATE ";
    private final String FT_DATETIME = " DATETIME ";
    private final String _END = "\");";

    private StringBuffer buffer;

    public ExecSQLBuildr() {
        buffer = new StringBuffer();

    }

    public String getAlterSqlAdd(String caller, String tableName, String fieldName, String type) {
        return caller + String.format(".execSQL(\"alter table `$S` add column `$S`\" );", tableName, fieldName, type);
    }

    public String getAlterSqlChange(String caller, Element element) {
        //  ALTER TABLE user_info CHANGE NAME name VARCHAR(10);
        buffer.setLength(0);
        buffer.append(caller);
        buffer.append(_EXEC_SQL);
        buffer.append(CMD_CREAT_TABLE);
        buffer.append(getTableName(element));
//        buffer.append(CHANGE_NAME);
        buffer.append(getOldName(element));
//        buffer.append(TO);
        buffer.append(element.getSimpleName());
        buffer.append(_END);
        // TODO: 2020-06-23 修改的字符串拼接还未完成
        return buffer.toString();
    }

    public String creatTable(String caller, Element element) {
        //①修改原来表的名字
        //
        //alter table List rename to Listold
        //
        //②新建修改列名之后的表
        //
        //create table List ( id int primary key,name text,agenum text)
        //
        //③从旧表中查询数据并插入新表
        //
        //insert into List select id,name,age from Listold
        //
        //④删除旧表
        //
        //drop table Listold
        //————————————————

        buffer.setLength(0);
        buffer.append(caller);
        buffer.append(_EXEC_SQL);
        buffer.append(CMD_CREAT_TABLE);
        buffer.append(getTableName(element));
        buffer.append(" (");
//        buffer.append();
        buffer.append(getOldName(element));
//        buffer.append(TO);
        buffer.append(element.getSimpleName());
        buffer.append(_END);
        // TODO: 2020-06-23 修改的字符串拼接还未完成
        return buffer.toString();
    }

    /**
     * 获取该属性类上的@Entity 中的表名，如果没有在@Entity 的tableName，
     * 那么就会认为类名就是表名
     *
     * @param element 属性元素
     * @return 表名
     */
    private String getTableName(Element element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        Entity annotation = typeElement.getAnnotation(Entity.class);
        String tableName = annotation.tableName();
        if (tableName.equals("")) {
            tableName = typeElement.getSimpleName().toString();
        }
        return tableName;
    }

    private String getOldName(Element element) {
        VariableElement vElement = (VariableElement) element;
        ExAlter annotation = vElement.getAnnotation(ExAlter.class);
        return annotation.oldFileName();
    }

    private String getFields(VariableElement element) {
        TypeElement typeElment = (TypeElement) element.getEnclosingElement();
        for (Element enclosedElement : typeElment.getEnclosedElements()) {
            VariableElement vElement = (VariableElement) enclosedElement.getEnclosingElement();
            String fieldName = vElement.getSimpleName().toString();
            // TODO: 2020-07-02 获取属性的类型，拼装字符串
        }
        return null;
    }

    /**
     * 获取元素匹配的类型
     *
     * @param vElement
     * @return
     */
    // TODO: 2020-07-02 在Processor类中校验该方法 ，可以考虑将该方法放到工具类中
    private String getFielType(VariableElement vElement) {
        String fileTypeStr = vElement.asType().toString();
        if (strEuqals(fileTypeStr, "int", "Integer")) {
            return FT_INTEGER;
        } else if (strEuqals(fileTypeStr, "double", "Double", "false", "Float")) {
            return FT_REAL;
        } else if (strEuqals(fileTypeStr, "boolean", "Boolean")) {
            return FT_BLOB;
        } else if (fileTypeStr.equals("String")) {
            return FT_TEXT;
        } else if (fileTypeStr.equals("Date")) {
            return FT_DATE;
        } else if (strEuqals(fileTypeStr, "char")) {
            return FT_TEXT;
        } else if (strEuqals(fileTypeStr, "Character")) {
            return FT_TEXT;
        }
        return null;
    }

    /**
     * 判断某个目标是否和cheks想匹配
     *
     * @param tage   目标
     * @param checks cheks
     * @return 只有有一个符合，就会跳出循环
     */
    private boolean strEuqals(String tage, String... checks) {
        for (String check : checks) {
            if (tage.contains(check)) {
                return true;
            }
        }
        return false;
    }


}
