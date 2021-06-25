package com.lookballs.app.sqlite.table;

import com.lookballs.sqlite.QuickSqliteSupport;

public class BaseBean extends QuickSqliteSupport {
    private String baseField;

    public String getBaseField() {
        return baseField;
    }

    public void setBaseField(String baseField) {
        this.baseField = baseField;
    }
}
