package com.lookballs.sqlite.core.crud;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.lookballs.sqlite.QuickSqliteSupport;
import com.lookballs.sqlite.core.FieldInfoBean;
import com.lookballs.sqlite.core.result.ResultBean;
import com.lookballs.sqlite.helper.SqliteHelper;

import java.lang.reflect.Field;
import java.util.List;

public class UpdateHandler extends BaseHandler {

    public UpdateHandler(SQLiteDatabase db) {
        super(db);
    }

    public <T extends QuickSqliteSupport> ResultBean onUpdate(T obj, String[] conditions) {
        ContentValues values = new ContentValues();

        QuickSqliteSupport emptyModel = SqliteHelper.getEmptyModel(obj);
        if (emptyModel != null) {
            List<Field> fieldList = SqliteHelper.getSupportFieldList(obj.getClass());
            for (Field field : fieldList) {
                FieldInfoBean emptyModelInfo = SqliteHelper.getFieldInfo(emptyModel, field);
                FieldInfoBean modelInfo = SqliteHelper.getFieldInfo(obj, field);
                Object emptyModelFieldValue = emptyModelInfo.fieldValue;
                Object modelFieldValue = modelInfo.fieldValue;
                if (modelFieldValue != null && emptyModelFieldValue != null) {
                    String realFieldValue = modelFieldValue.toString();
                    String emptyFieldValue = emptyModelFieldValue.toString();
                    if (!realFieldValue.equals(emptyFieldValue)) {
                        updateValueByFieldType(values, modelInfo.fieldType, modelInfo.fieldName, modelFieldValue);
                    }
                } else {
                    if (modelFieldValue != emptyModelFieldValue) {
                        updateValueByFieldType(values, modelInfo.fieldType, modelInfo.fieldName, modelFieldValue);
                    }
                }
            }
            return SqliteHelper.update(mDatabase, obj.getClass().getSimpleName(), values, getWhereClause(conditions), getWhereArgs(conditions));
        } else {
            ResultBean resultBean = new ResultBean();
            resultBean.setSuccess(false);
            resultBean.setException(new Exception("update to generate model is null"));
            return resultBean;
        }
    }
}
