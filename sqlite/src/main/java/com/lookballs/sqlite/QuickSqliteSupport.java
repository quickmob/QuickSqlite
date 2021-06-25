package com.lookballs.sqlite;

import com.lookballs.sqlite.annotation.Column;

public abstract class QuickSqliteSupport {
    @Column(ignore = true, desc = "表主键自增字段，对应表里的id字段。外部可以调用getAutoIncrementId获取表主键自增id。")
    private long autoIncrementId;

    public long getAutoIncrementId() {
        return autoIncrementId;
    }
}
