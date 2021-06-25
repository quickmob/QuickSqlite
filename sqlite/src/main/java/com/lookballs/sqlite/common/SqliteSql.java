package com.lookballs.sqlite.common;

public class SqliteSql {
    public static String getSelectLimitSql(String tableName, int page, int count) {
        return String.format("select * from %s limit %s,%s", tableName, (page - 1) * count, count);
    }

    public static String getCountSql(String tableName) {
        return String.format("select count(*) from %s", tableName);
    }

    public static String getInsertSql(String tableName, String fieldName, String fieldValue) {
        return String.format("insert into %s(%s) values(%s)", tableName, fieldName, fieldValue);
    }

    public static String getSelectIdLimitSql(String tableName, String idName) {
        return String.format("select * from %s where %s = ? limit %s,%s", tableName, idName, 0, 1);
    }

    public static String getCheckColumnExistsSql(String tableName, String columnName) {
        return String.format("select * from sqlite_master where name = '%s' and sql like '%s'", tableName, "%" + columnName + "%");
    }

    public static String getCheckTableExistsSql(String tableName) {
        return String.format("select * from sqlite_master where name = '%s'", tableName);
    }

    public static String getDeleteTableSql(String tableName) {
        return String.format("drop table %s", tableName);
    }

    public static String getClearTableSql(String tableName) {
        return String.format("delete from %s", tableName);
    }

    public static String getCreateTableSql(String tableName, String columnNames) {
        return String.format("create table if not exists %s(%s)", tableName, columnNames);
    }

    public static String getAlterTableSql(String tableName, String columnName) {
        return String.format("alter table %s add column %s ", tableName, columnName);
    }
}
