package com.lookballs.sqlite.core;

import com.lookballs.sqlite.QuickSqliteSupport;
import com.lookballs.sqlite.common.Constants;
import com.lookballs.sqlite.core.async.ResultExecutor;
import com.lookballs.sqlite.core.result.ResultBean;

public class ChainQuery {

    private String[] mColumns = null;
    private String[] mConditions = null;
    private String mGroupBy = null;
    private String mHaving = null;
    private String mOrderBy = null;
    private String mLimit = null;

    public ChainQuery() {

    }

    public ChainQuery columns(String... columns) {
        mColumns = columns;
        return this;
    }

    public ChainQuery conditions(String... conditions) {
        mConditions = conditions;
        return this;
    }

    public ChainQuery groupBy(String groupBy) {
        mGroupBy = groupBy;
        return this;
    }

    public ChainQuery having(String having) {
        mHaving = having;
        return this;
    }

    public ChainQuery orderBy(String orderBy) {
        mOrderBy = orderBy;
        return this;
    }

    public ChainQuery limit(int page, int count) {
        if (page < 0 || count < 0) {
            mLimit = null;
        } else {
            mLimit = page + "," + count;
        }
        return this;
    }

    public <T extends QuickSqliteSupport> ResultBean count(Class<T> cla) {
        return SqliteOperator.count(cla);
    }

    public <T extends QuickSqliteSupport> ResultExecutor countAsync(Class<T> cla) {
        return SqliteOperator.countAsync(cla);
    }

    public <T extends QuickSqliteSupport> ResultBean queryAll(Class<T> cla) {
        return SqliteOperator.query(cla, mColumns, null, null, null, Constants.FIELD_ID_PRIMARY_KEY, null);
    }

    public <T extends QuickSqliteSupport> ResultExecutor queryAllAsync(Class<T> cla) {
        return SqliteOperator.queryAsync(cla, mColumns, null, null, null, Constants.FIELD_ID_PRIMARY_KEY, null);
    }

    public <T extends QuickSqliteSupport> ResultBean queryFirst(Class<T> cla) {
        return SqliteOperator.query(cla, mColumns, null, null, null, Constants.FIELD_ID_PRIMARY_KEY, "1");
    }

    public <T extends QuickSqliteSupport> ResultExecutor queryFirstAsync(Class<T> cla) {
        return SqliteOperator.queryAsync(cla, mColumns, null, null, null, Constants.FIELD_ID_PRIMARY_KEY, "1");
    }

    public <T extends QuickSqliteSupport> ResultBean queryLast(Class<T> cla) {
        return SqliteOperator.query(cla, mColumns, null, null, null, Constants.FIELD_ID_PRIMARY_KEY + " desc", "1");
    }

    public <T extends QuickSqliteSupport> ResultExecutor queryLastAsync(Class<T> cla) {
        return SqliteOperator.queryAsync(cla, mColumns, null, null, null, Constants.FIELD_ID_PRIMARY_KEY + " desc", "1");
    }

    public <T extends QuickSqliteSupport> ResultBean query(Class<T> cla) {
        return SqliteOperator.query(cla, mColumns, mConditions, mGroupBy, mHaving, mOrderBy, mLimit);
    }

    public <T extends QuickSqliteSupport> ResultExecutor queryAsync(Class<T> cla) {
        return SqliteOperator.queryAsync(cla, mColumns, mConditions, mGroupBy, mHaving, mOrderBy, mLimit);
    }
}