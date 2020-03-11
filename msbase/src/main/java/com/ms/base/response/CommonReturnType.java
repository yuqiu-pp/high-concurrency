package com.ms.base.response;

public class CommonReturnType {
    // 请求处理结果，正确sucess，否则fail
    private String status;

    // 如果status=success，返回请求的json数据
    // 如果status=fail，data内使用通用的错误码格式，
    private Object data;

    // 默认参数
    public static CommonReturnType create(Object object){
        return CommonReturnType.create("success", object);
    }

    // 定义一个通用的创建方法
    public static CommonReturnType create(String status, Object object){
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus(status);
        commonReturnType.setData(object);
        return commonReturnType;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
