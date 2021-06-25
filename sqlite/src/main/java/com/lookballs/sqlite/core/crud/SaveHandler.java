package com.lookballs.sqlite.core.crud;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.lookballs.sqlite.QuickSqliteSupport;
import com.lookballs.sqlite.common.SqliteSql;
import com.lookballs.sqlite.core.FieldInfoBean;
import com.lookballs.sqlite.core.result.ResultBean;
import com.lookballs.sqlite.helper.SqliteHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SaveHandler extends BaseHandler {

    public SaveHandler(SQLiteDatabase db) {
        super(db);
    }

    public <T extends QuickSqliteSupport> ResultBean onSave(T baseObj) {
        List<T> listObj = new ArrayList<>();
        listObj.add(baseObj);
        return onSave(listObj);
    }

    public <T extends QuickSqliteSupport> ResultBean onSave(List<T> listBaseObj) {
        ResultBean resultBean = new ResultBean();

        if (listBaseObj == null && listBaseObj.size() == 0) {
            resultBean.setSuccess(false);
            resultBean.setException(new Exception("save the data is empty"));
        } else {
            try {
                mDatabase.beginTransaction();
                saveOperation(listBaseObj);
                mDatabase.setTransactionSuccessful();
                resultBean.setSuccess(true);
            } catch (Exception e) {
                resultBean.setSuccess(false);
                resultBean.setException(e);
            } finally {
                try {
                    mDatabase.endTransaction();
                } catch (Exception e) {
                    resultBean.setSuccess(false);
                    resultBean.setException(e);
                }
            }
        }

        return resultBean;
    }

    private <T extends QuickSqliteSupport> void saveOperation(List<T> listBaseObj) {
        Class<? extends QuickSqliteSupport> cla = listBaseObj.get(0).getClass();
        List<Field> fieldList = SqliteHelper.getSupportFieldList(cla);

        StringBuilder nameSb = new StringBuilder();
        StringBuilder valueSb = new StringBuilder();
        for (int i = 0; i < fieldList.size(); i++) {
            if (i == fieldList.size() - 1) {
                nameSb.append(fieldList.get(i).getName());
                valueSb.append("?");
            } else {
                nameSb.append(fieldList.get(i).getName()).append(",");
                valueSb.append("?,");
            }
        }
        String sql = SqliteSql.getInsertSql(cla.getSimpleName(), nameSb.toString(), valueSb.toString());

        SQLiteStatement state = mDatabase.compileStatement(sql);
        for (int i = 0; i < listBaseObj.size(); i++) {
            state.clearBindings();
            for (int j = 0; j < fieldList.size(); j++) {
                Field field = fieldList.get(j);
                int finalJ = j;
                FieldInfoBean fieldInfo = SqliteHelper.getFieldInfo(listBaseObj.get(i), field);
                saveValueByFieldType(state, finalJ + 1, fieldInfo.fieldType, fieldInfo.fieldValue);
            }
            state.executeInsert();
        }
        state.close();
    }
}
