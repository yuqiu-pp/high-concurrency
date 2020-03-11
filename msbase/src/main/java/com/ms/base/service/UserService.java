package com.ms.base.service;

import com.ms.base.dataobject.UserDO;
import com.ms.base.error.BusinessException;
import com.ms.base.service.model.UserModel;

public interface UserService {

    public UserModel getUserById(int id);

    // 创建用户记录
    public void register(UserModel userModel) throws BusinessException;

    // 用户登录
    public UserModel validateLogin(String telephone, String encryptPwd) throws BusinessException;
}
