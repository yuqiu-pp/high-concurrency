package com.ms.base.controller;

import com.ms.base.error.BusinessException;
import com.ms.base.error.EmBusinessErr;
import com.ms.base.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";

    // 定义ExceptionHandler解决未被controller层吸收的Exception
    // 否则会被抛到tomcat，只能返回500
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex){
        Map<String, Object> responseData = new HashMap<>();

        if (ex instanceof BusinessException){
            BusinessException businessException = (BusinessException)ex;
            responseData.put("errCode", businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());
        }else {
            // 由于@ResponseBody默认序列化的原因，不能直接将EmBusinessErr传给create
            responseData.put("errCode", EmBusinessErr.UNKNOW_ERROE.getErrCode());
            responseData.put("errMsg", EmBusinessErr.UNKNOW_ERROE.getErrMsg());
        }

        // 返回CommonReturnType
        CommonReturnType type = CommonReturnType.create("fail", responseData);

        return type;
    }
}
