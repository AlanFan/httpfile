package com.qydcos.common;

import java.io.Serializable;

/**
 * @author AlanFan
 */
public class ApiResult implements Serializable {

    private int code;

    private String msg;

    private Object data;


    public ApiResult() {
    }

    public ApiResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ApiResult ERROR(ResultCode resultCode) {

        return new ApiResult(resultCode.getCode(), resultCode.getMessage());
    }

    public static ApiResult ERROR(ResultCode resultCode, Object data) {

        return new ApiResult(resultCode.getCode(), resultCode.getMessage(), data);
    }

    public static ApiResult SUCCESS(Object data) {
        ApiResult apiResult = new ApiResult();
        apiResult.setResultCode(ResultCode.SUCCESS);
        apiResult.setData(data);
        return apiResult;
    }

    public void setResultCode(ResultCode code) {
        this.code = code.getCode();
        this.msg = code.getMessage();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
