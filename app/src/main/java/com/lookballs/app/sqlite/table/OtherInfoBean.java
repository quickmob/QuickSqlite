package com.lookballs.app.sqlite.table;

import com.lookballs.sqlite.annotation.Column;
import com.lookballs.sqlite.QuickSqliteSupport;

public class OtherInfoBean extends QuickSqliteSupport {
    private String other;
    @Column(ignore = true)
    private String test;

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
