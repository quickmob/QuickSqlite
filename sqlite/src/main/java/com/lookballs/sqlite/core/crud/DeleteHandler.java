package com.lookballs.sqlite.core.crud;

import android.database.sqlite.SQLiteDatabase;

import com.lookballs.sqlite.QuickSqliteSupport;
import com.lookballs.sqlite.core.result.ResultBean;
import com.lookballs.sqlite.helper.SqliteHelper;

public class DeleteHandler extends BaseHandler {

    public DeleteHandler(SQLiteDatabase db) {
        super(db);
    }

    public <T extends QuickSqliteSupport> ResultBean onDelete(Class<T> cla, String[] conditions) {
        return SqliteHelper.delete(mDatabase, cla.getSimpleName(), getWhereClause(conditions), getWhereArgs(conditions));
    }

    public <T extends QuickSqliteSupport> ResultBean onDeleteAll(Class<T> cla) {
        return SqliteHelper.delete(mDatabase, cla.getSimpleName(), null, null);
    }

    public <T extends QuickSqliteSupport> ResultBean onDeleteTable(Class<T> cla) {
        return SqliteHelper.deleteTable(mDatabase, cla.getSimpleName());
    }

    public ResultBean onDeleteDb() {
        return SqliteHelper.deleteDb(mDatabase);
    }
}
