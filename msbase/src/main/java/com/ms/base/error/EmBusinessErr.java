package com.ms.base.error;

public enum EmBusinessErr implements CommonError{

    // 通用错误类型  以0000开始
    // 需要用到setMsg，例如：邮箱为空，msg设为"邮箱参数为空"
    PARAMETER_NOT_FOUND(10001, "参数为空"),
    PARAMETER_VALIDTION_ERROR(10002, "参数不合法"),
    UNKNOW_ERROE(10003, "未知错误"),

    // 以10000开头的是用户错
    USER_NOT_FOUND(20001, "用户不存在"),
    USER_OTP_ERR(20002, "otp错误"),
    USER_LOGIN_ERR(20003, "手机号或密码错误"),

    // 以30000开头的是商品错
    ITEM_NOT_FOUND(30001, "商品信息不存在"),
    ;

    private String errMsg;
    private int errCode;

    EmBusinessErr(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
