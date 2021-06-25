package com.lookballs.sqlite;

import android.database.sqlite.SQLiteDatabase;

import com.lookballs.sqlite.core.ChainQuery;
import com.lookballs.sqlite.core.SqliteOperator;
import com.lookballs.sqlite.core.async.ResultExecutor;
import com.lookballs.sqlite.core.result.ResultBean;
import com.lookballs.sqlite.helper.SqliteHelper;

import java.util.List;
import java.util.Set;

public class QuickSqlite {

    private QuickSqlite() {

    }

    public static Set<SQLiteDatabase> getDatabases() {
        return SqliteOperator.getDatabases();
    }

    public static void useDatabase(SQLiteDatabase db) {
        SqliteOperator.useDatabase(db);
    }

    public static SQLiteDatabase getUseDatabase() {
        return SqliteOperator.getUseDatabase();
    }

    public static <T extends QuickSqliteSupport> ResultBean isTableExists(Class<T> cla) {
        return SqliteOperator.isTableExists(cla);
    }

    public static <T extends QuickSqliteSupport> ResultBean isColumnExists(Class<T> cla, String columnName) {
        return SqliteOperator.isColumnExists(cla, columnName);
    }

    /*****************************************************增操作*****************************************************/

    public static <T extends QuickSqliteSupport> ResultBean save(T obj) {
        return SqliteOperator.save(obj);
    }

    public static <T extends QuickSqliteSupport> ResultExecutor saveAsync(T obj) {
        return SqliteOperator.saveAsync(obj);
    }

    public static <T extends QuickSqliteSupport> ResultBean saveList(List<T> obj) {
        return SqliteOperator.saveList(obj);
    }

    public static <T extends QuickSqliteSupport> ResultExecutor saveListAsync(List<T> obj) {
        return SqliteOperator.saveListAsync(obj);
    }

    /*****************************************************改操作*****************************************************/

    public static <T extends QuickSqliteSupport> ResultBean update(T obj, long... ids) {
        return update(obj, SqliteHelper.getWhereOfIdsWithOr(ids));
    }

    public static <T extends QuickSqliteSupport> ResultExecutor updateAsync(T obj, long... ids) {
        return updateAsync(obj, SqliteHelper.getWhereOfIdsWithOr(ids));
    }

    public static <T extends QuickSqliteSupport> ResultBean update(T obj, String... conditions) {
        return SqliteOperator.update(obj, conditions);
    }

    public static <T extends QuickSqliteSupport> ResultExecutor updateAsync(T obj, String... conditions) {
        return SqliteOperator.updateAsync(obj, conditions);
    }

    /*****************************************************查操作*****************************************************/

    public static ChainQuery chainQuery() {
        return new ChainQuery();
    }

    public static <T extends QuickSqliteSupport> ResultBean queryBySql(Class<T> cla, String sql, String[] selectionArgs) {
        return SqliteOperator.queryBySql(cla, sql, selectionArgs);
    }

    public static <T extends QuickSqliteSupport> ResultExecutor queryBySqlAsync(Class<T> cla, String sql, String[] selectionArgs) {
        return SqliteOperator.queryBySqlAsync(cla, sql, selectionArgs);
    }

    /*****************************************************删操作*****************************************************/

    public static <T extends QuickSqliteSupport> ResultBean delete(Class<T> cla, long... ids) {
        return delete(cla, SqliteHelper.getWhereOfIdsWithOr(ids));
    }

    public static <T extends QuickSqliteSupport> ResultExecutor deleteAsync(Class<T> cla, long... ids) {
        return deleteAsync(cla, SqliteHelper.getWhereOfIdsWithOr(ids));
    }

    public static <T extends QuickSqliteSupport> ResultBean delete(Class<T> cla, String... conditions) {
        return SqliteOperator.delete(cla, conditions);
    }

    public static <T extends QuickSqliteSupport> ResultExecutor deleteAsync(Class<T> cla, String... conditions) {
        return SqliteOperator.deleteAsync(cla, conditions);
    }

    public static <T extends QuickSqliteSupport> ResultBean deleteAll(Class<T> cla) {
        return SqliteOperator.deleteAll(cla);
    }

    public static <T extends QuickSqliteSupport> ResultExecutor deleteAllAsync(Class<T> cla) {
        return SqliteOperator.deleteAllAsync(cla);
    }

    public static <T extends QuickSqliteSupport> ResultBean deleteTable(Class<T> cla) {
        return SqliteOperator.deleteTable(cla);
    }

    public static ResultBean deleteDb(SQLiteDatabase database) {
        return SqliteOperator.deleteDb(database);
    }
}
