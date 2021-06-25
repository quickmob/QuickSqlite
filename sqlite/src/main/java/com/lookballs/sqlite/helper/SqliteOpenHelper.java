package com.lookballs.sqlite.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lookballs.sqlite.core.result.ResultBean;
import com.lookballs.sqlite.utils.LogUtils;

public class SqliteOpenHelper extends SQLiteOpenHelper {

    private final String LOG_TAG = "SqliteOpenHelper";

    private Context mContext;
    private String DB_PATH = "";
    private String DB_NAME = "";
    private int DB_VERSION = 1;
    private SQLiteDatabase liteDatabase = null;//使用之前需要判空

    public SqliteOpenHelper(Context context, String dbPath, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName, factory, version);
        mContext = context;
        DB_PATH = dbPath;
        DB_NAME = dbName;
        DB_VERSION = version;
        LogUtils.i(LOG_TAG + ">>>init\nContext：" + mContext + "\nDB_PATH：" + DB_PATH + "\nDB_NAME：" + DB_NAME + "\nDB_VERSION：" + DB_VERSION);
    }

    public synchronized SQLiteDatabase getDatabase() {
        getWritableDatabase();
        return liteDatabase;
    }

    public synchronized ResultBean init(String... sqls) {
        if (liteDatabase != null) {
            int version = liteDatabase.getVersion();
            if (version < DB_VERSION) {
                //升级版本
                return onUpgradeVersion(version, DB_VERSION, sqls);
            } else if (version > DB_VERSION) {
                //降级版本
                return onDowngradeVersion(version, DB_VERSION, sqls);
            } else {
                liteDatabase.setVersion(DB_VERSION);
                //创建表
                return createTable(sqls);
            }
        } else {
            ResultBean resultBean = new ResultBean();
            resultBean.setSuccess(false);
            resultBean.setException(new Exception("SQLiteDatabase == null"));
            return resultBean;
        }
    }

    private SQLiteDatabase getCreateDb() {
        SQLiteDatabase sqLiteDatabase = SqliteHelper.createDb(DB_PATH, DB_NAME);
        return sqLiteDatabase;
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        LogUtils.i(LOG_TAG + ">>>getReadableDatabase");
        SQLiteDatabase sqLiteDatabase = getCreateDb();
        if (sqLiteDatabase != null) {
            liteDatabase = sqLiteDatabase;
            LogUtils.i(LOG_TAG + ">>>getReadableDatabase dbPath " + liteDatabase.getPath());
        }
        return liteDatabase;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        LogUtils.i(LOG_TAG + ">>>getWritableDatabase");
        SQLiteDatabase sqLiteDatabase = getCreateDb();
        if (sqLiteDatabase != null) {
            liteDatabase = sqLiteDatabase;
            LogUtils.i(LOG_TAG + ">>>getWritableDatabase dbPath " + liteDatabase.getPath());
        }
        return liteDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtils.i(LOG_TAG + ">>>onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.i(LOG_TAG + ">>>onUpgrade oldVersion " + oldVersion + " newVersion " + newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.i(LOG_TAG + ">>>onDowngrade oldVersion " + oldVersion + " newVersion " + newVersion);
    }

    /**
     * 创建表
     *
     * @param tableSqls
     */
    public ResultBean createTable(String... tableSqls) {
        ResultBean resultBean = new ResultBean();

        LogUtils.i(LOG_TAG + ">>>createTable");

        int successCount = 0;
        StringBuilder exceptionSb = new StringBuilder();
        if (tableSqls != null && tableSqls.length > 0) {
            for (int i = 0; i < tableSqls.length; i++) {
                ResultBean sqliteResultBean = SqliteHelper.createTable(liteDatabase, tableSqls[i]);
                if (sqliteResultBean.isSuccess()) {
                    successCount++;
                } else {
                    exceptionSb.append("\n").append(sqliteResultBean.getException().getMessage());
                }
            }
        }

        if (successCount == tableSqls.length) {
            resultBean.setSuccess(true);
        } else {
            resultBean.setSuccess(false);
            resultBean.setException(new Exception(exceptionSb.toString()));
        }
        return resultBean;
    }

    /**
     * 升级数据库版本
     */
    public ResultBean onUpgradeVersion(int oldVersion, int newVersion, String... sqls) {
        ResultBean resultBean = new ResultBean();

        LogUtils.i(LOG_TAG + ">>>onUpgradeVersion oldVersion " + oldVersion + " newVersion " + newVersion);

        int successCount = 0;
        StringBuilder exceptionSb = new StringBuilder();
        if (sqls != null && sqls.length > 0) {
            for (int i = 0; i < sqls.length; i++) {
                ResultBean sqliteResultBean = SqliteHelper.execSql(liteDatabase, sqls[i]);
                if (sqliteResultBean.isSuccess()) {
                    successCount++;
                } else {
                    exceptionSb.append("\n").append(sqliteResultBean.getException().getMessage());
                }
            }
        }

        if (successCount == sqls.length) {
            resultBean.setSuccess(true);
        } else {
            resultBean.setSuccess(false);
            resultBean.setException(new Exception(exceptionSb.toString()));
        }
        return resultBean;
    }

    /**
     * 降级数据库版本
     */
    public ResultBean onDowngradeVersion(int oldVersion, int newVersion, String... sqls) {
        ResultBean resultBean = new ResultBean();

        LogUtils.i(LOG_TAG + ">>>onDowngradeVersion oldVersion " + oldVersion + " newVersion " + newVersion);

        int successCount = 0;
        StringBuilder exceptionSb = new StringBuilder();
        if (sqls != null && sqls.length > 0) {
            for (int i = 0; i < sqls.length; i++) {
                ResultBean sqliteResultBean = SqliteHelper.execSql(liteDatabase, sqls[i]);
                if (sqliteResultBean.isSuccess()) {
                    successCount++;
                } else {
                    exceptionSb.append("\n").append(sqliteResultBean.getException().getMessage());
                }
            }
        }

        if (successCount == sqls.length) {
            resultBean.setSuccess(true);
        } else {
            resultBean.setSuccess(false);
            resultBean.setException(new Exception(exceptionSb.toString()));
        }
        return resultBean;
    }
}