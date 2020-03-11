package com.ms.base.controller;

import com.alibaba.druid.util.StringUtils;
import com.ms.base.error.BusinessException;
import com.ms.base.error.CommonError;
import com.ms.base.error.EmBusinessErr;
import com.ms.base.response.CommonReturnType;
import com.ms.base.controller.viewobject.UserVO;
import com.ms.base.service.impl.UserServiceImpl;
import com.ms.base.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static com.ms.base.error.EmBusinessErr.USER_OTP_ERR;


@Controller("user")
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    UserServiceImpl userService;

    @Autowired
    HttpServletRequest httpServletRequest;

    @RequestMapping(value="/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    @CrossOrigin
    public CommonReturnType login(@RequestParam(name="telephone") String telephone,
                                  @RequestParam(name="password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // controller层不做什么，调用service
        // 是否要通过UserModel传递参数 ？ 保持领域模型原则，应该用吧
        // UserModel userModel = new UserModel();
        // userModel.setTelphone(telephone);
        // userModel.setPassword(encodeByMd5(password));
        // 改进：直接传的telephone、password，可能觉的UserModel有点大

        UserModel userModel = userService.validateLogin(telephone, this.encodeByMd5(password));

        // 改进：保存登录状态
        httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);

        return CommonReturnType.create("登录成功");
    }


    @RequestMapping(value="/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public CommonReturnType register(@RequestParam(name="telephone") String telephone,
                                     @RequestParam(name="otpCode") String otpCode,
                                     @RequestParam(name="name") String name,
                                     @RequestParam(name="gender") Integer gender,
                                     @RequestParam(name="password") String password,
                                     @RequestParam(name="age") Integer age) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // 参数校验
        // 改进：要放到service层，因为可能有跳过controller，直接访问service的情况

        // otpcode校验
        // if (httpServletRequest.getSession().getAttribute("telephone") != otpCode){
        //     return CommonReturnType.create("fail", USER_OTP_ERR);
        // }
        // 改进：字符串比较用StringUtils.equals, 封装了字符串判空处理；
        //      使用统一的错误返回机制
        String otpInSession = (String) this.httpServletRequest.getSession().getAttribute(telephone);
        if (!StringUtils.equals(otpCode, otpInSession)){
            throw new BusinessException(EmBusinessErr.PARAMETER_VALIDTION_ERROR);
        }

        // 写数据库.  和service层传递UserModel
        UserModel userModel = new UserModel();
        userModel.setTelphone(telephone);
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender)));
        userModel.setAge(age);
        // password要加密
        userModel.setPassword(this.encodeByMd5(password));

        userService.register(userModel);

        return CommonReturnType.create("注册成功");
    }


    private String encodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String rst = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return rst;
    }


    // 获取otp码
    @RequestMapping(value="/getotp", method={RequestMethod.POST}, consumes={CONTENT_TYPE_FORMED})
    @ResponseBody
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public CommonReturnType getOtp(@RequestParam(name="telephone") String telephone){
        // 生成otp码
        Random random = new Random();
        Integer randomInt = random.nextInt(9999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        // 与手机号绑定
        this.httpServletRequest.getSession().setAttribute(telephone, otpCode);
        // System.out.println(this.httpServletRequest.getSession().getAttribute(telephone));
        // 发送短信
        System.out.println("telephone："+ telephone + " otpCode："+otpCode);

        // 返回
        return CommonReturnType.create("短信已发送");
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        // 根据用户id获取用户信息
        UserModel userModel = userService.getUserById(id);

        if (userModel == null){
            throw new BusinessException(EmBusinessErr.USER_NOT_FOUND);
        }
        UserVO userVO = convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);

        return userVO;
    }

}
