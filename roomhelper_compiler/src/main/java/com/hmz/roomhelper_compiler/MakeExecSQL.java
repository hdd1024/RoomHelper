package com.hmz.roomhelper_compiler;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hmz.roomhelper_annotation.FieldHlp;
import com.hmz.roomhelper_compiler.utils.Utils;

import java.util.List;

import javax.annotation.Nullable;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * ALTER TABLE user_info CHANGE NAME name VARCHAR(10);
 */
public class MakeExecSQL {

    private final String _EXEC_SQL = ".execSQL(\"";
    private final String CMD_CREAT_TABLE = "CREATE TABLE ";
    private final String CMD_ALTER_TABLE = "ALTER TABLE ";
    private final String CMD_DROP_TABLE = "DROP TABLE ";
    private final String CMD_INSERT_INTO = "INSERT INTO ";
    private final String AGU_RENAME = "RENAME ";
    private final String AGU_SELECT = "SELECT ";
    private final String AGU_TO = "TO ";
    private final String AGU_FROM = "FROM ";
    private final String AGU_ADD = "ADD ";
    private final String AGU_ADD_COLUMN = "ADD COLUMN ";
    private final String AGU_PRIMARY_KEY = "PRIMARY KEY ";
    private final String AGU_AUTOINCREMENT = "AUTOINCREMENT ";
    private final String FT_NOT_NULL = "NOT NULL ";
    private final String FT_NUMERIC = "NUMERIC ";//
    private final String FT_INTEGER = "INTEGER ";//byte，short，int，long
    private final String FT_REAL = "REAL ";
    private final String FT_BLOB = "BLOB ";
    private final String FT_TEXT = "TEXT ";
    private final String FT_DATE = "DATE ";
    private final String FT_DATETIME = "DATETIME ";
    private final String _END = "\");";

    private StringBuilder strBuilder;

    public MakeExecSQL() {
        strBuilder = new StringBuilder();

    }

    public String getAlterSqlChange(String caller, Element element) {


        return strBuilder.toString();
    }

    /**
     * 修改表名称
     *
     * @param caller      字符串头部，该字符串会与.execSQL("组成如下：database..execSQL()
     * @param newName     新的表名
     * @param typeElement 要操作的类元素
     * @return 拼装完成的字符串如下字符串：
     * database..execSQL("ALTER TABLE List RENAME TO  tb_list_new");
     */
    public String changeTableName(String caller, String newName, TypeElement typeElement) {
        // alter table tb_list rename to  tb_list_new
        strBuilder.setLength(0);
        strBuilder.append(caller);
        strBuilder.append(_EXEC_SQL);
        strBuilder.append(CMD_ALTER_TABLE);
        strBuilder.append(" ");
        strBuilder.append(getTableName(typeElement));
        strBuilder.append(" ");
        strBuilder.append(AGU_RENAME);
        strBuilder.append(AGU_TO);
        strBuilder.append(newName);
        strBuilder.append(_END);
        return strBuilder.toString();
    }

    public String deletTable(String caller, TypeElement typeElement) {
        return deletTable(caller, getTableName(typeElement));
    }

    /**
     * 删除表
     *
     * @param caller    字符串头部，该字符串会与.execSQL("组成如下：database..execSQL()
     * @param tableName 要删除的旧表名称
     * @return 拼装完成的字符串如下字符串：
     * database..execSQL("ALTER TABLE List RENAME TO tb_list_new");
     */
    public String deletTable(String caller, String tableName) {
        //drop table tb_list_new
        strBuilder.setLength(0);
        strBuilder.append(caller);
        strBuilder.append(_EXEC_SQL);
        strBuilder.append(CMD_DROP_TABLE);
        strBuilder.append(" ");
        strBuilder.append(tableName);
        strBuilder.append(_END);
        return strBuilder.toString();
    }

    /**
     * 创建表
     *
     * @param caller      字符串头部，该字符串会与.execSQL("组成如下：database..execSQL()
     * @param typeElement 要操作的类元素
     * @return 拼装完成的字符串如下字符串：
     * database..execSQL("CREATE TABLE List ( id INTEGER PRIMARY KEY,name TEXT,age TEXT)");
     */
    public String creatTable(String caller, TypeElement typeElement) {
        // create table  tb_list_new ( id int primary key,name text,age text)
        strBuilder.setLength(0);
        strBuilder.append(caller);
        strBuilder.append(_EXEC_SQL);
        strBuilder.append(CMD_CREAT_TABLE);
        strBuilder.append(getTableName(typeElement));
        strBuilder.append(" (");
        strBuilder.append(makeCreateFields(typeElement, false));
        strBuilder.append(")");
        strBuilder.append(_END);
        return strBuilder.toString();
    }

    /**
     * 添加单个字段
     *
     * @param caller     字符串头部，该字符串会与.execSQL("组成如下：database..execSQL()
     * @param varElement 添加的属性值元素
     * @return 拼装完成的字符串如下字符串：
     * database..execSQL("ALTER TABLE table_name ADD COLUMN column_name TEXT");
     */
    public String addColumn(String caller, VariableElement varElement) {
        // alter table table_name add column column_name text
        strBuilder.setLength(0);
        strBuilder.append(caller);
        strBuilder.append(_EXEC_SQL);
        strBuilder.append(CMD_ALTER_TABLE);
        strBuilder.append(" ");
        strBuilder.append(getTableNameByField(varElement));
        strBuilder.append(" ");
        strBuilder.append(AGU_ADD_COLUMN);
        strBuilder.append(varElement.getSimpleName());
        strBuilder.append(" ");
        strBuilder.append(getFielType(varElement));
        strBuilder.append(_END);
        return strBuilder.toString();
    }

    /**
     * 添加多个字段
     *
     * @param caller      字符串头部，该字符串会与.execSQL("组成如下：database..execSQL()
     * @param typeElement 添加的属性值元素
     * @return 拼装完成的字符串如下字符串：
     * database..execSQL("ALTER TABLE table_name ADD (column_name TEXT time TEXT, dura TEXT)");
     */
    public String addColumns(String caller, TypeElement typeElement) {
        // alter table table_name add column column_name text
        strBuilder.setLength(0);
        strBuilder.append(caller);
        strBuilder.append(_EXEC_SQL);
        strBuilder.append(CMD_ALTER_TABLE);
        strBuilder.append(" ");
        strBuilder.append(getTableName(typeElement));
        strBuilder.append(" ");
        strBuilder.append(AGU_ADD);
        strBuilder.append("(");
        strBuilder.append(makeCreateFields(typeElement, true));
        strBuilder.append(")");
        strBuilder.append(_END);
        return strBuilder.toString();
    }

    public String insertInto(String caller, TypeElement fromElement,
                             TypeElement toElement) {
        return insertInto(caller, fromElement, null, true, getTableName(toElement));
    }

    public String insertInto(String caller, TypeElement fromElement,
                             String fromTableName, TypeElement toElement) {
        return insertInto(caller, fromElement, fromTableName, true, getTableName(toElement));
    }

    /**
     * 将一个表的数据迁移到另外一个表中
     *
     * @param caller        字符串头部，该字符串会与.execSQL("组成如下：database..execSQL()
     * @param fromElement   数据来源类
     * @param fromTableName 来源表的名称，如果为null，则会从fromElement元素中获取
     * @param toTableName   将数据添加到的表的名字
     * @return 拼装完成的字符串如下字符串：
     * database..execSQL("INSERT INTO new_table SELECT id,name,age FROM old_table");
     */
    public String insertInto(String caller, TypeElement fromElement, @Nullable String fromTableName,
                             boolean oldField, String toTableName) {
        //insert into new_table select id,name,age from  old_table
        strBuilder.setLength(0);
        strBuilder.append(caller);
        strBuilder.append(_EXEC_SQL);
        strBuilder.append(CMD_INSERT_INTO);
        strBuilder.append(" ");
        strBuilder.append(toTableName);
        strBuilder.append(" ");
        strBuilder.append(AGU_SELECT);
        strBuilder.append(" ");
        strBuilder.append(makeInsertIntoFields(fromElement, oldField));
        strBuilder.append(" ");
        strBuilder.append(AGU_FROM);
        if (fromTableName == null) {
            strBuilder.append(getTableName(fromElement));
        } else {
            strBuilder.append(fromTableName);
        }
        strBuilder.append(_END);
        return strBuilder.toString();
    }

    private String getTableName(TypeElement typeElement) {

        Entity annotation = typeElement.getAnnotation(Entity.class);
        String tableName = annotation.tableName();
        if (tableName.equals("")) {
            tableName = typeElement.getSimpleName().toString();
        }
        return tableName;
    }

    private String getOldName(VariableElement vElement) {
        FieldHlp annotation = vElement.getAnnotation(FieldHlp.class);
        return annotation.oldFieldName();
    }

    /**
     * 获取该属性类上的@Entity 中的表名，如果没有在@Entity 的tableName，
     * 那么就会认为类名就是表名
     *
     * @param element 属性元素
     * @return 表名
     */
    private String getTableNameByField(VariableElement element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        Entity annotation = typeElement.getAnnotation(Entity.class);
        String tableName = annotation.tableName();
        if (tableName.equals("")) {
            tableName = typeElement.getSimpleName().toString();
        }
        return tableName;
    }

    /**
     * 组装该类的所有属性，用户将标的数据复制给另外一张表
     *
     * @param typeElement 类元素
     * @param oldField    如果存在@FieldHlp 是否获取修改元素的名称,true 该属性值名称已就属性名为准
     * @return 返回值例如："id,name,age"
     */
    private String makeInsertIntoFields(TypeElement typeElement, boolean oldField) {
        StringBuilder builder = new StringBuilder();
        boolean firstField = true;
        List<? extends Element> encElements = typeElement.getEnclosedElements();
        for (Element encElement : encElements) {
            if (encElement.getKind().isField()) {
                if (!firstField)
                    builder.append(",");
                firstField = false;
                FieldHlp fieldHlp = encElement.getAnnotation(FieldHlp.class);
                if (fieldHlp != null && oldField && !fieldHlp.oldFieldName().equals("")) {
                    //该属性为修改属性
                    builder.append(fieldHlp.oldFieldName());
                } else {
                    builder.append(encElement.getSimpleName());
                }

            }
        }
        return builder.toString();
    }

    /**
     * 组装该类元素中的所有属性，用于创建表使用
     *
     * @param typeElment   类元素
     * @param onlyFieldHlp 是否值注重@FieldHlp注解的属性
     * @return 返回的值例如："ID INTEGER PRIMARY KEY AUTOINCREMENT, Modify_Username text"
     */
    private String makeCreateFields(TypeElement typeElment, boolean onlyFieldHlp) {
        StringBuilder fiedsBuilser = new StringBuilder();
        List<? extends Element> enclosedElements = typeElment.getEnclosedElements();
        //第一个不需要,
        boolean firstField = true;
        for (int i = 0; i < enclosedElements.size(); i++) {
            Element encElement = enclosedElements.get(i);
            //是否为属性
            boolean field = encElement.getKind().isField();
            if (field) {
                if (onlyFieldHlp) {
                    FieldHlp annotation = encElement.getAnnotation(FieldHlp.class);
                    if (annotation == null) continue;
                }
                if (!firstField) fiedsBuilser.append(",");
                firstField = false;
                //获取属性名
                String fieldName = encElement.toString();
                //获取属性类型
                String fielType = getFielType(encElement);
                fiedsBuilser.append("`"+fieldName+"`");
                fiedsBuilser.append(" ");
                fiedsBuilser.append(fielType);
                PrimaryKey primaryKey = encElement.getAnnotation(PrimaryKey.class);
                if (primaryKey != null) {
                    fiedsBuilser.append(AGU_PRIMARY_KEY);
                    boolean autoGenerate = primaryKey.autoGenerate();
                    if (autoGenerate)
                        fiedsBuilser.append(AGU_AUTOINCREMENT);
                }
            }
        }
        return fiedsBuilser.toString();
    }

    /**
     * 获取元素匹配的类型
     */
    private String getFielType(Element element) {
        String fileTypeStr = element.asType().toString();
        if (Utils.strEuqals(fileTypeStr, "int", "Integer", "byte", "Byte",
                "short", "Short", "long", "Long")) {
            return FT_INTEGER;
        } else if (Utils.strEuqals(fileTypeStr, "double",
                "Double", "float", "Float")) {
            return FT_REAL;
        } else if (Utils.strEuqals(fileTypeStr, "boolean", "Boolean")) {
            return FT_BLOB;
        } else if (fileTypeStr.contains("String")) {
            return FT_TEXT;
        } else if (fileTypeStr.contains("Date")) {
            return FT_DATE;
        } else if (Utils.strEuqals(fileTypeStr, "char")) {
            return FT_TEXT;
        } else if (Utils.strEuqals(fileTypeStr, "Character")) {
            return FT_TEXT;
        }
        return null;
    }

}
