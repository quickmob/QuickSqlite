package com.lookballs.sqlite.core;

public class FieldInfoBean {
    public String fieldType;
    public String fieldName;
    public Object fieldValue;

    public FieldInfoBean(String fieldType, String fieldName, Object fieldValue) {
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
