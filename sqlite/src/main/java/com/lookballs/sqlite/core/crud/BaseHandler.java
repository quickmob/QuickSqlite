package com.lookballs.sqlite.core.crud;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.lookballs.sqlite.QuickSqliteSupport;
import com.lookballs.sqlite.common.Constants;
import com.lookballs.sqlite.helper.SqliteHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class BaseHandler {

    public SQLiteDatabase mDatabase;

    public BaseHandler(SQLiteDatabase db) {
        mDatabase = db;
    }

    protected <T extends QuickSqliteSupport> void queryValueByFieldType(Cursor cursor, int columnIndex, T model, String fieldType, String fieldName) throws Exception {
        if (SqliteHelper.check_string(fieldType) || SqliteHelper.check_short(fieldType)) {
            if (SqliteHelper.check_string(fieldType)) {
                setModelFieldValue(model, fieldName, cursor.getString(columnIndex), model.getClass());
            } else if (SqliteHelper.check_short(fieldType)) {
                setModelFieldValue(model, fieldName, cursor.getShort(columnIndex), model.getClass());
            }
        } else if (SqliteHelper.check_long(fieldType) || SqliteHelper.check_int(fieldType) || SqliteHelper.check_boolean(fieldType)) {
            if (SqliteHelper.check_long(fieldType)) {
                setModelFieldValue(model, fieldName, cursor.getLong(columnIndex), model.getClass());
            } else if (SqliteHelper.check_int(fieldType)) {
                setModelFieldValue(model, fieldName, cursor.getInt(columnIndex), model.getClass());
            } else if (SqliteHelper.check_boolean(fieldType)) {
                setModelFieldValue(model, fieldName, (cursor.getInt(columnIndex) == 1), model.getClass());
            }
        } else if (SqliteHelper.check_byte(fieldType)) {
            setModelFieldValue(model, fieldName, cursor.getBlob(columnIndex), model.getClass());
        } else if (SqliteHelper.check_float(fieldType) || SqliteHelper.check_double(fieldType)) {
            if (SqliteHelper.check_float(fieldType)) {
                setModelFieldValue(model, fieldName, cursor.getFloat(columnIndex), model.getClass());
            } else if (SqliteHelper.check_double(fieldType)) {
                setModelFieldValue(model, fieldName, cursor.getDouble(columnIndex), model.getClass());
            }
        }
    }

    protected void updateValueByFieldType(ContentValues values, String fieldType, String fieldName, Object fieldValue) {
        if (SqliteHelper.check_string(fieldType) || SqliteHelper.check_short(fieldType)) {
            String value = String.valueOf(fieldValue);
            values.put(fieldName, value);
        } else if (SqliteHelper.check_long(fieldType) || SqliteHelper.check_int(fieldType) || SqliteHelper.check_boolean(fieldType)) {
            if (SqliteHelper.check_long(fieldType)) {
                long value = (Long) fieldValue;
                values.put(fieldName, value);
            } else if (SqliteHelper.check_int(fieldType)) {
                int value = (int) fieldValue;
                values.put(fieldName, value);
            } else if (SqliteHelper.check_boolean(fieldType)) {
                boolean value = (boolean) fieldValue;
                if (value) {
                    values.put(fieldName, 1);
                } else {
                    values.put(fieldName, 0);
                }
            }
        } else if (SqliteHelper.check_byte(fieldType)) {
            byte[] value = (byte[]) fieldValue;
            values.put(fieldName, value);
        } else if (SqliteHelper.check_float(fieldType) || SqliteHelper.check_double(fieldType)) {
            if (SqliteHelper.check_float(fieldType)) {
                float value = (float) fieldValue;
                values.put(fieldName, value);
            } else if (SqliteHelper.check_double(fieldType)) {
                double value = (double) fieldValue;
                values.put(fieldName, value);
            }
        }
    }

    protected void saveValueByFieldType(SQLiteStatement state, int index, String fieldType, Object fieldValue) {
        if (SqliteHelper.check_string(fieldType) || SqliteHelper.check_short(fieldType)) {
            String value = String.valueOf(fieldValue);
            state.bindString(index, value);
        } else if (SqliteHelper.check_long(fieldType) || SqliteHelper.check_int(fieldType) || SqliteHelper.check_boolean(fieldType)) {
            if (SqliteHelper.check_long(fieldType)) {
                long value = (Long) fieldValue;
                state.bindLong(index, value);
            } else if (SqliteHelper.check_int(fieldType)) {
                int value = (int) fieldValue;
                state.bindLong(index, value);
            } else if (SqliteHelper.check_boolean(fieldType)) {
                boolean value = (boolean) fieldValue;
                if (value) {
                    state.bindLong(index, 1);
                } else {
                    state.bindLong(index, 0);
                }
            }
        } else if (SqliteHelper.check_byte(fieldType)) {
            byte[] value = (byte[]) fieldValue;
            state.bindBlob(index, value);
        } else if (SqliteHelper.check_float(fieldType) || SqliteHelper.check_double(fieldType)) {
            if (SqliteHelper.check_float(fieldType)) {
                float value = (float) fieldValue;
                state.bindDouble(index, value);
            } else if (SqliteHelper.check_double(fieldType)) {
                double value = (double) fieldValue;
                state.bindDouble(index, value);
            }
        }
    }

    protected String getWhereClause(String... conditions) {
        if (conditions != null && conditions.length > 0) {
            return conditions[0];
        }
        return null;
    }

    protected String[] getWhereArgs(String... conditions) {
        if (conditions != null && conditions.length > 1) {
            String[] whereArgs = new String[conditions.length - 1];
            System.arraycopy(conditions, 1, whereArgs, 0, conditions.length - 1);
            return whereArgs;
        }
        return null;
    }

    protected void setAutoIncrementId(QuickSqliteSupport model, long id) throws Exception {
        setFiledValue(model, Constants.FIELD_ID_PRIMARY_KEY_OPEN, id, QuickSqliteSupport.class);
    }

    protected void setModelFieldValue(Object model, String fieldName, Object fieldValue, Class<?> modelClass) throws Exception {
        if (modelClass == QuickSqliteSupport.class || modelClass == Object.class) {
            return;
        }
        try {
            setFiledValue(model, fieldName, fieldValue, modelClass);
        } catch (NoSuchFieldException e) {
            setModelFieldValue(model, fieldName, fieldValue, model.getClass().getSuperclass());
        }
    }

    protected void setFiledValue(Object model, String fieldName, Object fieldValue, Class<?> modelClass) throws Exception {
        Field objectField = modelClass.getDeclaredField(fieldName);
        objectField.setAccessible(true);
        objectField.set(model, fieldValue);
    }

    protected Object createInstanceFromClass(Class<?> modelClass) {
        try {
            Constructor<?> constructor = findBestSuitConstructor(modelClass);
            return constructor.newInstance(getConstructorParams(modelClass, constructor));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Constructor<?> findBestSuitConstructor(Class<?> modelClass) {
        Constructor<?>[] constructors = modelClass.getDeclaredConstructors();
        if (constructors.length == 0) {
            throw new RuntimeException(modelClass.getName() + " has no constructor. could not handle it");
        }
        Constructor<?> bestSuitConstructor = null;
        int minConstructorParamLength = Integer.MAX_VALUE;
        for (Constructor<?> constructor : constructors) {
            Class<?>[] types = constructor.getParameterTypes();
            boolean canUseThisConstructor = true; // under some conditions, constructor can not use for create instance
            for (Class<?> parameterType : types) {
                if (parameterType == modelClass || parameterType.getName().startsWith("com.android") && parameterType.getName().endsWith("InstantReloadException")) {
                    // we can not use this constructor
                    canUseThisConstructor = false;
                    break;
                }
            }
            if (canUseThisConstructor) { // we can use this constructor
                if (types.length < minConstructorParamLength) { // find the constructor with least parameter
                    bestSuitConstructor = constructor;
                    minConstructorParamLength = types.length;
                }
            }
        }
        if (bestSuitConstructor != null) {
            bestSuitConstructor.setAccessible(true);
        } else {
            StringBuilder builder = new StringBuilder(modelClass.getName()).append(" has no suited constructor to new instance. Constructors defined in class:");
            for (Constructor<?> constructor : constructors) {
                builder.append("\n").append(constructor.toString());
            }
            throw new RuntimeException(builder.toString());
        }
        return bestSuitConstructor;
    }

    private Object[] getConstructorParams(Class<?> modelClass, Constructor<?> constructor) {
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            params[i] = getInitParamValue(modelClass, paramTypes[i]);
        }
        return params;
    }

    private Object getInitParamValue(Class<?> modelClass, Class<?> paramType) {
        String paramTypeName = paramType.getName();
        if (SqliteHelper.check_boolean(paramTypeName)) {
            return false;
        }
        if (SqliteHelper.check_float(paramTypeName)) {
            return 0f;
        }
        if (SqliteHelper.check_double(paramTypeName)) {
            return 0.0;
        }
        if (SqliteHelper.check_int(paramTypeName)) {
            return 0;
        }
        if (SqliteHelper.check_long(paramTypeName)) {
            return 0L;
        }
        if (SqliteHelper.check_short(paramTypeName)) {
            return 0;
        }
        if (SqliteHelper.check_byte(paramTypeName)) {
            return new byte[0];
        }
        if (SqliteHelper.check_string(paramTypeName)) {
            return "";
        }
        if (modelClass == paramType) {
            return null;
        }
        return createInstanceFromClass(paramType);
    }
}
