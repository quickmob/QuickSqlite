package com.lookballs.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.lookballs.sqlite.callback.InitResultCallback;
import com.lookballs.sqlite.common.Constants;
import com.lookballs.sqlite.common.SqliteSql;
import com.lookballs.sqlite.core.SqliteOperator;
import com.lookballs.sqlite.core.result.ResultBean;
import com.lookballs.sqlite.helper.SqliteHelper;
import com.lookballs.sqlite.helper.SqliteOpenHelper;
import com.lookballs.sqlite.utils.LogUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class QuickSqliteConfig {

    private final String LOG_TAG = "QuickSqliteConfig";

    private QuickSqliteConfig(Builder builder) {
        //设置日志开关
        LogUtils.setLogEnabled(builder.isLogEnabled);
        //开始建库操作
        if (checkMustParamConfig(builder)) {
            LogUtils.i(LOG_TAG + ">>>init start");
            SqliteOpenHelper sqliteOpenHelper = new SqliteOpenHelper(builder.context, builder.dbPath, builder.dbName, builder.factory, builder.version);
            SQLiteDatabase sqLiteDatabase = sqliteOpenHelper.getDatabase();
            if (sqLiteDatabase != null) {
                createOrAlterTable(builder, sqliteOpenHelper, sqLiteDatabase);
            } else {
                checkFailCallback(builder, "SQLiteDatabase == null");
            }
            LogUtils.i(LOG_TAG + ">>>init end");
        }
    }

    private boolean checkMustParamConfig(Builder builder) {
        if (builder.context == null) {
            checkFailCallback(builder, "context == null");
            return false;
        } else if (TextUtils.isEmpty(builder.dbPath)) {
            checkFailCallback(builder, "dbPath isEmpty");
            return false;
        } else if (TextUtils.isEmpty(builder.dbName)) {
            checkFailCallback(builder, "dbName isEmpty");
            return false;
        } else if (builder.version < 1) {
            checkFailCallback(builder, "version must be >= 1，but was " + builder.version);
            return false;
        }
        return true;
    }

    private void checkFailCallback(Builder builder, String msg) {
        if (builder.callback != null) {
            ResultBean resultBean = new ResultBean();
            resultBean.setSuccess(false);
            resultBean.setException(new Exception(msg));
            resultBean.setResult(null);
            builder.callback.callback(resultBean);
        }
    }

    private void createOrAlterTable(Builder builder, SqliteOpenHelper sqliteOpenHelper, SQLiteDatabase sqLiteDatabase) {
        if (builder.classes != null && builder.classes.length > 0) {
            List<String> sqls = new ArrayList<>();
            for (int i = 0; i < builder.classes.length; i++) {
                StringBuilder sb = new StringBuilder();

                List<Field> fieldList = SqliteHelper.getSupportFieldList(builder.classes[i]);
                for (int j = 0; j < fieldList.size(); j++) {
                    Field field = fieldList.get(j);
                    String fieldName = field.getName();
                    //得到拼接后的部分sql信息
                    String str = SqliteHelper.getSqlFieldInfo(field);
                    //检测当前数据库版本 < 配置的版本 && 表存在 && 有新字段---往已存在的表增加新字段
                    if (sqLiteDatabase.getVersion() < builder.version && SqliteHelper.checkTableExists(sqLiteDatabase, builder.classes[i].getSimpleName()).isSuccess() && !SqliteHelper.checkColumnExists(sqLiteDatabase, builder.classes[i].getSimpleName(), fieldName).isSuccess()) {
                        //添加sql语句
                        sqls.add(SqliteSql.getAlterTableSql(builder.classes[i].getSimpleName(), str));
                    } else {//否则就创建表
                        if (!TextUtils.isEmpty(str)) {
                            sb.append(str);
                            sb.append(",");
                        }
                    }
                }
                //创建表
                sb.append(Constants.FIELD_ID_PRIMARY_KEY).append(" INTEGER PRIMARY KEY AUTOINCREMENT");
                //添加sql语句
                sqls.add(SqliteSql.getCreateTableSql(builder.classes[i].getSimpleName(), sb.toString()));
            }
            //执行sql并回调
            ResultBean resultBean = sqliteOpenHelper.init(sqls.toArray(new String[sqls.size()]));
            resultBean.setResult(sqLiteDatabase);

            SqliteOperator.setDatabase(sqLiteDatabase);
            SqliteOperator.useDatabase(sqLiteDatabase);
            if (builder.callback != null) {
                builder.callback.callback(resultBean);
            }
        }
    }

    public static QuickSqliteConfig.Builder with() {
        return new QuickSqliteConfig.Builder();
    }

    public static class Builder {
        private Class<? extends QuickSqliteSupport>[] classes = null;//需要创建的表class---可不配置
        private Context context = null;//上下文---必须配置
        private SQLiteDatabase.CursorFactory factory = null;//CursorFactory---必须配置，可传空
        private String dbPath = "";//数据库存储路径---必须配置
        private String dbName = "";//数据库文件名---必须配置
        private int version = 0;//数据库版本---必须配置
        private boolean isLogEnabled = false;//日志开关，默认关闭---可不配置
        private InitResultCallback callback = null;//初始化回调---可不配置

        public Builder() {

        }

        public Builder createDb(String dbPath, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
            this.dbName = dbName;
            this.dbPath = dbPath;
            this.factory = factory;
            this.version = version;
            return this;
        }

        public Builder createTable(Class<? extends QuickSqliteSupport>... classes) {
            this.classes = classes;
            return this;
        }

        public Builder setLogEnabled(boolean isLogEnabled) {
            this.isLogEnabled = isLogEnabled;
            return this;
        }

        public Builder initCallBack(InitResultCallback callback) {
            this.callback = callback;
            return this;
        }

        public QuickSqliteConfig build(Context context) {
            this.context = context;
            return new QuickSqliteConfig(this);
        }
    }
}
