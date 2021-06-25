package com.lookballs.sqlite.common;

public class Constants {
    //内部自增主键字段名
    public static final String FIELD_ID_PRIMARY_KEY = "id";
    //对外使用的自增主键字段名
    public static final String FIELD_ID_PRIMARY_KEY_OPEN = "autoIncrementId";

    //字段类型
    public static final class FileType {
        public static final String SHORT1 = "short";
        public static final String SHORT2 = "java.lang.Short";

        public static final String STRING1 = "java.lang.String";

        public static final String LONG1 = "long";
        public static final String LONG2 = "java.lang.Long";

        public static final String INT1 = "int";
        public static final String INT2 = "java.lang.Integer";

        public static final String BOOLEAN1 = "boolean";
        public static final String BOOLEAN2 = "java.lang.Boolean";

        public static final String BYTE1 = "[B";
        public static final String BYTE2 = "[Ljava.lang.Byte;";

        public static final String FLOAT1 = "float";
        public static final String FLOAT2 = "java.lang.Float";

        public static final String DOUBLE1 = "double";
        public static final String DOUBLE2 = "java.lang.Double";
    }
}
