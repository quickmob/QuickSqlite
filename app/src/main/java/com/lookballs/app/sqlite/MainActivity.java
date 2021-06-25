package com.lookballs.app.sqlite;

import android.Manifest;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lookballs.app.sqlite.table.OtherInfoBean;
import com.lookballs.app.sqlite.table.UserInfoBean;
import com.lookballs.sqlite.QuickSqlite;
import com.lookballs.sqlite.QuickSqliteConfig;
import com.lookballs.sqlite.callback.AsyncResultCallback;
import com.lookballs.sqlite.callback.InitResultCallback;
import com.lookballs.sqlite.core.result.ResultBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private TextView dbName_tv;

    private final String[] READ_WRITE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private String dbPath = "";
    private String dbName = "test.db";
    private SQLiteDatabase sqLiteDatabase1, sqLiteDatabase2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbName_tv = findViewById(R.id.dbName_tv);

        PermissionUtils.permission(READ_WRITE_PERMISSIONS).callback(new PermissionUtils.SimpleCallback() {
            @Override
            public void onGranted() {
                //创建外部存储
                dbPath = SDCardUtils.getSDCardInfo().get(0).getPath() + File.separator + "QuickSqlite" + File.separator + "sqlite" + File.separator;
            }

            @Override
            public void onDenied() {
                //创建内部存储
                dbPath = getDatabasePath(dbName).getParent() + File.separator;
            }
        }).request();
    }

    //初始化数据库
    public void initDb1(View view) {
        QuickSqliteConfig.with()
                .createDb(dbPath, "1" + dbName, null, 1)
                .createTable(UserInfoBean.class, OtherInfoBean.class)
                .setLogEnabled(true)
                .initCallBack(new InitResultCallback<SQLiteDatabase>() {
                    @Override
                    public void callback(ResultBean<SQLiteDatabase> resultBean) {
                        if (resultBean.isSuccess()) {
                            sqLiteDatabase1 = resultBean.getResult();
                            dbName_tv.setText("当前数据库：" + "1" + dbName);
                            ToastUtils.showShort("数据库初始化成功");
                        } else {
                            ToastUtils.showShort("数据库初始化失败：" + resultBean.getException().getMessage());
                        }
                    }
                })
                .build(this);
    }

    //初始化数据库
    public void initDb2(View view) {
        QuickSqliteConfig.with()
                .createDb(dbPath, "2" + dbName, null, 1)
                .createTable(UserInfoBean.class, OtherInfoBean.class)
                .setLogEnabled(true)
                .initCallBack(new InitResultCallback<SQLiteDatabase>() {
                    @Override
                    public void callback(ResultBean<SQLiteDatabase> resultBean) {
                        if (resultBean.isSuccess()) {
                            sqLiteDatabase2 = resultBean.getResult();
                            dbName_tv.setText("当前数据库：" + "2" + dbName);
                            ToastUtils.showShort("数据库初始化成功");
                        } else {
                            ToastUtils.showShort("数据库初始化失败：" + resultBean.getException().getMessage());
                        }
                    }
                })
                .build(this);
    }

    //删除数据库
    public void deleteDb1(View view) {
        ResultBean resultBean = QuickSqlite.deleteDb(sqLiteDatabase1);
        if (resultBean.isSuccess()) {
            sqLiteDatabase1 = null;
            ToastUtils.showShort("数据库删除成功");
        } else {
            ToastUtils.showShort("数据库删除失败：" + resultBean.getException().getMessage());
        }
    }

    //删除数据库
    public void deleteDb2(View view) {
        ResultBean resultBean = QuickSqlite.deleteDb(sqLiteDatabase2);
        if (resultBean.isSuccess()) {
            sqLiteDatabase2 = null;
            ToastUtils.showShort("数据库删除成功");
        } else {
            ToastUtils.showShort("数据库删除失败：" + resultBean.getException().getMessage());
        }
    }

    //删除数据库表
    public void deleteUserTable(View view) {
        ResultBean resultBean = QuickSqlite.deleteTable(UserInfoBean.class);
        if (resultBean.isSuccess()) {
            ToastUtils.showShort("数据库表删除成功");
        } else {
            ToastUtils.showShort("数据库表删除失败：" + resultBean.getException().getMessage());
        }
    }

    //删除数据库表
    public void deleteOtherTable(View view) {
        ResultBean resultBean = QuickSqlite.deleteTable(OtherInfoBean.class);
        if (resultBean.isSuccess()) {
            ToastUtils.showShort("数据库表删除成功");
        } else {
            ToastUtils.showShort("数据库表删除失败：" + resultBean.getException().getMessage());
        }
    }

    //切换数据库
    public void useDb1(View view) {
        if (sqLiteDatabase1 == null) {
            ToastUtils.showShort("请先初始化数据库1");
            return;
        }
        QuickSqlite.useDatabase(sqLiteDatabase1);
        dbName_tv.setText("当前数据库：" + "1" + dbName);
    }

    //切换数据库
    public void useDb2(View view) {
        if (sqLiteDatabase2 == null) {
            ToastUtils.showShort("请先初始化数据库2");
            return;
        }
        QuickSqlite.useDatabase(sqLiteDatabase2);
        dbName_tv.setText("当前数据库：" + "2" + dbName);
    }

    //表是否存在
    public void isTableExists(View view) {
        Log.i("isTableExists", "开始");
        ResultBean resultBean = QuickSqlite.isTableExists(UserInfoBean.class);
        if (resultBean.isSuccess()) {
            Log.i("isTableExists", "结束");
            ToastUtils.showShort("存在");
        } else {
            ToastUtils.showShort("不存在：" + resultBean.getException().getMessage());
        }
    }

    //表字段是否存在
    public void isColumnExists(View view) {
        Log.i("isColumnExists", "开始");
        ResultBean resultBean = QuickSqlite.isColumnExists(UserInfoBean.class, "username");
        if (resultBean.isSuccess()) {
            Log.i("isColumnExists", "结束");
            ToastUtils.showShort("存在");
        } else {
            ToastUtils.showShort("不存在：" + resultBean.getException().getMessage());
        }
    }

    /***********************************************这是一条分界线***********************************************/

    //插入单条数据
    public void insertData1(View view) {
        UserInfoBean userInfoBean = new UserInfoBean();
        //String
        userInfoBean.setUsername("测试用户名");
        userInfoBean.setPassword("123456789");
        short s = 123;
        userInfoBean.setShortField(s);
        userInfoBean.setBaseField("BaseField");
        //long
        userInfoBean.setCreateTime(System.currentTimeMillis());
        userInfoBean.setLevel(10);
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        userInfoBean.setBindMobile(true);
        //byte
        byte[] b = new byte[]{11, 11};
        userInfoBean.setIcon(b);
        //double
        userInfoBean.setPayMoney(11.11);
        userInfoBean.setMoney(12.12f);

        Log.i("insertData", "开始单条");
        ResultBean resultBean = QuickSqlite.save(userInfoBean);
        if (resultBean.isSuccess()) {
            Log.i("insertData", "结束单条");
            ToastUtils.showShort("插入成功");
        } else {
            ToastUtils.showShort("插入失败：" + resultBean.getException().getMessage());
        }
    }

    //插入多条数据
    public void insertData2(View view) {
        List<UserInfoBean> list = new ArrayList<>();
        for (int i = 0; i < 500000; i++) {
            UserInfoBean userInfoBean = new UserInfoBean();
            //String
            userInfoBean.setUsername("用户名" + (i + 1));
            userInfoBean.setPassword("123456789");
            short s = 123;
            userInfoBean.setShortField(s);
            userInfoBean.setBaseField("BaseField");
            //long
            userInfoBean.setCreateTime(System.currentTimeMillis());
            userInfoBean.setLevel(10);
            Date date = new Date();
            date.setTime(System.currentTimeMillis());
            userInfoBean.setBindMobile(true);
            //byte
            byte[] b = new byte[]{11, 11};
            userInfoBean.setIcon(b);
            //double
            userInfoBean.setPayMoney(11.11);
            userInfoBean.setMoney(12.12f);

            list.add(userInfoBean);
        }
        Log.i("insertData", "开始多条：" + list.size());
        ResultBean resultBean = QuickSqlite.saveList(list);
        if (resultBean.isSuccess()) {
            Log.i("insertData", "结束多条");
            ToastUtils.showShort("插入成功");
        } else {
            ToastUtils.showShort("插入失败：" + resultBean.getException().getMessage());
        }
    }

    //异步插入多条数据
    public void insertData3(View view) {
        //插入数据
        List<UserInfoBean> list = new ArrayList<>();
        for (int i = 0; i < 500000; i++) {
            UserInfoBean userInfoBean = new UserInfoBean();
            //String
            userInfoBean.setUsername("用户名" + (i + 1));
            userInfoBean.setPassword("123456789");
            short s = 123;
            userInfoBean.setShortField(s);
            userInfoBean.setBaseField("BaseField");
            //long
            userInfoBean.setCreateTime(System.currentTimeMillis());
            userInfoBean.setLevel(10);
            Date date = new Date();
            date.setTime(System.currentTimeMillis());
            userInfoBean.setBindMobile(true);
            //byte
            byte[] b = new byte[]{11, 11};
            userInfoBean.setIcon(b);
            //double
            userInfoBean.setPayMoney(11.11);
            userInfoBean.setMoney(12.12f);

            list.add(userInfoBean);
        }
        Log.i("insertData", "开始多条异步：" + list.size());
        QuickSqlite.saveListAsync(list).listener(new AsyncResultCallback() {
            @Override
            public void callback(ResultBean resultBean) {
                if (resultBean.isSuccess()) {
                    Log.i("insertData", "结束多条异步");
                    ToastUtils.showShort("异步插入成功");
                } else {
                    ToastUtils.showShort("异步插入失败：" + resultBean.getException().getMessage());
                }
            }
        });
    }

    //删除数据，返回受影响的行数
    public void deleteData1(View view) {
        Log.i("deleteData", "开始");
        ResultBean<Long> resultBean = QuickSqlite.delete(UserInfoBean.class, 1, 2, 3);
        if (resultBean.isSuccess()) {
            Log.i("deleteData", "结束");
            ToastUtils.showShort("删除成功：" + resultBean.getResult());
        } else {
            ToastUtils.showShort("删除失败：" + resultBean.getException().getMessage());
        }
    }

    //删除数据，返回受影响的行数
    public void deleteData2(View view) {
        Log.i("deleteData", "开始");
        ResultBean<Long> resultBean = QuickSqlite.delete(UserInfoBean.class, "username=? or username=?", "测试用户名", "测试");
        if (resultBean.isSuccess()) {
            Log.i("deleteData", "结束");
            ToastUtils.showShort("删除成功：" + resultBean.getResult());
        } else {
            ToastUtils.showShort("删除失败：" + resultBean.getException().getMessage());
        }
    }

    //删除表内所有数据，返回受影响的行数
    public void deleteData3(View view) {
        Log.i("deleteData", "开始");
        ResultBean<Long> resultBean = QuickSqlite.deleteAll(UserInfoBean.class);
        if (resultBean.isSuccess()) {
            Log.i("deleteData", "结束");
            ToastUtils.showShort("删除成功：" + resultBean.getResult());
        } else {
            ToastUtils.showShort("删除失败：" + resultBean.getException().getMessage());
        }
    }

    //更新数据，返回受影响的行数
    public void updateData1(View view) {
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setPassword("123456");
        userInfoBean.setLevel(999);
        Log.i("updateData", "开始");
        ResultBean<Long> resultBean = QuickSqlite.update(userInfoBean, 1, 2);
        if (resultBean.isSuccess()) {
            Log.i("updateData", "结束");
            ToastUtils.showShort("更新成功：" + resultBean.getResult());
        } else {
            ToastUtils.showShort("更新失败：" + resultBean.getException().getMessage());
        }
    }

    //更新数据，返回受影响的行数
    public void updateData2(View view) {
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setUsername("测试");
        Log.i("updateData", "开始");
        ResultBean<Long> resultBean = QuickSqlite.update(userInfoBean, "username=?", "测试用户名");
        if (resultBean.isSuccess()) {
            Log.i("updateData", "结束");
            ToastUtils.showShort("更新成功：" + resultBean.getResult());
        } else {
            ToastUtils.showShort("更新失败：" + resultBean.getException().getMessage());
        }
    }

    //查询数量数据，返回数量
    public void queryData1(View view) {
        Log.i("queryData", "开始");
        ResultBean<Long> resultBean = QuickSqlite.chainQuery().count(UserInfoBean.class);
        if (resultBean.isSuccess()) {
            Log.i("queryData", "结束");
            ToastUtils.showShort("查询成功：" + resultBean.getResult());
        } else {
            ToastUtils.showShort("查询失败：" + resultBean.getException().getMessage());
        }
    }

    //查询全部数据，返回List<T>列表
    public void queryData2(View view) {
        Log.i("queryData", "开始");
        ResultBean<List<UserInfoBean>> resultBean = QuickSqlite.chainQuery().queryAll(UserInfoBean.class);
        if (resultBean.isSuccess()) {
            Log.i("queryData", "结束");
            ToastUtils.showShort("查询成功：" + GsonUtils.toJson(resultBean.getResult()));
        } else {
            ToastUtils.showShort("查询失败：" + resultBean.getException().getMessage());
        }
    }

    //查询数据，返回List<T>列表
    public void queryData3(View view) {
        Log.i("queryData", "开始");
        ResultBean<List<UserInfoBean>> resultBean = QuickSqlite.chainQuery().conditions("id=? or id=? and password=?", String.valueOf(1), String.valueOf(2), "123456").query(UserInfoBean.class);
        if (resultBean.isSuccess()) {
            Log.i("queryData", "结束");
            ToastUtils.showShort("查询成功：" + GsonUtils.toJson(resultBean.getResult()));
        } else {
            ToastUtils.showShort("查询失败：" + resultBean.getException().getMessage());
        }
    }

    //查询数据，返回List<T>列表
    public void queryData4(View view) {
        Log.i("queryData", "开始");
        ResultBean<List<UserInfoBean>> resultBean = QuickSqlite.chainQuery().limit(0, 5).query(UserInfoBean.class);
        if (resultBean.isSuccess()) {
            Log.i("queryData", "结束");
            ToastUtils.showShort("查询成功：" + GsonUtils.toJson(resultBean.getResult()));
        } else {
            ToastUtils.showShort("查询失败：" + resultBean.getException().getMessage());
        }
    }

    //查询数据，返回List<T>列表
    public void queryData5(View view) {
        Log.i("queryData", "开始");
        ResultBean<List<UserInfoBean>> resultBean = QuickSqlite.chainQuery().columns("id", "username", "password").conditions("username like ?", "%测试%").query(UserInfoBean.class);
        if (resultBean.isSuccess()) {
            Log.i("queryData", "结束");
            ToastUtils.showShort("查询成功：" + GsonUtils.toJson(resultBean.getResult()));
        } else {
            ToastUtils.showShort("查询失败：" + resultBean.getException().getMessage());
        }
    }

    //查询表内第一条数据，返回List<T>列表
    public void queryData6(View view) {
        Log.i("queryData", "开始");
        ResultBean<List<UserInfoBean>> resultBean = QuickSqlite.chainQuery().queryFirst(UserInfoBean.class);
        if (resultBean.isSuccess()) {
            Log.i("queryData", "结束");
            ToastUtils.showShort("查询成功：" + GsonUtils.toJson(resultBean.getResult()));
        } else {
            ToastUtils.showShort("查询失败：" + resultBean.getException().getMessage());
        }
    }

    //查询表内最后一条数据，返回List<T>列表
    public void queryData7(View view) {
        Log.i("queryData", "开始");
        ResultBean<List<UserInfoBean>> resultBean = QuickSqlite.chainQuery().queryLast(UserInfoBean.class);
        if (resultBean.isSuccess()) {
            Log.i("queryData", "结束");
            ToastUtils.showShort("查询成功：" + GsonUtils.toJson(resultBean.getResult()));
        } else {
            ToastUtils.showShort("查询失败：" + resultBean.getException().getMessage());
        }
    }

    //查询数据，返回List<T>列表
    public void queryData8(View view) {
        String sql = "select * from UserInfoBean where username=? limit ?,?";
        String[] selectionArgs = new String[]{"测试用户名", String.valueOf(0), String.valueOf(3)};
        Log.i("queryData", "开始");
        ResultBean<List<UserInfoBean>> resultBean = QuickSqlite.queryBySql(UserInfoBean.class, sql, selectionArgs);
        if (resultBean.isSuccess()) {
            Log.i("queryData", "结束");
            ToastUtils.showShort("查询成功：" + GsonUtils.toJson(resultBean.getResult()));
        } else {
            ToastUtils.showShort("查询失败：" + resultBean.getException().getMessage());
        }
    }
}
