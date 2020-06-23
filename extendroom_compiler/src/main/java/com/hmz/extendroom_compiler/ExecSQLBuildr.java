package com.hmz.extendroom_compiler;

import java.util.Map;

/**
 * ALTER TABLE user_info CHANGE NAME name VARCHAR(10);
 */
public class ExecSQLBuildr {
    private final String exec_sql = ".execSQL(\"";
    private final String CREAT_TABLE = " CREATE TABLE";
    private final String ALTER_TABLE = " ALTER TABLE";
    private final String ADD_COLUMN = " ADD COLUMN";
    private final String CHANGE_NAME = " CHANGE NAME";
    private final String NOT_NULL = " NOT NULL";
    private final String PRIMARY_KEY = " PRIMARY KEY";
    private final String TEXT = " TEXT";
    private final String INTEGER = " INTEGER";
    private final String end = "\");";

    private StringBuffer buffer;

    public String getAlterSqlAdd(String caller, String tableName, String fieldName, String type) {
        return caller + String.format(".execSQL(\"alter table `$S` add column `$S`\" );", tableName, fieldName, type);
    }

    public String getAlterSqlChange(String caller, String tableName, Map<String, String> fileNameAndTypeMap) {
        //  ALTER TABLE user_info CHANGE NAME name VARCHAR(10);
        buffer = new StringBuffer(caller);
        buffer.append(CREAT_TABLE);
        buffer.append(tableName);
        buffer.append(CHANGE_NAME);
//        buffer.append()
        // TODO: 2020-06-23 修改的字符串拼接还未完成
        return buffer.toString();
    }

    public String execSql() {
        return exec_sql;
    }

    public String creat_table() {
        return CREAT_TABLE;
    }

    public String alter_table() {
        return ALTER_TABLE;
    }

    public String add_column() {
        return ADD_COLUMN;
    }

    public String not_null() {
        return NOT_NULL;
    }

    public String primary_key() {
        return PRIMARY_KEY;
    }

    public String text() {
        return TEXT;
    }

    public String integer() {
        return INTEGER;
    }
}
