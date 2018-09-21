package com.qydcos.common;

/**
 * @author AlanFan
 */
public enum ResultCode {

    SUCCESS(1, "成功"),

    /* 参数错误 10001 - 19999*/
    PARAM_IS_INVALID(10000, "参数无效"),

    /* 业务错误 20000 - 29999 */
    FILE_IS_EMPTY(20000, "文件不能为空"),
    FILE_SAVE_ERROR(20001, "保存文件出错"),
    FILE_NOT_EXISTED(20002, "文件不存在"),
    FILE_TYPE_NOT_SUPPORTED(20003, "文件类型不支持"),
    FILE_SIZE_TOO_BIG(20004, "文件过大");


    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;

    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public java.lang.String getMessage() {
        return message;
    }

    public void setMessage(java.lang.String message) {
        this.message = message;
    }
}
