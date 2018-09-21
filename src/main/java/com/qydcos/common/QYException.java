package com.qydcos.common;

/**
 * @author AlanFan
 */
public class QYException extends Exception {

    private ApiResult error;

    public QYException(ApiResult error) {
        super(error.getMsg());
        this.error = error;
    }

    public ApiResult getError() {
        return error;
    }

    public void setError(ApiResult error) {
        this.error = error;
    }
}
