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
    USER_LOGIN_ERR(20004, "手机号或密码错误"),
    USER_NOT_LOGIN(20003, "用户未登录"),

    // 以30000开头的是商品错
    ITEM_NOT_FOUND(30001, "商品信息不存在"),


    // 以40000开头是订单错
    ORDER_ID_DUPLICATE(40001, "订单号重复"),
    ORDER_CREATE_FAILE(40002, "订单创建失败"),

    // 以50000开头是订单错
    PROMO_ITEM_NOT_FOUND(50001, "秒杀商品不存在"),
    PROMO_VALIDATION_ERROR(50002, "秒杀活动未开始或已结束"),
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
