package com.ms.base.error;

public interface CommonError {

    public String getErrMsg();

    public int getErrCode();

    public CommonError setErrMsg(String errMsg);
}
