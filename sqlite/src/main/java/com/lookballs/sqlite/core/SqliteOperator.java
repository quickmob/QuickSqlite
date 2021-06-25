package com.lookballs.sqlite.core;

import android.database.sqlite.SQLiteDatabase;

import com.lookballs.sqlite.QuickSqlite;
import com.lookballs.sqlite.QuickSqliteSupport;
import com.lookballs.sqlite.core.async.ResultExecutor;
import com.lookballs.sqlite.core.crud.DeleteHandler;
import com.lookballs.sqlite.core.crud.QueryHandler;
import com.lookballs.sqlite.core.crud.SaveHandler;
import com.lookballs.sqlite.core.crud.UpdateHandler;
import com.lookballs.sqlite.core.result.ResultBean;
import com.lookballs.sqlite.helper.SqliteHelper;
import com.lookballs.sqlite.utils.SqliteUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SqliteOperator {

    private static Set<SQLiteDatabase> databases = null;
    private static SQLiteDatabase useDatabase = null;

    public static void setDatabase(SQLiteDatabase db) {
        synchronized (QuickSqlite.class) {
            if (databases == null) {
                databases = new HashSet<>();
            }
            if (db != null) {
                databases.add(db);
            }
        }
    }

    public static Set<SQLiteDatabase> getDatabases() {
        return databases;
    }

    public static void useDatabase(SQLiteDatabase db) {
        synchronized (QuickSqlite.class) {
            //设置默认SQLiteDatabase或切换SQLiteDatabase
            if (db != null) {
                useDatabase = db;
            }
        }
    }

    public static SQLiteDatabase getUseDatabase() {
        return useDatabase;
    }

    public static <T extends QuickSqliteSupport> ResultBean isTableExists(Class<T> cla) {
        if (getUseDatabase() == null) {
            ResultBean resultBean = new ResultBean();
            resultBean.setSuccess(false);
            resultBean.setException(new Exception("SQLiteDatabase == null"));
            return resultBean;
        }
        synchronized (QuickSqlite.class) {
            return SqliteHelper.checkTableExists(getUseDatabase(), cla.getSimpleName());
        }
    }

    public static <T extends QuickSqliteSupport> ResultBean isColumnExists(Class<T> cla, String columnName) {
        if (getUseDatabase() == null) {
            ResultBean resultBean = new ResultBean();
            resultBean.setSuccess(false);
            resultBean.setException(new Exception("SQLiteDatabase == null"));
            return resultBean;
        }
        synchronized (QuickSqlite.class) {
            return SqliteHelper.checkColumnExists(getUseDatabase(), cla.getSimpleName(), columnName);
        }
    }

    /*****************************************************增操作*****************************************************/

    public static <T extends QuickSqliteSupport> ResultBean save(T obj) {
        if (getUseDatabase() == null) {
            ResultBean resultBean = new ResultBean();
            resultBean.setSuccess(false);
            resultBean.setException(new Exception("SQLiteDatabase == null"));
            return resultBean;
        }
        synchronized (QuickSqlite.class) {
            SaveHandler saveHandler = new SaveHandler(getUseDatabase());
            return saveHandler.onSave(obj);
        }
    }

    public static <T extends QuickSqliteSupport> ResultExecutor saveAsync(T obj) {
        final ResultExecutor executor = new ResultExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (QuickSqlite.class) {
                    final ResultBean resultBean = save(obj);
                    SqliteUtils.runOnUiThread(executor.getListener() != null, new Runnable() {
                        @Override
                        public void run() {
                            executor.getListener().callback(resultBean);
                        }
                    });
                }
            }
        };
        executor.submit(runnable);
        return executor;
    }

    public static <T extends QuickSqliteSupport> ResultBean saveList(List<T> obj) {
        if (getUseDatabase() == null) {
            ResultBean resultBean = new ResultBean();
            resultBean.setSuccess(false);
            resultBean.setException(new Exception("SQLiteDatabase == null"));
            return resultBean;
        }
        synchronized (QuickSqlite.class) {
            SaveHandler saveHandler = new SaveHandler(getUseDatabase());
            return saveHandler.onSave(obj);
        }
    }

    public static <T extends QuickSqliteSupport> ResultExecutor saveListAsync(List<T> obj) {
        final ResultExecutor executor = new ResultExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (QuickSqlite.class) {
                    final ResultBean resultBean = saveList(obj);
                    SqliteUtils.runOnUiThread(executor.getListener() != null, new Runnable() {
                        @Override
                        public void run() {
                            executor.getListener().callback(resultBean);
                        }
                    });
                }
            }
        };
        executor.submit(runnable);
        return executor;
    }

    /*****************************************************改操作*****************************************************/

    public static <T extends QuickSqliteSupport> ResultBean update(T obj, String[] conditions) {
        if (getUseDatabase() == null) {
            ResultBean resultBean = new ResultBean();
            resultBean.setSuccess(false);
            resultBean.setException(new Exception("SQLiteDatabase == null"));
            return resultBean;
        }
        synchronized (QuickSqlite.class) {
            UpdateHandler updateHandler = new UpdateHandler(getUseDatabase());
            return updateHandler.onUpdate(obj, conditions);
        }
    }

    public static <T extends QuickSqliteSupport> ResultExecutor updateAsync(T obj, String[] conditions) {
        final ResultExecutor executor = new ResultExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (QuickSqlite.class) {
                    final ResultBean resultBean = update(obj, conditions);
                    SqliteUtils.runOnUiThread(executor.getListener() != null, new Runnable() {
                        @Override
                        public void run() {
                            executor.getListener().callback(resultBean);
                        }
                    });
                }
            }
        };
        executor.submit(runnable);
        return executor;
    }

    /*****************************************************查操作*****************************************************/

    public static <T extends QuickSqliteSupport> ResultBean count(Class<T> cla) {
        if (getUseDatabase() == null) {
            ResultBean resultBean = new ResultBean();
            resultBean.setSuccess(false);
            resultBean.setException(new Exception("SQLiteDatabase == null"));
            return resultBean;
        }
        synchronized (QuickSqlite.class) {
            QueryHandler queryHandler = new QueryHandler(getUseDatabase());
            return queryHandler.onQueryCount(cla);
        }
    }

    public static <T extends QuickSqliteSupport> ResultExecutor countAsync(Class<T> cla) {
        final ResultExecutor executor = new ResultExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (QuickSqlite.class) {
                    final ResultBean resultBean = count(cla);
                    SqliteUtils.runOnUiThread(executor.getListener() != null, new Runnable() {
                        @Override
                        public void run() {
                            executor.getListener().callback(resultBean);
                        }
                    });
                }
            }
        };
        executor.submit(runnable);
        return executor;
    }

    public static <T extends QuickSqliteSupport> ResultBean query(Class<T> cla, String[] columns, String[] conditions, String groupBy, String having, String orderBy, String limit) {
        if (getUseDatabase() == null) {
            ResultBean resultBean = new ResultBean();
            resultBean.setSuccess(false);
            resultBean.setException(new Exception("SQLiteDatabase == null"));
            return resultBean;
        }
        synchronized (QuickSqlite.class) {
            QueryHandler queryHandler = new QueryHandler(getUseDatabase());
            return queryHandler.onQuery(cla, columns, conditions, groupBy, having, orderBy, limit);
        }
    }

    public static <T extends QuickSqliteSupport> ResultExecutor queryAsync(Class<T> cla, String[] columns, String[] conditions, String groupBy, String having, String orderBy, String limit) {
        final ResultExecutor executor = new ResultExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (QuickSqlite.class) {
                    final ResultBean resultBean = query(cla, columns, conditions, groupBy, having, orderBy, limit);
                    SqliteUtils.runOnUiThread(executor.getListener() != null, new Runnable() {
                        @Override
                        public void run() {
                            executor.getListener().callback(resultBean);
                        }
                    });
                }
            }
        };
        executor.submit(runnable);
        return executor;
    }

    public static <T extends QuickSqliteSupport> ResultBean queryBySql(Class<T> cla, String sql, String[] selectionArgs) {
        if (getUseDatabase() == null) {
            ResultBean resultBean = new ResultBean();
            resultBean.setSuccess(false);
            resultBean.setException(new Exception("SQLiteDatabase == null"));
            return resultBean;
        }
        synchronized (QuickSqlite.class) {
            QueryHandler queryHandler = new QueryHandler(getUseDatabase());
            return queryHandler.onQueryBySql(cla, sql, selectionArgs);
        }
    }

    public static <T extends QuickSqliteSupport> ResultExecutor queryBySqlAsync(Class<T> cla, String sql, String[] selectionArgs) {
        final ResultExecutor executor = new ResultExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (QuickSqlite.class) {
                    final ResultBean resultBean = queryBySql(cla, sql, selectionArgs);
                    SqliteUtils.runOnUiThread(executor.getListener() != null, new Runnable() {
                        @Override
                        public void run() {
                            executor.getListener().callback(resultBean);
                        }
                    });
                }
            }
        };
        executor.submit(runnable);
        return executor;
    }

    /*****************************************************删操作*****************************************************/

    public static <T extends QuickSqliteSupport> ResultBean delete(Class<T> cla, String[] conditions) {
        if (getUseDatabase() == null) {
            ResultBean resultBean = new ResultBean();
            resultBean.setSuccess(false);
            resultBean.setException(new Exception("SQLiteDatabase == null"));
            return resultBean;
        }
        synchronized (QuickSqlite.class) {
            DeleteHandler deleteHandler = new DeleteHandler(getUseDatabase());
            return deleteHandler.onDelete(cla, conditions);
        }
    }

    public static <T extends QuickSqliteSupport> ResultExecutor deleteAsync(Class<T> cla, String[] conditions) {
        final ResultExecutor executor = new ResultExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (QuickSqlite.class) {
                    final ResultBean resultBean = delete(cla, conditions);
                    SqliteUtils.runOnUiThread(executor.getListener() != null, new Runnable() {
                        @Override
                        public void run() {
                            executor.getListener().callback(resultBean);
                        }
                    });
                }
            }
        };
        executor.submit(runnable);
        return executor;
    }

    public static <T extends QuickSqliteSupport> ResultBean deleteAll(Class<T> cla) {
        if (getUseDatabase() == null) {
            ResultBean resultBean = new ResultBean();
            resultBean.setSuccess(false);
            resultBean.setException(new Exception("SQLiteDatabase == null"));
            return resultBean;
        }
        synchronized (QuickSqlite.class) {
            DeleteHandler deleteHandler = new DeleteHandler(getUseDatabase());
            return deleteHandler.onDeleteAll(cla);
        }
    }

    public static <T extends QuickSqliteSupport> ResultExecutor deleteAllAsync(Class<T> cla) {
        final ResultExecutor executor = new ResultExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (QuickSqlite.class) {
                    final ResultBean resultBean = deleteAll(cla);
                    SqliteUtils.runOnUiThread(executor.getListener() != null, new Runnable() {
                        @Override
                        public void run() {
                            executor.getListener().callback(resultBean);
                        }
                    });
                }
            }
        };
        executor.submit(runnable);
        return executor;
    }

    public static <T extends QuickSqliteSupport> ResultBean deleteTable(Class<T> cla) {
        if (getUseDatabase() == null) {
            ResultBean resultBean = new ResultBean();
            resultBean.setSuccess(false);
            resultBean.setException(new Exception("SQLiteDatabase == null"));
            return resultBean;
        }
        synchronized (QuickSqlite.class) {
            DeleteHandler deleteHandler = new DeleteHandler(getUseDatabase());
            return deleteHandler.onDeleteTable(cla);
        }
    }

    public static ResultBean deleteDb(SQLiteDatabase database) {
        if (getUseDatabase() == null) {
            ResultBean resultBean = new ResultBean();
            resultBean.setSuccess(false);
            resultBean.setException(new Exception("SQLiteDatabase == null"));
            return resultBean;
        }
        synchronized (QuickSqlite.class) {
            DeleteHandler deleteHandler = new DeleteHandler(database);
            return deleteHandler.onDeleteDb();
        }
    }
}
