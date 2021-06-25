package com.lookballs.sqlite.core.crud;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lookballs.sqlite.QuickSqliteSupport;
import com.lookballs.sqlite.common.Constants;
import com.lookballs.sqlite.common.SqliteSql;
import com.lookballs.sqlite.core.result.ResultBean;
import com.lookballs.sqlite.helper.SqliteHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class QueryHandler extends BaseHandler {

    public QueryHandler(SQLiteDatabase db) {
        super(db);
    }

    public <T extends QuickSqliteSupport> ResultBean onQueryCount(Class<T> cla) {
        ResultBean<Long> resultBean = new ResultBean<>();

        Cursor cursor = null;
        try {
            String sql = SqliteSql.getCountSql(cla.getSimpleName());
            cursor = mDatabase.rawQuery(sql, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    resultBean.setSuccess(true);
                    resultBean.setResult(cursor.getLong(0));
                } else {
                    resultBean.setSuccess(true);
                    resultBean.setResult(0L);
                }
            } else {
                resultBean.setSuccess(false);
                resultBean.setException(new Exception("cursor is null"));
            }
        } catch (Exception e) {
            resultBean.setSuccess(false);
            resultBean.setException(e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return resultBean;
    }

    public <T extends QuickSqliteSupport> ResultBean onQuery(Class<T> cla, String[] columns, String[] conditions, String groupBy, String having, String orderBy, String limit) {
        ResultBean<List<T>> resultBean = new ResultBean<>();

        Cursor cursor = mDatabase.query(cla.getSimpleName(), columns, getWhereClause(conditions), getWhereArgs(conditions), groupBy, having, orderBy, limit);
        queryOperation(cla, cursor, resultBean);

        return resultBean;
    }

    public <T extends QuickSqliteSupport> ResultBean onQueryBySql(Class<T> cla, String sql, String[] selectionArgs) {
        ResultBean<List<T>> resultBean = new ResultBean<>();

        Cursor cursor = mDatabase.rawQuery(sql, selectionArgs);
        queryOperation(cla, cursor, resultBean);

        return resultBean;
    }

    private <T extends QuickSqliteSupport> void queryOperation(Class<T> cla, Cursor cursor, ResultBean<List<T>> resultBean) {
        try {
            if (cursor != null) {
                List<T> dataList = new ArrayList<>();
                List<Field> fieldList = SqliteHelper.getSupportFieldList(cla);
                if (cursor.moveToFirst()) {
                    do {
                        T modelInstance = (T) createInstanceFromClass(cla);
                        setAutoIncrementId(modelInstance, cursor.getLong(cursor.getColumnIndexOrThrow(Constants.FIELD_ID_PRIMARY_KEY)));
                        for (int j = 0; j < fieldList.size(); j++) {
                            String fieldType = fieldList.get(j).getType().getName();
                            String fieldName = fieldList.get(j).getName();
                            int columnIndex = cursor.getColumnIndex(fieldName);
                            if (columnIndex >= 0) {
                                queryValueByFieldType(cursor, columnIndex, modelInstance, fieldType, fieldName);
                            }
                        }
                        dataList.add(modelInstance);
                    } while (cursor.moveToNext());
                }
                resultBean.setSuccess(true);
                resultBean.setResult(dataList);
            } else {
                resultBean.setSuccess(false);
                resultBean.setException(new Exception("cursor is null"));
            }
        } catch (Exception e) {
            resultBean.setSuccess(false);
            resultBean.setException(e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }
}
