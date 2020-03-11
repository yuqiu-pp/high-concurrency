package com.ms.base.error;


// 包装器业务异常类实现
public class BusinessException extends Exception implements CommonError{
    // 实际上是EmBusiness类
    private CommonError commonError;


    // 直接接收EmBusinessError传参用于构造异常
    public BusinessException(CommonError commonError){
        super();
        this.commonError = commonError;
    }

    public BusinessException(CommonError commonError, String errMst){
        super();
        this.commonError = commonError;
        this.setErrMsg(errMst);
    }


    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        return this.commonError.setErrMsg(errMsg);
    }
}
