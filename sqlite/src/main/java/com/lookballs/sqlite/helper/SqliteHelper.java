package com.lookballs.sqlite.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lookballs.sqlite.QuickSqliteSupport;
import com.lookballs.sqlite.annotation.Column;
import com.lookballs.sqlite.common.Constants;
import com.lookballs.sqlite.common.SqliteSql;
import com.lookballs.sqlite.core.FieldInfoBean;
import com.lookballs.sqlite.core.result.ResultBean;
import com.lookballs.sqlite.utils.LogUtils;
import com.lookballs.sqlite.utils.SqliteUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class SqliteHelper {

    /**
     * 校验字段类型
     *
     * @param fieldType
     * @return
     */
    public static boolean check_string(String fieldType) {
        return Constants.FileType.STRING1.equals(fieldType);
    }

    public static boolean check_short(String fieldType) {
        return Constants.FileType.SHORT1.equals(fieldType) || Constants.FileType.SHORT2.equals(fieldType);
    }

    public static boolean check_int(String fieldType) {
        return Constants.FileType.INT1.equals(fieldType) || Constants.FileType.INT2.equals(fieldType);
    }

    public static boolean check_long(String fieldType) {
        return Constants.FileType.LONG1.equals(fieldType) || Constants.FileType.LONG2.equals(fieldType);
    }

    public static boolean check_float(String fieldType) {
        return Constants.FileType.FLOAT1.equals(fieldType) || Constants.FileType.FLOAT2.equals(fieldType);
    }

    public static boolean check_double(String fieldType) {
        return Constants.FileType.DOUBLE1.equals(fieldType) || Constants.FileType.DOUBLE2.equals(fieldType);
    }

    public static boolean check_boolean(String fieldType) {
        return Constants.FileType.BOOLEAN1.equals(fieldType) || Constants.FileType.BOOLEAN2.equals(fieldType);
    }

    public static boolean check_byte(String fieldType) {
        return Constants.FileType.BYTE1.equals(fieldType) || Constants.FileType.BYTE2.equals(fieldType);
    }

    /**
     * 执行sql语句
     *
     * @param sqLiteDatabase SQLiteDatabase
     * @param sql            数据库语句
     * @return
     */
    public static ResultBean execSql(SQLiteDatabase sqLiteDatabase, String sql) {
        LogUtils.i("execSql>>>" + sql);
        ResultBean resultBean = new ResultBean();

        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.execSQL(sql);
            sqLiteDatabase.setTransactionSuccessful();
            resultBean.setSuccess(true);
        } catch (Exception e) {
            resultBean.setSuccess(false);
            resultBean.setException(e);
        } finally {
            try {
                sqLiteDatabase.endTransaction();
            } catch (Exception e) {
                resultBean.setSuccess(false);
                resultBean.setException(e);
            }
        }
        return resultBean;
    }

    /**
     * 创建数据库并且得到一个SQLiteDatabase
     *
     * @param dbPath 数据库文件存放路径
     * @param dbName 数据库文件名
     * @return
     */
    public static SQLiteDatabase createDb(String dbPath, String dbName) {
        SQLiteDatabase db = null;
        File file = new File(dbPath);
        boolean isCreateFolder = file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
        if (isCreateFolder) {
            try {
                db = SQLiteDatabase.openOrCreateDatabase(dbPath + dbName, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LogUtils.e("createDb>>>create " + dbPath + " folder fail，cannot create SQLiteDatabase");
        }
        return db;
    }

    /**
     * 删除数据库
     *
     * @param database 数据库
     * @return
     */
    public static ResultBean deleteDb(SQLiteDatabase database) {
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(SQLiteDatabase.deleteDatabase(new File(database.getPath())));
        return resultBean;
    }

    /**
     * 创建表
     *
     * @param sqLiteDatabase SQLiteDatabase
     * @param sql            创建表的数据库语句，例如：create table usertable(_id integer primary key autoincrement,name text,address text)
     */
    public static ResultBean createTable(SQLiteDatabase sqLiteDatabase, String sql) {
        return execSql(sqLiteDatabase, sql);
    }

    /**
     * 删除表
     *
     * @param sqLiteDatabase SQLiteDatabase
     * @param table          需要删除表的数据库表名
     */
    public static ResultBean deleteTable(SQLiteDatabase sqLiteDatabase, String table) {
        String sql = SqliteSql.getDeleteTableSql(table);
        return execSql(sqLiteDatabase, sql);
    }

    /**
     * 删除表数据
     *
     * @param sqLiteDatabase SQLiteDatabase
     * @param table          需要删除数据的数据库表名
     */
    public static ResultBean clearTable(SQLiteDatabase sqLiteDatabase, String table) {
        String sql = SqliteSql.getClearTableSql(table);
        return execSql(sqLiteDatabase, sql);
    }

    /**
     * 删除数据
     *
     * @param sqLiteDatabase SQLiteDatabase
     * @param table          数据库表名
     * @param whereClause    条件，例如：id=?
     * @param whereArgs      条件参数，例如：{String.valueOf(2)}
     */
    public static ResultBean delete(SQLiteDatabase sqLiteDatabase, String table, String whereClause, String[] whereArgs) {
        ResultBean<Long> resultBean = new ResultBean<>();

        long resultValue = -1;
        try {
            sqLiteDatabase.beginTransaction();
            resultValue = sqLiteDatabase.delete(table, whereClause, whereArgs);
            if (resultValue != -1) {
                sqLiteDatabase.setTransactionSuccessful();
            }
        } catch (Exception e) {
            resultBean.setException(e);
        } finally {
            try {
                sqLiteDatabase.endTransaction();
            } catch (Exception e) {
                resultBean.setException(e);
            }
        }
        resultBean.setSuccess(resultValue != -1);
        resultBean.setResult(resultValue);
        return resultBean;
    }

    /**
     * 更新数据
     *
     * @param sqLiteDatabase SQLiteDatabase
     * @param table          数据库表名
     * @param values         需要更新的键值
     * @param whereClause    条件，例如：id=?
     * @param whereArgs      条件参数，例如：{String.valueOf(2)}
     */
    public static ResultBean update(SQLiteDatabase sqLiteDatabase, String table, ContentValues values, String whereClause, String[] whereArgs) {
        ResultBean<Long> resultBean = new ResultBean<>();

        long resultValue = -1;
        try {
            sqLiteDatabase.beginTransaction();
            resultValue = sqLiteDatabase.update(table, values, whereClause, whereArgs);
            if (resultValue != -1) {
                sqLiteDatabase.setTransactionSuccessful();
            }
        } catch (Exception e) {
            resultBean.setException(e);
        } finally {
            try {
                sqLiteDatabase.endTransaction();
            } catch (Exception e) {
                resultBean.setException(e);
            }
        }
        resultBean.setSuccess(resultValue != -1);
        resultBean.setResult(resultValue);
        return resultBean;
    }

    /**
     * 检查表中某列是否存在
     *
     * @param sqLiteDatabase SQLiteDatabase
     * @param tableName      表名
     * @param columnName     列名
     * @return
     */
    public static ResultBean checkColumnExists(SQLiteDatabase sqLiteDatabase, String tableName, String columnName) {
        ResultBean resultBean = new ResultBean();

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(SqliteSql.getCheckColumnExistsSql(tableName, columnName), null);

            resultBean.setSuccess(cursor != null && cursor.moveToFirst());
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

    /**
     * 检查表是否存在
     *
     * @param sqLiteDatabase SQLiteDatabase
     * @param tableName      表名
     * @return
     */
    public static ResultBean checkTableExists(SQLiteDatabase sqLiteDatabase, String tableName) {
        ResultBean resultBean = new ResultBean();

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(SqliteSql.getCheckTableExistsSql(tableName), null);

            resultBean.setSuccess(cursor != null && cursor.moveToFirst());
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

    /**
     * 拼接id条件
     *
     * @param ids
     * @return
     */
    public static String getWhereOfIdsWithOr(long... ids) {
        if (ids != null && ids.length > 0) {
            StringBuilder whereClause = new StringBuilder();
            boolean needOr = false;
            for (long id : ids) {
                if (needOr) {
                    whereClause.append(" or ");
                }
                needOr = true;
                whereClause.append(Constants.FIELD_ID_PRIMARY_KEY).append(" = ");
                whereClause.append(id);
            }
            return whereClause.toString();
        }
        return null;
    }

    /**
     * 根据Field字段得到拼接后的sql信息
     *
     * @param field
     * @return
     */
    public static String getSqlFieldInfo(Field field) {
        String fieldType = field.getType().getName();
        String fieldName = field.getName();
        StringBuilder sb = new StringBuilder();

        sb.append(fieldName).append(" ");
        if (SqliteHelper.check_string(fieldType) || SqliteHelper.check_short(fieldType)) {
            sb.append("TEXT ");
        } else if (SqliteHelper.check_long(fieldType) || SqliteHelper.check_int(fieldType) || SqliteHelper.check_boolean(fieldType)) {
            sb.append("LONG ");
        } else if (SqliteHelper.check_byte(fieldType)) {
            sb.append("BLOB ");
        } else if (SqliteHelper.check_float(fieldType) || SqliteHelper.check_double(fieldType)) {
            sb.append("REAL ");
        }

        Column annotation = field.getAnnotation(Column.class);
        if (annotation != null) {
            if (!annotation.nullable()) {
                sb.append("NOT NULL ");
            }
            if (annotation.unique()) {
                sb.append("UNIQUE ");
            }
            sb.append("DEFAULT ").append(annotation.defaultValue());
        }

        return sb.toString();
    }

    /**
     * 根据Field字段获取信息
     *
     * @param obj
     * @param field
     */
    public static <T extends QuickSqliteSupport> FieldInfoBean getFieldInfo(T obj, Field field) {
        String fieldType = field.getType().getName();
        String fieldName = field.getName();
        Object fieldValue = null;
        try {
            field.setAccessible(true);
            fieldValue = field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new FieldInfoBean(fieldType, fieldName, fieldValue);
    }

    /**
     * 获取一个SqlitePalSupport空值对象
     *
     * @param baseObj
     * @return
     */
    public static QuickSqliteSupport getEmptyModel(QuickSqliteSupport baseObj) {
        try {
            String className = baseObj.getClass().getName();
            Class<?> modelClass = Class.forName(className);
            return (QuickSqliteSupport) modelClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取class文件里面的所有支持的字段
     *
     * @param obj SqlitePalSupport
     * @return
     */
    public static List<Field> getSupportFieldList(Class<? extends QuickSqliteSupport> obj) {
        List<Field> fieldList = new ArrayList<>();
        try {
            Class clazz = Class.forName(obj.getName());
            Class superClazz = clazz.getSuperclass();

            Field[] fields = clazz.getDeclaredFields();
            Field[] superFields = superClazz.getDeclaredFields();
            Field[] allFields = SqliteUtils.concatAll(fields, superFields);
            if (allFields != null && allFields.length > 0) {
                for (Field field : allFields) {
                    Column annotation = field.getAnnotation(Column.class);
                    if (annotation != null) {
                        if (annotation.ignore()) {
                            continue;
                        }
                    }

                    int modifiers = field.getModifiers();
                    if (!Modifier.isStatic(modifiers)) {
                        String fieldType = field.getType().getName();
                        if (isFieldTypeSupported(fieldType)) {
                            fieldList.add(field);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return fieldList;
    }

    /**
     * 判断是否支持字段类型。当前仅支持基本数据类型和字符串
     *
     * @param fieldType
     * @return
     */
    public static boolean isFieldTypeSupported(String fieldType) {
        if (SqliteHelper.check_boolean(fieldType)) {
            return true;
        }
        if (SqliteHelper.check_float(fieldType)) {
            return true;
        }
        if (SqliteHelper.check_double(fieldType)) {
            return true;
        }
        if (SqliteHelper.check_int(fieldType)) {
            return true;
        }
        if (SqliteHelper.check_long(fieldType)) {
            return true;
        }
        if (SqliteHelper.check_short(fieldType)) {
            return true;
        }
        if (SqliteHelper.check_byte(fieldType)) {
            return true;
        }
        if (SqliteHelper.check_string(fieldType)) {
            return true;
        }
        return false;
    }
}
