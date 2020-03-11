package com.ms.base.service.impl;

import com.ms.base.dao.UserDOMapper;
import com.ms.base.dao.UserPasswordDOMapper;
import com.ms.base.dataobject.UserDO;
import com.ms.base.dataobject.UserPasswordDO;
import com.ms.base.error.BusinessException;
import com.ms.base.error.CommonError;
import com.ms.base.error.EmBusinessErr;
import com.ms.base.service.UserService;
import com.ms.base.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;




    @Override
    public UserModel getUserById(int id){
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(id);

        // 通过用户id获取密码的密文
        UserModel userModel = convertToDataModel(userDO, userPasswordDO);
        return userModel;
    }

    // 事务？ 写库失败   注解
    // 返回？ 使用统一的错误返回机制 BusinessException，所以不需要返回值
    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        // if (userModel.getName()==null || userModel.getTelphone()==null){
        //     throw new BusinessException(EmBusinessErr.PARAMETER_NOT_FOUND, "姓名和手机号不能为空");
        // }
        // 改进：参数检查，健壮性
        //      apache commons的stringUtils.isEmpty，封装了字符串判null和长度为0。判断字符串是否为空，1.先确保不是null,2.判断长度
        if (userModel == null){
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDTION_ERROR);
        }
        //
        if (StringUtils.isEmpty(userModel.getName())
                || userModel.getAge()==null
                || userModel.getGender()==null
                || StringUtils.isEmpty(userModel.getTelphone())
                || StringUtils.isEmpty(userModel.getPassword())
                ){
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDTION_ERROR);
        }

        // 保存UserDO
        // UserDO userDO = new UserDO();
        // BeanUtils.copyProperties(userModel, userDO);
        // if (userDOMapper.insert(userDO) < 1){
        //     return false;
        // }
        // 改进：封装;
        //      使用insertSelective，而不是insert。避免发生null值覆盖数据库值的情况。
        //      数据库字段默认值尽量设为empty string，不要为null，因为null对前端没意义，且java处理空指针。
        //      但不为null会影响唯一索引，这种字段要注意一下。例如：第三方登录，这是可能没有手机号，手机号如果设为not null就无法设唯一索引
        UserDO userDO = convertFromUserModel(userModel);
        try {
            userDOMapper.insertSelective(userDO);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDTION_ERROR, "手机号重复");
        }


        // 保存UserPasswordDO
        // UserPasswordDO userPasswordDO = new UserPasswordDO();
        // BeanUtils.copyProperties(userModel, userPasswordDO);
        // if (userPasswordDOMapper.insert(userPasswordDO) < 1){
        //     return false;
        // }
        // userDO没写成功时，还没有id呢？只能读数据库(通过手机号查询)，不能从UserModel取
        // 改进：封装；
        //      user_id还要赋值，BeanUtils不能解决问题。 不需要通过其它SQL语句读取，xml文件设置keyProperty="id" useGeneratedKeys="true"
        userModel.setId(userDO.getId());

        UserPasswordDO userPasswordDO = convertPwdFromUserModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;
    }

    // 返回值 ？boolean  统一用throw new BusinessException
    // 改进：返回UserModel给controller
    @Override
    public UserModel validateLogin(String telephone, String encryptPwd) throws BusinessException {
        // 参数检查
        if (StringUtils.isEmpty(telephone) || StringUtils.isEmpty(encryptPwd)){
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDTION_ERROR);
        }
        // 验证密码. 加密在controller层完成，因为那有封装好的接口
        UserDO userDO = userDOMapper.selectByTelephone(telephone);
        // 改进：判空
        if (userDO == null){
            throw new BusinessException(EmBusinessErr.USER_LOGIN_ERR);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        if (!StringUtils.equals(userPasswordDO.getEncrypt(), encryptPwd)) {
            throw new BusinessException(EmBusinessErr.USER_LOGIN_ERR);
        }
        // UserModel userModel = new UserModel();
        // BeanUtils.copyProperties(userDO, userModel);
        // userModel.setPassword(userPasswordDO.getEncrypt());
        // 改进：用封装的接口合并
        UserModel userModel = convertToDataModel(userDO, userPasswordDO);

        return userModel;
    }

    // 改进：参数为空校验
    private UserDO convertFromUserModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);
        return userDO;
    }

    private UserPasswordDO convertPwdFromUserModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrypt(userModel.getPassword());
        userPasswordDO.setUserId(userModel.getId());

        return userPasswordDO;
    }


    private UserModel convertToDataModel(UserDO userDO, UserPasswordDO userPasswordDO){
        if (userDO == null){
            return null;
        }
        UserModel userModel = new UserModel();
        // userModel.setId(userDO.getId());
        // userModel.setAge(userDO.getAge());
        // userModel.setGender(userDO.getGender());
        // userModel.setName(userDO.getName());
        // userModel.setRegisterMode(userDO.getRegisterMode());
        // userModel.setTelphone(userDO.getTelphone());
        BeanUtils.copyProperties(userDO, userModel);

        userModel.setPassword(userPasswordDO.getEncrypt());

        return userModel;
    }
}
